package com.example.smarttask

// Data class (propiedades en camelCase)
data class Task(
    val nombre: String,
    val prioridad: Int,        // 1 es baja prioridad y 10 es alta prioridad
    val horasEstimadas: Int,
    val completado: Boolean
)

// High-Order Function (nombre en camelCase)
fun procesoTareas(tareas: List<Task>, action: (Task) -> Unit) {
    tareas.forEach { action(it) }
}

fun main() {
    println("----- Smart Task Manager -----")
    println()

    // Lista de tareas
    val tareas = listOf(
        Task("Comprar la despensa de la semana", 9, 3, true),
        Task("Ir a mi clase de ingles", 10, 3, false),
        Task("Ordenar los cajones de ropa", 5, 3, true),
        Task("Hacer apuntes de distintas materias", 10, 6, false),
        Task("Limpiar la laptop", 10, 2, true),                    // ← Corregido: "Limpiar"
        Task("Limpieza profunda de la casa", 10, 5, false),
        Task("Subir codigos a repositorios de GitHub", 8, 2, true), // ← Corregido: "repositorios"
        Task("Diseñar sitio web usando Angular", 4, 3, false),
        Task("Bañar a mi mascota", 6, 3, true),
        Task("Pagar la mensualidad de la universidad", 10, 2, false)
    )

    // 1. Imprimir todas las tareas
    println("----- Todas las tareas -----")
    tareas.forEach { tarea ->
        println(
            "- ${tarea.nombre} " +
                    "| Prioridad: ${tarea.prioridad} " +
                    "| Horas: ${tarea.horasEstimadas} " +
                    "| Completada: ${if (tarea.completado) "SI" else "NO"}"
        )
    }
    println()

    // 2. Filtrar tareas completadas
    println("----- Tareas completadas -----")
    val tareasCompletadas = tareas.filter { it.completado }
    if (tareasCompletadas.isNotEmpty()) {
        tareasCompletadas.forEach { tarea ->
            println("La tarea ${tarea.nombre} se completo en ${tarea.horasEstimadas} horas")
        }
    } else {
        println("No hay ninguna tarea completada")
    }
    println()

    // 3. Filtrar tareas pendientes
    println("----- Tareas pendientes -----")
    val tareasPendientes = tareas.filter { !it.completado }
    if (tareasPendientes.isNotEmpty()) {
        tareasPendientes.forEach { tarea ->
            println("La tarea ${tarea.nombre} le faltan ${tarea.horasEstimadas} horas para finalizarla")
        }
    } else {
        println("No hay ninguna tarea pendiente")
    }
    println()

    // 4. Mostrar solo los nombres usando map()
    println("----- Nombres de las tareas -----")
    val nombresTareas = tareas.map { it.nombre }
    nombresTareas.forEach { nombre ->
        println("Tarea: $nombre")
    }
    println()

    // 5. Obtener tareas con prioridad alta (>= 8)
    println("----- Tareas con prioridad alta -----")
    val prioridadAlta = tareas.filter { it.prioridad >= 8 }
    if (prioridadAlta.isNotEmpty()) {
        prioridadAlta.forEach { tarea ->
            println("Tarea: ${tarea.nombre} tiene una prioridad de ${tarea.prioridad}")
        }
    } else {
        println("No hay tareas con alta prioridad")
    }
    println()

    // 6. Ordenar tareas por prioridad de mayor a menor
    println("----- Ordenar tareas por prioridad (mayor a menor) -----")
    val tareasOrdenadas = tareas.sortedByDescending { it.prioridad }
    tareasOrdenadas.forEach { tarea ->
        println(
            "${tarea.nombre} " +
                    "| Prioridad: ${tarea.prioridad} " +
                    "| Estado: ${if (tarea.completado) "Completado" else "Pendiente"}"
        )
    }
    println()

    // 7. Calcular total de horas estimadas usando sumOf()
    println("----- Total de horas estimadas -----")
    val horasTotales = tareas.sumOf { it.horasEstimadas }
    println("El total de horas estimadas es de: $horasTotales horas")
    println()

    // 8. Calcular promedio de horas estimadas
    println("----- Promedio de horas estimadas -----")
    val promedioHoras = tareas.map { it.horasEstimadas }.average()
    println("El promedio de horas por tarea es: ${String.format("%.2f", promedioHoras)} horas")
    println()

    // 9. Buscar una tarea específica usando find()
    println("----- Buscar una tarea -----")
    val nombreBuscado = "Pagar la mensualidad de la universidad"
    val tareaEncontrada = tareas.find { it.nombre == nombreBuscado }
    if (tareaEncontrada != null) {
        println("Tarea encontrada: '${tareaEncontrada.nombre}'")
        println(
            "Prioridad: ${tareaEncontrada.prioridad} " +
                    "| Horas: ${tareaEncontrada.horasEstimadas} " +
                    "| Completada: ${if (tareaEncontrada.completado) "SI" else "NO"}"
        )
    } else {
        println("No se encontro ninguna tarea: '$nombreBuscado'")
    }
    println()

    // 10. Usar la high-order function procesoTareas()
    println("----- Usando la funcion High-Order para el proceso de las tareas -----")
    println("Procesando tareas pendientes:")
    procesoTareas(tareasPendientes) { tarea ->
        println("Tarea pendiente: ${tarea.nombre} (Prioridad: ${tarea.prioridad})")
    }
    println()

    // 11. Usar la funcion copy() para una nueva version de una tarea
    println("----- Usar funcion copy() para actualizar una tarea -----")
    val tareaOriginal = tareas.find { it.nombre == "Comprar la despensa de la semana" }
    if (tareaOriginal != null) {
        // Crear una copia modificada
        val tareaActualizada = tareaOriginal.copy(completado = true)

        println("Tarea original:")
        println("- ${tareaOriginal.nombre} | Completada: ${if (tareaOriginal.completado) "SI" else "NO"}")

        println("Tarea actualizada (usando copy):")
        println("- ${tareaActualizada.nombre} | Completada: ${if (tareaActualizada.completado) "SI" else "NO"}")

        println("La tarea original NO se modifico (inmutabilidad demostrada)")
    } else {
        println("No se encontro la tarea a actualizar")
    }
    println()

}