package com.example.avance_1
//Avance 1 - Procesamiento funcional de pedidos

//Cliente
data class Client(
    val id: Int,
    val name: String,
    val email: String,
    val phone: String
)

//Producto
data class Product(
    val id: Int,
    val name: String,
    val category: String,
    val price: Double
)

//Artículo del pedido (producto más su cantidad)
data class OrderItem(
    val product: Product,
    val quantity: Int
){
    //Propiedad calculada: subtotal del artículo
    val subtotal: Double
        get() = product.price * quantity
}

//Pedido
data class Order(
    val id: Int,
    val client: Client,
    val items: List<OrderItem>,
    val status: String //Manejara estados como "Pendiente", "Enviado", "Entregado", "Cancelado"
){
    //Monto total del pedido
    val totalPedido: Double
        get() = items.sumOf { it.subtotal }
}

// Cargar datos manualmente
fun main(){ //Abrimos Main
    println("----- Sistema de procesamiento de pedidos -----\n")

    //Clientes
    val clients = listOf(
        Client(1, "Juan Pérez", "juan.Perez@gmail.com", "555-1234"),
        Client(2, "María Gómez", "maria.Gomez@gmail.com", "555-5678"),
        Client(3, "Carlos López", "carlos.lopez@gmail.com", "555-8765"),
        Client(4, "Ana Martínez", "ana.martinez@gmail.com", "555-4321"),
        Client(5, "Luis Fernández", "luis.fernandez@gmail.com", "555-9876"),
        Client(6, "Laura Rodríguez", "laura.rodriguez@gmail.com", "555-3456"),
        Client(7, "Pedro Sánchez", "pedro.sanchez@gmail.com", "555-6543")
    )

    //Productos
    val products = listOf(
        Product(1, "Laptop Gamer X1", "Electrónica", 15000.0),
        Product(2, "Mouse Inalámbrico", "Electrónica", 450.0),
        Product(3, "Teclado Mecánico", "Electrónica", 1200.0),
        Product(4, "Monitor 24 pulgadas", "Electrónica", 3200.0),
        Product(5, "Camiseta Deportiva", "Ropa", 350.0),
        Product(6, "Pantalón de mezclilla", "Ropa", 800.0),
        Product(7, "Zapatos Casual", "Ropa", 1200.0),
        Product(8, "Chaqueta Impermeable", "Ropa", 1800.0),
        Product(9, "Novela de ciencia ficción", "Libros", 280.0),
        Product(10, "Libro de programación Kotlin", "Libros", 550.0),
        Product(11, "Set de tazas", "Hogar", 320.0),
        Product(12, "Lámpara de escritorio", "Hogar", 650.0)
    )

    //Pedidos
    val orders = listOf(
        Order(
            id = 1,
            client = clients[0],
            items = listOf(
                OrderItem(products[0], 1),
                OrderItem(products[1], 2)
            ),
            status = "Entregado"
        ),
        Order(
            id = 2,
            client = clients[1],
            items = listOf(
                OrderItem(products[2], 1),
                OrderItem(products[3], 1)
            ),
            status = "Entregado"
        ),
        Order(
            id = 3,
            client = clients[2],
            items = listOf(
                OrderItem(products[5], 2),
                OrderItem(products[4], 1),
                OrderItem(products[6], 1)
            ),
            status = "Enviado"
        ),
        Order(
            id = 4,
            client = clients[3],
            items = listOf(
                OrderItem(products[8], 1),
                OrderItem(products[9], 1),
            ),
            status = "Pendiente"
        ),
        Order(
            id = 5,
            client = clients[4],
            items = listOf(
                OrderItem(products[10], 3),
                OrderItem(products[11], 1),
            ),
            status = "Entregado"
        ),
        Order(
            id = 6,
            client = clients[5],
            items = listOf(
                OrderItem(products[7], 1),
            ),
            status = "Cancelado"
        ),
        Order(
            id = 7,
            client = clients[6],
            items = listOf(
                OrderItem(products[1], 3),
                OrderItem(products[2], 1),
            ),
            status = "Pendiente"
        ),
    )

    //1. Mostrar todos los pedidos
    println("----- Mostrar todos los pedidos registrados -----")
    orders.forEach { order ->  println("Pedido #${order.id} | Cliente: ${order.client.name} | Estatus: ${order.status} | Total: $${String.format("%.2f", order.totalPedido)}")
    }
    println()

    //2. Mostrar todos los productos disponibles
    println("----- Mostrar los pedidos disponibles -----")
    products.forEach { product -> println("- ${product.name} | Categoria: ${product.category} | Precio: $${String.format("%.2f", product.price)}")
    }
    println()

    //3. Filtrar pedidos por estatus
    println("----- Filtrar pedidos por estatus -----")
    val Estatus = listOf("Pendiente", "Enviado", "Entregado", "Cancelado")
    Estatus.forEach {status -> val Filtrar = orders.filter { it.status == status }
        println("- $status (${Filtrar.size}):")
        Filtrar.forEach { order -> println("- Pedido #${order.id} | ${order.client.name} | $${String.format("%.2f", order.totalPedido)}")
        }
    }
    println()

    //4. Filtrar productos por categoria
    println("----- Filtrar productos por categoria -----")
    val categorias = products.map { it.category }.distinct()
    categorias.forEach { category -> val ProductosCategoria = products.filter { it.category == category }
        println("- $category(${ProductosCategoria.size} productos): ")
        ProductosCategoria.forEach { product -> println("  - ${product.name} | $${String.format("%.2f", product.price)}")
        }
    }
    println()

    //5. Buscar productos por nombre
    println("----- Buscar producto por nombre -----")
    val BuscarProducto = "Libro"
    val EncontrarProducto = products.filter { it.name.contains(BuscarProducto, ignoreCase = true) }
    println("Estamos buscando el producto: '$BuscarProducto'")
    if (EncontrarProducto.isNotEmpty()) {
        EncontrarProducto.forEach { product ->
            println(" -${product.name} | ${product.category} | $${String.format("%.2f", product.price)}")
        }
    }else{
        println("No se encontro un producto con ese nombre")
    }
    println()

    //6. Ordenar pedidos por precio de menor a mayor
    println("----- Ordenar los productos por precio de menor a mayor -----")
    val OrdenarPrecio = products.sortedBy { it.price }
    OrdenarPrecio.forEach { product -> println("- ${product.name} |$${String.format("%.2f", product.price)}")
    }
    println()

    //7. Ordenar pedidos por monto total
    println("----- Pedidos ordenados por monto total de mayor a menor -----")
    val OrdenarResumen = orders.sortedByDescending { it.totalPedido }
    OrdenarResumen.forEach { order -> println("Pedido #${order.id} | ${order.client.name} | Total: $${String.format("%.2f", order.totalPedido)} | Estatus: ${order.status}")
    }
    println()

    //8. Calcular el total vendido considerando solo los pedidos entregados
    println("----- Calcular el total vendido de pedidos entregados -----")
    val EntregaTotal = orders.filter { it.status == "Entregado" }.sumOf { it.totalPedido }
    println("El total de ventas entregadas fueron de: $${String.format("%.2f", EntregaTotal)}")
    println()

    //9. Calcular cuentos pedidos existen por estatus
    println("----- Cantidad de pedidos por estatus -----")
    val OrdenarEstatus = Estatus.associateWith { status -> orders.count{it.status == status}
    }
    OrdenarEstatus.forEach {(status, count) -> println("$status: $count pedido(s)")
    }
    println()

    //10. Mostrar los productos con precio mayor a una cantidad definida
    println("----- Productos con precio mayor -----")
    val PrecioMinimo = 1000.0
    val ProductosCaros = products.filter { it.price > PrecioMinimo }
    if (ProductosCaros.isNotEmpty()) {
        ProductosCaros.forEach { product ->
            println("- ${product.name} | ${product.category} | $${String.format("%.2f", product.price)}")
        }
    }else{
        println("No hay productos con un precio maypr a $${String.format("%.2f", PrecioMinimo)}")
    }
    println()

    //11. Mostrar el cliente asociado a cada pedido
    println("----- Clientes asociados a cada pedido -----")
    orders.forEach { order -> println("Pedido #${order.id} -> ${order.client.name} (${order.client.email})")
    }
    println()

    //12. Generar una lista resumida con el nombre del cliente, total del pedido y estatus
    println("----- Lista resumida de pedidos -----")
    val ResumenPedidos = orders.map { order -> "Cliente: ${order.client.name} | Total: $${String.format("%.2f", order.totalPedido)} | Estatus: ${order.status}"
    }
    ResumenPedidos.forEach { Resumen -> println("- ${Resumen}") }
    println()


} //Cerramos Main