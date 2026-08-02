package com.example.recommendationsystem.reporting

import com.example.recommendationsystem.domain.*
import com.example.recommendationsystem.infrastructure.DataGenerator
import com.example.recommendationsystem.profile.generateUserProfile
import com.example.recommendationsystem.recommendation.generateRecommendations
import com.example.recommendationsystem.scoring.*
import java.io.File

fun GenerateReport() {
    println("---- GENERANDO REPORTE DE RECOMENDACIONES ----\n")

    // Generar datos
    val users = DataGenerator.generateUsers(100)
    val products = DataGenerator.generateProducts(500)
    val interactions = DataGenerator.generateInteractions(users, products, 10000)
    val targetUser = users.first()

    // Reglas
    val rules = listOf(
        preferredCategoryRule(),
        blockedCategoryPenalty(),
        ratingScore(),
        purchaseHistoryScore(generateUserProfile(targetUser, interactions, products)),
        viewedRecentlyScore(),
        removeFromCartPenalty(),
        stockPenalty(),
        tagBonus()
    )

    // Generar recomendaciones
    val result = generateRecommendations(targetUser, products, interactions, rules, 10)

    // Generar archivo de reporte
    val reportContent = buildString {
        appendLine("=" .repeat(60))
        appendLine("SISTEMA DE RECOMENDACIONES - REPORTE")
        appendLine("=" .repeat(60))
        appendLine()
        appendLine("ESTADÍSTICAS GENERALES")
        appendLine("-" .repeat(40))
        appendLine("  Usuarios totales: ${users.size}")
        appendLine("  Productos totales: ${products.size}")
        appendLine("  Interacciones totales: ${interactions.size}")
        appendLine()

        appendLine("USUARIO ANALIZADO")
        appendLine("-" .repeat(40))
        appendLine("  ID: ${targetUser.id}")
        appendLine("  Nombre: ${targetUser.name}")
        appendLine("  Categorias preferidas: ${targetUser.preferredCategories.joinToString()}")
        appendLine("  Categorias bloqueadas: ${targetUser.blockedCategories.joinToString()}")
        appendLine()

        when (result) {
            is AppResult.Success -> {
                appendLine("RECOMENDACIONES GENERADAS (${result.value.size})")
                appendLine("-" .repeat(40))
                result.value.forEachIndexed { i, rec ->
                    appendLine()
                    appendLine("  ${i + 1}. ${rec.product.name}")
                    appendLine("  Categoria: ${rec.product.category}")
                    appendLine("  Precio: $${String.format("%.2f", rec.product.price)}")
                    appendLine("  Calificacion: ${rec.product.rating}")
                    appendLine("  Stock: ${rec.product.stock}")
                    appendLine("  Score: ${String.format("%.2f", rec.score)}")
                    appendLine("  Razones:")
                    rec.reasons.forEach { reason ->
                        appendLine("    -$reason")
                    }
                }
            }
            is AppResult.Failure -> {
                appendLine("ERRORES:")
                result.errors.forEach { error ->
                    appendLine("  -$error")
                }
            }
        }

        appendLine()
        appendLine("=" .repeat(60))
        appendLine("FIN DEL REPORTE")
        appendLine("=" .repeat(60))
    }

    // Guardar en archivo
    val file = File("reporte_recomendaciones.txt")
    file.writeText(reportContent)
    println("Reporte guardado en: ${file.absolutePath}")
}