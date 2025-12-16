package com.example.afrodisiaco

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
// IMPORTANTE: FragmentActivity es necesario para la Biometría
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

// Importamos TODAS las pantallas
import com.example.afrodisiaco.screens.*

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializamos el ViewModel que controla toda la app
        val viewModel: MainViewModel by viewModels()

        setContent {
            // Observamos el estado global (tema, idioma, usuario)
            val uiState by viewModel.uiState.collectAsState()
            val isDarkTheme = uiState.theme == "dark"

            // Aplicamos el tema (Oscuro o Claro)
            MaterialTheme(
                colorScheme = if (isDarkTheme) darkColorScheme() else lightColorScheme()
            ) {
                MainAppContent(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppContent(viewModel: MainViewModel) {
    // Variables de estado y navegación
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    // Estado local para controlar la pantalla de Splash (Marca)
    var showSplash by remember { mutableStateOf(true) }

    // --- LÓGICA DE FLUJO DE PANTALLAS ---

    // 1. PANTALLA DE CARGA (SPLASH)
    if (showSplash) {
        SplashScreen(onTimeout = { showSplash = false })
    }
    // 2. TUTORIAL DE BIENVENIDA (ONBOARDING) - Solo si es la primera vez
    else if (uiState.isFirstTime) {
        OnboardingScreen(onFinished = { viewModel.completeOnboarding() })
    }
    // 3. PANTALLA DE LOGIN - Si no ha iniciado sesión
    else if (!uiState.isLoggedIn) {
        LoginScreen(viewModel)
    }
    // 4. APLICACIÓN PRINCIPAL (Sidebar + Contenido)
    else {
        // Obtenemos la ruta actual para saber qué item del menú resaltar
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        // Menú Lateral (Drawer)
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                AppDrawerContent(
                    viewModel = viewModel,
                    currentRoute = currentRoute,
                    onNavigate = { route -> navController.navigate(route) },
                    onClose = { scope.launch { drawerState.close() } }
                )
            }
        ) {
            // Estructura de la pantalla (Scaffold)
            Scaffold(
                // Botón Flotante de Llamada (Abajo a la derecha)
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:+525551234567")
                            }
                            context.startActivity(intent)
                        },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(Icons.Default.Call, contentDescription = "Llamar")
                    }
                }
            ) { innerPadding ->
                Box(modifier = Modifier.padding(innerPadding)) {

                    // --- SISTEMA DE NAVEGACIÓN (LAS PANTALLAS REALES) ---
                    NavHost(navController = navController, startDestination = "home") {

                        // Inicio
                        composable("home") {
                            HomeScreen(viewModel = viewModel, onNavigate = { route -> navController.navigate(route) })
                        }

                        // Paquetes
                        composable("packages") {
                            PackagesScreen(viewModel = viewModel, onNavigate = { route -> navController.navigate(route) })
                        }

                        // Reservas
                        composable("booking") {
                            BookingScreen(viewModel = viewModel)
                        }

                        // Portafolio (Horizontal)
                        composable("portfolio") {
                            PortfolioScreen(viewModel)
                        }

                        // Acerca de (Placeholder)
                        composable("about") {
                            Box(Modifier.padding(16.dp)) { Text("Acerca de nosotros (Próximamente)") }
                        }

                        // Contacto (Redes, WhatsApp)
                        composable("contact") {
                            ContactScreen()
                        }

                        // Estadísticas
                        composable("stats") {
                            StatsScreen()
                        }

                        // Configuración (Calificación)
                        composable("settings") {
                            SettingsScreen(viewModel)
                        }

                        // Ayuda (FAQ)
                        composable("help") {
                            HelpScreen()
                        }
                    }

                    // Botón Flotante del Menú (Arriba a la izquierda)
                    // Lo ponemos aquí para imitar el diseño web original
                    SmallFloatingActionButton(
                        onClick = { scope.launch { drawerState.open() } },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(16.dp),
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = "Menú")
                    }
                }
            }
        }
    }
}

// ==========================================
// FUNCIONES DE UTILIDAD (ORIENTACIÓN DE PANTALLA)
// ==========================================

/**
 * Función composable que fuerza la orientación de la pantalla mientras
 * el usuario permanezca en ella. Al salir, restaura la orientación original.
 */
@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context.findActivity()
        if (activity == null) {
            onDispose { }
        } else {
            val originalOrientation = activity.requestedOrientation
            activity.requestedOrientation = orientation
            onDispose {
                activity.requestedOrientation = originalOrientation
            }
        }
    }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}