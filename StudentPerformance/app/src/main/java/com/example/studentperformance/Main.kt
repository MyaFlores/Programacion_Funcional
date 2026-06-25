package com.example.studentperformance
import kotlin.math.roundToInt

// Data class para el estudiante
data class Student(
    val name: String,
    val grade: Double?,           // Puede ser null (calificación pendiente)
    val attendance: Double,       // Porcentaje de asistencia (0-100)
    val advisor: String?          // Puede ser null (sin asesor asignado)
)

// Enum para niveles de desempeño
enum class PerformanceLevel {
    EXCELLENT,    // 9-10
    GOOD,         // 8-8.9
    AVERAGE,      // 7-7.9
    AT_RISK,      // <7
    PENDING       // Sin calificación
}

fun main() {
    println("----- STUDENT PERFORMANCE ANALYTICS SYSTEM -----")

    // 1. Registrar al menos 15 estudiantes
    val students = listOf(
        Student("Ana Martinez", 9.5, 95.0, "Dra. Gomez"),
        Student("Carlos Lopez", 8.2, 88.0, "Dr. Perez"),
        Student("Maria Garcia", null, 65.0, null),      // Sin calificación + sin asesor
        Student("Jorge Rodriguez", 6.5, 45.0, "Dr. Lopez"),
        Student("Laura Fernández", 9.0, 92.0, "Dra. Martinez"),
        Student("Diego Sanchez", 7.8, 70.0, null),      // Sin asesor
        Student("Valentina Ruiz", null, 50.0, "Dr. Sanchez"),
        Student("Andres Torres", 5.5, 60.0, "Dr. Ruiz"),
        Student("Camila Herrera", 10.0, 98.0, "Dra. Gomez"),
        Student("Luis Mendoza", null, 30.0, "Dr. Perez"),
        Student("Fernanda Castro", 8.9, 85.0, "Dra. Lopez"),
        Student("Ricardo Peña", 7.2, 72.0, null),
        Student("Sofia Ramirez", 6.8, 55.0, "Dra. Martinez"),
        Student("Emilio Vega", 9.2, 90.0, "Dr. Sanchez"),
        Student("Daniela Ortiz", null, 40.0, null),
        Student("Gabriel Soto", 8.5, 82.0, "Dra. Gomez")
    )

    // 2. Clasificar automáticamente el desempeño usando rangos de calificación
    println("----- Clasificacion de desempeño academico -----\n")

    students.forEach { student ->
        val performance = getPerformanceLevel(student.grade)
        val gradeText = student.grade?.let {
            String.format("%.1f", it)
        } ?: "Pendiente"

        println(" - ${student.name}")
        println("   Calificacion: $gradeText")
        println("   Desempeño: $performance")
        println("   Asistencia: ${student.attendance}%")
        println("   Asesor: ${student.advisor ?: "Sin asesor asignado"}")
        println()
    }

    // 3. Identificar estudiantes en situación de riesgo
    println("----- Estudiantes en situacion de riesgo -----\n")

    val atRiskStudents = students.filter { student ->
        // Bajo rendimiento (calificación < 7) O baja asistencia (< 60%) O información incompleta
        (student.grade != null && student.grade < 7.0) ||
                student.attendance < 60.0 ||
                student.grade == null ||
                student.advisor == null
    }

    atRiskStudents.forEach { student ->
        val reasons = mutableListOf<String>()
        if (student.grade != null && student.grade < 7.0) reasons.add("- Bajo rendimiento (${student.grade})")
        if (student.attendance < 60.0) reasons.add("Baja asistencia (${student.attendance}%)")
        if (student.grade == null) reasons.add("Calificacion pendiente")
        if (student.advisor == null) reasons.add("Sin asesor asignado")

        println("- ${student.name}")
        println("   Razones: ${reasons.joinToString(", ")}")
        println()
    }

    // 4. Reporte completo de todos los estudiantes
    println("----- Reporte completo de estudiantes -----\n")
    println("${"Estudiante".padEnd(25)} | ${"Calif".padEnd(6)} | ${"Asist".padEnd(6)} | ${"Desempeño".padEnd(12)} | Asesor")
    println("-".repeat(80))

    students.forEach { student ->
        val gradeText = student.grade?.let { String.format("%.1f", it) } ?: "Pend"
        val performance = getPerformanceLevel(student.grade)
        val advisorText = student.advisor ?: "Sin asesor"

        println("${student.name.padEnd(25)} | ${gradeText.padEnd(6)} | ${student.attendance.roundToInt().toString().padEnd(6)}% | ${performance.toString().padEnd(12)} | $advisorText")
    }
    println()

    // 5. Listado exclusivo de estudiantes en riesgo
    println("----- Estudiantes en riesgo -----\n")
    println("Total de estudiantes en riesgo: ${atRiskStudents.size}")
    atRiskStudents.forEachIndexed { index, student ->
        println("${index + 1}. ${student.name}")
    }
    println()

    // 6. Agrupar estudiantes según nivel de desempeño
    println("----- Estudiantes agrupados por desempeño -----\n")

    val groupedByPerformance = students.groupBy { getPerformanceLevel(it.grade) }

    PerformanceLevel.values().forEach { level ->
        val studentsInLevel = groupedByPerformance[level] ?: emptyList()
        println("- $level (${studentsInLevel.size} estudiantes):")
        studentsInLevel.forEach { student ->
            val gradeText = student.grade?.let { String.format("%.1f", it) } ?: "Pendiente"
            println("   - ${student.name} (Calif: $gradeText, Asist: ${student.attendance}%)")
        }
        println()
    }

    // 7. Calcular indicadores generales del grupo
    println("----- Indicadores generales del grupo -----\n")

    // Estudiantes con calificación (excluyendo null)
    val studentsWithGrade = students.filter { it.grade != null }
    val averageGrade = studentsWithGrade.map { it.grade!! }.average()

    // Cantidad de estudiantes por categoría (usando rangos)
    val excellentCount = studentsWithGrade.count { it.grade!! in 9.0..10.0 }
    val goodCount = studentsWithGrade.count { it.grade!! in 8.0..8.9 }
    val averageCount = studentsWithGrade.count { it.grade!! in 7.0..7.9 }
    val atRiskByGradeCount = studentsWithGrade.count { it.grade!! < 7.0 }
    val pendingCount = students.count { it.grade == null }

    // Porcentaje de estudiantes en riesgo
    val riskPercentage = (atRiskStudents.size.toDouble() / students.size) * 100

    println("Promedio general: ${String.format("%.2f", averageGrade)}")
    println()
    println("Distribución por desempeño (con calificación):")
    println("  - Excelente (9-10): $excellentCount estudiantes")
    println("  - Bueno (8-8.9): $goodCount estudiantes")
    println("  - Regular (7-7.9): $averageCount estudiantes")
    println("  - En riesgo académico (<7): $atRiskByGradeCount estudiantes")
    println("  - Calificación pendiente: $pendingCount estudiantes")
    println()
    println("Porcentaje de estudiantes en riesgo: ${String.format("%.1f", riskPercentage)}%")
    println()

    // 8. Detectar registros incompletos
    println("----- Registros incompletos -----\n")

    val incompleteRecords = students.filter { it.grade == null || it.advisor == null }

    incompleteRecords.forEach { student ->
        println("- ${student.name}:")
        if (student.grade == null) println("   - Calificación no registrada")
        if (student.advisor == null) println("   - Asesor no asignado")
    }
    println()

    // 9. Resumen ejecutivo
    println("----- RESUMEN EJECUTIVO -----\n")

    val overallStatus = when {
        averageGrade >= 8.0 && riskPercentage < 30 -> "BUENO - El grupo tiene buen desempeño general"
        averageGrade >= 7.0 && riskPercentage < 50 -> "REGULAR - Se requiere seguimiento a estudiantes en riesgo"
        else -> "CRITICO - Se necesita intervencion urgente"
    }

    println("----- RESUMEN GENERAL DEL GRUPO -----")
    println("- Total estudiantes: ${students.size}")
    println("- Promedio general: ${String.format("%.2f", averageGrade)}")
    println("- Estudiantes en riesgo: ${atRiskStudents.size} (${String.format("%.1f", riskPercentage)}%)")
    println("- Registros incompletos: ${incompleteRecords.size}")
    println()
    println("Evaluacion general: $overallStatus")
}

// Función para obtener el nivel de desempeño usando rangos
fun getPerformanceLevel(grade: Double?): PerformanceLevel {
    return when (grade) {
        null -> PerformanceLevel.PENDING
        in 9.0..10.0 -> PerformanceLevel.EXCELLENT
        in 8.0..8.9 -> PerformanceLevel.GOOD
        in 7.0..7.9 -> PerformanceLevel.AVERAGE
        else -> PerformanceLevel.AT_RISK
    }
}
