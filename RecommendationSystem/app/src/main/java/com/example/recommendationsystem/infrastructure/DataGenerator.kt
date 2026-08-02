package com.example.recommendationsystem.infrastructure

import com.example.recommendationsystem.domain.*

object DataGenerator {
    private val categories = listOf(
        "Electronica", "Ropa", "Libros", "Hogar", "Deportes",
        "Juguetes", "Alimentos", "Belleza", "Automotriz", "Mascotas"
    )

    private val names = listOf(
        "Ana", "Carlos", "Maria", "Jorge", "Laura", "Diego",
        "Valentina", "Andres", "Sofia", "Luis", "Camila", "Mateo",
        "Isabella", "Sebastian", "Valeria", "Daniel", "Gabriela",
        "David", "Lucia", "Emilio"
    )

    private val productNames = listOf(
        "Laptop", "Smartphone", "Auriculares", "Teclado", "Mouse",
        "Monitor", "Camara", "Impresora", "Tablet", "Altavoz",
        "Camiseta", "Pantalon", "Zapatos", "Chaqueta", "Bufanda",
        "Libro", "Revista", "Comic", "Manga", "Novela",
        "Sofa", "Mesa", "Silla", "Lampara", "Estante"
    )

    private val tags = listOf(
        "popular", "nuevo", "oferta", "quality", "trending",
        "eco-friendly", "premium", "bestseller", "limited", "exclusive",
        "recomendado", "top", "viral", "must-have", "edicion-limitada"
    )

    fun generateUsers(count: Int = 1000): List<User> {
        return (1..count).map { id ->
            val preferred = categories.shuffled().take((1..4).random()).toSet()
            val blocked = categories.shuffled().take((0..2).random()).toSet()
            User(
                id = id,
                name = "${names.random()} ${(1..999).random()}",
                preferredCategories = preferred - blocked,
                blockedCategories = blocked
            )
        }
    }

    fun generateProducts(count: Int = 10000): List<Product> {
        return (1..count).map { id ->
            Product(
                id = id,
                name = "${productNames.random()} ${(1..999).random()}",
                category = categories.random(),
                price = (100..100000).random().toDouble() / 10,
                rating = (0..50).random().toDouble() / 10,
                stock = (0..200).random(),
                tags = tags.shuffled().take((1..5).random()).toSet()
            )
        }
    }

    fun generateInteractions(
        users: List<User>,
        products: List<Product>,
        count: Int = 100000
    ): List<Interaction> {
        val types = InteractionType.values().toList()
        val now = System.currentTimeMillis()

        return (1..count).map { id ->
            Interaction(
                userId = users.random().id,
                productId = products.random().id,
                type = types.random(),
                timestamp = now - (1L..30L * 24 * 60 * 60 * 1000).random()
            )
        }
    }
}