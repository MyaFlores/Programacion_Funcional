package com.example.practica_4
//Uso de Nullability
fun main(){
    //Si usamos el signo de ?, esa variable si puede aceptar valores nulos
    val nombre : String? = "Maria"
    println(nombre?.length)

    //Ejercicio 1 - Operador Elvis
    val nombreFinal = nombre ?: "Hay un valor nulo"
    println("Ejercicio 1: $nombreFinal")

    //Ejercicio 2 - Hacer una variable anulable que se llame userName, si tiene un valor imprime el nombre del usuario y si es null que imprima que es un usuario invitado
    val userName : String? = "Raul" //Esta variable puedes cambiarla para que reciba un valor nulo o un valor definido
    val resultadoUserName = userName?: "Usuario invitado"
    println("Ejercicio 2: $resultadoUserName")
    println()

    //Ejercicio 3 - Variable que sea anulable que se llame mensaje y mostraremos cuantos caracteres tiene el mensaje del ejercicio 0, si no hay mensaje avisamos que no hay mensaje disponible
    val mensaje : String? = null //Esta variable puedes cambiarla para que reciba un valor nulo o un valor definido
    val caracteres = mensaje?.length ?: 0
    val resultadoMensaje = if(mensaje != null){
        "El mensaje tiene esta cantidad de caracteres: $caracteres"
    } else {
        "No hay mensaje disponible"
    }
    println("Ejercicio 3: $resultadoMensaje")

    //Ejercicio 4 - Variable que recibe valores nulos, si el correo existe mostramos un mensaje de exito, si no existe mostramos un mensaje de negacion
    val email : String? = "ejemplo01@gmail.com" //Esta variable puedes cambiarla para que reciba un valor nulo o un valor definido
    val resultadoEmail = if(email != null){
        "El correo $email existe"
    } else {
        "No existe el correo"
    }
    println("Ejercicio 4: $resultadoEmail")
}
