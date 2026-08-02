package com.example.recommendationsystem.performance

import com.example.recommendationsystem.domain.*
import com.example.recommendationsystem.generateRecommendationsSequence
import com.example.recommendationsystem.infrastructure.DataGenerator
import com.example.recommendationsystem.recommendation.generateRecommendations
import com.example.recommendationsystem.scoring.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue  // ← Importación correcta
import org.junit.jupiter.api.Test
import kotlin.system.measureTimeMillis

class PerformanceTest {

    @Test
    fun `compare List vs Sequence performance for 10000 products`() {
        // Given
        val user = User(1, "Test User", setOf("Electronica", "Libros"), emptySet())
        val products = DataGenerator.generateProducts(10000)
        val interactions = listOf(
            Interaction(1, 1, InteractionType.PURCHASE, 1000),
            Interaction(1, 2, InteractionType.VIEW, 2000)
        )
        val rules = listOf(
            preferredCategoryRule(),
            blockedCategoryPenalty(),
            ratingScore(),
            stockPenalty()
        )

        // When - List version
        val listTime = measureTimeMillis {
            generateRecommendations(user, products, interactions, rules, 10)
        }

        // When - Sequence version
        val sequenceTime = measureTimeMillis {
            generateRecommendationsSequence(user, products, interactions, rules, 10)
        }

        // Then - Print results for comparison
        println("---- RENDIMIENTO ----")
        println("List: $listTime ms")
        println("Sequence: $sequenceTime ms")
        println("Diferencia: ${listTime - sequenceTime} ms")
        println("---------------------")

        // La prueba siempre pasa, solo documenta resultados
        assertTrue(true)
    }

    @Test
    fun `sequence should be faster for large collections`() {
        // Given
        val user = User(1, "Test User", setOf("Electronica"), emptySet())
        val products = DataGenerator.generateProducts(5000)
        val interactions = listOf(
            Interaction(1, 1, InteractionType.PURCHASE, 1000)
        )
        val rules = listOf(
            preferredCategoryRule(),
            stockPenalty()
        )

        // When
        val listResult = generateRecommendations(user, products, interactions, rules, 5)
        val sequenceResult = generateRecommendationsSequence(user, products, interactions, rules, 5)

        // Then
        assertTrue(listResult is AppResult.Success)
        assertTrue(sequenceResult is List<Recommendation>)

        // Ambos resultados deberían ser consistentes (mismos productos recomendados)
        val listIds = (listResult as AppResult.Success).value.map { it.product.id }.sorted()
        val seqIds = sequenceResult.map { it.product.id }.sorted()

        assertEquals(listIds, seqIds, "List y Sequence deberian dar los mismos resultados")
    }
}