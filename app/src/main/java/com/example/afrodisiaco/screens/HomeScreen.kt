package com.example.afrodisiaco.screens

import com.example.afrodisiaco.MainViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun HomeScreen(viewModel: MainViewModel, onNavigate: (String) -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        // --- HERO SECTION ---
        Box(modifier = Modifier.height(500.dp).fillMaxWidth()) {
            AsyncImage(
                model = "https://images.unsplash.com/photo-1702087277678-5eab7d115668?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&q=80&w=1080",
                contentDescription = "Concert",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Gradiente oscuro encima de la imagen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent, MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = viewModel.t("welcome"),
                    color = MaterialTheme.colorScheme.tertiaryContainer, // Color accent simulado
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = viewModel.t("soundLight"),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = viewModel.t("slogan"),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(onClick = { onNavigate("booking") }) {
                        Text(viewModel.t("bookNow"))
                    }
                    OutlinedButton(
                        onClick = { onNavigate("packages") },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Text(viewModel.t("packages"))
                    }
                }
            }
        }

        // --- SERVICES SECTION ---
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = viewModel.t("ourServices"),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp).align(Alignment.CenterHorizontally)
            )

            val services = listOf(
                Triple(Icons.Default.Radio, "professionalSound", "https://images.unsplash.com/photo-1705930927973-29e7059477ad?q=80&w=1080"),
                Triple(Icons.Default.Lightbulb, "eventLighting", "https://images.unsplash.com/photo-1566904501875-35009b7075fb?q=80&w=1080"),
                Triple(Icons.Default.MusicNote, "djService", "https://images.unsplash.com/photo-1758550445980-4d099c6de8d8?q=80&w=1080")
            )

            services.forEach { (icon, titleKey, imageUrl) ->
                ServiceCard(icon, viewModel.t(titleKey), viewModel.t("${titleKey}Desc"), imageUrl)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // --- CTA SECTION ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.secondary)
                    )
                )
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = viewModel.t("bookYourEvent"),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = viewModel.t("slogan"),
                    color = Color.White.copy(alpha = 0.9f),
                    modifier = Modifier.padding(vertical = 16.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = { onNavigate("booking") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(viewModel.t("bookNow"))
                }
            }
        }
    }
}

@Composable
fun ServiceCard(icon: ImageVector, title: String, desc: String, imageUrl: String) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Box(modifier = Modifier.height(200.dp)) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.3f)))

                // Icono flotante
                Box(
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.BottomStart)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                        .padding(8.dp)
                ) {
                    Icon(icon, contentDescription = null, tint = Color.White)
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = desc, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}