plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        applicationId = "com.hijano.games"
        minSdk = Versions.MIN_SDK
        targetSdk = Versions.TARGET_SDK
        versionCode = Versions.VERSION_CODE
        versionName = Versions.VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("${Libs.APP_COMPAT}:${Versions.APP_COMPAT}")
    implementation("${Libs.CONSTRAINT_LAYOUT}:${Versions.CONSTRAINT_LAYOUT}")
    implementation("${Libs.CORE_KTX}:${Versions.CORE_KTX}")
    implementation("${Libs.COROUTINES_ANDROID}:${Versions.COROUTINES}")
    implementation("${Libs.HILT_ANDROID}:${Versions.HILT}")
    kapt("${Libs.HILT_COMPILER}:${Versions.HILT}")
    implementation("${Libs.LIFECYCLE_RUNTIME_KTX}:${Versions.LIFECYCLE}")
    implementation("${Libs.MATERIAL}:${Versions.MATERIAL}")
    implementation("${Libs.NAVIGATION_FRAGMENT_KTX}:${Versions.NAVIGATION}")
    implementation("${Libs.NAVIGATION_UI_KTX}:${Versions.NAVIGATION}")
    implementation("${Libs.RECYCLER_VIEW}:${Versions.RECYCLER_VIEW}")
}
