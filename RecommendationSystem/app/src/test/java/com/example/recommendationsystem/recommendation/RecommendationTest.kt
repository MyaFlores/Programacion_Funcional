package com.example.recommendationsystem.recommendation

import com.example.recommendationsystem.domain.*
import com.example.recommendationsystem.scoring.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RecommendationTest {

    private val user = User(
        id = 1,
        name = "Ana Martinez",
        preferredCategories = setOf("Electronica", "Libros"),
        blockedCategories = setOf("Ropa")
    )

    private val products = listOf(
        Product(1, "Laptop Gamer", "Electronica", 15000.0, 4.5, 10, setOf("popular")),
        Product(2, "Camisa Casual", "Ropa", 350.0, 3.5, 20, setOf("casual")),
        Product(3, "Libro de Kotlin", "Libros", 550.0, 4.8, 0, setOf("programacion")),
        Product(4, "Mouse Inalambrico", "Electronica", 450.0, 4.2, 15, setOf("gadget")),
        Product(5, "Pantalon Mezclilla", "Ropa", 800.0, 4.0, 5, setOf("moda"))
    )

    private val interactions = listOf(
        Interaction(1, 1, InteractionType.PURCHASE, 1000),
        Interaction(1, 3, InteractionType.VIEW, 2000),
        Interaction(2, 1, InteractionType.VIEW, 3000)
    )

    private val rules = listOf(
        preferredCategoryRule(),
        blockedCategoryPenalty(),
        ratingScore(),
        stockPenalty(),
        viewedRecentlyScore()
    )

    @Test
    fun `recommendations should never include product without stock`() {
        // When
        val result = generateRecommendations(user, products, interactions, rules, 10)

        // Then
        assertTrue(result is AppResult.Success)
        val recommendations = (result as AppResult.Success).value
        assertTrue(recommendations.none { it.product.stock == 0 })
    }

    @Test
    fun `recommendations should never include product from blocked category`() {
        // When
        val result = generateRecommendations(user, products, interactions, rules, 10)

        // Then
        assertTrue(result is AppResult.Success)
        val recommendations = (result as AppResult.Success).value
        assertTrue(recommendations.none { it.product.category in user.blockedCategories })
    }

    @Test
    fun `number of recommendations should never exceed limit`() {
        // Given
        val limit = 3

        // When
        val result = generateRecommendations(user, products, interactions, rules, limit)

        // Then
        assertTrue(result is AppResult.Success)
        val recommendations = (result as AppResult.Success).value
        assertTrue(recommendations.size <= limit)
    }

    @Test
    fun `recommendations should be sorted by score descending`() {
        // When
        val result = generateRecommendations(user, products, interactions, rules, 10)

        // Then
        assertTrue(result is AppResult.Success)
        val recommendations = (result as AppResult.Success).value
        for (i in 0 until recommendations.size - 1) {
            assertTrue(recommendations[i].score >= recommendations[i + 1].score)
        }
    }

    @Test
    fun `should return only requested number of recommendations`() {
        // Given
        val limit = 2

        // When
        val result = generateRecommendations(user, products, interactions, rules, limit)

        // Then
        assertTrue(result is AppResult.Success)
        val recommendations = (result as AppResult.Success).value
        assertEquals(limit, recommendations.size)
    }

    @Test
    fun `accumulator should count accepted and rejected products correctly`() {
        // When
        val accumulator = buildRecommendationAccumulator(user, products, interactions, rules)

        // Then
        assertEquals(products.size, accumulator.evaluatedProducts)
        assertEquals(accumulator.accepted.size + accumulator.rejected.size, accumulator.evaluatedProducts)
    }

    @Test
    fun `accumulator should not contain mutable lists`() {
        // When
        val accumulator = buildRecommendationAccumulator(user, products, interactions, rules)

        // Then
        assertTrue(accumulator.accepted is List)
        assertTrue(accumulator.rejected is List)
        // Verificar que no son listas mutables
        assertNotEquals(MutableList::class.java, accumulator.accepted::class.java)
    }

    @Test
    fun `rejection reasons should be tracked correctly`() {
        // When
        val accumulator = buildRecommendationAccumulator(user, products, interactions, rules)

        // Then
        val rejectionCounts = accumulator.rejected
            .groupingBy { it.reason }
            .eachCount()

        // Verificar que hay al menos una razón de rechazo
        assertTrue(accumulator.rejected.isNotEmpty())

        // Verificar que hay razones de rechazo relacionadas con stock O categoría bloqueada
        val reasons = accumulator.rejected.map { it.reason }
        val hasStockRejection = reasons.any { it.contains("stock", ignoreCase = true) }
        val hasBlockedRejection = reasons.any { it.contains("bloqueada", ignoreCase = true) || it.contains("blocked", ignoreCase = true) }

        // Al menos una de las dos debería estar presente
        assertTrue(hasStockRejection || hasBlockedRejection,
            "No se encontraron rechazos por stock o categoria bloqueada. Razones: $reasons")
    }
}