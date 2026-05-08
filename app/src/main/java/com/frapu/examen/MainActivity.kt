package com.frapu.examen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GuessNumberApp()
                }
            }
        }
    }
}

@Composable
fun GuessNumberApp() {
    // 1. Estado para el número aleatorio (entre 0 y 100)
    var targetNumber by remember { mutableIntStateOf(Random.nextInt(0, 101)) }

    // 2. Estado para el texto que ingresa el usuario
    var inputText by remember { mutableStateOf("") }

    // 3. Estados para manejar el Dialog
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Adivina el número (0 - 100)",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(24.dp))

        // TextField configurado para aceptar solo números
        TextField(
            value = inputText,
            onValueChange = { newValue ->
                // Filtro para asegurar que SOLO se ingresen dígitos
                if (newValue.all { it.isDigit() }) {
                    inputText = newValue
                }
            },
            label = { Text("Ingresa un número") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Button para comprobar el número
        Button(onClick = {
            val guess = inputText.toIntOrNull()
            if (guess != null) {
                // Lógica de comparación
                dialogMessage = when {
                    guess > targetNumber -> "El número ingresado ($guess) es MAYOR al número secreto."
                    guess < targetNumber -> "El número ingresado ($guess) es MENOR al número secreto."
                    else -> {
                        // Si adivina, se genera un nuevo número automáticamente
                        targetNumber = Random.nextInt(0, 101)
                        "¡Felicidades! $guess es el número correcto. \nSe ha generado un nuevo número para jugar otra vez."
                    }
                }
                showDialog = true
            } else {
                dialogMessage = "Por favor, ingresa un número válido."
                showDialog = true
            }
        }) {
            Text("Comprobar")
        }
    }

    // Componente Dialog para mostrar el resultado
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Resultado") },
            text = { Text(dialogMessage) },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    // Si el usuario ganó, limpiamos el TextField para el nuevo juego
                    if (dialogMessage.contains("Felicidades")) {
                        inputText = ""
                    }
                }) {
                    Text("Aceptar")
                }
            }
        )
    }
}