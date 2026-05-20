package com.example.sistemaestudiante

fun main() {
    // Seccion 1: Informacion general del estudiante
    val nombreCurso: String = "Programacion Funcional"
    val nombreProfesor: String = "Raul Flores Castañon"
    var maximoEstudiantes: Int = 5

    println("--- Sistema de registro de estudiantes ---\n")
    println("Curso: $nombreCurso")
    println("Profesor: $nombreProfesor")
    println("Maximo de estudiantes: $maximoEstudiantes\n")

    // Seccion 2: Registro de estudiantes
    val listaEstudiantes = mutableListOf<String>()

    println("Registro de estudiantes")
    for (i in 1..maximoEstudiantes) {
        println("Ingresa el nombre del estudiante $i: ")
        val nombreEstudiante = readln()
        listaEstudiantes.add(nombreEstudiante)
    }

    println("\n--- Lista completa de los estudiantes ---")
    println(listaEstudiantes)

    // Seccion 3: Registro de calificaciones
    val calificacionesEstudiantes = mutableMapOf<String, Int>()

    println("\n--- Registro de calificaciones ---")
    for (estudiante in listaEstudiantes) {
        println("Ingresa la calificacion de $estudiante: ")
        val calificacion = readln().toInt()
        calificacionesEstudiantes[estudiante] = calificacion
    }

    // Seccion 4: Evaluacion de estudiantes
    println("\n--- Resultados de la evaluacion ---")
    var aprobados = 0
    var reprobados = 0
    var sumaCalificaciones = 0

    for (estudiante in listaEstudiantes) {
        val calificacion = calificacionesEstudiantes[estudiante] ?: 0
        sumaCalificaciones += calificacion

        if (calificacion >= 70) {
            println("$estudiante - Calificacion: $calificacion - APROBADO")
            aprobados++
        } else {
            println("$estudiante - Calificacion: $calificacion - REPROBADO")
            reprobados++
        }
    }

    // Seccion 5: Sets (ciudades de origen de estudiantes)
    val ciudadesOrigen = mutableSetOf<String>()

    println("\n--- Registro de las ciudades de origen ---")
    for (estudiante in listaEstudiantes) {
        println("Ingresa la ciudad de origen de $estudiante: ")
        val ciudad = readln()
        ciudadesOrigen.add(ciudad)
    }

    println("\n--- Ciudades registradas ---")
    println("Todas las ciudades: $ciudadesOrigen")
    println("Cantidad de ciudades diferentes: ${ciudadesOrigen.size}")

    // Seccion 6: Resultados finales
    val promedioGeneral = sumaCalificaciones.toDouble() / listaEstudiantes.size

    println("\n--- Resultados finales de la actividad ---")
    println("Nombre del curso: $nombreCurso")
    println("Nombre del profesor: $nombreProfesor")
    println("Total de estudiantes registrados: ${listaEstudiantes.size}")
    println("Lista completa de estudiantes: $listaEstudiantes")
    println("Promedio general del grupo: ${String.format("%.2f", promedioGeneral)}")
    println("Cantidad de estudiantes aprobados: $aprobados")
    println("Cantidad de estudiantes reprobados: $reprobados")
}