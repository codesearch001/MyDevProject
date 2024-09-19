package com.snofed.publicapp.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.ConnectivityManager
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.snofed.publicapp.utils.SnofedConstants.Companion.FIRST_TIME_APP_USE
import com.snofed.publicapp.utils.SnofedConstants.Companion.PREFS_NAME
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SnofedUtils {

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001


        // Function to check if location permissions are granted
        fun isLocationPermissionGranted(context: Context): Boolean {
            return ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }

        // Function to request location permissions
        fun requestLocationPermissions(activity: Activity) {
            ActivityCompat.requestPermissions(activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        }

        // Function to check if GPS is enabled
        fun isGPSEnabled(context: Context): Boolean {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }

        // Function to prompt user to enable GPS if disabled
        fun promptEnableGPS(activity: Activity) {
            val gpsIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            activity.startActivity(gpsIntent)
        }


        fun calculateAvgPace(seconds: Double, tripLength: Double): Double {
            return seconds / (tripLength) * 16.667
        }


        fun getDateNow(format: String): String {
            val sdf = SimpleDateFormat(format, Locale.getDefault())
            return sdf.format(Date())
        }

        // Check if it's the first time the app is being used
        fun isFirstTimeAppUse(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(FIRST_TIME_APP_USE, true) // default to true if not set
        }

        // Set that the app has been used for the first time (after login or first sync)
        fun setFirstTimeAppUse(context: Context, isFirstTime: Boolean) {
            val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean(FIRST_TIME_APP_USE, isFirstTime)
            editor.apply()
        }

        fun checkIfThereIsNetworkConnection(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }
    }



    /*  fun checkIfGPSEnabled(context: Context): Boolean {
        val manager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val builder = AlertDialog.Builder(context)
            builder.setMessage(R.string.startnewwo_enablegps_diag_msg)
                .setCancelable(false)
                .setPositiveButton(R.string.startnewwo_enablegps_diag_yes_btn) { dialog, _ ->
                    context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }
                .setNegativeButton(R.string.startnewwo_enablegps_diag_no_btn) { dialog, _ ->
                    dialog.cancel()
                }
            val alert = builder.create()
            alert.show()
            false
        } else {
            true
        }
    }*/

}