package com.example.afrodisiaco.screens

import androidx.compose.ui.graphics.Color
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.afrodisiaco.MainViewModel

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    var showRatingDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Configuración", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        // Opción de Calificar
        Button(onClick = { showRatingDialog = true }, modifier = Modifier.fillMaxWidth()) {
            Text("Calificar Aplicación")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Versión de la App: 1.0.0", color = Color.Gray)
    }

    if (showRatingDialog) {
        RatingDialog(onDismiss = { showRatingDialog = false })
    }
}

@Composable
fun RatingDialog(onDismiss: () -> Unit) {
    var rating by remember { mutableStateOf(0) }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Califícanos") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("¿Qué te parece nuestra app?")
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Default.Star else Icons.Outlined.StarOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(40.dp).clickable { rating = i }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                Toast.makeText(context, "¡Gracias por tus $rating estrellas!", Toast.LENGTH_SHORT).show()
                onDismiss()
            }) { Text("Enviar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}