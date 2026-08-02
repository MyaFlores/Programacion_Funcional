package com.example.recommendationsystem.similarity

import com.example.recommendationsystem.domain.User
import com.example.recommendationsystem.profile.UserProfile

fun calculateSimilarity(
    firstProfile: UserProfile,
    secondProfile: UserProfile
): Double {
    val commonCategories = firstProfile.favoriteCategories intersect secondProfile.favoriteCategories
    val totalCategories = (firstProfile.favoriteCategories + secondProfile.favoriteCategories).toSet()

    val commonTags = firstProfile.favoriteTags intersect secondProfile.favoriteTags
    val totalTags = (firstProfile.favoriteTags + secondProfile.favoriteTags).toSet()

    val categorySimilarity = if (totalCategories.isNotEmpty()) {
        commonCategories.size.toDouble() / totalCategories.size
    } else 0.0

    val tagSimilarity = if (totalTags.isNotEmpty()) {
        commonTags.size.toDouble() / totalTags.size
    } else 0.0

    // Ponderar: 60% categorías, 40% tags
    return (categorySimilarity * 0.6) + (tagSimilarity * 0.4)
}

fun findSimilarUsers(
    targetUser: User,
    users: List<User>,
    profiles: Map<Int, UserProfile>,
    limit: Int
): List<Pair<User, Double>> {
    val targetProfile = profiles[targetUser.id]
    return if (targetProfile != null) {
        users
            .filter { it.id != targetUser.id }
            .map { user ->
                val profile = profiles[user.id]
                val similarity = if (profile != null) {
                    calculateSimilarity(targetProfile, profile)
                } else 0.0
                user to similarity
            }
            .sortedByDescending { it.second }
            .take(limit)
    } else {
        emptyList()
    }
}