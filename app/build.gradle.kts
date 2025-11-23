plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.proyecto2"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.proyecto2"
        minSdk = 24
        targetSdk = 36
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
    
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "win32-x86-64/attach_hotspot_windows.dll"
            excludes += "win32-x86/attach_hotspot_windows.dll"
            excludes += "META-INF/licenses/ASM"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    // --- BOM (Bill of Materials) ---
    implementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(platform(libs.androidx.compose.bom))

    // --- Core & Lifecycle ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    // --- Jetpack Compose UI ---
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)

    // --- Material Design 3 ---
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3-window-size-class")

    // --- Navigation ---
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // --- ViewModel y LiveData para Compose ---
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.3")

    // --- Persistencia Local ---
    implementation("androidx.datastore:datastore-preferences:1.1.7")

    // --- Retrofit y Gson ---
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    // --- Carga de imágenes ---
    implementation("io.coil-kt:coil-compose:2.5.0")

    // --- Testing ---
    testImplementation(libs.junit)
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // ---Kotest---
    androidTestImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    androidTestImplementation("io.kotest:kotest-assertions-core:5.8.0")

    // ---JUnit5---
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")

    // ---MockK---
    androidTestImplementation("io.mockk:mockk:1.13.10")
    androidTestImplementation("io.mockk:mockk-android:1.13.10")

    // --- Debug ---
    debugImplementation(libs.androidx.compose.ui.tooling)

    // --- ViewModel y LiveData para Compose ---
    implementation("androidx.compose.runtime:runtime-livedata:1.7.4")
}
