package com.snofed.publicapp

import android.app.Application
import com.facebook.FacebookSdk
import com.facebook.appevents.AppEventsLogger



import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class SnofedApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize the Facebook SDK
        FacebookSdk.sdkInitialize(applicationContext)
        AppEventsLogger.activateApp(this)
    }
}

