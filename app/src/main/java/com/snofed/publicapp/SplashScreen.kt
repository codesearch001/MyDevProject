package com.snofed.publicapp

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import com.snofed.publicapp.utils.TokenManager
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    val tokenManager: TokenManager by lazy {
        TokenManager(this) // Initialization code here
    }
    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // pref = SharePreferenceProvider(this)
        val deviceID = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        // Common.app_id=deviceID
        //toast(Common.app_id)
        //Toast.makeText(this,""+pref?.userid,Toast.LENGTH_LONG).show()
        window.statusBarColor = Color.parseColor("#FFE0E0")


        print("ss"+ tokenManager.getToken())
        Handler().postDelayed({
            val intent = Intent(this, OnBoarding::class.java)
            startActivity(intent)
            finish()
//            if (tokenManager.getToken()!!.isEmpty()) {
//                val intent = Intent(this, OnBoarding::class.java)
//                startActivity(intent)
//                finish()
//            } else {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//            val intent = Intent(this, HomeDashBoardActivity::class.java)
//            startActivity(intent)
//            finish()
        }, 3000) // delaying for 2 seconds...
    }
}