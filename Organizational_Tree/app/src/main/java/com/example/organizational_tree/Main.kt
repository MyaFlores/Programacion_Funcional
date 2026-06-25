package com.example.organizational_tree

// Clase de datos

data class Employee(
    val name: String,
    val position: String,
    val email: String? = null,  // Puede ser null (correo no registrado)
    val phone: String? = null
)

// Nodo del árbol
data class EmployeeNode(
    val employee: Employee,
    val children: MutableList<EmployeeNode> = mutableListOf()
)

// Clase de arbol organizacional

class OrganizationalTree {
    var root: EmployeeNode? = null

    // Agregar un empleado como hijo de otro
    fun addEmployee(parentName: String, newEmployee: Employee): Boolean {
        val parentNode = findNode(root, parentName)
        return if (parentNode != null) {
            parentNode.children.add(EmployeeNode(newEmployee))
            true
        } else false
    }

    // Buscar un nodo por nombre (recursivo)
    private fun findNode(node: EmployeeNode?, name: String): EmployeeNode? {
        if (node == null) return null
        if (node.employee.name == name) return node
        for (child in node.children) {
            val found = findNode(child, name)
            if (found != null) return found
        }
        return null
    }

    // Buscar un empleado por nombre (público)
    fun findEmployee(name: String): EmployeeNode? {
        return findNode(root, name)
    }

    // Contar total de empleados (recursivo)
    fun countEmployees(): Int {
        return countNodes(root)
    }

    private fun countNodes(node: EmployeeNode?): Int {
        if (node == null) return 0
        var count = 1
        for (child in node.children) {
            count += countNodes(child)
        }
        return count
    }

    // Calcular la profundidad máxima (niveles)
    fun maxDepth(): Int {
        return calculateDepth(root)
    }

    private fun calculateDepth(node: EmployeeNode?): Int {
        if (node == null) return 0
        if (node.children.isEmpty()) return 1
        var maxChildDepth = 0
        for (child in node.children) {
            val childDepth = calculateDepth(child)
            if (childDepth > maxChildDepth) {
                maxChildDepth = childDepth
            }
        }
        return 1 + maxChildDepth
    }

    // Identificar empleados sin correo
    fun employeesWithoutEmail(): List<Employee> {
        return findWithoutEmail(root)
    }

    private fun findWithoutEmail(node: EmployeeNode?): List<Employee> {
        if (node == null) return emptyList()
        val result = mutableListOf<Employee>()
        if (node.employee.email == null) {
            result.add(node.employee)
        }
        for (child in node.children) {
            result.addAll(findWithoutEmail(child))
        }
        return result
    }

    // Mostrar la estructura del árbol (gráficamente)
    fun displayTree() {
        if (root != null) {
            printTree(root!!, 0)
        } else {
            println("El arbol esta vacío")
        }
    }

    private fun printTree(node: EmployeeNode, level: Int) {
        val indent = "  ".repeat(level)
        val prefix = if (level == 0) "" else "├── "
        val emailText = node.employee.email ?: "Sin correo"
        println("$indent$prefix${node.employee.name} (${node.employee.position}) - $emailText")
        for (child in node.children) {
            printTree(child, level + 1)
        }
    }

    // Recorrido para obtener todos los empleados (para lista tradicional)
    fun getAllEmployees(): List<Employee> {
        return flattenTree(root)
    }

    private fun flattenTree(node: EmployeeNode?): List<Employee> {
        if (node == null) return emptyList()
        val result = mutableListOf(node.employee)
        for (child in node.children) {
            result.addAll(flattenTree(child))
        }
        return result
    }

    // Obtener empleados por nivel (para estadísticas)
    fun getEmployeesByLevel(): Map<Int, List<Employee>> {
        val result = mutableMapOf<Int, MutableList<Employee>>()
        populateByLevel(root, 0, result)
        return result
    }

    private fun populateByLevel(node: EmployeeNode?, level: Int, map: MutableMap<Int, MutableList<Employee>>) {
        if (node == null) return
        map.getOrPut(level) { mutableListOf() }.add(node.employee)
        for (child in node.children) {
            populateByLevel(child, level + 1, map)
        }
    }
}

// ========== FUNCIÓN PRINCIPAL ==========

fun main() {
    println("------ Organizational Tree Explorer ------")

    // Crear el árbol organizacional
    val company = OrganizationalTree()

    // ========== 1. CONSTRUIR LA JERARQUÍA ==========

    // Director General
    val ceo = Employee("Ana Martinez", "Directora General", "ana.ceo@empresa.com", "555-0001")
    company.root = EmployeeNode(ceo)

    // Gerentes
    val gerente1 = Employee("Carlos Lopez", "Gerente de Ventas", "carlos.ventas@empresa.com", "555-1001")
    val gerente2 = Employee("Maria Garcia", "Gerente de Marketing", "maria.marketing@empresa.com", "555-1002")
    val gerente3 = Employee("Jorge Rodriguez", "Gerente de Operaciones", null, "555-1003")

    company.addEmployee("Ana Martinez", gerente1)
    company.addEmployee("Ana Martinez", gerente2)
    company.addEmployee("Ana Martinez", gerente3)

    // Supervisores - Gerente de Ventas
    val supVentas1 = Employee("Laura Fernandez", "Supervisor de Ventas Norte", "laura.ventas@empresa.com", "555-2001")
    val supVentas2 = Employee("Diego Sanchez", "Supervisor de Ventas Sur", null, "555-2002")
    company.addEmployee("Carlos Lopez", supVentas1)
    company.addEmployee("Carlos Lopez", supVentas2)

    // Supervisores - Gerente de Marketing
    val supMarketing1 = Employee("Valentina Ruiz", "Supervisor de Marketing Digital", "valentina.marketing@empresa.com", "555-2003")
    val supMarketing2 = Employee("Andres Torres", "Supervisor de Marketing Tradicional", "andres.marketing@empresa.com", "555-2004")
    company.addEmployee("Maria Garcia", supMarketing1)
    company.addEmployee("Maria Garcia", supMarketing2)

    // Supervisores - Gerente de Operaciones
    val supOperaciones1 = Employee("Camila Herrera", "Supervisor de Logistica", null, "555-2005")
    val supOperaciones2 = Employee("Luis Mendoza", "Supervisor de Produccion", "luis.operaciones@empresa.com", "555-2006")
    company.addEmployee("Jorge Rodriguez", supOperaciones1)
    company.addEmployee("Jorge Rodriguez", supOperaciones2)

    // Empleados - Ventas Norte
    company.addEmployee("Laura Fernandez", Employee("Sofia Ramirez", "Vendedor", "sofia.ventas@empresa.com", "555-3001"))
    company.addEmployee("Laura Fernandez", Employee("Emilio Vega", "Vendedor", null, "555-3002"))

    // Empleados - Ventas Sur
    company.addEmployee("Diego Sanchez", Employee("Daniela Ortiz", "Vendedor", "daniela.ventas@empresa.com", "555-3003"))
    company.addEmployee("Diego Sanchez", Employee("Gabriel Soto", "Vendedor", null, "555-3004"))

    // Empleados - Marketing Digital
    company.addEmployee("Valentina Ruiz", Employee("Isabella Rios", "Analista de Marketing", "isabella.marketing@empresa.com", "555-3005"))
    company.addEmployee("Valentina Ruiz", Employee("Mateo Gonzalez", "Analista de Marketing", null, "555-3006"))

    // Empleados - Marketing Tradicional
    company.addEmployee("Andres Torres", Employee("Lucia Medina", "Analista de Marketing", "lucia.marketing@empresa.com", "555-3007"))
    company.addEmployee("Andres Torres", Employee("Pablo Herrera", "Analista de Marketing", "pablo.marketing@empresa.com", "555-3008"))

    // Empleados - Logística
    company.addEmployee("Camila Herrera", Employee("Renata Vargas", "Coordinador de Logistica", null, "555-3009"))
    company.addEmployee("Camila Herrera", Employee("Tomas Reyes", "Coordinador de Logistica", "tomas.logistica@empresa.com", "555-3010"))

    // Empleados - Producción
    company.addEmployee("Luis Mendoza", Employee("Valeria Castro", "Supervisor de Produccion", "valeria.produccion@empresa.com", "555-3011"))
    company.addEmployee("Luis Mendoza", Employee("Nicolas Silva", "Operario", null, "555-3012"))

    // ========== 2. MOSTRAR ESTRUCTURA GRÁFICAMENTE ==========
    println("------ Estructura organizacional ------\n")
    company.displayTree()
    println()

    // ========== 3. LOCALIZAR EMPLEADO POR NOMBRE ==========
    println("------ Busqueda de un empleado ------\n")
    val searchName = "Maria Garcia"
    val found = company.findEmployee(searchName)
    if (found != null) {
        println("Empleado encontrado:")
        println("  Nombre: ${found.employee.name}")
        println("  Puesto: ${found.employee.position}")
        println("  Correo: ${found.employee.email ?: "No registrado"}")
        println("  Telefono: ${found.employee.phone ?: "No registrado"}")
        println("  Reporta directamente a: ${found.children.size} empleado(s)")
    } else {
        println("No se encontró al empleado '$searchName'")
    }
    println()

    // ========== 4. CONTAR TOTAL DE EMPLEADOS ==========
    println("------ Estadisticas generales ------\n")
    val totalEmployees = company.countEmployees()
    println("Total de empleados: $totalEmployees")

    // ========== 5. DETERMINAR NIVELES ==========
    val depth = company.maxDepth()
    println("Niveles de profundidad: $depth")

    // ========== 6. IDENTIFICAR EMPLEADOS SIN CORREO ==========
    val sinCorreo = company.employeesWithoutEmail()
    println("Empleados sin correo registrado: ${sinCorreo.size}")
    sinCorreo.forEach { emp ->
        println("   - ${emp.name} (${emp.position})")
    }
    println()

    // ========== 7. ESTADÍSTICAS POR ÁREA ==========
    println("------ Estadisticas por area ------\n")
    val byLevel = company.getEmployeesByLevel()
    byLevel.forEach { (level, employees) ->
        val nivelNombre = when (level) {
            0 -> "Direccion General"
            1 -> "Gerentes"
            2 -> "Supervisores"
            3 -> "Empleados"
            else -> "Nivel $level"
        }
        println("- $nivelNombre: ${employees.size} empleado(s)")
        employees.forEach { emp ->
            println("   - ${emp.name} (${emp.position})")
        }
        println()
    }

    // ========== 8. COMPARACIÓN: LISTA vs ÁRBOL ==========
    println("------ Comparacion de lista y arbol ------\n")

    // Representación como lista (aplanada)
    val listaEmpleados = company.getAllEmployees()
    println("Representacion como lista (aplanada):")
    listaEmpleados.forEachIndexed { index, emp ->
        println("   ${index + 1}. ${emp.name} - ${emp.position}")
    }
    println()

    // Representación como árbol (ya mostrada)
    println(" Representacin como arbol (jerarquica):")
    company.displayTree()
    println()

    // ========== 9. ANÁLISIS COMPARATIVO ==========
    println("------ Analisis de estructuras ------\n")
    println("LISTA:")
    println("  - Ventajas: Simple, acceso por indice, facil de iterar")
    println("  - Desventajas: No muestra jerarquías, dificil encontrar jefes/subordinados")
    println()
    println("ARBOL:")
    println("  - Ventajas: Representa jerarquias naturalmente, facil navegacion padre-hijo")
    println("  - Desventajas: Implementacion mas compleja, mayor uso de memoria")
    println()
    println("Para una estructura organizacional, arbol es mas adecuado")
    println("porque refleja naturalmente las relaciones jerarquicas de la empresa")

    // ========== 10. REPORTE FINAL ==========
    println("\n------ Reporte final ------")
    println("RESUMEN EJECUTIVO")
    println("  - Total de empleados: $totalEmployees")
    println("  - Profundidad maxima: $depth niveles")
    println("  - Empleados sin correo: ${sinCorreo.size}")
    println("  - Empleados por nivel:")
    byLevel.forEach { (level, employees) ->
        val nivelNombre = when (level) {
            0 -> "Direccion"
            1 -> "Gerentes"
            2 -> "Supervisores"
            3 -> "Operativos"
            else -> "Nivel $level"
        }
        println("     - $nivelNombre: ${employees.size}")
    }
    println("Arbol construido exitosamente ")
    println("Todas las operaciones recursivas funcionaron correctamente")
}

