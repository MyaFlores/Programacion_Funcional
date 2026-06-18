package com.example.ejemplo1

import kotlin.random.Random

fun main() {
    // ===== PARTE 1: Variables básicas =====
    val nombre: String = "Raul"
    var edad: Int = 20  // Variable mutable para la edad
    val calificacion = 50
    val numero = 2

    println(nombre)
    println(edad)
    println("Mi nombre es $nombre, y tengo $edad años")

    // ===== PARTE 2: Trabajando con listas (Programación Funcional) =====
    val colores: List<String> = listOf("Rojo", "Naranja", "Amarillo", "Verde", "Azul", "Morado")
    println("Lista completa: $colores")
    println("Color especifico: ${colores[2]}")

    // ===== PARTE 3: Estructuras de control corregidas =====
    // IF tradicional corregido
    if (edad >= 18) {
        println("Eres mayor de edad")
    } else {
        println("Eres menor de edad")
    }

    // Versión funcional usando expresión when (más elegante)
    val mensajeEdad = when {
        edad >= 18 -> "Eres mayor de edad"
        edad >= 13 -> "Eres adolescente"
        else -> "Eres menor de edad"
    }
    println(mensajeEdad)

    //Calificaciones
    val mensajeCali = when {
        calificacion in 0..69 -> "No pasaste la materia"
        calificacion in 70..89 -> "Pasaste la materia"
        calificacion in 90..100 -> "Felicidades, sacaste muy buenas calificaciones"
        else -> "Valor no valido, ingresa un nuero"
    }
    println(mensajeCali)

    //FOR
    for (numero in 1..10 step 2) {
        println(numero)
    }

    for (numero in 10 downTo 1){
        println(numero)
    }

    // VERSIÓN CORRECTA 1: Solo mostrando los datos
    println("Ingresa tu nombre: ")
    val nombre1 = readln()  // readln() devuelve String

    println("Ingresa tu edad: ")
    val edad1 = readln()     // Esto es String, no número

    println("Mi nombre es $nombre1, y tengo $edad1 años")

}