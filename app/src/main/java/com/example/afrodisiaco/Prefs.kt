package com.example.afrodisiaco

import android.content.Context

class Prefs(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("AfrodisiacoPrefs", Context.MODE_PRIVATE)

    // Configuración básica
    fun isFirstTime(): Boolean = sharedPreferences.getBoolean("isFirstTime", true)
    fun setFirstTime(value: Boolean) = sharedPreferences.edit().putBoolean("isFirstTime", value).apply()

    // Configuración de Seguridad
    fun isBiometricEnabled(): Boolean = sharedPreferences.getBoolean("biometric", false)
    fun setBiometricEnabled(enabled: Boolean) = sharedPreferences.edit().putBoolean("biometric", enabled).apply()

    fun is2FAEnabled(): Boolean = sharedPreferences.getBoolean("2fa", false)
    fun set2FAEnabled(enabled: Boolean) = sharedPreferences.edit().putBoolean("2fa", enabled).apply()

    // --- ESTO ES LO QUE NECESITAS PARA LA HUELLA ---
    fun saveCredentials(email: String, pass: String) {
        sharedPreferences.edit()
            .putString("saved_email", email)
            .putString("saved_pass", pass)
            .apply()
    }

    fun getSavedEmail(): String = sharedPreferences.getString("saved_email", "") ?: ""
    fun getSavedPassword(): String = sharedPreferences.getString("saved_pass", "") ?: ""

    fun clearCredentials() {
        sharedPreferences.edit().remove("saved_email").remove("saved_pass").apply()
    }
}