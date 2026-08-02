package com.example.recommendationsystem.normalization

import com.example.recommendationsystem.domain.Product
import org.junit.jupiter.api.Assertions.*  // ← Importación para assertTrue
import org.junit.jupiter.api.Test

class NormalizationTest {

    @Test
    fun `normalizeName should trim and remove extra spaces`() {
        // Given
        val name = "  Laptop   Gamer   "

        // When
        val result = normalizeName(name)

        // Then
        assertEquals("Laptop Gamer", result)
    }

    @Test
    fun `normalizeCategory should trim and convert to lowercase`() {
        // Given
        val category = "  ELECTRONICA  "

        // When
        val result = normalizeCategory(category)

        // Then
        assertEquals("electronica", result)
    }

    @Test
    fun `normalizeTags should remove duplicates and empty tags`() {
        // Given
        val tags = setOf("  popular  ", "  ", "GAMING", "gaming", "  oferta  ")

        // When
        val result = normalizeTags(tags)

        // Then
        assertEquals(setOf("popular", "gaming", "oferta"), result)
    }

    @Test
    fun `roundPrice should round to 2 decimals`() {
        // Given
        val price = 99.999

        // When
        val result = roundPrice(price)

        // Then
        assertEquals(100.0, result)
    }

    @Test
    fun `normalizeProduct should apply all normalizations`() {
        // Given
        val product = Product(
            id = 1,
            name = "  Laptop  Gamer  ",
            category = "  ELECTRONICA  ",
            price = 99.999,
            rating = 4.5,
            stock = 10,
            tags = setOf("  popular  ", "gaming", "GAMING")
        )

        // When
        val result = normalizeProduct(product)

        // Then
        assertEquals("Laptop Gamer", result.name)
        assertEquals("electronica", result.category)
        assertEquals(100.0, result.price)
        assertEquals(setOf("popular", "gaming"), result.tags)
    }

    // ========== PRUEBA DE COMPOSICIÓN CORREGIDA ==========

    @Test
    fun `function composition should work correctly`() {
        // Esta prueba demuestra la composición de funciones
        // usando funciones que operan sobre el mismo tipo

        // Definimos funciones de ejemplo que operan sobre String
        val trimAndLower: (String) -> String = { it.trim().lowercase() }
        val removeExtraSpaces: (String) -> String = { it.replace(Regex("\\s+"), " ") }

        // Componemos: primero trimAndLower, luego removeExtraSpaces
        val composed = trimAndLower then removeExtraSpaces

        // Given
        val value = "  HOLA   MUNDO  "

        // When
        val result = composed(value)

        // Then
        assertEquals("hola mundo", result)
    }

    // Otra forma de probar composición con tipos numéricos
    @Test
    fun `function composition with numeric functions`() {
        // Funciones que operan sobre Double
        val addOne: (Double) -> Double = { it + 1 }
        val multiplyByTwo: (Double) -> Double = { it * 2 }

        // Componemos: primero addOne, luego multiplyByTwo
        val addThenMultiply = addOne then multiplyByTwo

        // Given
        val value = 5.0

        // When
        val result = addThenMultiply(value)

        // Then
        assertEquals(12.0, result) // (5 + 1) * 2 = 12
    }

    // Inline function for composition (necesaria para que compile)
    infix fun <A, B, C> ((A) -> B).then(
        next: (B) -> C
    ): (A) -> C = { value ->
        next(this(value))
    }
}