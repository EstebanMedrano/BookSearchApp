plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("kapt")
}

android {
    namespace = "com.example.booksearchapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.booksearchapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)

    // **Importante: Incluye explícitamente las dependencias de Compose Material y Foundation**
    implementation("androidx.compose.ui:ui:1.6.0") // Reemplaza con la versión más reciente
    implementation("androidx.compose.ui:ui-graphics:1.6.0") // Reemplaza con la versión más reciente
    implementation("androidx.compose.ui:ui-tooling-preview:1.6.0") // Reemplaza con la versión más reciente

    implementation("androidx.compose.material:material:1.6.0") // Reemplaza con la versión más reciente (Material Design 2)
    implementation("androidx.compose.material:material-icons-extended:1.6.0") // Reemplaza con la versión más reciente

    implementation("androidx.compose.foundation:foundation:1.6.0") // Reemplaza con la versión más reciente (para LazyColumn, items)
    implementation("androidx.compose.foundation:foundation-layout:1.6.0") // Reemplaza con la versión más reciente (para Spacer, Column, Row)

    implementation("androidx.compose.material3:material3:1.2.0") // Reemplaza con la versión más reciente (si la necesitas)

    implementation("androidx.compose.runtime:runtime-livedata:1.6.0") // Reemplaza con la versión más reciente (para collectAsState)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0") // Reemplaza con la versión más reciente

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // Room
    val room_version = "2.6.1" // Usa la versión más reciente estable
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48") // Usa la versión más reciente estable
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0") // Usa la versión más reciente estable

    // Navigation Compose
    implementation("androidx.navigation:navigation-compose:2.7.7") // Usa la versión más reciente estable

    // **Puedes comentar o eliminar esta línea por ahora para evitar posibles conflictos de versiones**
    // implementation(platform(libs.androidx.compose.bom))
}