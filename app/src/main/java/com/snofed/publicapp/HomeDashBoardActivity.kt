package com.snofed.publicapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.core.view.GravityCompat
import androidx.core.view.WindowInsetsCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.snofed.publicapp.databinding.ActivityMainHomeBinding
import com.snofed.publicapp.databinding.NavHeaderMainBinding
import com.snofed.publicapp.utils.DrawerController
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeDashBoardActivity : AppCompatActivity(), DrawerController {
    private lateinit var binding: ActivityMainHomeBinding // Generated binding class
    private lateinit var appBarConfiguration: AppBarConfiguration
    /*private lateinit var fragmentManager: FragmentManager*/
    private lateinit var navController: NavController
    private lateinit var navigationView: NavigationView
    @Inject
    lateinit var tokenManager: TokenManager


    
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main_home)
        binding = ActivityMainHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Get the root view of the layout
        val rootView = window.decorView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            rootView.setOnApplyWindowInsetsListener { view, insets ->
                val systemGestureInsets = insets.getInsets(WindowInsetsCompat.Type.systemGestures())
                view.setPadding(view.paddingLeft, view.paddingTop, view.paddingRight, systemGestureInsets.bottom)
                insets
            }
        } else {
            // For older versions, use alternative methods if necessary
            // For example, manually adjust padding or margins if needed
        }
        // Optionally, apply fullscreen and immersive mode
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        }
        tokenManager.getClientId().toString()
        tokenManager.getUserId().toString()

        // setSupportActionBar(binding.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout//navigation_view

        // Disable drawer gesture (swipe-to-open)
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        // Set up the navigation header
        navigationView = binding.navigationView

        val headerView = navigationView.getHeaderView(0)// Set up the navigation header using View Binding
        val headerBinding = NavHeaderMainBinding.bind(headerView)

        headerBinding.backBtn.setOnClickListener {
            // Handle back button click
            onBackPressed()
        }

        val navigation_view: NavigationView = binding.navigationView

        val navView: BottomNavigationView = binding.bottomNavigationView

        navController = findNavController(R.id.fragmentContainer)
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
                R.id.nav_drawer_logout -> { // Handle logout click
                    handleLogout()
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
                R.id.linksFragment -> hideBottomNav()
                R.id.purchaseOptionsFragment -> hideBottomNav()
                R.id.trailsStatusFragment -> hideBottomNav()
                R.id.feedBackFragment -> hideBottomNav()
                R.id.singleResortsActivitiesFragment -> hideBottomNav()
                R.id.feedViewImageFragment -> hideBottomNav()
                R.id.mapFeedFragment -> hideBottomNav()
                R.id.startMapRideFragment -> hideBottomNav()
                R.id.mapExploreFragment -> hideBottomNav()
                R.id.twitterFragment -> hideBottomNav()
                R.id.orderTicketFragment -> hideBottomNav()
                R.id.newTicketFragment -> hideBottomNav()
                R.id.resortTrailStatusMapFragment -> hideBottomNav()
                R.id.resortSingleTrailsStatusDetailsFragment -> hideBottomNav()
                R.id.customDialogFragmentFragment -> hideBottomNav()
                R.id.clubEventFragment -> hideBottomNav()
                R.id.browseClubMapFragment -> hideBottomNav()
                R.id.singleResortReportProblemChooseLocationFragment -> hideBottomNav()
                R.id.singleResortReportProblemFragment -> hideBottomNav()
                R.id.rideLogsFragment -> hideBottomNav()
                R.id.feedBackDefaultCategoryListFragment -> hideBottomNav()
                R.id.feedBackDetailsFragment -> hideBottomNav()

                else -> showBottomNav()
            }
        }
    }

    private fun handleLogout() {
        val builder = AlertDialog.Builder(this, R.style.RoundedAlertDialog)
        builder.setTitle("Confirm Logout")
            .setMessage(getString(R.string.logout_from_track4))
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                // User confirmed logout
                tokenManager.clearSession() // Clear user session

                // Show a message
                Toast.makeText(this, R.string.t_logged_out, Toast.LENGTH_SHORT).show()

                // Navigate to the login activity
                val intent = Intent(this, OnBoarding::class.java)
                startActivity(intent)
                finish() // Optional: finish the current activity
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                // User cancelled the logout
                dialog.dismiss() // Close the dialog
            }
            .setCancelable(true) // Make the dialog cancelable
            .show() // Display the dialog
    }


    /*private fun enterFullScreenMode() {
        // Optionally, apply fullscreen and immersive mode
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                )
    }*/

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

    override fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }
}