package com.example.afrodisiaco.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ContactScreen() {
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Contáctanos", style = MaterialTheme.typography.headlineMedium)
        Text("Estamos disponibles 24/7 para tus eventos.", color = Color.Gray)
        Spacer(modifier = Modifier.height(24.dp))

        ContactButton("WhatsApp", Icons.Default.Phone, Color(0xFF25D366)) {
            val url = "https://api.whatsapp.com/send?phone=525551234567"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            context.startActivity(intent)
        }

        ContactButton("Enviar Correo", Icons.Default.Email, Color(0xFFEA4335)) {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:contacto@soundlightpro.com")
                putExtra(Intent.EXTRA_SUBJECT, "Informes sobre evento")
            }
            context.startActivity(intent)
        }

        ContactButton("Instagram", Icons.Default.Phone, Color(0xFFE1306C)) { // Usando icono genérico
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com"))
            context.startActivity(intent)
        }
    }
}

@Composable
fun ContactButton(text: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = color)
    ) {
        Icon(icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text)
    }
}