package com.example.recommendationsystem.scoring

import com.example.recommendationsystem.domain.*
import com.example.recommendationsystem.profile.UserProfile

typealias ScoringRule = (User, Product, List<Interaction>) -> RuleEvaluation

//Reglas de puntuación

fun preferredCategoryRule(): ScoringRule = { user, product, _ ->
    val score = if (product.category in user.preferredCategories) 30.0 else 0.0
    RuleEvaluation(
        score = score,
        reason = if (score > 0) "Coincide con categoría preferida" else null
    )
}

fun blockedCategoryPenalty(): ScoringRule = { user, product, _ ->
    val score = if (product.category in user.blockedCategories) -20.0 else 0.0
    RuleEvaluation(
        score = score,
        reason = if (score < 0) "Categoría bloqueada" else null
    )
}

fun ratingScore(): ScoringRule = { _, product, _ ->
    val score = product.rating * 2
    RuleEvaluation(
        score = score,
        reason = if (score > 0) "Producto con calificación alta" else null
    )
}

fun purchaseHistoryScore(profile: UserProfile): ScoringRule = { _, product, interactions ->
    val usersWhoBought = interactions
        .filter { it.type == InteractionType.PURCHASE && it.productId == product.id }
        .map { it.userId }
        .toSet()

    val score = usersWhoBought.size * 0.5
    RuleEvaluation(
        score = score,
        reason = if (score > 0) "Popular entre usuarios" else null
    )
}

fun viewedRecentlyScore(): ScoringRule = { _, product, interactions ->
    val recentViews = interactions
        .filter { it.type == InteractionType.VIEW && it.productId == product.id }
        .count()

    val score = (recentViews * 0.3).coerceAtMost(5.0)
    RuleEvaluation(
        score = score,
        reason = if (score > 0) "Producto visto recientemente" else null
    )
}

fun removeFromCartPenalty(): ScoringRule = { _, product, interactions ->
    val removals = interactions
        .filter { it.type == InteractionType.REMOVE_FROM_CART && it.productId == product.id }
        .count()

    val score = -(removals * 5.0)
    RuleEvaluation(
        score = score,
        reason = if (removals > 0) "Eliminado del carrito $removals veces" else null
    )
}

fun stockPenalty(): ScoringRule = { _, product, _ ->
    val score = if (product.stock < 10) -15.0 else 0.0
    RuleEvaluation(
        score = score,
        reason = if (score < 0) "Stock bajo" else null
    )
}

fun tagBonus(): ScoringRule = { user, product, interactions ->
    val userPurchases = interactions
        .filter { it.type == InteractionType.PURCHASE && it.userId == user.id }
        .map { it.productId }
        .toSet()

    // Simulación de tags de productos comprados por el usuario
    val userTags = setOf("popular", "trending", "high-quality")

    val commonTags = product.tags intersect userTags
    val score = commonTags.size * 5.0
    RuleEvaluation(
        score = score,
        reason = if (score > 0) "Etiquetas relacionadas: ${commonTags.joinToString()}" else null
    )
}

//Aplicación de reglas

fun applyScoringRules(
    user: User,
    product: Product,
    interactions: List<Interaction>,
    rules: List<ScoringRule>
): RuleEvaluation {
    val evaluations = rules.map { rule ->
        rule(user, product, interactions)
    }

    val totalScore = evaluations.fold(0.0) { acc, eval -> acc + eval.score }
    val reasons = evaluations.mapNotNull { it.reason }

    return RuleEvaluation(
        score = totalScore,
        reason = reasons.joinToString("; ")
    )
}