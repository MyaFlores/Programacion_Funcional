package com.example.aplicacion_1


import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.example.aplicacion_1.ui.theme.Aplicacion_1Theme
import java.security.KeyStore
import kotlin.math.max
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Aplicacion_1Theme {  // ← Tu tema
                RandomNumberGenerator()  // ← Llamar a tu función
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomNumberGenerator(){
    var minInput by remember { mutableStateOf("") }
    var maxInput by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<Int?>(null) }
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Generador de numeros aleatorios",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = minInput,
                onValueChange = { minInput = it },
                label = {Text("Valor minimo")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = maxInput,
                onValueChange = {maxInput = it},
                label = {Text("Valor maximo")},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val min = minInput.toIntOrNull()
                    val max = maxInput.toIntOrNull()
                    if (min!= null && max != null && min <= max){
                        result = Random.nextInt(min, max + 1)
                        focusManager.clearFocus()
                    }
                    else
                    {
                        result = null

                    }
                }
            ){
                Text("Generar numero")
            }
            Spacer(modifier = Modifier.height(32.dp))
            result.let {
                Text("Numero generado: $it", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun RandomNumberGeneratorPreview() {
    RandomNumberGenerator()
}





