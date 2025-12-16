package com.example.afrodisiaco.screens

import com.example.afrodisiaco.MainViewModel
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.background

@Composable
fun PackagesScreen(viewModel: MainViewModel, onNavigate: (String) -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = viewModel.t("ourPackages"),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = viewModel.t("slogan"),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        val packages = listOf(
            PackageData(
                viewModel.t("basicPackage"), "$299", viewModel.t("basicDesc"),
                "https://images.unsplash.com/photo-1705930927973-29e7059477ad?q=80&w=1080",
                listOf(viewModel.t("soundSystem"), viewModel.t("basicLighting"), "4 ${viewModel.t("hours")}"),
                false
            ),
            PackageData(
                viewModel.t("standardPackage"), "$599", viewModel.t("standardDesc"),
                "https://images.unsplash.com/photo-1758550445980-4d099c6de8d8?q=80&w=1080",
                listOf(viewModel.t("advancedSound"), viewModel.t("proLighting"), viewModel.t("djIncluded"), "6 ${viewModel.t("hours")}"),
                true // Popular
            ),
            PackageData(
                viewModel.t("premiumPackage"), "$999", viewModel.t("premiumDesc"),
                "https://images.unsplash.com/photo-1566904501875-35009b7075fb?q=80&w=1080",
                listOf(viewModel.t("premiumSound"), viewModel.t("premiumLighting"), viewModel.t("proDJ"), viewModel.t("effects"), "8 ${viewModel.t("hours")}"),
                false
            )
        )

        packages.forEach { pkg ->
            PackageCard(pkg, onNavigate, viewModel)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

data class PackageData(
    val name: String, val price: String, val description: String,
    val image: String, val features: List<String>, val popular: Boolean
)

@Composable
fun PackageCard(pkg: PackageData, onNavigate: (String) -> Unit, viewModel: MainViewModel) {
    val borderColor = if (pkg.popular) MaterialTheme.colorScheme.primary else Color.Transparent
    val borderWidth = if (pkg.popular) 2.dp else 0.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(borderWidth, borderColor, CardDefaults.shape),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            if (pkg.popular) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Más Popular", color = Color.White, style = MaterialTheme.typography.labelMedium)
                }
            }

            Box(modifier = Modifier.height(200.dp)) {
                AsyncImage(
                    model = pkg.image, contentDescription = null,
                    contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))))
                )
                Column(
                    modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
                ) {
                    Text(pkg.name, color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Text(pkg.price, color = Color.White, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                Text(pkg.description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Spacer(modifier = Modifier.height(16.dp))

                pkg.features.forEach { feature ->
                    Row(modifier = Modifier.padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(feature)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = { onNavigate("booking") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = if (pkg.popular) ButtonDefaults.buttonColors() else ButtonDefaults.filledTonalButtonColors()
                ) {
                    Text(viewModel.t("selectPackage"))
                }
            }
        }
    }
}