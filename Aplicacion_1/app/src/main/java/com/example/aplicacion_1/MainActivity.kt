package com.example.aplicacion_1


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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aplicacion_1.ui.theme.Aplicacion_1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

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

    Surface() {
       modifier = Modifier.fillMaxSize(),
       color = MaterialTheme.colorScheme.background

        Column() {
            modifier = Modifier.padding(24.dp),
                .fillMaxWidth()
            horizontalAlignment = Alignment.CenterHorizontally
            Text("Generador de numeros aleatorios"),
            style = MaterialTheme.typography.headlineSmall
            Spacer(modifier.Modifier.Height(24.dp))
            OutlinedTextField(
                value = minInput,
                onValueChange = minInput = it
                label = (Text("Valor minimo")),
                keyboardOptions =
            )
        }
    }



}


