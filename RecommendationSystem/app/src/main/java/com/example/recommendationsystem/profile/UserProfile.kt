package com.example.recommendationsystem.profile

import com.example.recommendationsystem.domain.*

data class UserProfile(
    val favoriteCategories: List<String>,
    val favoriteTags: List<String>,
    val averagePurchasePrice: Double,
    val viewedProductIds: Set<Int>,
    val purchasedProductIds: Set<Int>,
    val interactionFrequency: Map<InteractionType, Int>
)

fun generateUserProfile(
    user: User,
    interactions: List<Interaction>,
    products: List<Product>
): UserProfile {
    val userInteractions = interactions.filter { it.userId == user.id }

    val purchasedIds = userInteractions
        .filter { it.type == InteractionType.PURCHASE }
        .map { it.productId }
        .toSet()

    val viewedIds = userInteractions
        .filter { it.type == InteractionType.VIEW }
        .map { it.productId }
        .toSet()

    val purchasedProducts = products.filter { it.id in purchasedIds }

    val avgPrice = if (purchasedProducts.isNotEmpty()) {
        purchasedProducts.map { it.price }.average()
    } else 0.0

    val categories = purchasedProducts
        .groupBy { it.category }
        .mapValues { it.value.size }
        .entries
        .sortedByDescending { it.value }
        .map { it.key }

    val tags = purchasedProducts
        .flatMap { it.tags }
        .groupingBy { it }
        .eachCount()
        .entries
        .sortedByDescending { it.value }
        .map { it.key }

    val frequency = InteractionType.values()
        .associateWith { type ->
            userInteractions.count { it.type == type }
        }

    return UserProfile(
        favoriteCategories = categories,
        favoriteTags = tags,
        averagePurchasePrice = avgPrice,
        viewedProductIds = viewedIds,
        purchasedProductIds = purchasedIds,
        interactionFrequency = frequency
    )
}