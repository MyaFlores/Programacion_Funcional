package com.example.examen_parcial1

data class Student(
    val Nombre: String,
    val Calificacion: Int,
    val Faltas: Int
)

fun main(){
    println("---- Sistema de estudiantes ----\n")
    val Students = listOf(
        Student("Mya Flores Castañon",95,1),
        Student("Raul Flores Castañon", 70, 3),
        Student("Azul Ximena Sandoval Laurean", 79, 4),
        Student("Carlos Andres Ramirez Ulloa", 90, 2),
        Student("Christina Abigail Zazueta Reyes", 90, 1),
        Student("Gloria Esperanza Garcia Esperanza", 60, 5),
        Student("Ana Lorena Barajas Ramirez", 65, 1)
    )

    println("---- Mostrar todos los estudiantes ----")
    Students.forEach { student -> println("${student.Nombre} | Calificacion: ${student.Calificacion} | Faltas: ${student.Faltas}") }
    println()

    println("---- Filtrar y mostrar los estudiantes aprobados ----")
    val EstudiantesAprobados = Students.filter {it.Calificacion >= 70}
    if (EstudiantesAprobados.isNotEmpty()) {
        EstudiantesAprobados.forEach { Student -> println("${Student.Nombre}, su calificacion es de: ${Student.Calificacion}") }
    }else{
        println("No hay estudiantes aprobados")
    }
    println()

    println("---- Estudiantes en riesgo si su calificacion es menor a 70 o tiene mas de 3 faltas")
    val EstudianteRiesgo = Students.filter { it.Calificacion < 70 || it.Faltas > 3 }
    if (EstudianteRiesgo.isNotEmpty()) {
        EstudianteRiesgo.forEach { Student -> println("${Student.Nombre}, tiene ${Student.Faltas} faltas y ${Student.Calificacion} de calificacion") }
    }else{
        println("No hay estudiantes en riesgo de reprobar")
    }
    println()

    println("---- Nueva lista con solo nombres usando map() ----")
    val EstudianteNombre = Students.map { it.Nombre }
    EstudianteNombre.forEach { name -> println("- ${name}") }
    println()

    println("---- Promedio general ----")
    val Promedio = Students.map { it.Calificacion }.average()
    println("Promedio del grupo: ${String.format("%.2f", Promedio)}")
    println()

    println("---- Mensaje final ----")
    if (Promedio >= 80){
        println("El rendimiento del grupo es bueno")
    } else {
        println("El grupo necesita mejorar")
    }

}

