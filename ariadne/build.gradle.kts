plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.org.jetbrains.kotlin.kapt)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.franshizastr.ariadne"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.franshizastr.ariadne"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // di
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // room
    implementation(libs.room)

    // navigation
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // projects
    implementation(projects.data.login)
    implementation(projects.data.records)
    implementation(projects.data.races)

    implementation(projects.domain.login)
    implementation(projects.domain.records)
    implementation(projects.domain.races)

    implementation(projects.presentation.login)
    implementation(projects.presentation.records)
    implementation(projects.presentation.races)

    implementation(projects.core)
    implementation(projects.utils)
    implementation(projects.designSystem)

    implementation(libs.play.services.location)

    implementation(libs.accompanist.permissions)

    // core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}