package com.example.actividad_1

//Data class para estudiantes
data class Student(
    val nombre: String,
    val grado: Double?,
    val email: String?,
    val acesor: String?,
    val telefono: String?,
    val matriculado: Boolean
)

fun main(){
    println("----- Actividad 1: Sistema de registro de estudiantes -----")

    //1. Registrar al menos 10 estudiantes
    val students = listOf(
        Student("Ana Martínez", 9.5, "ana@email.com", "Dr. Pérez", "555-0101", true),
        Student("Carlos López", 8.2, null, "Dra. Gómez", "555-0102", true),
        Student("María García", null, "maria@email.com", null, "555-0103", true),
        Student("Jorge Rodríguez", 6.5, "jorge@email.com", "Dr. López", null, false),
        Student("Laura Fernández", 9.0, null, null, "555-0105", true),
        Student("Diego Sánchez", 7.8, "diego@email.com", "Dra. Martínez", null, true),
        Student("Valentina Ruiz", null, null, "Dr. Sánchez", "555-0107", false),
        Student("Andrés Torres", 5.5, "andres@email.com", null, "555-0108", true),
        Student("Camila Herrera", 10.0, "camila@email.com", "Dra. Pérez", "555-0109", true),
        Student("Luis Mendoza", null, "luis@email.com", "Dr. Gómez", null, true),
        Student("Fernanda Castro", 8.9, null, "Dra. López", "555-0111", true),
        Student("Ricardo Peña", 7.2, "ricardo@email.com", null, null, false)
    )

    //2. Mostrar la información completa de cada estudiante
    println("----- Informacion completa de los estudiantes -----")
    students.forEach { student -> println("Estudiante: ${student.nombre}")

    //3. Si un estudiante no tiene correo, mostrar: "Correo eletronico no registrado"
    val emailText = student.email ?: "Correo eletronico no registrado"
    println("El correo del estudiante es: ${emailText}")

    //4. Si un estudiante no tiene asesor, mostrar "Sin asesor asignado"
    val asesorText = student.acesor ?: "Sin acesor asignado"
    println("El asesor del estudiante es: ")

    //5. Si un estudiante no tiene telefono, mostrar: "Telefono no registrado"
    val telefonoText = student.telefono ?: "Telefono no registrado"
    println("El telefono del estudiante es: ${telefonoText}")

    //6. Si un estudiante no tiene calificación, mostrar: "Calificación pendiente"
    val gradoText = student.grado?.let {
        "$it - ${getGradoClasificacion(it)}"
        } ?: "Calificacion pendiente"
        println("La calificacion del estudiante es: ${gradoText}")

    //Saber si el estudiante esta inscrito o no
    val matriculaStatus = if (student.matriculado) "Estudiante inscrito" else "Estudiante NO inscrito"
    println("Estado del estudiante: ${matriculaStatus}")
    println()
    }

    //7. Clasificar a los estudiantes
    println("----- Clasificacion de estudiantes -----")
    students.forEach { student -> val clasificacion = student.grado?.let { grado ->
        when {
            grado >= 9.0 -> "Excelente"
            grado >= 8.0 -> "Bueno"
            grado >= 7.0 -> "Regular"
            else -> "Estudiante en riesgo"
        }
      } ?: "Evaluacion pendiente"
        println("${student.nombre}: $clasificacion")
    }
    println()

    //8. Calcular el promedio solo de los estudiantes que si tienen calificación
    println("----- Promedio general de estudiantes con calificacion -----")
    val estudianteGrado = students.filter { it.grado != null }
    val promedioGrado = estudianteGrado.map { it.grado ?: 0.0 }.average()

    println("Estudiantes con calificacion: ${estudianteGrado.size}")
    println("Promedio general: ${String.format("%.2f", promedioGrado)}")
    println()

    //9. Mostrar una lista de estudiantes con información incompleta
    println("----- Estudiantes con informacion incompleta -----")
    val estudiantesIncompletos = students.filter { student ->
        student.grado == null || student.email == null || student.acesor == null || student.telefono == null }
        estudiantesIncompletos.forEach { student -> println("- ${student.nombre}")

            if (student.grado == null) println(" - Calificacion pendiente")
            if (student.email == null) println(" - Correo eletronico no registrado")
            if (student.acesor == null) println(" - Sin asesor asignado")
            if (student.telefono == null) println(" - Telefono no registrado")
        }
    println()

    //10. Mostrar un resumen final
    println("----- Resumen final -----\n")
    val gradoEstudiante = students.filter { it.grado == null }
    val emailEstudiante = students.filter { it.email == null }
    val asesorEstudiante = students.filter { it.acesor == null }
    val telefonoEstudiante = students.filter { it.telefono == null }

    println("Total de estudiantes: ${students.size}")
    println("Estudiantes con calificacion: ${estudianteGrado.size}")
    println("Estudiantes sin calificacion: ${gradoEstudiante.size}")
    println("Estudiantes sin correo: ${emailEstudiante.size}")
    println("Estudiantes sin acesor: ${asesorEstudiante.size}")
    println("Estudiantes sin telefono: ${telefonoEstudiante.size}")
    println("Promedio general: ${String.format("%.2f", promedioGrado)}")
}
fun getGradoClasificacion(grado: Double): String{
    return when {
        grado >= 9.0 -> "Excelente"
        grado >= 8.0 -> "Bueno"
        grado >= 7.0 -> "Regular"
        else -> "Estudiante en riesgo"
    }
}