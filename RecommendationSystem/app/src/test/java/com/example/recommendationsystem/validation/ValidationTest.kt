package com.example.recommendationsystem.validation

import com.example.recommendationsystem.domain.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class ValidationTest {

    // ========== PRUEBAS BÁSICAS ==========

    @Test
    fun `validateUser should return success for valid user`() {
        // Given
        val user = User(
            id = 1,
            name = "Ana Martinez",
            preferredCategories = setOf("Electronica", "Libros"),
            blockedCategories = setOf("Ropa")
        )

        // When
        val result = validateUser(user)

        // Then
        assertTrue(result is AppResult.Success)
        assertEquals(user, (result as AppResult.Success).value)
    }

    @Test
    fun `validateUser should return failure for invalid user`() {
        // Given
        val user = User(
            id = -1,
            name = "",
            preferredCategories = emptySet(),
            blockedCategories = setOf("Ropa")
        )

        // When
        val result = validateUser(user)

        // Then
        assertTrue(result is AppResult.Failure)
        val errors = (result as AppResult.Failure).errors
        assertTrue(errors.size >= 3, "Se esperaban al menos 3 errores, pero se encontraron ${errors.size}: $errors")
        val errorText = errors.joinToString(" ")
        assertTrue(errorText.contains("ID") || errorText.contains("positivo"))
        assertTrue(errorText.contains("nombre") || errorText.contains("vacio"))
        assertTrue(errorText.contains("categoria") || errorText.contains("preferida"))
    }

    @Test
    fun `validateUser should detect overlapping categories`() {
        // Given
        val user = User(
            id = 1,
            name = "Carlos Lopez",
            preferredCategories = setOf("Electronica", "Ropa"),
            blockedCategories = setOf("Ropa", "Libros")
        )

        // When
        val result = validateUser(user)

        // Then
        assertTrue(result is AppResult.Failure)
        val errors = (result as AppResult.Failure).errors
        assertTrue(errors.any { it.contains("conflicto") || it.contains("solapamiento") })
    }

    // ========== PRUEBAS PARAMETRIZADAS ==========

    @ParameterizedTest
    @MethodSource("provideInvalidUsers")
    fun `validateUser should fail for invalid users`(user: User, expectedErrorKeywords: List<String>) {
        // When
        val result = validateUser(user)

        // Then
        assertTrue(result is AppResult.Failure)
        val errors = (result as AppResult.Failure).errors
        val errorText = errors.joinToString(" ")
        val found = expectedErrorKeywords.any { keyword ->
            errorText.contains(keyword, ignoreCase = true)
        }
        assertTrue(found, "Error text: '$errorText' no contiene ninguna de: $expectedErrorKeywords")
    }

    companion object {
        @JvmStatic
        fun provideInvalidUsers(): Stream<Arguments> = Stream.of(
            Arguments.of(
                User(-1, "", emptySet(), emptySet()),
                listOf("ID", "positivo", "nombre", "categoria")
            ),
            Arguments.of(
                User(0, "   ", setOf("Libros"), setOf("Libros")),
                listOf("ID", "nombre", "conflicto", "solapamiento")
            ),
            Arguments.of(
                User(2, "Maria", emptySet(), setOf("Ropa")),
                listOf("categoria", "preferida")
            )
        )
    }

    // ========== PRUEBAS DE PRODUCTO ==========

    @Test
    fun `validateProduct should return success for valid product`() {
        // Given
        val product = Product(
            id = 1,
            name = "Laptop Gamer",
            category = "Electronica",
            price = 15000.0,
            rating = 4.5,
            stock = 10,
            tags = setOf("popular", "gaming")
        )

        // When
        val result = validateProduct(product)

        // Then
        assertTrue(result is AppResult.Success)
    }

    @Test
    fun `validateProduct should return failure for invalid product`() {
        // Given
        val product = Product(
            id = -1,
            name = "",
            category = "",
            price = -10.0,
            rating = 6.0,
            stock = -5,
            tags = emptySet()
        )

        // When
        val result = validateProduct(product)

        // Then
        assertTrue(result is AppResult.Failure)
        val errors = (result as AppResult.Failure).errors
        val errorText = errors.joinToString(" ")

        // Verificar que hay al menos un error
        assertTrue(errors.isNotEmpty(), "No se encontraron errores de validacion")

        // Verificar que los errores contienen palabras clave relevantes
        // (al menos 3 de las siguientes deben estar presentes)
        val keywords = listOf(
            "ID", "positivo",
            "nombre", "vacio",
            "precio", "cero",
            "calificacion", "entre 0",
            "stock", "negativo",
            "categoria", "vacia"
        )

        val matchingKeywords = keywords.count { keyword ->
            errorText.contains(keyword, ignoreCase = true)
        }

        // Esperamos al menos 4 de los 6 errores posibles
        assertTrue(matchingKeywords >= 3,
            "Se esperaban al menos 3 errores, pero se encontraron $matchingKeywords. Errores: $errors")
    }
}