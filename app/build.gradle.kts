// Aplicación de plugins necesarios para el proyecto
plugins {
    alias(libs.plugins.android.application) // Plugin para aplicaciones Android
    alias(libs.plugins.google.gms.google.services) // Plugin de Google Services para integrar servicios de Firebase
}

android {
    // Namespace del proyecto (identificador único)
    namespace = "alcaide.bautista.pmdm03_mab_v03"
    compileSdk = 35 // Versión del SDK utilizada para compilar la aplicación

    defaultConfig {
        applicationId = "alcaide.bautista.pmdm03_mab_v03" // Identificador único de la aplicación en el sistema Android
        minSdk = 29 // Mínima versión del sistema operativo que soportará la app
        targetSdk = 35 // Versión del sistema objetivo al que se optimiza la app
        versionCode = 1 // Código interno de versión de la app
        versionName = "1.0" // Versión visible al usuario

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" // Runner para pruebas instrumentadas
    }

    buildTypes {
        release {
            isMinifyEnabled = false // Minificación desactivada para la versión release
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), // Archivo ProGuard predeterminado
                "proguard-rules.pro" // Reglas adicionales para optimización y ofuscación
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11 // Versión de Java para el código fuente
        targetCompatibility = JavaVersion.VERSION_11 // Versión de Java para el código de destino
    }

    buildFeatures {
        viewBinding = true // Activación de ViewBinding para simplificar la interacción con las vistas
        dataBinding = true // Activación de DataBinding para enlazar datos con las vistas directamente
    }
}

dependencies {
    // Librerías esenciales de Android
    implementation(libs.core.splashscreen) // Compatibilidad con la pantalla de inicio
    implementation(libs.appcompat) // Compatibilidad con versiones antiguas de Android
    implementation(libs.material) // Componentes de Material Design
    implementation(libs.constraintlayout) // Layout basado en restricciones
    implementation(libs.recyclerview) // Componente para listas dinámicas
    implementation(libs.cardview) // Vista para tarjetas
    implementation(libs.annotation) // Anotaciones de Android

    // Componentes de navegación
    implementation(libs.navigation.fragment) // Navegación entre fragmentos
    implementation(libs.navigation.ui) // Componentes de UI relacionados con la navegación

    // Dependencias de Firebase
    implementation(platform(libs.firebase.bom)) // Firebase BOM para gestionar versiones
    implementation(libs.firebase.auth) // Autenticación de Firebase
    implementation(libs.firebase.analytics) // Analíticas de Firebase
    implementation(libs.firebase.ui.auth) // Interfaz de usuario para autenticación con Firebase

    // Retrofit (para llamadas a APIs)
    implementation(libs.retrofit) // Cliente HTTP Retrofit
    implementation(libs.converter.gson) // Convertidor Gson para serialización/deserialización de JSON
    implementation (libs.squareup.picasso)
    implementation(libs.firebase.firestore)


    // Dependencias de pruebas
    testImplementation(libs.junit) // Pruebas unitarias con JUnit
    androidTestImplementation(libs.ext.junit) // Pruebas instrumentadas con JUnit
    androidTestImplementation(libs.espresso.core) // Librería Espresso para pruebas de UI
}
