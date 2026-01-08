plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.navigation.safeargs)
    alias(libs.plugins.kotlin.parcelize)


}

android {
    namespace = "com.livestreaming.tv"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.livestreaming.tv"
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
    implementation(libs.material)

    //Media 3
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.exoplayer.dash)
    implementation(libs.media3.ui)

    // Ktor Client
    implementation("io.ktor:ktor-client-core:2.3.7")
    implementation("io.ktor:ktor-client-android:2.3.7")
    implementation("io.ktor:ktor-client-serialization:2.3.7")
    implementation("io.ktor:ktor-client-logging:2.3.7")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")

    // VLC Android SDK
    implementation("org.videolan.android:libvlc-all:3.6.5")
}