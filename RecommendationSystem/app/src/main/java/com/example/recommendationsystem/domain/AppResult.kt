package com.example.recommendationsystem.domain

sealed class AppResult<out T> {
    data class Success<T>(val value: T) : AppResult<T>()
    data class Failure(val errors: List<String>) : AppResult<Nothing>()
}

// Funciones de extensión para AppResult
fun <T, R> AppResult<T>.map(transform: (T) -> R): AppResult<R> {
    return when (this) {
        is AppResult.Success -> AppResult.Success(transform(value))
        is AppResult.Failure -> AppResult.Failure(errors)
    }
}

fun <T, R> AppResult<T>.flatMap(transform: (T) -> AppResult<R>): AppResult<R> {
    return when (this) {
        is AppResult.Success -> transform(value)
        is AppResult.Failure -> AppResult.Failure(errors)
    }
}

fun <T, R> AppResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (List<String>) -> R
): R {
    return when (this) {
        is AppResult.Success -> onSuccess(value)
        is AppResult.Failure -> onFailure(errors)
    }
}

fun <T> AppResult<T>.getOrElse(defaultValue: () -> T): T {
    return when (this) {
        is AppResult.Success -> value
        is AppResult.Failure -> defaultValue()
    }
}
