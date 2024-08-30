package com.snofed.publicapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity


@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    /* val tokenManager: TokenManager by lazy {
         TokenManager(this) // Initialization code here
     }*/

    private val firstSync = true
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateStatusBarColor()
        setContentView(R.layout.activity_splash)


        //val deviceID = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        // Common.app_id=deviceID
        //toast(Common.app_id)
        //Toast.makeText(this,""+pref?.userid,Toast.LENGTH_LONG).show()
        // print("ss"+ tokenManager.getToken())
        Handler().postDelayed({
            val intent = Intent(this, OnBoarding::class.java)
            startActivity(intent)
            finish()
            /*if (tokenManager.getToken()!!.isEmpty()) {
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
            finish()*/
        }, 3000) // delaying for 2 seconds...
    }

    private fun updateStatusBarColor() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = Color.parseColor("#005387") // Example color
    }
}