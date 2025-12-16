package com.example.afrodisiaco.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp // Importación correcta para ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.afrodisiaco.MainViewModel

@Composable
fun AppDrawerContent(
    viewModel: MainViewModel,
    currentRoute: String?,
    onNavigate: (String) -> Unit,
    onClose: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    ModalDrawerSheet {
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Sound & Light Pro",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp)
        )
        HorizontalDivider() // Versión moderna de Divider
        Spacer(Modifier.height(16.dp))

        // LISTA DE OPCIONES DEL MENÚ
        val menuItems = listOf(
            Triple("home", "home", Icons.Default.Home),
            Triple("packages", "packages", Icons.Default.Inventory),
            Triple("booking", "booking", Icons.Default.DateRange),
            // IMPORTANTE: "portfolio" en minúscula para que coincida con MainActivity
            Triple("portfolio", "portfolio", Icons.Default.Work),
            Triple("about", "about", Icons.Outlined.Info),
            Triple("contact", "contact", Icons.Default.Email),
            // Opciones nuevas (texto directo porque no están en traducciones todavía)
            Triple("stats", "Estadísticas", Icons.Default.BarChart),
            Triple("settings", "Configuración", Icons.Default.Settings),
            Triple("help", "Ayuda", Icons.Default.Help)
        )

        // Generamos los botones
        menuItems.forEach { (id, labelKeyOrText, icon) ->
            // Si el texto es una de las claves antiguas, lo traducimos. Si es nuevo, lo mostramos tal cual.
            val labelText = if (labelKeyOrText in listOf("Estadísticas", "Configuración", "Ayuda")) {
                labelKeyOrText
            } else {
                viewModel.t(labelKeyOrText)
            }

            NavigationDrawerItem(
                label = { Text(labelText) },
                icon = { Icon(icon, contentDescription = null) },
                selected = currentRoute == id,
                onClick = {
                    onNavigate(id)
                    onClose()
                },
                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        HorizontalDivider()

        // --- PIE DE PÁGINA (Tema, Idioma, Logout) ---
        Column(modifier = Modifier.padding(16.dp)) {

            // Selector de Tema (Oscuro/Claro)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (uiState.theme == "light") Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (uiState.theme == "light") "Modo Claro" else "Modo Oscuro")
                }
                Switch(
                    checked = uiState.theme == "dark",
                    onCheckedChange = { isDark -> viewModel.setTheme(if (isDark) "dark" else "light") }
                )
            }

            Spacer(Modifier.height(16.dp))

            // Selector de Idioma (ES/EN)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(if (uiState.language == "es") "Español" else "English")
                }
                Switch(
                    checked = uiState.language == "en",
                    onCheckedChange = { isEn -> viewModel.setLanguage(if (isEn) "en" else "es") }
                )
            }

            Spacer(Modifier.height(16.dp))

            // Botón de Cerrar Sesión
            Button(
                onClick = {
                    viewModel.logout()
                    onClose()
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Usamos la versión AutoMirrored para evitar errores en versiones nuevas
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text(viewModel.t("logout"))
            }
        }
    }
}