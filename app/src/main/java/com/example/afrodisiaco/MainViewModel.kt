package com.example.afrodisiaco

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val prefs = Prefs(application)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    data class AppUiState(
        val isLoggedIn: Boolean = false,
        val isLoading: Boolean = false,
        val error: String? = null,
        val language: String = "es",
        val theme: String = "light",
        val userEmail: String = "",
        val isFirstTime: Boolean = true,
        val isOtpRequired: Boolean = false,
        val generatedOtp: String = ""
    )

    private val _uiState = MutableStateFlow(AppUiState(isFirstTime = prefs.isFirstTime()))
    val uiState: StateFlow<AppUiState> = _uiState.asStateFlow()

    init {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _uiState.update { it.copy(isLoggedIn = true, userEmail = currentUser.email ?: "") }
        }
    }

    // --- LOGIN CON FIREBASE (Modificado para guardar credenciales) ---
    fun loginWithFirebase(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 1. SI LA HUELLA ESTÁ ACTIVADA, GUARDAMOS LAS CREDENCIALES
                    if (prefs.isBiometricEnabled()) {
                        prefs.saveCredentials(email, pass)
                    } else {
                        prefs.clearCredentials()
                    }

                    // 2. PROCESO DE 2FA
                    if (prefs.is2FAEnabled()) {
                        val mockOtp = (1000..9999).random().toString()
                        _uiState.update { it.copy(isLoading = false, isOtpRequired = true, generatedOtp = mockOtp, userEmail = email) }
                    } else {
                        _uiState.update { it.copy(isLoading = false, isLoggedIn = true, userEmail = email) }
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false, error = task.exception?.message ?: "Error") }
                    Toast.makeText(getApplication(), "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    // --- ESTA ES LA FUNCIÓN QUE FALTABA ---
    fun loginWithBiometrics() {
        // Recuperamos los datos guardados en el teléfono
        val savedEmail = prefs.getSavedEmail()
        val savedPass = prefs.getSavedPassword()

        if (savedEmail.isNotEmpty() && savedPass.isNotEmpty()) {
            // Si existen, hacemos login automático en Firebase
            Toast.makeText(getApplication(), "Validando credenciales...", Toast.LENGTH_SHORT).show()
            loginWithFirebase(savedEmail, savedPass)
        } else {
            // Si no existen, avisamos al usuario
            Toast.makeText(getApplication(), "Primero debes iniciar sesión manualmente una vez y activar la casilla de huella", Toast.LENGTH_LONG).show()
        }
    }
    // ---------------------------------------

    fun registerWithFirebase(email: String, pass: String) {
        if (email.isBlank() || pass.isBlank()) return
        _uiState.update { it.copy(isLoading = true, error = null) }
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _uiState.update { it.copy(isLoading = false, isLoggedIn = true, userEmail = email) }
                    Toast.makeText(getApplication(), "¡Cuenta creada!", Toast.LENGTH_SHORT).show()
                } else {
                    _uiState.update { it.copy(isLoading = false, error = task.exception?.message) }
                    Toast.makeText(getApplication(), "Error registro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun verifyOtp(inputCode: String): Boolean {
        if (inputCode == _uiState.value.generatedOtp) {
            _uiState.update { it.copy(isLoggedIn = true, isOtpRequired = false) }
            return true
        }
        return false
    }

    fun logout() {
        auth.signOut()
        _uiState.update { it.copy(isLoggedIn = false, userEmail = "") }
    }

    // Ya no usamos forceLoginBiometric antigua, usamos la nueva loginWithBiometrics arriba

    fun setLanguage(lang: String) { _uiState.update { it.copy(language = lang) } }
    fun setTheme(theme: String) { _uiState.update { it.copy(theme = theme) } }

    fun completeOnboarding() {
        prefs.setFirstTime(false)
        _uiState.update { it.copy(isFirstTime = false) }
    }

    fun t(key: String): String {
        val lang = _uiState.value.language
        return Translations.data[lang]?.get(key) ?: key
    }
}