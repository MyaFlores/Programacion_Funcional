package com.example.recommendationsystem.domain

data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val stock: Int,
    val tags: Set<String>
)

