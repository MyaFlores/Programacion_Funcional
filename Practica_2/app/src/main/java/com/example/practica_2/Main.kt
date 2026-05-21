package com.example.practica2.com.example.practica_2

fun main(){
    //Lista con las materias que estamos mirando este cuatrimestre
    //Mini proyecto 1: nombre y edad
    val nombre: String = "Raul"

    println("Ingresa tu edad: ")
    val edad = readln().toInt()

    println("Hola, soy $nombre, y tengo $edad años")

    //Mini proyecto 2: materias del cuatrimestre
    val materias : List<String> = listOf(
        "Programacion funcional",
        "Desarrollo de Software Backend III",
        "Desarrollo de Software Frontend II",
        "Desarrollo de Aplicaciones Moviles",
        "Programacion Orientada a Pruebas")

    println("\nLas materias que llevo en este cuatrimestre son:")
    println(materias[0])
    println(materias[1])
    println(materias[2])
    println(materias[3])
    println(materias[4])


    //Mini proyecto 3: Determinar si nuestra edad en impar o par
    if (edad % 2 == 0){
        println("Tu edad es par")
    } else {
        println("Tu edad es impar")
    }

    //Mini proyecto 4: Ingresar productos
    println("Ingresa el precio de tu producto: ")
    val precio = readln().toInt()

    println("Ingresa la cantidad del producto: ")
    val cantidad = readln().toInt()

    val total = precio * cantidad

    if (total > 100){
        println("Se aplicara el 30% de descuento a su compra: ")
        val totalDescuento = total * 0.7
        println("Compra total con descuento: $totalDescuento")
    } else {
        println("Este es tu resultado final $total")
    }

    //Mini proyecto 5: Lista de Hoobies
    val hoobies = setOf(
        "Leer historias de terror",
        "Jugar Tomodachi Life",
        "Pintar libros de colorear de mandalas",
        "Hacer bordados"
    )
    println(hoobies)

    //Mini proyecto 6: Paises y capitales
    val estudiante = mapOf(
        "Nombre" to "Raul",
        "Carrera" to "Ingenieria en desarrollo de software",
        "Escuela" to "CESUN universidad "
    )
    println(estudiante)

    //Mini proyecto 7: Mapa de paises y capitales
    val capitales = mapOf(
        "Mexico" to "Ciudad de Mexico",
        "Rusia" to "Moscu",
        "Francia" to "Paris",
        "Japon" to "Tokio",
        "Corea del sur " to "Seul"
    )

    //Mini proyecto 8: Mapa de paises y ciudades
    println("Un solo valor")
    val pais = "Francia"
    val Capital =capitales[pais]
    if(Capital !=null){
        println("La capital $pais es $Capital")
    }else{
        println("Pais no encontrado")
    }
    println("Todos los paises y capitales")
    for((paises, capitales) in capitales){
        println("pais: $paises | capital: $capitales")
    }







}