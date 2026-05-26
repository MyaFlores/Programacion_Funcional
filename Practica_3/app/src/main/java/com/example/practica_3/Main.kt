package com.example.practica_3

//Data Class de Alumnos
data class Alumno(
    val nombre: String,
    val activo: Boolean,
    val edad: Int,
    val grupo: String,
    val matricula: Int
)

fun main(){
    //Funcion normal
    fun saludar (nombre: String){
        println("Hola $nombre")
    }

    //Funcion con parametro
    fun suma(num1: Int, num2: Int): Int{
        return num1 + num2
    }

    //Funcion de orden superior (Lambda)
    fun operacion(
        num1: Int,
        num2: Int,
        calculo: (Int, Int) -> Int
    ): Int {
        return calculo(num1, num2)
    }

    val saludo = {println("Hola")}
    val numCubo = {num: Int -> num * num * num}
    val resultado = operacion(5, 7){x, y -> x+y}

    //Listas
    val colores: List<String> = listOf(
        "Rojo", "Amarillo", "Azul", "Verde", "Morado",
        "Naranja", "Cian", "Negro", "Cafe", "Blanco",
        "Magenta", "Rosa", "Amarrillo Limon", "Gris", "Lavanda"
        )
    val numeros: List<Int> = listOf(
        1,2,3,4,5,
        6,7,8,9,10,
        11,12,13,14,15
    )

    val numerosPares = numeros.filter { it % 2 == 0 }

    println("Lista de colores")
    colores.forEach { println(it) }

    val nuevaLista = numeros.map { it + 3 }
    val total = nuevaLista.reduce { ac, valor -> ac + valor }
    val coloresOrdenados = colores.sorted()
    val numeroEncontrado = numeros.find { it > 5 }

    //Lista de alumnos
    val ListaAlumnos = listOf(
        Alumno(
            "Raul Flores Castañon",
            false,
            21,
            "6CPGM",
            20230059,
        ),
        Alumno(
            "Christina Abigail Zazuete Reyes",
            true,
            20,
            "6APGM",
            20230060
        ),
        Alumno(
            "Carlos Andres Ramirez Ulloa",
            true,
            20,
            "6APGM",
            20230061
        )
    )
    val AlumnosActivos = ListaAlumnos.filter { it.activo }

    //Imprimir los resultados de cada funcion
    println("---Resultados de cada función---")
    saludar("Raul")
    println("El resultado de la suma es: ${suma(5,7)}")
    println("Cubo de 3: ${numCubo(3)}")
    println("Resultado de operacion: $resultado")

    println("---Listas---")
    println("Lista de colores: $colores")
    println("Lista de numeros: $numeros")
    println("Numeros pares: $numerosPares")
    println("Nueva lista: $nuevaLista")
    println("Suma total de la nueva lista: $total")
    println("Colores ordenados: $coloresOrdenados")
    println("Primer numero mayor que 5: $numeroEncontrado")

    println("---Alumnos---")
    println("Lista completa de alumnos")
    ListaAlumnos.forEach { alumno -> println("" +
            "- ${alumno.nombre} " +
            "(Activo: ${alumno.activo}, Edad: ${alumno.edad})")
    }

    println("\nAlumnos activos:")
    if (AlumnosActivos.isNotEmpty()){
        AlumnosActivos.forEach { alumno -> println("- ${alumno.nombre}")
        }
    } else {
        println("No hay alumnos activos")
    }

    println("---Filtrado---")
    println("Total de alumnos: ${ListaAlumnos.size}")
    println("Alumnos activos: ${AlumnosActivos.size}")
    println("Alumnos inactivos: ${ListaAlumnos.size - AlumnosActivos.size}")
}