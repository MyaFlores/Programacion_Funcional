package com.example.recommendationsystem

import com.example.recommendationsystem.domain.*
import com.example.recommendationsystem.infrastructure.DataGenerator
import com.example.recommendationsystem.profile.generateUserProfile
import com.example.recommendationsystem.recommendation.*
import com.example.recommendationsystem.scoring.*
import com.example.recommendationsystem.similarity.findSimilarUsers

fun main() {
    println("Sistema Funcional de Recomendaciones")
    println("=" .repeat(50))

    // 1. Generar datos
    println("\nGenerando datos...")
    val users = DataGenerator.generateUsers(1000)
    val products = DataGenerator.generateProducts(10000)
    val interactions = DataGenerator.generateInteractions(users, products, 100000)
    println("${users.size} usuarios, ${products.size} productos, ${interactions.size} interacciones")

    // 2. Seleccionar un usuario de prueba
    val testUser = users.first()
    println("\nUsuario de prueba: ${testUser.name} (ID: ${testUser.id})")

    // 3. Generar perfil
    println("\nGenerando perfil...")
    val profile = generateUserProfile(testUser, interactions, products)
    println("  Categorias favoritas: ${profile.favoriteCategories.take(3)}")
    println("  Tags favoritos: ${profile.favoriteTags.take(3)}")

    // 4. Configurar reglas
    val rules = listOf(
        preferredCategoryRule(),
        blockedCategoryPenalty(),
        ratingScore(),
        purchaseHistoryScore(profile),
        viewedRecentlyScore(),
        removeFromCartPenalty(),
        stockPenalty(),
        tagBonus()
    )

    // 5. Generar recomendaciones (LIST)
    println("\nGenerando recomendaciones (LIST)...")
    val startList = System.currentTimeMillis()
    val result = generateRecommendations(
        user = testUser,
        products = products,
        interactions = interactions,
        rules = rules,
        limit = 10
    )
    val endList = System.currentTimeMillis()
    println("Tiempo: ${endList - startList}ms")

    // 6. Mostrar resultados
    when (result) {
        is AppResult.Success -> {
            println("\n${result.value.size} recomendaciones:")
            result.value.forEachIndexed { i, rec ->
                println("   ${i + 1}. ${rec.product.name}")
                println("  Score: ${String.format("%.2f", rec.score)}")
                println("  Razones: ${rec.reasons.take(3).joinToString(", ")}")
            }
        }
        is AppResult.Failure -> {
            println("\nErrores:")
            result.errors.forEach { println("   - $it") }
        }
    }

    // 7. Generar recomendaciones (SEQUENCE)
    println("\nGenerando recomendaciones (SEQUENCE)...")
    val startSeq = System.currentTimeMillis()
    val seqRecommendations = generateRecommendationsSequence(
        user = testUser,
        products = products,
        interactions = interactions,
        rules = rules,
        limit = 10
    )
    val endSeq = System.currentTimeMillis()
    println("Tiempo: ${endSeq - startSeq}ms")

    // 8. Acumulador
    println("\nAcumulador de recomendaciones...")
    val accumulator = buildRecommendationAccumulator(testUser, products, interactions, rules)
    println("  Productos evaluados: ${accumulator.evaluatedProducts}")
    println("  Aceptados: ${accumulator.accepted.size}")
    println("  Rechazados: ${accumulator.rejected.size}")
    println("  Score total: ${String.format("%.2f", accumulator.totalScore)}")

    // 9. Usuarios similares
    println("\nUsuarios similares...")
    val profiles = users.associateWith { user ->
        generateUserProfile(user, interactions, products)
    }
    val similarUsers = findSimilarUsers(
        targetUser = testUser,
        users = users,
        profiles = profiles.mapKeys { it.key.id },
        limit = 3
    )
    similarUsers.forEach { (user, similarity) ->
        println("   ${user.name} - Similitud: ${String.format("%.2f", similarity)}")
    }

    // 10. Reporte de rechazos
    println("\nCausas de rechazo:")
    val rejectionCauses = accumulator.rejected
        .groupingBy { it.reason }
        .eachCount()
        .entries
        .sortedByDescending { it.value }
    rejectionCauses.take(5).forEach { (reason, count) ->
        println("   $reason: $count productos")
    }

    println("\nSistema completado exitosamente")
}

// Versión con Sequence para comparar rendimiento
fun generateRecommendationsSequence(
    user: User,
    products: List<Product>,
    interactions: List<Interaction>,
    rules: List<ScoringRule>,
    limit: Int
): List<Recommendation> {
    return products.asSequence()
        .filter { it.stock > 0 }
        .filterNot { it.category in user.blockedCategories }
        .map { product ->
            val eval = applyScoringRules(user, product, interactions, rules)
            product to eval
        }
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
        .toList()
}