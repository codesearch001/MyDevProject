package com.snofed.publicapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.SnofedConstants
import com.snofed.publicapp.utils.SnofedUtils
import com.snofed.publicapp.utils.TokenManager


@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

     val tokenManager: TokenManager by lazy {
         TokenManager(this) // Initialization code here
     }
    var settings: SharedPreferences? = null
    private var firstSync = true
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //updateStatusBarColor()
        setContentView(R.layout.activity_splash)

        if (!SnofedUtils.checkIfThereIsNetworkConnection(this)) {
            //finishSplashScreen()
            return
        }

        Handler(Looper.getMainLooper()).postDelayed({
            navigateToAppropriateScreen()
        }, 3000) // Delay for 3 seconds


       /* Handler().postDelayed({
            val intent = Intent(this, OnBoarding::class.java)
            startActivity(intent)
            finish()
            *//*if (tokenManager.getToken()!!.isEmpty()) {
                val intent = Intent(this, OnBoarding::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            val intent = Intent(this, HomeDashBoardActivity::class.java)
            startActivity(intent)
            finish()*//*
        }, 3000) // delaying for 2 seconds...*/
    }

    private fun navigateToAppropriateScreen() {
        // Retrieve the user token and check if it's empty
        val token = tokenManager.getToken().orEmpty()
        // Check if it's the user's first time and whether the token is not empty
        val isFirstTime = Helper.PrefsManager.isFirstTime(this)

        Log.d("SplashScreen", "Token: $token")
        Log.d("SplashScreen", "Is First Time: $isFirstTime")

        if (isFirstTime) {
            // If it's the first time, start OnBoardingActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Check if the access token is set and navigate accordingly
            if (tokenManager.getToken().isNullOrEmpty()) {
                startActivity(Intent(this, HomeDashBoardActivity::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))

            }
        }
        // Finish the SplashScreenActivity
        finish()
    }


    /*private fun finishSplashScreen() {
         if (firstSync) {
             firstSync = false

             // Add a delay to ensure the splash screen lasts at least for a short time
             Handler(Looper.getMainLooper()).postDelayed({
                 if (SnofedUtils.isFirstTimeAppUse(applicationContext)) {
                     // First time use - navigate to IntroductionActivity
                     settings?.edit()?.putBoolean(SnofedConstants.FIRST_TIME_APP_SYNC, false)?.apply()
                     val intent = Intent(this, OnBoarding::class.java)
                     intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                     startActivity(intent)
                 } else {
                     // Check if the access token is set and navigate accordingly
                     if (tokenManager.getToken().isNullOrEmpty()) {
                         startActivity(Intent(this, HomeDashBoardActivity::class.java))
                     } else {
                         startActivity(Intent(this, HomeNewActivity::class.java))
                     }
                 }
                 // Finish the SplashScreenActivity
                 finish()
             }, 1000) // 1-second delay (adjust as necessary)
         }
     }*/

    private fun updateStatusBarColor() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor("#005387") // Example color
    }
}