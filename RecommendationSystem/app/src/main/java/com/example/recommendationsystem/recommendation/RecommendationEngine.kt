package com.example.recommendationsystem.recommendation

import com.example.recommendationsystem.domain.*
import com.example.recommendationsystem.normalization.*
import com.example.recommendationsystem.scoring.*
import com.example.recommendationsystem.validation.*

fun generateRecommendations(
    user: User,
    products: List<Product>,
    interactions: List<Interaction>,
    rules: List<ScoringRule>,
    limit: Int
): AppResult<List<Recommendation>> {
    // 1. Validar usuario
    val validatedUser = validateUser(user)
    if (validatedUser is AppResult.Failure) {
        return validatedUser
    }

    // 2. Normalizar productos y filtrar
    val normalizedProducts = products
        .map { normalizeProduct(it) }
        .filter { it.stock > 0 }  // Eliminar sin stock
        .filterNot { it.category in user.blockedCategories }  // Excluir bloqueadas

    // 3. Calcular puntuaciones
    val scoredProducts = normalizedProducts.map { product ->
        val evaluation = applyScoringRules(user, product, interactions, rules)
        product to evaluation
    }

    // 4. Filtrar puntuaciones negativas y ordenar
    val recommendations = scoredProducts
        .filter { it.second.score > 0 }
        .sortedWith(compareByDescending<Pair<Product, RuleEvaluation>> { it.second.score }
            .thenByDescending { it.first.rating }
            .thenByDescending { it.first.price })
        .take(limit)
        .map { (product, eval) ->
            Recommendation(
                product = product,
                score = eval.score,
                reasons = eval.reason?.split("; ") ?: emptyList()
            )
        }

    return AppResult.Success(recommendations)
}

fun buildRecommendationAccumulator(
    user: User,
    products: List<Product>,
    interactions: List<Interaction>,
    rules: List<ScoringRule>
): RecommendationAccumulator {
    val normalizedProducts = products
        .map { normalizeProduct(it) }

    return normalizedProducts.fold(
        RecommendationAccumulator(0, emptyList(), emptyList(), 0.0)
    ) { acc, product ->
        val isBlocked = product.category in user.blockedCategories
        val hasStock = product.stock > 0

        if (isBlocked) {
            acc.copy(
                evaluatedProducts = acc.evaluatedProducts + 1,
                rejected = acc.rejected + RejectedProduct(product, "Categoria bloqueada")
            )
        } else if (!hasStock) {
            acc.copy(
                evaluatedProducts = acc.evaluatedProducts + 1,
                rejected = acc.rejected + RejectedProduct(product, "Sin stock")
            )
        } else {
            val eval = applyScoringRules(user, product, interactions, rules)
            if (eval.score > 0) {
                val recommendation = Recommendation(
                    product = product,
                    score = eval.score,
                    reasons = eval.reason?.split("; ") ?: emptyList()
                )
                acc.copy(
                    evaluatedProducts = acc.evaluatedProducts + 1,
                    accepted = acc.accepted + recommendation,
                    totalScore = acc.totalScore + eval.score
                )
            } else {
                acc.copy(
                    evaluatedProducts = acc.evaluatedProducts + 1,
                    rejected = acc.rejected + RejectedProduct(product, "Puntuacion baja")
                )
            }
        }
    }
}