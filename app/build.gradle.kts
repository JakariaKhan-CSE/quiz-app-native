plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.synapse.spark"
    compileSdk {
        version = release(36)
    }
    buildFeatures {
        dataBinding = true
    }
    defaultConfig {
        applicationId = "com.synapse.spark"
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
}

//key alis:key0 //key pass :bponi123

dependencies {
    // Retrofit (for API calls)
    implementation("com.squareup.retrofit2:retrofit:3.0.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
// ViewModel (for data handling)
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.9.4")
    implementation("androidx.lifecycle:lifecycle-livedata:2.9.4")
// RecyclerView (for results list)
    implementation("androidx.recyclerview:recyclerview:1.4.0")
// Core Splash Screen API
    implementation("androidx.core:core-splashscreen:1.2.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}