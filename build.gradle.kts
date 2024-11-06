buildscript {

    dependencies {
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath ("io.realm:realm-gradle-plugin:10.18.0")
        classpath ("com.android.tools.build:gradle:8.0.2") // Use the latest stable version compatible with targetSdkVersion 34
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.20") // Kotlin plugin if using Kotlin
        classpath ("com.google.gms:google-services:4.4.2")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    kotlin("plugin.serialization") version "1.8.10"
    //alias(libs.plugins.google.gms.google.services) apply false // Use the appropriate Kotlin version


}