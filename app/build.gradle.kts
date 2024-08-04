plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.snofed.publicapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.snofed.publicapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //implementation("com.github.pedroSG94.RootEncoder:library:2.4.3")

    //val hilt_version="2.44"
    // val hilt_version="2.38.1"
    //implementation("com.google.dagger:hilt-android:$hilt_version")
    //kapt("com.google.dagger:hilt-android-compiler:$hilt_version")
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")


    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")


    val lifecycle_version = "2.5.0-beta01"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")


    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")


    val coroutines_version = "1.6.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutines_version")

    val navigation_version = "2.5.3"
    implementation("androidx.navigation:navigation-fragment-ktx:$navigation_version")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation_version")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    implementation("com.facebook.android:facebook-android-sdk:5.11.0")
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("io.coil-kt:coil-compose:2.7.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Mapbox Maps SDK v11 (lower version)
    implementation("com.mapbox.maps:android:11.4.0")


    // Mapbox SDK dependencies
    /* implementation("com.mapbox.mapboxsdk:mapbox-android-sdk:9.6.2")
    implementation("com.mapbox.mapboxsdk:mapbox-sdk-turf:6.6.0")*/

    // Mapbox Maps SDK for Android
    /*    implementation("com.mapbox.mapboxsdk:mapbox-android-sdk:8.6.7") {
            exclude(group = "com.mapbox.mapboxsdk", module = "mapbox-android-telemetry-okhttp3")
        }
        implementation("com.mapbox.mapboxsdk:mapbox-sdk-turf:5.9.0-alpha.1")*/
    // MAPBOX
    //implementation ('com.mapbox.mapboxsdk:mapbox-android-sdk:9.7.2') {
    /* implementation ('com.mapbox.mapboxsdk:mapbox-android-sdk:8.6.7') {
         exclude group: 'com.mapbox.mapboxsdk', module: 'mapbox-android-telemetry-okhttp3'
     }
     implementation 'com.mapbox.mapboxsdk:mapbox-sdk-turf:5.9.0-alpha.1'*/
    /*   //2.3.0
       //val gropi_version = "2.5.3"
       val gropi_version = "2.3.0"
       implementation ("com.xwray:groupie:$gropi_version")
       implementation ("com.xwray:groupie-kotlin-android-extensions:$gropi_version")
       implementation ("com.xwray:groupie-databinding:$gropi_version")*/

    /*implementation ("com.xwray:groupie:2.10.1")
    implementation  ("com.xwray:groupie-viewbinding:2.10.1")*/

}
kapt {
    correctErrorTypes = true
}