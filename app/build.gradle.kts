plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("realm-android")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services") // Add this line
}

android {
    namespace = "com.snofed.publicapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.snofed.publicapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
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
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.toasty)

    implementation ("org.jetbrains.kotlin:kotlin-stdlib:1.8.10") // Update with the version you use
    //implementation("com.github.pedroSG94.RootEncoder:library:2.4.3")

    //val hilt_version="2.44"
    // val hilt_version="2.38.1"
    //implementation("com.google.dagger:hilt-android:$hilt_version")
    //kapt("com.google.dagger:hilt-android-compiler:$hilt_version")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1") // Ensure this version matches the Kotlin plugin

    implementation ("com.intuit.sdp:sdp-android:1.1.1")

    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")


    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation ("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

  /*  implementation("io.realm:realm-gradle-plugin:10.18.0")*/
   // implementation ("io.realm:realm-android-library:10.13.0")

    val lifecycle_version = "2.5.0-beta01"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-core:2.8.4")


    val retrofit_version = "2.9.0"
    implementation("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation("com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.4")


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

    //Mapbox Maps SDK v11 (lower version)
    implementation("com.mapbox.maps:android:11.4.0")
    implementation("com.mapbox.navigationcore:navigation:3.3.0")
    implementation("com.mapbox.mapboxsdk:mapbox-sdk-turf:5.9.0-alpha.1")


    implementation ("de.hdodenhof:circleimageview:3.1.0")

    implementation ("com.google.android.gms:play-services-location:21.0.1")



    // CameraX core library
    implementation ("androidx.camera:camera-core:1.1.0")
    implementation ("androidx.camera:camera-camera2:1.1.0")
    implementation ("androidx.camera:camera-lifecycle:1.1.0")
    implementation ("androidx.camera:camera-view:1.1.0")


//// Google Services
    implementation ("com.google.firebase:firebase-auth:22.0.0")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.android.gms:play-services-auth:20.5.0")
//    implementation("com.google.android.gms:play-services-auth:20.7.0")
//    implementation ("com.google.firebase:firebase-auth-ktx:23.1.0") // Firebase Authentication
//    implementation("com.google.firebase:firebase-auth:21.0.1")
//    //implementation platform ("com.google.firebase:firebase-bom:26.1.0")
//    implementation("com.google.firebase:firebase-messaging")


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