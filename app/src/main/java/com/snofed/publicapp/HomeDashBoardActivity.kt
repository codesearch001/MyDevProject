package com.snofed.publicapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.google.android.material.navigation.NavigationView
import com.snofed.publicapp.databinding.ActivityMainHomeBinding
import com.snofed.publicapp.databinding.NavHeaderMainBinding
import com.snofed.publicapp.ui.dashboardFragment.HomeFragment
import com.snofed.publicapp.ui.note.MainFragment
import com.snofed.publicapp.ui.note.NoteFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeDashBoardActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainHomeBinding // Generated binding class
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var fragmentManager: FragmentManager
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main_home)
        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setSupportActionBar(binding.toolbar)
        // setSupportActionBar(binding.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout//navigation_view
        // Set up the navigation header
        navigationView = binding.navigationView

        val headerView = navigationView.getHeaderView(0)// Set up the navigation header using View Binding
        val headerBinding = NavHeaderMainBinding.bind(headerView)

        headerBinding.backBtn.setOnClickListener {
            // Handle back button click
            onBackPressed()
        }
        binding.ddd.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val navigation_view: NavigationView = binding.navigationView

        val navView: BottomNavigationView = binding.bottomNavigationView
        navController = findNavController(R.id.fragment_container)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
                R.id.resorts_fragment,
                R.id.feed_fragment,
                R.id.eventFragment,
                R.id.settingFragment,
                R.id.helpFragment
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navigation_view.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_drawer_setting -> {
                    navController.navigate(R.id.settingFragment)
                    true
                }

                R.id.nav_drawer_help -> {
                    navController.navigate(R.id.helpFragment)
                    true
                }

                else -> false
            }.also {
                drawerLayout.closeDrawers() // Close the drawer after selecting an item
            }
        }

        //Hide/show bottom Navigation for other Fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.purchaseHistoryFragment2 -> hideBottomNav()
                R.id.membershipFragment -> hideBottomNav()
                R.id.buyMembershipFragment -> hideBottomNav()
                R.id.supportingMemberFragment -> hideBottomNav()
                R.id.bacomeAMemberFragment -> hideBottomNav()
                R.id.supportingMemberListFragment -> hideBottomNav()
                R.id.supportingMemDetailsFragment -> hideBottomNav()
                R.id.clubSubMembersFragment -> hideBottomNav()
                R.id.singleEventDetailsFragment -> hideBottomNav()
                R.id.profileSettingFragment -> hideBottomNav()
                R.id.settingFragment -> hideBottomNav()
                R.id.helpFragment -> hideBottomNav()
                else -> showBottomNav()
            }
        }

    }

    private fun showBottomNav() {
        binding.bottomNavigationView.visibility = View.VISIBLE

    }

    private fun hideBottomNav() {
        binding.bottomNavigationView.visibility = View.GONE

    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}