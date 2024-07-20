package com.snofed.publicapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.android.material.navigation.NavigationView
import com.snofed.publicapp.databinding.ActivityHomeNewBinding
import com.snofed.publicapp.databinding.ActivityMainHomeBinding
import com.snofed.publicapp.ui.dashboardFragment.HomeFragment
import com.snofed.publicapp.ui.note.MainFragment
import com.snofed.publicapp.ui.note.NoteFragment
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