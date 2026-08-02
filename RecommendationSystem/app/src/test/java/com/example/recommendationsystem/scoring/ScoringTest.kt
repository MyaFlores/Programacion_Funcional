package com.example.recommendationsystem.scoring

import com.example.recommendationsystem.domain.*
import com.example.recommendationsystem.profile.UserProfile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ScoringTest {

    private val user = User(
        id = 1,
        name = "Ana Martinez",
        preferredCategories = setOf("Electronica", "Libros"),
        blockedCategories = setOf("Ropa")
    )

    private val product = Product(
        id = 1,
        name = "Laptop Gamer",
        category = "Electronica",
        price = 15000.0,
        rating = 4.5,
        stock = 10,
        tags = setOf("popular", "gaming")
    )

    private val interactions = listOf(
        Interaction(1, 1, InteractionType.VIEW, 1000),
        Interaction(1, 1, InteractionType.PURCHASE, 2000),
        Interaction(2, 1, InteractionType.VIEW, 3000)
    )

    private val profile = UserProfile(
        favoriteCategories = listOf("Electronica"),
        favoriteTags = listOf("popular"),
        averagePurchasePrice = 15000.0,
        viewedProductIds = setOf(1),
        purchasedProductIds = setOf(1),
        interactionFrequency = mapOf(InteractionType.VIEW to 1, InteractionType.PURCHASE to 1)
    )

    @Test
    fun `preferredCategoryRule should give 30 points for preferred category`() {
        // Given
        val rule = preferredCategoryRule()

        // When
        val result = rule(user, product, interactions)

        // Then
        assertEquals(30.0, result.score)
        assertNotNull(result.reason)
        assertTrue(result.reason!!.contains("categoria preferida"))
    }

    @Test
    fun `preferredCategoryRule should give 0 points for non-preferred category`() {
        // Given
        val product = product.copy(category = "Ropa")
        val rule = preferredCategoryRule()

        // When
        val result = rule(user, product, interactions)

        // Then
        assertEquals(0.0, result.score)
        assertNull(result.reason)
    }

    @Test
    fun `blockedCategoryPenalty should give -20 points for blocked category`() {
        // Given
        val product = product.copy(category = "Ropa")
        val rule = blockedCategoryPenalty()

        // When
        val result = rule(user, product, interactions)

        // Then
        assertEquals(-20.0, result.score)
        assertNotNull(result.reason)
        assertTrue(result.reason!!.contains("bloqueada"))
    }

    @Test
    fun `ratingScore should give 2x rating points`() {
        // Given
        val rule = ratingScore()

        // When
        val result = rule(user, product, interactions)

        // Then
        assertEquals(9.0, result.score)
    }

    @Test
    fun `purchaseHistoryScore should give points based on purchases`() {
        // Given
        val rule = purchaseHistoryScore(profile)

        // When
        val result = rule(user, product, interactions)

        // Then
        assertTrue(result.score > 0)
        assertNotNull(result.reason)
    }

    @Test
    fun `stockPenalty should give -15 points for low stock`() {
        // Given
        val product = product.copy(stock = 5)
        val rule = stockPenalty()

        // When
        val result = rule(user, product, interactions)

        // Then
        assertEquals(-15.0, result.score)
        assertNotNull(result.reason)
        assertTrue(result.reason!!.contains("bajo"))
    }

    @Test
    fun `applyScoringRules should combine all rules correctly`() {
        // Given
        val rules = listOf(
            preferredCategoryRule(),
            ratingScore(),
            stockPenalty()
        )

        // When
        val result = applyScoringRules(user, product, interactions, rules)

        // Then
        val expectedScore = 30.0 + 9.0 + 0.0  // stockPenalty no aplica porque stock=10
        assertEquals(expectedScore, result.score)
        assertNotNull(result.reason)
    }
}