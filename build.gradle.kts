// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.NAVIGATION}")
    }
}

plugins {
    id("com.android.application") version Versions.AGP apply false
    id("com.android.library") version Versions.AGP apply false
    id("org.jetbrains.kotlin.android") version Versions.KOTLIN apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
