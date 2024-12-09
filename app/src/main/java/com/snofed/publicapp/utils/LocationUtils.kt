import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*

object LocationUtils {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    // Initialize location client and request setup
    fun initialize(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        setupLocationRequest()
    }

    // Set up location request details
    private fun setupLocationRequest() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .setWaitForAccurateLocation(true)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                location?.let {
                    onLocationFetched?.invoke(it) // Callback when location is available
                    fusedLocationClient.removeLocationUpdates(this) // Stop updates after the first location
                }
            }
        }
    }

    // Method to handle GPS icon click
    fun handleGpsClick(
        context: Activity,
        requestLocationPermissionLauncher: ActivityResultLauncher<String>,
        gpsSettingsLauncher: ActivityResultLauncher<Intent>,
        onLocationFetched: ((Location) -> Unit)
    ) {
        // Check for location permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Request location permission
            requestLocationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            // Permission granted; proceed to enable GPS and fetch location
            enableGPS(context, gpsSettingsLauncher, onLocationFetched)
        }
    }

    // Method to enable GPS
    private fun enableGPS(
        context: Activity,
        gpsSettingsLauncher: ActivityResultLauncher<Intent>,
        onLocationFetched: ((Location) -> Unit)
    ) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // GPS not enabled, prompt the user to enable it
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            gpsSettingsLauncher.launch(intent)
        } else {
            // GPS is enabled, request location updates
            requestLocationUpdates(context, onLocationFetched) // Pass context here
        }
    }

    // Add context as a parameter to the function
    private fun requestLocationUpdates(context: Context, onLocationFetched: ((Location) -> Unit)) {
        LocationUtils.onLocationFetched = onLocationFetched // Set callback
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permissions are not granted, so return without requesting updates
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }



    // Callback variable to handle location fetched event
    private var onLocationFetched: ((Location) -> Unit)? = null
}
