plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.onlineshop"
    compileSdk = 36  // updated from 35

    defaultConfig {
        applicationId = "com.example.onlineshop"
        minSdk = 24
        targetSdk = 36  // updated from 35
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


// -----------------------------------------
// CORE ANDROIDX
// -----------------------------------------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

// -----------------------------------------
// COMPOSE & UI
// -----------------------------------------
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.constraintlayout.compose)

// -----------------------------------------
// TOOLING & PREVIEW
// -----------------------------------------
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.ui.tooling.preview)

// -----------------------------------------
// ACCOMPANIST (Pager indicators)
// -----------------------------------------
    implementation(libs.accompanist.pager.indicators)

// -----------------------------------------
// IMAGE LOADING (COIL)
// -----------------------------------------
    implementation(libs.coil.compose)

// -----------------------------------------
// FIREBASE
// -----------------------------------------
    implementation(libs.firebase.database)

// -----------------------------------------
// JSON
// -----------------------------------------
    implementation(libs.gson)

// -----------------------------------------
// GLANCE (App Widgets)
// -----------------------------------------
    implementation(libs.androidx.glance)
    implementation(libs.androidx.glance.material3)

// -----------------------------------------
// TESTING
// -----------------------------------------
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)


}
