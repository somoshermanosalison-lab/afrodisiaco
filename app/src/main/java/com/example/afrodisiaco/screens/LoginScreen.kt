package com.example.afrodisiaco.screens

import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.afrodisiaco.MainViewModel

@Composable
fun LoginScreen(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Si el sistema pide código OTP (2FA), mostramos esa pantalla
    if (uiState.isOtpRequired) {
        OtpScreen(viewModel)
    } else {
        LoginForm(viewModel)
    }
}

@Composable
fun OtpScreen(viewModel: MainViewModel) {
    var code by remember { mutableStateOf("") }
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Verificación de 2 Pasos", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        // Mostramos el código simulado para que puedas probarlo
        Text("Código de seguridad: ${uiState.generatedOtp}", color = Color.Gray)

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = code,
            onValueChange = { if (it.length <= 4) code = it },
            label = { Text("Introduce el código") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (viewModel.verifyOtp(code)) {
                    Toast.makeText(context, "Verificación exitosa", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Código incorrecto", Toast.LENGTH_LONG).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Verificar")
        }
    }
}

@Composable
fun LoginForm(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    // Estado local del formulario
    val isLoginMode = remember { mutableStateOf(true) } // true = Login, false = Registro
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    val context = LocalContext.current

    // Leemos las preferencias de seguridad
    var useBiometric by remember { mutableStateOf(viewModel.prefs.isBiometricEnabled()) }
    var use2FA by remember { mutableStateOf(viewModel.prefs.is2FAEnabled()) }

    // Función para invocar la huella digital
    fun launchBiometric() {
        val activity = context as? FragmentActivity ?: return
        val executor = ContextCompat.getMainExecutor(context)

        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                // CUANDO LA HUELLA ES CORRECTA:
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // Llamamos a la función que usa los datos guardados
                    viewModel.loginWithBiometrics()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Solo mostramos error si no fue cancelado por el usuario
                    if (errorCode != BiometricPrompt.ERROR_USER_CANCELED && errorCode != BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                        Toast.makeText(context, "Error: $errString", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Inicio de Sesión")
            .setSubtitle("Usa tu huella para entrar")
            .setNegativeButtonText("Cancelar")
            .build()

        biometricPrompt.authenticate(promptInfo)
    }

    // Diseño visual (Gradiente)
    val gradient = Brush.linearGradient(
        colors = listOf(Color(0xFF6200EE), Color(0xFF3700B3), Color(0xFF03DAC5))
    )

    Box(
        modifier = Modifier.fillMaxSize().background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Si está cargando (conectando a Firebase), mostramos círculo de carga
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                    Text("Conectando...", style = MaterialTheme.typography.bodySmall)
                } else {
                    // --- FORMULARIO NORMAL ---
                    Box(
                        modifier = Modifier.size(64.dp).background(MaterialTheme.colorScheme.primary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.MusicNote, null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }

                    Text(
                        viewModel.t(if (isLoginMode.value) "login" else "createAccount"),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    // Mostrar errores si existen (ej. contraseña incorrecta)
                    if (uiState.error != null) {
                        Text(
                            text = uiState.error ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    OutlinedTextField(
                        value = email.value,
                        onValueChange = { email.value = it },
                        label = { Text(viewModel.t("email")) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text(viewModel.t("password")) },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Opciones de seguridad (Solo visibles en modo Login)
                    if (isLoginMode.value) {
                        Column(Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = use2FA,
                                    onCheckedChange = {
                                        use2FA = it
                                        viewModel.prefs.set2FAEnabled(it)
                                    }
                                )
                                Text("Verificación en 2 pasos")
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = useBiometric,
                                    onCheckedChange = {
                                        useBiometric = it
                                        viewModel.prefs.setBiometricEnabled(it)
                                    }
                                )
                                Text("Usar Huella Digital")
                            }
                        }
                    }

                    // BOTÓN PRINCIPAL (Login o Registro)
                    Button(
                        onClick = {
                            // .trim() elimina espacios invisibles al principio y al final
                            val cleanEmail = email.value.trim()
                            val cleanPassword = password.value.trim()

                            if (isLoginMode.value) {
                                viewModel.loginWithFirebase(cleanEmail, cleanPassword)
                            } else {
                                viewModel.registerWithFirebase(cleanEmail, cleanPassword)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(viewModel.t(if (isLoginMode.value) "loginButton" else "registerButton"))
                    }

                    // Botón de Huella (si está activo)
                    if (useBiometric && isLoginMode.value) {
                        FilledTonalButton(
                            onClick = { launchBiometric() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Entrar con Huella")
                        }
                    }

                    TextButton(onClick = { isLoginMode.value = !isLoginMode.value }) {
                        Text(viewModel.t(if (isLoginMode.value) "dontHaveAccount" else "alreadyHaveAccount"))
                    }
                }
            }
        }
    }
}