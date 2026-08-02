package com.example.recommendationsystem.validation

import com.example.recommendationsystem.domain.*

typealias ValidationRule<T> = (T) -> List<String>

//Validación de usuario

fun validateUser(user: User): AppResult<User> {
    val rules = listOf<ValidationRule<User>>(
        { user ->
            if (user.id > 0) emptyList()
            else listOf("El ID del usuario debe ser positivo")
        },
        { user ->
            if (user.name.isNotBlank()) emptyList()
            else listOf("El nombre del usuario no puede estar vacio")
        },
        { user ->
            if (user.preferredCategories.isNotEmpty()) emptyList()
            else listOf("El usuario debe tener al menos una categoria preferida")
        },
        { user ->
            val overlap = user.preferredCategories intersect user.blockedCategories
            if (overlap.isEmpty()) emptyList()
            else listOf("Categorias en conflicto: ${overlap.joinToString()}")
        }
    )
    return validate(user, rules)
}

//Validación de producto

fun validateProduct(product: Product): AppResult<Product> {
    val rules = listOf<ValidationRule<Product>>(
        { p ->
            if (p.id > 0) emptyList()
            else listOf("El ID del producto debe ser positivo")
        },
        { p ->
            if (p.name.isNotBlank()) emptyList()
            else listOf("El nombre del producto no puede estar vacio")
        },
        { p ->
            if (p.price > 0) emptyList()
            else listOf("El precio debe ser mayor que cero")
        },
        { p ->
            if (p.rating in 0.0..5.0) emptyList()
            else listOf("La calificacion debe estar entre 0 y 5")
        },
        { p ->
            if (p.stock >= 0) emptyList()
            else listOf("El stock no puede ser negativo")
        },
        { p ->
            if (p.category.isNotBlank()) emptyList()
            else listOf("La categoria no puede estar vacia")
        }
    )
    return validate(product, rules)
}

//Validación generica

fun <T> validate(
    value: T,
    rules: List<ValidationRule<T>>
): AppResult<T> {
    val errors = rules.flatMap { it(value) }
    return if (errors.isEmpty()) {
        AppResult.Success(value)
    } else {
        AppResult.Failure(errors)
    }
}