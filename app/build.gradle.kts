plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.app.corpaliv_grupo1"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.app.corpaliv_grupo1"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {    signingConfigs {
        create("release") {
            storeFile = file("../keystore/corpaliv-release.jks")
            storePassword = "valeale2026"
            keyAlias = "corpaliv"
            keyPassword = "valeale2026"
        }
    }

        buildTypes {
            release {
                signingConfig = signingConfigs.getByName("release")
                optimization {
                    enable = false
                }
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests {
            // Evita "Method ... not mocked" con android.util.Log en tests JVM
            isReturnDefaultValues = true
            all {
                it.useJUnitPlatform()
                // MockK/Byte Buddy con Java 25
                it.jvmArgs("-Dnet.bytebuddy.experimental=true")
            }
        }
    }
}

dependencies {
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation("androidx.compose.material3:material3-window-size-class:1.3.1")
    implementation("androidx.datastore:datastore-preferences:1.1.0")
    implementation(libs.androidx.room3.runtime)
    ksp(libs.androidx.room3.compiler)

    // Red
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Tests unitarios (JUnit5 + Kotest + MockK)
    testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
    testImplementation("io.kotest:kotest-assertions-core:5.9.1")
    testImplementation("io.mockk:mockk:1.13.13")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    // Tests de UI (instrumentados)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}