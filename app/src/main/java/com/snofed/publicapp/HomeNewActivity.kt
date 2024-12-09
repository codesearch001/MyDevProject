package com.snofed.publicapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeNewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_new)

    }
    override fun onBackPressed() {
        super.onBackPressed()
    }
}