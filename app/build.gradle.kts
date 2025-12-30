plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.example.livestreamingtv"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.livestreamingtv"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.leanback)
    implementation(libs.glide)
    
    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    
    // Fragment Lifecycle
    implementation(libs.androidx.fragment.ktx)
    
    // CardView
    implementation(libs.androidx.cardview)
    
    // RecyclerView
    implementation(libs.androidx.recyclerview)
    
    // ViewPager2
    implementation(libs.androidx.viewpager2)
    
    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    
    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    
    // DataStore Preferences
    implementation(libs.androidx.datastore.preferences)

    // Google Material Design
    implementation("com.google.android.material:material:1.12.0")
}