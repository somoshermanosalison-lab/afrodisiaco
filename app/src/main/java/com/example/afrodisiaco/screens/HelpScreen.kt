package com.example.afrodisiaco.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun HelpScreen() {
    val faqs = listOf(
        "¿Cómo reservo un evento?" to "Ve a la pestaña 'Agendar', selecciona una fecha disponible, elige tu paquete y llena el formulario.",
        "¿Qué métodos de pago aceptan?" to "Aceptamos transferencias bancarias, tarjetas de crédito y efectivo.",
        "¿Puedo cancelar mi reserva?" to "Sí, puedes cancelar hasta 48 horas antes del evento sin costo."
    )

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item { Text("Ayuda / Cómo hacerlo", style = MaterialTheme.typography.headlineMedium) }
        item { Spacer(modifier = Modifier.height(16.dp)) }

        items(faqs.size) { index ->
            FAQItem(question = faqs[index].first, answer = faqs[index].second)
        }
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { expanded = !expanded }) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(question, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Icon(if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown, contentDescription = null)
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(answer)
            }
        }
    }
}