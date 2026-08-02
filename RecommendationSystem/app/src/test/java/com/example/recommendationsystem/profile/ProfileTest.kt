package com.example.recommendationsystem.profile

import com.example.recommendationsystem.domain.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProfileTest {

    @Test
    fun `profile should contain correct favorite categories`() {
        // Given
        val user = User(1, "Ana", setOf("Electronica", "Libros"), emptySet())
        val products = listOf(
            Product(1, "Laptop", "Electronica", 1000.0, 4.5, 10, emptySet()),
            Product(2, "Libro", "Libros", 50.0, 4.8, 5, emptySet()),
            Product(3, "Ropa", "Ropa", 100.0, 3.5, 20, emptySet())
        )
        val interactions = listOf(
            Interaction(1, 1, InteractionType.PURCHASE, 1000),
            Interaction(1, 2, InteractionType.PURCHASE, 2000)
        )

        // When
        val profile = generateUserProfile(user, interactions, products)

        // Then
        assertTrue(profile.favoriteCategories.contains("Electronica"))
        assertTrue(profile.favoriteCategories.contains("Libros"))
    }

    @Test
    fun `profile should have correct purchased product ids`() {
        // Given
        val user = User(1, "Ana", emptySet(), emptySet())
        val products = listOf(
            Product(1, "Laptop", "Electronica", 1000.0, 4.5, 10, emptySet()),
            Product(2, "Mouse", "Electronica", 50.0, 4.0, 15, emptySet())
        )
        val interactions = listOf(
            Interaction(1, 1, InteractionType.PURCHASE, 1000),
            Interaction(1, 2, InteractionType.VIEW, 2000)
        )

        // When
        val profile = generateUserProfile(user, interactions, products)

        // Then
        assertTrue(profile.purchasedProductIds.contains(1))
        assertFalse(profile.purchasedProductIds.contains(2))
    }

    @Test
    fun `profile should calculate average purchase price correctly`() {
        // Given
        val user = User(1, "Ana", emptySet(), emptySet())
        val products = listOf(
            Product(1, "Laptop", "Electronica", 1000.0, 4.5, 10, emptySet()),
            Product(2, "Mouse", "Electronica", 50.0, 4.0, 15, emptySet()),
            Product(3, "Teclado", "Electronica", 200.0, 4.2, 5, emptySet())
        )
        val interactions = listOf(
            Interaction(1, 1, InteractionType.PURCHASE, 1000),
            Interaction(1, 2, InteractionType.PURCHASE, 2000),
            Interaction(1, 3, InteractionType.PURCHASE, 3000)
        )

        // When
        val profile = generateUserProfile(user, interactions, products)

        // Then
        val expectedAverage = (1000.0 + 50.0 + 200.0) / 3
        assertEquals(expectedAverage, profile.averagePurchasePrice, 0.01)
    }
}