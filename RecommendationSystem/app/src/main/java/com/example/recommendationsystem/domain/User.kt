package com.example.recommendationsystem.domain

data class User(
    val id: Int,
    val name: String,
    val preferredCategories: Set<String>,
    val blockedCategories: Set<String>
)

