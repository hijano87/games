import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

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

        buildConfigField("String", "BASE_URL", "\"https://api.igdb.com/\"")
        buildConfigField(
            "String",
            "CLIENT_ID",
            "\"${gradleLocalProperties(rootDir)["clientId"]}\""
        )
        buildConfigField(
            "String",
            "BEARER_TOKEN",
            "\"${gradleLocalProperties(rootDir)["bearerToken"]}\""
        )
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
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("${Libs.APP_COMPAT}:${Versions.APP_COMPAT}")
    implementation("${Libs.CONSTRAINT_LAYOUT}:${Versions.CONSTRAINT_LAYOUT}")
    implementation("${Libs.CORE_KTX}:${Versions.CORE_KTX}")
    implementation("${Libs.COROUTINES_ANDROID}:${Versions.COROUTINES}")
    implementation("${Libs.GLIDE}:${Versions.GLIDE}")
    kapt("${Libs.GLIDE_COMPILER}:${Versions.GLIDE}")
    implementation("${Libs.HILT_ANDROID}:${Versions.HILT}")
    kapt("${Libs.HILT_COMPILER}:${Versions.HILT}")
    implementation("${Libs.LIFECYCLE_RUNTIME_KTX}:${Versions.LIFECYCLE}")
    implementation("${Libs.MATERIAL}:${Versions.MATERIAL}")
    implementation("${Libs.NAVIGATION_FRAGMENT_KTX}:${Versions.NAVIGATION}")
    implementation("${Libs.NAVIGATION_UI_KTX}:${Versions.NAVIGATION}")
    implementation("${Libs.PAGING}:${Versions.PAGING}")
    implementation("${Libs.RECYCLER_VIEW}:${Versions.RECYCLER_VIEW}")
    implementation("${Libs.SWIPE_REFRESH_LAYOUT}:${Versions.SWIPE_REFRESH_LAYOUT}")

    implementation("${Libs.ROOM}:${Versions.ROOM}")
    kapt("${Libs.ROOM_COMPILER}:${Versions.ROOM}")
    implementation("${Libs.ROOM_KTX}:${Versions.ROOM}")
    implementation("${Libs.ROOM_PAGING}:${Versions.ROOM}")

    implementation("${Libs.RETROFIT}:${Versions.RETROFIT}")
    implementation("${Libs.CONVERTER_SCALARS}:${Versions.RETROFIT}")
    implementation("${Libs.CONVERTER_MOSHI}:${Versions.RETROFIT}")
    implementation("${Libs.MOSHI}:${Versions.MOSHI}")
    kapt("${Libs.MOSHI_KOTLIN_CODEGEN}:${Versions.MOSHI}")
    implementation("${Libs.LOGGING_INTERCEPTOR}:${Versions.LOGGING_INTERCEPTOR}")

    implementation("${Libs.IGDB_API_JVM}:${Versions.IGDB_API_JVM}")
}
