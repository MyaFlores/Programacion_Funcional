package com.example.recommendationsystem.similarity

import com.example.recommendationsystem.domain.User
import com.example.recommendationsystem.profile.UserProfile
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SimilarityTest {

    @Test
    fun testSimilarityIdenticalProfiles() {
        val profile1 = UserProfile(
            favoriteCategories = listOf("Electronica", "Libros"),
            favoriteTags = listOf("popular", "gaming"),
            averagePurchasePrice = 1000.0,
            viewedProductIds = setOf(1, 2),
            purchasedProductIds = setOf(1),
            interactionFrequency = emptyMap()
        )
        val profile2 = profile1.copy()

        val similarity = calculateSimilarity(profile1, profile2)

        assertEquals(1.0, similarity, 0.01)
    }

    @Test
    fun testSimilarityDifferentProfiles() {
        val profile1 = UserProfile(
            favoriteCategories = listOf("Electronica"),
            favoriteTags = listOf("gaming"),
            averagePurchasePrice = 1000.0,
            viewedProductIds = setOf(1),
            purchasedProductIds = setOf(1),
            interactionFrequency = emptyMap()
        )
        val profile2 = UserProfile(
            favoriteCategories = listOf("Ropa"),
            favoriteTags = listOf("moda"),
            averagePurchasePrice = 100.0,
            viewedProductIds = setOf(2),
            purchasedProductIds = setOf(2),
            interactionFrequency = emptyMap()
        )

        val similarity = calculateSimilarity(profile1, profile2)

        assertEquals(0.0, similarity, 0.01)
    }

    @Test
    fun testSimilarityRange() {
        val profile1 = UserProfile(
            favoriteCategories = listOf("Electronica", "Libros", "Hogar"),
            favoriteTags = listOf("popular", "gaming", "oferta"),
            averagePurchasePrice = 1000.0,
            viewedProductIds = setOf(1, 2, 3),
            purchasedProductIds = setOf(1),
            interactionFrequency = emptyMap()
        )
        val profile2 = UserProfile(
            favoriteCategories = listOf("Electronica", "Ropa"),
            favoriteTags = listOf("popular", "moda"),
            averagePurchasePrice = 500.0,
            viewedProductIds = setOf(1, 4),
            purchasedProductIds = setOf(1),
            interactionFrequency = emptyMap()
        )

        val similarity = calculateSimilarity(profile1, profile2)

        assertTrue(similarity in 0.0..1.0)
    }

    @Test
    fun testFindSimilarUsersSorted() {
        val targetUser = User(1, "Ana", emptySet(), emptySet())
        val users = listOf(
            User(2, "Carlos", emptySet(), emptySet()),
            User(3, "María", emptySet(), emptySet()),
            User(4, "Jorge", emptySet(), emptySet())
        )

        val profiles = mapOf(
            1 to UserProfile(
                listOf("Electronica", "Libros"),
                listOf("popular"),
                1000.0,
                setOf(1, 2),
                setOf(1),
                emptyMap()
            ),
            2 to UserProfile(
                listOf("Electronica", "Libros"),
                listOf("popular"),
                1000.0,
                setOf(1, 2),
                setOf(1),
                emptyMap()
            ),
            3 to UserProfile(
                listOf("Ropa", "Hogar"),
                listOf("moda"),
                500.0,
                setOf(3, 4),
                setOf(3),
                emptyMap()
            ),
            4 to UserProfile(
                listOf("Electronica"),
                listOf("gaming"),
                2000.0,
                setOf(1, 3),
                setOf(1),
                emptyMap()
            )
        )

        val result = findSimilarUsers(targetUser, users, profiles, 2)

        assertEquals(2, result.size)
        assertTrue(result[0].second >= result[1].second)
    }
}