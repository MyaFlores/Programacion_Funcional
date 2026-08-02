package com.example.recommendationsystem.domain

data class Interaction(
    val userId: Int,
    val productId: Int,
    val type: InteractionType,
    val timestamp: Long
)

enum class InteractionType {
    VIEW,
    FAVORITE,
    PURCHASE,
    REMOVE_FROM_CART
}

