package com.example.recommendationsystem.domain

data class Recommendation(
    val product: Product,
    val score: Double,
    val reasons: List<String>
)

data class RejectedProduct(
    val product: Product,
    val reason: String
)

data class RuleEvaluation(
    val score: Double,
    val reason: String? = null
)

data class RecommendationReport(
    val totalProducts: Int,
    val totalRecommendations: Int,
    val averageScore: Double,
    val recommendationsByCategory: Map<String, Int>,
    val rejectionReasons: Map<String, Int>,
    val mostPopularProducts: List<Product>
)

data class RecommendationAccumulator(
    val evaluatedProducts: Int,
    val accepted: List<Recommendation>,
    val rejected: List<RejectedProduct>,
    val totalScore: Double
)

