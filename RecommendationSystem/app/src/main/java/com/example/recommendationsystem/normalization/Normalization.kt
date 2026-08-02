package com.example.recommendationsystem.normalization

import com.example.recommendationsystem.domain.Product

//Funciones de normalización

fun normalizeName(name: String): String {
    return name.trim().replace(Regex("\\s+"), " ")
}

fun normalizeCategory(category: String): String {
    return category.trim().lowercase()
}

fun normalizeTags(tags: Set<String>): Set<String> {
    return tags
        .map { it.trim().lowercase() }
        .filter { it.isNotBlank() }
        .toSet()
}

fun roundPrice(price: Double): Double {
    return String.format("%.2f", price).toDouble()
}

fun normalizeProduct(product: Product): Product {
    return product.copy(
        name = normalizeName(product.name),
        category = normalizeCategory(product.category),
        tags = normalizeTags(product.tags),
        price = roundPrice(product.price)
    )
}

//Composición de funciones

infix fun <A, B, C> ((A) -> B).then(
    next: (B) -> C
): (A) -> C = { value ->
    next(this(value))
}