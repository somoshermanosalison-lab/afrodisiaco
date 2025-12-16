// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // 1. Plugins de Android y Kotlin (Usando alias del catálogo, que es lo moderno)
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // 2. Plugin de Google Services (Necesario para Firebase)
    // Este no suele estar en el catálogo por defecto, así que lo dejamos explícito.
    id("com.google.gms.google-services") version "4.4.2" apply false
}