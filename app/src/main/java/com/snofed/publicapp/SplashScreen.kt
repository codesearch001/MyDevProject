package com.snofed.publicapp

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
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
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        // Manually initialize tokenManager
        tokenManager = TokenManager(this)

        // Simulate loading time with a delay (Optional)
        Handler(Looper.getMainLooper()).postDelayed({
            navigateToNextScreen()
        }, 1500) // 3 seconds delay to show splash screen
    }

    private fun navigateToNextScreen() {
        when {
            isUserFirstTimeLaunched() -> {
                // User opens the app for the first time, navigate to OnBoarding
                setFirstTimeLaunch()
                startLoginActivity() // Navigate to login or onboarding
            }
            isUserLoggedIn() -> {
                // User is already logged in, navigate to the dashboard
                val intent = Intent(this, HomeDashBoardActivity::class.java)
                startActivity(intent)
                finish()
            }
            else -> {
                // User is not logged in, navigate to login
                startLoginActivity()
            }
        }
    }

    private fun isUserLoggedIn(): Boolean {
        val token = tokenManager.getToken()
        return !token.isNullOrEmpty()
    }

    private fun startLoginActivity() {
        startActivity(Intent(this, OnBoarding::class.java)) // Or LoginActivity
        finish()
    }

    private fun setFirstTimeLaunch() {
        AppPreference.savePreference(
            this, SharedPreferenceKeys.FIRST_TIME_LAUNCHED,
            "first_time_launched_successfully"
        )
    }

    private fun isUserFirstTimeLaunched(): Boolean {
        return TextUtils.isEmpty(
            AppPreference.getPreference(this, SharedPreferenceKeys.FIRST_TIME_LAUNCHED)
        )
    }
}
