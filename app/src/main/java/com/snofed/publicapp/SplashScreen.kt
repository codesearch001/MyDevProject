package com.snofed.publicapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.SharedPreferenceKeys
import com.snofed.publicapp.utils.SnofedConstants
import com.snofed.publicapp.utils.SnofedUtils
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreen : AppCompatActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //updateStatusBarColor()
        setContentView(R.layout.activity_splash)
        // Manually initialize tokenManager
        tokenManager = TokenManager(this)
        tokenManager.getUserId()
        tokenManager.getToken()
        tokenManager.getClientId()
       /* if (!SnofedUtils.checkIfThereIsNetworkConnection(this)) {
            navigateToAppropriateScreen()
            return
        }*/

        if (isUserLoggedIn()) {
            val intent = Intent(this, HomeDashBoardActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            Log.e("werw","sdfewr")
            val intent = Intent(this, HomeDashBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
        if (isUserFirstTimeLaunched()) {
            setFirstTimeLaunch()
            startLoginActivity()
            return
        }

        if (isUserAlreadyLoggedIn()) {
            startLoginActivity()
            return
        }
       /* Handler(Looper.getMainLooper()).postDelayed({
            navigateToAppropriateScreen()
        }, 3000)*/ // Delay for 3 seconds
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

    private fun isUserAlreadyLoggedIn(): Boolean {
        val da = AppPreference.getPreference(this, SharedPreferenceKeys.IS_USER_LOGGED_IN)
        return TextUtils.isEmpty(da)
    }

    private fun isUserLoggedIn(): Boolean {
        val da = AppPreference.getPreference(this, SharedPreferenceKeys.IS_GUEST_LOGGED_IN)
        return !TextUtils.isEmpty(da)
    }

    private fun startLoginActivity() {
        startActivity(Intent(this, OnBoarding::class.java))
    }

    private fun setFirstTimeLaunch() {
        AppPreference.savePreference(this, SharedPreferenceKeys.FIRST_TIME_LAUNCHED,
            "first_time_launched_successfully")
    }

    private fun isUserFirstTimeLaunched(): Boolean {
        return TextUtils.isEmpty(AppPreference.getPreference(this,
            SharedPreferenceKeys.FIRST_TIME_LAUNCHED))
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

}