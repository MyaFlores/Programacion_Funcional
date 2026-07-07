package com.example.pharmacy_inventory

import java.net.BindException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

// Clase de datos
data class Medication(
    val name: String,
    val lab : String?,
    val category : String?,
    val price : Double,
    val quantity: Int,
    val expiryDate : String?,
    val requiresPrescription: Boolean
)

// Enum para la clafisficacion de investario
enum class InventoryLevel{
    SUFFICIENT,
    MEDIUM,
    LOW,
    OUT_OF_STOCK
}

fun main(){
    println("------ Sistema de una farmacia - Smart Pharmacy Inventory Dashboard ------")

    //Registrar 12 medicamentos
    val medications = listOf(
        Medication(
            name = "Paracetamol 500mg",
            lab = "Bayer",
            category = "Analgesico",
            price = 85.50,
            quantity = 150,
            expiryDate = "2025-12-31",
            requiresPrescription = false
        ),
        Medication(
            name = "Ibuprofeno 400mg",
            lab = "Pfizer",
            category = "Antiinflamatorio",
            price = 120.00,
            quantity = 45,
            expiryDate = "2025-08-15",
            requiresPrescription = false
        ),
        Medication(
            name = "Amoxicilina 500mg",
            lab = "AstraZeneca",
            category = "Antibiotico",
            price = 210.00,
            quantity = 30,
            expiryDate = "2025-06-20",
            requiresPrescription = true
        ),
        Medication(
            name = "Omeprazol 20mg",
            lab = "Novartis",
            category = "Antibiotico",
            price = 95.75,
            quantity = 60,
            expiryDate = "2025-09-10",
            requiresPrescription = false
        ),
        Medication(
            name = "Losartan 50mg",
            lab = null,
            category = "Antihipertensivo",
            price = 180.00,
            quantity = 15,
            expiryDate = "2025-07-01",
            requiresPrescription = true
        ),
        Medication(
            name = "Metformina 850mg",
            lab = "Merck",
            category = null,
            price = 95.00,
            quantity = 8,
            expiryDate = "2025-11-15",
            requiresPrescription = true
        ),
        Medication(
            name = "Salbutamol Inhalador",
            lab = "GlaxoSmithKline",
            category = "Respiratorio",
            price = 320.50,
            quantity = 0,
            expiryDate = null,
            requiresPrescription = true
        ),
        Medication(
            name = "Diclofenaco 50mg",
            lab = null,
            category = "Antiinflamatorio",
            price = 110.00,
            quantity = 25,
            expiryDate = "2025-10-30",
            requiresPrescription = false
        ),
        Medication(
            name = "Clonazepam 2mg",
            lab = "Roche",
            category = null,
            price = 250.00,
            quantity = 0,
            expiryDate = "2025-05-15",
            requiresPrescription = true
        ),
        Medication(
            name = "Loratadina 10mg",
            lab = "Schering-Plough",
            category = "Antihistaminico",
            price = 75.50,
            quantity = 55,
            expiryDate = "2026-01-20",
            requiresPrescription = false
        ),
        Medication(
            name = "Enalapril 10mg",
            lab = null,
            category = "Antihipertensivo",
            price = 165.00,
            quantity = 12,
            expiryDate = null,
            requiresPrescription = true
        ),
        Medication(
            name = "Tramadol 50mg",
            lab = "Grunenthal",
            category = "Analgesico",
            price = 280.00,
            quantity = 18,
            expiryDate = "2025-09-25",
            requiresPrescription = true
        ),
        Medication(
            name = "Atorvastatina 20mg",
            lab = "Pfizer",
            category = "Hipolipemiante",
            price = 340.00,
            quantity = 3,
            expiryDate = "2026-03-15",
            requiresPrescription = true
        ),
        Medication(
            name = "Ranitidina 150mg",
            lab = "Sanofi",
            category = null,
            price = 65.00,
            quantity = 22,
            expiryDate = "2025-12-01",
            requiresPrescription = false
        )
    )

    println("------ Mostrar lista completa con los medicamentos ------")
    println()

    println("${"Nombre".padEnd(25)} | ${"Laboratorio".padEnd(18)} | ${"Categoria".padEnd(16)} " +
            "| ${"Precio".padEnd(8)} | ${"Stock".padEnd(6)} | ${"Receta".padEnd(6)} | Caducidad")
    println("-".repeat(110))
    medications.forEach { med ->
        val labText = med.lab ?: "No registrado"
        val categoryText = med.category ?: "No registrada"
        val expiryText = med.expiryDate ?: "Pendiente"
        val prescriptionText = if (med.requiresPrescription) "Si" else "No"

        println("${med.name.padEnd(25)} | ${labText.padEnd(18)} | ${categoryText.padEnd(16)} | " +
                "$${String.format("%.2f", med.price).padEnd(8)} | ${med.quantity.toString().padEnd(6)} | " +
                "${prescriptionText.padEnd(6)} | $expiryText"
        )
    }
    println()

    println("------ Manejo de datos faltantes ------")
    medications.forEach { med ->
        if (med.lab == null) println("Laboratorio no registrado")
        if (med.category == null) println("Categoria no registrada")
        if (med.expiryDate == null) println("Fecha de caducidad")
        if (med.lab != null && med.category != null && med.expiryDate != null){
            println("Informacion completada")
        }
    }
    println()

    println("------ Clasificacion por nivel de inventario ------")
    fun getInventoryLevel(quantity: Int): InventoryLevel{
        return when {
            quantity >= 50 -> InventoryLevel.SUFFICIENT
            quantity in 20..49 -> InventoryLevel.MEDIUM
            quantity in 1..19 -> InventoryLevel.LOW
            else -> InventoryLevel.OUT_OF_STOCK
        }
    }

    medications.forEach { med ->
        val level = getInventoryLevel(med.quantity)
        val levelText = when (level) {
            InventoryLevel.SUFFICIENT -> "Inventario suficiente"
            InventoryLevel.MEDIUM -> "Inventario medio"
            InventoryLevel.LOW -> "Inventario bajo"
            InventoryLevel.OUT_OF_STOCK -> "Agotado"
        }
        println("${med.name.padEnd(25)} | Stock: ${med.quantity.toString().padEnd(4)} | $levelText")
    }
    println()

    println("------ Medicamentos en riesgo ------")
    val atRiskMedications = medications.filter { med ->
        med.quantity < 20 || med.quantity == 0 || med.lab == null
                || med.category == null || med.expiryDate == null
    }

    if (atRiskMedications.isNotEmpty()) {
        atRiskMedications.forEach { med ->
            val reasons = mutableListOf<String>()
            if (med.quantity < 20 && med.quantity > 0) reasons.add("Inventario bajo (${med.quantity})")
            if (med.quantity == 0) reasons.add("Agotado")
            if (med.lab == null) reasons.add("Laboratorio no registrado")
            if (med.category == null) reasons.add("Categoria no registrada")
            if (med.expiryDate == null) reasons.add("Fecha de caducidad pendiente")

            println("Medicamento en riesgo: ${med.name}")
            println("Razon: ${reasons.joinToString (", ")}")
            println()
        }
    } else {
        println("No hay ningun medicamento en riesgo")
    }

    println("------ Estadisticas de inventario ------")

    // Valor total del inventario
    val totalInventoryValue = medications.sumOf { it.price * it.quantity }
    println("Valor total del inventario: $${String.format("%.2f", totalInventoryValue)}")

    // Precio promedio
    val averagePrice = medications.map { it.price }.average()
    println("Precio promedio de medicamentos: $${String.format("%.2f", averagePrice)}")

    // Medicamentos que requieren receta
    val prescriptionCount = medications.count { it.requiresPrescription }
    println("Medicamentos que requieren receta: $prescriptionCount")

    // Agrupar por categoría
    println("\nMedicamentos agrupados por categoria:")
    val groupedByCategory = medications.groupBy { it.category ?: "Sin categoria" }
    groupedByCategory.forEach { (category, meds) ->
        println(" - $category: ${meds.size} medicamento(s)")
        meds.forEach { med ->
            println(" - ${med.name} | Stock: ${med.quantity} | $${String.format("%.2f", med.price)}")
        }
    }
    println()

    println("------ Listado de medicamentos en riesgo ------")
    println("Total de medicamentos en riesgo: ${atRiskMedications.size}")
    atRiskMedications.forEachIndexed { index, med ->
        println("${index + 1}. ${med.name} | Stock: ${med.quantity} | Precio: $${String.format("%.2f", med.price)}")
    }
    println()

    println("------ Porcentaje de medicamentos en riesgo ------")
    val riskPercentage = (atRiskMedications.size.toDouble() / medications.size) * 100
    println("${String.format("%.1f", riskPercentage)}% de los medicamentos estan en riesgo")
    println()

    println("------ Resumen ------")

    val totalMedications = medications.size
    val withCategory = medications.count { it.category != null }
    val withoutCategory = medications.count { it.category == null }
    val withoutLab = medications.count { it.lab == null }
    val withoutExpiry = medications.count { it.expiryDate == null }
    val outOfStock = medications.count { it.quantity == 0 }
    val lowStock = medications.count { it.quantity in 1..19 }

    println("- Total de medicamentos: $totalMedications")
    println("- Medicamentos con categoria registrada: $withCategory")
    println("- Medicamentos sin categoria: $withoutCategory")
    println("- Medicamentos sin laboratorio: $withoutLab")
    println("- Medicamentos sin fecha de caducidad: $withoutExpiry")
    println("- Medicamentos agotados: $outOfStock")
    println("- Medicamentos con inventario bajo (1-19): $lowStock")
    println("- Precio promedio: $${String.format("%.2f", averagePrice)}")
    println("- Valor total del inventario: $${String.format("%.2f", totalInventoryValue)}")
    println("- Porcentaje de medicamentos en riesgo: ${String.format("%.1f", riskPercentage)}%")
    println()



}