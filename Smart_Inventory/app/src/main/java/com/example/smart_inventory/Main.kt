package com.example.smart_inventory

import kotlin.math.roundToInt

// Data class para el producto
data class Product(
    val nombre: String,
    val categoria: String?,    // Puede ser null (sin categoría)
    val precio: Double,
    val stock: Int
)

// Enum para rangos de precio
enum class PriceRange {
    ECONOMICO,   // 0 - 500
    MEDIO,     // 501 - 2000
    CARO,  // 2001 - 5000
    PREMIUM     // 5001+
}

fun main() {
    println(" ----- SMART INVENTORY ANALYZER -----\n")

    // Registrar al menos 15 productos
    val products = listOf(
        Product("Laptop Gamer X1", "Electronica", 15000.0, 8),
        Product("Mouse Inalambrico", "Electronica", 450.0, 25),
        Product("Teclado Mecanico", "Electronica", 1200.0, 15),
        Product("Monitor 24 pulgadas", "Electronica", 3200.0, 5),
        Product("Camiseta Deportiva", "Ropa", 350.0, 30),
        Product("Pantalón de mezclilla", "Ropa", 800.0, 12),
        Product("Zapatos Casual", "Ropa", 1200.0, 3),
        Product("Chaqueta Impermeable", "Ropa", 1800.0, 0),
        Product("Novela de ciencia ficción", "Libros", 280.0, 10),
        Product("Libro de programación Kotlin", "Libros", 550.0, 0),
        Product("Set de tazas", "Hogar", 320.0, 8),
        Product("Lampara de escritorio", "Hogar", 650.0, 6),
        Product("Silla Ergonomica", null, 2800.0, 2),      // Sin categoría
        Product("Mesa de Centro", null, 3500.0, 0),       // Sin categoría
        Product("Cafetera Automatica", "Electrodomesticos", 4200.0, 4),
        Product("Licuadora Profesional", "Electrodomesticos", 2800.0, 7),
        Product("Auriculares Bluetooth", "Electronica", 850.0, 0)
    )

    // 1. Mostrar listado completo de todos los productos
    println("----- Listado completo de productos -----\n")
    println("${"Producto".padEnd(30)} | ${"Categoria".padEnd(18)} | ${"Precio".padEnd(8)} | Existencias")
    println("-".repeat(75))

    products.forEach { product ->
        val categoryText = product.categoria ?: "Sin categoria"
        println("${product.nombre.padEnd(30)} | ${categoryText.padEnd(18)} | $${String.format("%.2f", product.precio).padEnd(8)} | ${product.stock}")
    }
    println()

    // 2. Clasificar productos según su rango de precio
    println("----- Clasificacion por rango de precio -----\n")

    products.forEach { product ->
        val range = getPriceRange(product.precio)
        println("${product.nombre}: $${String.format("%.2f", product.precio)} → $range")
    }
    println()

    // 3. Identificar productos agotados (stock == 0)
    println("----- Mostrar productos agotados -----\n")

    val outOfStock = products.filter { it.stock == 0 }
    if (outOfStock.isNotEmpty()) {
        outOfStock.forEach { product ->
            println("- ${product.nombre} | Categoria: ${product.categoria ?: "Sin categoria"} | Precio: $${String.format("%.2f", product.precio)}")
        }
    } else {
        println("No hay productos agotados")
    }
    println()

    // 4. Identificar productos con inventario bajo (stock < 5)
    println("----- Productos con inventario agotado -----\n")

    val lowStock = products.filter { it.stock in 1..4 }
    if (lowStock.isNotEmpty()) {
        lowStock.forEach { product ->
            println("- ${product.nombre} | Stock: ${product.stock} | Categoria: ${product.categoria ?: "Sin categoria"}")
        }
    } else {
        println("No hay productos con inventario bajo")
    }
    println()

    // 5. Agrupar productos por categoría (tratando los null)
    println("----- Productos agotados por categoria -----\n")

    val groupedByCategory = products.groupBy { it.categoria ?: "Sin categoria" }

    groupedByCategory.forEach { (category, productsInCategory) ->
        println("- $category (${productsInCategory.size} productos):")
        productsInCategory.forEach { product ->
            println("   - ${product.nombre} | Stock: ${product.stock} | $${String.format("%.2f", product.precio)}")
        }
        println()
    }

    // 6. Detectar productos sin categoría
    println("----- Productos sin categoria -----\n")

    val productsWithoutCategory = products.filter { it.categoria == null }
    if (productsWithoutCategory.isNotEmpty()) {
        productsWithoutCategory.forEach { product ->
            println("🏷️ ${product.nombre} | Precio: $${String.format("%.2f", product.precio)} | Stock: ${product.stock}")
        }
        println("\nEstos productos necesitan asignación de categoria")
    } else {
        println("Todos los productos tienen categoria asignada")
    }
    println()

    // 7. Calcular valor total del inventario
    println("----- Valor total del inventario -----\n")

    val totalInventoryValue = products.sumOf { it.precio * it.stock }
    println("Valor total del inventario: $${String.format("%.2f", totalInventoryValue)}")

    // 8. Precio promedio
    val averagePrice = products.map { it.precio }.average()
    println("Precio promedio: $${String.format("%.2f", averagePrice)}")

    // 9. Producto más caro
    val mostExpensive = products.maxByOrNull { it.precio }
    mostExpensive?.let {
        println("Producto mas caro: ${it.nombre} | $${String.format("%.2f", it.precio)}")
    }

    // 10. Producto con menor existencia
    val lowestStock = products.minByOrNull { it.stock }
    lowestStock?.let {
        println("Producto con menor stock: ${it.nombre} | Stock: ${it.stock}")
    }
    println()

    // 11. Productos que requieren reposición inmediata (stock == 0)
    println("----- Productos que requieren reposicion inmediata -----\n")

    val needsReplenishment = products.filter { it.stock == 0 }
    if (needsReplenishment.isNotEmpty()) {
        println("Los siguientes productos requieren reposición inmediata:")
        needsReplenishment.forEach { product ->
            println("   • ${product.nombre} | Categoria: ${product.categoria ?: "Sin categoria"} | Precio: $${String.format("%.2f", product.precio)}")
        }
    } else {
        println("Todos los productos tienen inventario disponible")
    }
    println()

    // 12. Reporte final del inventario
    println("----- Reporte final del inventario -----\n")

    val totalProducts = products.size
    val categoriesCount = products.mapNotNull { it.categoria }.distinct().size
    val withCategory = products.count { it.categoria != null }
    val withoutCategory = products.count { it.categoria == null }
    val totalStock = products.sumOf { it.stock }
    val productsWithStock = products.filter { it.stock > 0 }
    val productsWithoutStock = products.filter { it.stock == 0 }
    val lowStockCount = products.filter { it.stock in 1..4 }.size

    println("RESUMEN GENERAL:")
    println(" - Total de productos: $totalProducts")
    println(" - Categorías distintas: $categoriesCount")
    println(" - Productos con categoría: $withCategory")
    println(" - Productos sin categoría: $withoutCategory")
    println(" - Unidades totales en inventario: $totalStock")
    println(" - Productos con stock disponible: ${productsWithStock.size}")
    println(" - Productos agotados: ${productsWithoutStock.size}")
    println(" - Productos con stock bajo (1-4): $lowStockCount")
    println(" - Valor total del inventario: $${String.format("%.2f", totalInventoryValue)}")
    println(" - Precio promedio: $${String.format("%.2f", averagePrice)}")

    // Estado general del inventario
    val inventoryStatus = when {
        productsWithoutStock.isEmpty() && lowStockCount < 3 -> "EXCELENTE - Inventario saludable"
        productsWithoutStock.isNotEmpty() && lowStockCount < 5 -> "REGULAR - Algunos productos agotados"
        else -> "CRITICO - Se requiere reposicion urgente"
    }
    println("\nEstado del inventario: $inventoryStatus")
    println()

    // 13. Estadísticas resumidas por categoría
    println("----- Estadisticas por categoria -----\n")

    groupedByCategory.forEach { (category, productsInCategory) ->
        val count = productsInCategory.size
        val totalValue = productsInCategory.sumOf { it.precio * it.stock }
        val avgPrice = productsInCategory.map { it.precio }.average()
        val totalStock = productsInCategory.sumOf { it.stock }
        val outOfStockCount = productsInCategory.count { it.stock == 0 }
        val lowStockInCategory = productsInCategory.count { it.stock in 1..4 }

        println("- $category")
        println("  - Productos: $count")
        println("  - Valor total: $${String.format("%.2f", totalValue)}")
        println("  - Precio promedio: $${String.format("%.2f", avgPrice)}")
        println("  - Unidades en stock: $totalStock")
        println("  - Agotados: $outOfStockCount")
        if (lowStockInCategory > 0) {
            println("  - Stock bajo: $lowStockInCategory")
        }
        println()
    }
}

// Función para obtener el rango de precio usando intervalos
fun getPriceRange(price: Double): PriceRange {
    return when (price) {
        in 0.0..500.0 -> PriceRange.ECONOMICO
        in 501.0..2000.0 -> PriceRange.MEDIO
        in 2001.0..5000.0 -> PriceRange.CARO
        else -> PriceRange.PREMIUM
    }
}
