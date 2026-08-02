package com.example.recommendationsystem.reporting

import com.example.recommendationsystem.domain.*
import com.example.recommendationsystem.recommendation.buildRecommendationAccumulator
import com.example.recommendationsystem.scoring.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ReportingTest {

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
        Product(4, "Mouse Inalambrico", "Electronica", 450.0, 4.2, 15, setOf("gadget"))
    )

    private val interactions = listOf(
        Interaction(1, 1, InteractionType.PURCHASE, 1000),
        Interaction(1, 3, InteractionType.VIEW, 2000)
    )

    private val rules = listOf(
        preferredCategoryRule(),
        blockedCategoryPenalty(),
        ratingScore(),
        stockPenalty()
    )

    @Test
    fun `report should contain total products evaluated`() {
        // When
        val accumulator = buildRecommendationAccumulator(user, products, interactions, rules)

        // Then
        assertEquals(products.size, accumulator.evaluatedProducts)
    }

    @Test
    fun `report should track accepted and rejected products`() {
        // When
        val accumulator = buildRecommendationAccumulator(user, products, interactions, rules)

        // Then
        assertEquals(
            accumulator.accepted.size + accumulator.rejected.size,
            accumulator.evaluatedProducts
        )
    }

    @Test
    fun `rejection reasons should be specific`() {
        // When
        val accumulator = buildRecommendationAccumulator(user, products, interactions, rules)

        // Then
        val reasons = accumulator.rejected.map { it.reason }

        // Verificar que hay rechazos
        assertTrue(accumulator.rejected.isNotEmpty())

        // Verificar que las razones contienen palabras clave relevantes
        val reasonText = reasons.joinToString(" ")
        val hasRelevantReason = reasonText.contains("stock", ignoreCase = true) ||
                reasonText.contains("bloqueada", ignoreCase = true) ||
                reasonText.contains("puntuacion", ignoreCase = true) ||
                reasonText.contains("blocked", ignoreCase = true)

        assertTrue(hasRelevantReason,
            "Las razones de rechazo no son especificas: $reasons")
    }

    @Test
    fun `total score should be sum of all accepted scores`() {
        // When
        val accumulator = buildRecommendationAccumulator(user, products, interactions, rules)

        // Then
        val expectedScore = accumulator.accepted.sumOf { it.score }
        assertEquals(expectedScore, accumulator.totalScore, 0.01)
    }
}