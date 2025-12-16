package com.example.afrodisiaco.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StatsScreen() {
    val data = listOf(
        "Bodas" to 0.8f,
        "Fies." to 0.6f,
        "Corp." to 0.4f,
        "Otros" to 0.2f
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Estadísticas de Eventos", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(32.dp))

        Card(modifier = Modifier.fillMaxWidth().height(300.dp)) {
            Row(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                data.forEach { (label, percentage) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // Barra del gráfico
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .fillMaxHeight(percentage) // Altura basada en el porcentaje
                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(label)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Total de servicios este mes: 24", style = MaterialTheme.typography.titleMedium)
    }
}