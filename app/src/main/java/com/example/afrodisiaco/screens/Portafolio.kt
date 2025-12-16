package com.example.afrodisiaco.screens

import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.afrodisiaco.LockScreenOrientation
import com.example.afrodisiaco.MainViewModel

@Composable
fun PortfolioScreen(viewModel: MainViewModel) {
    // 1. FORZAR HORIZONTAL
    // Al entrar a esta pantalla, el celular se girará automáticamente.
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

    // Datos de prueba (Simulando tu portafolio)
    val portfolioItems = listOf(
        PortfolioItem("Boda Elegante", "Jardín Las Rosas", "https://images.unsplash.com/photo-1519741497674-611481863552?q=80&w=1080"),
        PortfolioItem("Concierto Rock", "Estadio Azul", "https://images.unsplash.com/photo-1470225620780-dba8ba36b745?q=80&w=1080"),
        PortfolioItem("Fiesta Neon", "Club 54", "https://images.unsplash.com/photo-1566737236500-c8ac43014a67?q=80&w=1080"),
        PortfolioItem("Evento Corporativo", "Hotel Hilton", "https://images.unsplash.com/photo-1505373877841-8d25f7d46678?q=80&w=1080"),
        PortfolioItem("Iluminación Arquitectónica", "Museo Soumaya", "https://images.unsplash.com/photo-1517457373958-b7bdd4587205?q=80&w=1080"),
        PortfolioItem("DJ Set en Vivo", "Playa del Carmen", "https://images.unsplash.com/photo-1571266028243-3716f02d2d2e?q=80&w=1080")
    )

    // Estado para el modal de "Zoom" de imagen
    var selectedImage by remember { mutableStateOf<PortfolioItem?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp) // Menos padding en horizontal para aprovechar espacio
    ) {
        Column {
            // Título
            Text(
                text = "Nuestro Portafolio",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Desliza para ver nuestros eventos recientes",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // GRILLA ADAPTATIVA
            // En horizontal cabrán 3 columnas, en vertical (si girara) cabrían 2.
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 200.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp) // Espacio para el FAB
            ) {
                items(portfolioItems) { item ->
                    PortfolioCard(item) {
                        selectedImage = item
                    }
                }
            }
        }
    }

    // Modal de Zoom (Si tocas una foto)
    if (selectedImage != null) {
        Dialog(onDismissRequest = { selectedImage = null }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(300.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box {
                    AsyncImage(
                        model = selectedImage!!.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    // Botón cerrar
                    IconButton(
                        onClick = { selectedImage = null },
                        modifier = Modifier.align(Alignment.TopEnd).padding(8.dp)
                    ) {
                        Icon(Icons.Default.ZoomIn, "Cerrar", tint = Color.White)
                    }
                    // Título abajo
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.6f))
                            .padding(16.dp)
                    ) {
                        Text(selectedImage!!.title, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// Modelo de datos simple
data class PortfolioItem(val title: String, val location: String, val imageUrl: String)

@Composable
fun PortfolioCard(item: PortfolioItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp) // Altura fija para que se vean uniformes
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box {
            // Imagen de fondo
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradiente negro abajo para que el texto se lea bien
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 100f
                        )
                    )
            )

            // Textos
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(12.dp)
            ) {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.location,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
            }
        }
    }
}