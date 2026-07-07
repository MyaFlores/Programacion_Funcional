package com.example.logic_analyzer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.logic_analyzer.ui.theme.Logic_AnalyzerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Logic_AnalyzerTheme {
                LogicAnalyzerApp()
            }
        }
    }
}

@Composable
fun LogicAnalyzerApp() {
    val booleanList = remember { mutableStateListOf<Boolean>() }
    var inputValue by remember { mutableStateOf("") }
    var showResults by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título de la aplicación
            Text(
                text = "Analizador de expresiones lógicas",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Agrega valores booleanos y analiza los resultados. Puedes utilizar los botones o la caja de texto",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Inputs y botones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = inputValue,
                    onValueChange = { inputValue = it },
                    placeholder = { Text("true / false") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Button(
                    onClick = {
                        when (inputValue.lowercase()) {
                            "true" -> {
                                booleanList.add(true)
                                inputValue = ""
                                showResults = false
                            }
                            "false" -> {
                                booleanList.add(false)
                                inputValue = ""
                                showResults = false
                            }
                            else -> { /* Ignorar entrada inválida */ }
                        }
                    }
                ) {
                    Text("Agregar")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Botones para agregar valores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        booleanList.add(true)
                        showResults = false
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("TRUE")
                }
                OutlinedButton(
                    onClick = {
                        booleanList.add(false)
                        showResults = false
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("FALSE")
                }
                OutlinedButton(
                    onClick = {
                        booleanList.clear()
                        showResults = false
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Limpiar")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar lista de valores
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                if (booleanList.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Lista vacía",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Agrega valores usando los botones",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(8.dp)
                    ) {
                        items(booleanList) { value ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = if (value) "TRUE" else "FALSE",
                                    color = if (value) Color.Green else Color.Red
                                )
                                Text(
                                    text = "Índice: ${booleanList.indexOf(value)}",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón para analizar
            Button(
                onClick = { showResults = true },
                modifier = Modifier.fillMaxWidth(),
                enabled = booleanList.isNotEmpty()
            ) {
                Text("Analizar")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resultados
            if (showResults && booleanList.isNotEmpty()) {
                ResultsCard(booleanList)
            }
        }
    }
}

@Composable
fun ResultsCard(booleanList: List<Boolean>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),  // ← Corregido
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Resultados del análisis",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Análisis funcional
            val trueCount = booleanList.count { it }
            val falseCount = booleanList.count { !it }

            // Porcentajes
            val total = booleanList.size.toDouble()
            val truePercentage = (trueCount / total) * 100
            val falsePercentage = (falseCount / total) * 100

            // all, any, none
            val allTrue = booleanList.all { it }
            val anyTrue = booleanList.any { it }
            val noneTrue = booleanList.none { it }

            // Invertir todos los valores (NOT)
            val invertedList = booleanList.map { !it }

            // Filtrar solo TRUE y solo FALSE
            val onlyTrue = booleanList.filter { it }
            val onlyFalse = booleanList.filter { !it }

            // Cantidades
            ResultRow(label = "Cantidad de TRUE:", value = "$trueCount")
            ResultRow(label = "Cantidad de FALSE:", value = "$falseCount")
            ResultRow(label = "Total de elementos:", value = "${booleanList.size}")

            Spacer(modifier = Modifier.height(8.dp))
            Divider()

            // Porcentajes
            ResultRow(
                label = "Porcentaje de TRUE:",
                value = String.format("%.1f%%", truePercentage)
            )
            ResultRow(
                label = "Porcentaje de FALSE:",
                value = String.format("%.1f%%", falsePercentage)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider()

            // Evaluaciones lógicas
            ResultRow(
                label = "¿Todos son TRUE?",
                value = if (allTrue) "Sí" else "No",
                highlight = allTrue
            )
            ResultRow(
                label = "¿Existe al menos un TRUE?",
                value = if (anyTrue) "Sí" else "No",
                highlight = anyTrue
            )
            ResultRow(
                label = "🔍 ¿Ninguno es TRUE?",
                value = if (noneTrue) "Sí" else "No",
                highlight = noneTrue
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider()

            // Invertir todos los valores (NOT)
            Text(
                text = "Invertir todos los valores (NOT):",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = invertedList.joinToString(", ") { if (it) "TRUE" else "FALSE" },
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider()

            // Mostrar únicamente valores TRUE
            Text(
                text = "Solo valores TRUE:",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = if (onlyTrue.isEmpty()) "No hay valores TRUE"
                else onlyTrue.joinToString(", ") { "TRUE" },
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Mostrar únicamente valores FALSE
            Text(
                text = "Solo valores FALSE:",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = if (onlyFalse.isEmpty()) "No hay valores FALSE"
                else onlyFalse.joinToString(", ") { "FALSE" },
                fontSize = 13.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // fold para demostración
            val trueCountFold = booleanList.fold(0) { acc, value -> acc + if (value) 1 else 0 }
            Text(
                text = "Conteo de TRUE (usando fold): $trueCountFold",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ResultRow(label: String, value: String, highlight: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = if (highlight) FontWeight.Bold else FontWeight.Normal,
            color = if (highlight) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
@Preview(showBackground = true)
@Composable
fun LogicAnalyzerPreview() {
    Logic_AnalyzerTheme {
        LogicAnalyzerApp()
    }
}