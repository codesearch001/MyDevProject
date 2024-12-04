package com.snofed.publicapp

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.FirebaseApp


import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration


@HiltAndroidApp
class SnofedApplication : Application() {

    private var config: RealmConfiguration? = null
    override fun onCreate() {
        super.onCreate()
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        // Initialize the Facebook SDK
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)


        Realm.init(this)
         config = RealmConfiguration.Builder().name("snofed.realm")
            .schemaVersion(0)
            .compactOnLaunch()
            .allowWritesOnUiThread(true)
            .build()

        config.let { Realm.setDefaultConfiguration(it) }
       // Realm.setDefaultConfiguration(config)
       // initRealm()

    }

/*    private fun initRealm() {
        // Ready our SDK
        Realm.init(this)
        // Creating our db with custom properties
        val config = RealmConfiguration.Builder()
            .name("snofed.realm")
            .schemaVersion(1)
            .build()
        Realm.setDefaultConfiguration(config)
    }*/
    companion object {
        var isRecordingWorkoutProcessing: Boolean = false
    }
}

