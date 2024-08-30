package com.snofed.publicapp.maps

import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.snofed.publicapp.databinding.FragmentStartMapRideBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import android.provider.Settings
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.ColorRes
import com.mapbox.maps.plugin.locationcomponent.location
import com.snofed.publicapp.databinding.DialogSaveRideBinding
import com.snofed.publicapp.utils.MediaReader


@AndroidEntryPoint
class StartMapRideFragment : Fragment() {
    private var _binding: FragmentStartMapRideBinding? = null
    private val binding get() = _binding!!
    private val feedWorkoutViewModel by viewModels<AuthViewModel>()


    private lateinit var mediaReader: MediaReader
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val updateInterval: Long = 1000 // Update interval in milliseconds
    private var lastLocation: Location? = null
    private var startTimeForSpeed: Long = 0 // Start time for speed calculation
    private var startTime: Long = 0
    private var totalDistance = 0.0 // Variable to store total distance in kilometers
    private var isTracking = false

    private var distanceFormatted: String = ""
    private var speedFormatted: String = ""
    private var elapsedTimeFormatted: String = ""

    // Battery optimization intervals
    private val highAccuracyUpdateInterval: Long = 10000 // 10 seconds
    private val balancedPowerUpdateInterval: Long = 10000 // 10 seconds

    private lateinit var imageView: ImageView
    private var uri: Uri? = null // Store the current URI

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_start_map_ride, container, false)
        _binding = FragmentStartMapRideBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize MediaReader with this fragment
        mediaReader = MediaReader(this)

        handler = Handler(Looper.getMainLooper())
        mapView = binding.mapView
        // Initialize TextViews
        binding.timeTextView.text = "00:00:00"
        binding.speedTextView.text = "0.00 km/h"
        binding.distanceTextView.text = "0.00 km"


        // Update the status bar color here
        updateStatusBarColor()
        openRideCamera()
        handlePickedOrCapturedImage()


        /// Button Click Listeners
        binding.startButton.setOnClickListener { startTracking() }
        binding.btnEnd.setOnClickListener { stopTracking() }
        binding.btnPause.setOnClickListener { pauseTracking() }
        binding.resumeButton.setOnClickListener { resumeTracking() }


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, highAccuracyUpdateInterval)
                .setMinUpdateIntervalMillis(highAccuracyUpdateInterval)
                .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult
                for (location in locationResult.locations) {
                    handleLocationUpdate(location)
                }
            }
        }

        // Restore map state
        checkLocationPermission()
    }

    private fun handlePickedOrCapturedImage() {
        // Get the URI from MediaReader
        val uri = uri?.let {
            mediaReader.getImageUri(it)
        }

        uri?.let {
            // Get the file path from the URI
            val filePath = mediaReader.getRealPathFromUri(uri)
            print("filePath" + filePath)
            // Upload the image to the server
            filePath?.let { path ->
               // uploadImageToServer(path)
            }
        }
    }

    private fun openRideCamera() {
        // Set click listener on ImageView
        binding.rideCameraMap.setOnClickListener {
            showImageOptionsDialog()
        }
    }

    private fun showImageOptionsDialog() {
        val options = arrayOf("Take Photo")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Select Image Source")
        builder.setItems(options) { _: DialogInterface, which: Int ->
            when (which) {
                0 -> mediaReader.checkPermissionsAndOpenCamera() // Take Photo
            }
        }
        builder.show()
    }

    private fun updateStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = requireActivity().window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.parseColor("#011737") // Example color
        }
    }

    private fun startTracking() {
        isTracking = true
        startTime = System.currentTimeMillis()
        startTimeForSpeed = startTime
        startLocationUpdates()
        binding.startButton.visibility = View.GONE
        binding.btnLL.visibility = View.VISIBLE
        binding.btnEnd.visibility = View.VISIBLE
        binding.rideCameraMap.visibility = View.VISIBLE
        binding.resumeButton.visibility = View.GONE
    }

    private fun pauseTracking() {
        isTracking = false
        stopLocationUpdates()
        binding.btnPause.visibility = View.GONE
        binding.resumeButton.visibility = View.VISIBLE
    }

    private fun resumeTracking() {
        isTracking = true
        startTimeForSpeed = System.currentTimeMillis()
        startLocationUpdates()
        binding.resumeButton.visibility = View.GONE
        binding.btnLL.visibility = View.VISIBLE
        binding.btnPause.visibility = View.VISIBLE
    }

    private fun stopTracking() {
        isTracking = false
        stopLocationUpdates()

        // Send the data to the server
        //feedWorkoutViewModel.sendRideData(elapsedTimeFormatted, distanceFormatted, speedFormatted)
        Toast.makeText(requireContext(), "Save ride ", Toast.LENGTH_SHORT).show()
// Show the custom dialog when End button is clicked
        showSaveRideDialog()
        // Observe the response from the ViewModel
        /*feedWorkoutViewModel.rideDataState.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(
                    requireContext(),
                    "Failed to send ride data: ${it.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }*/
    }

    private fun showSaveRideDialog() {
        // Use view binding for the custom dialog
        val dialogBinding = DialogSaveRideBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)


        // Set dialog title (optional, already set in XML)
        dialogBinding.dialogTitle.text = "You finished your ride"

        // Set click listener for Save button
        dialogBinding.buttonSaveRide.setOnClickListener {
            val comment = dialogBinding.editTextComment.text.toString()
            val isPublic = dialogBinding.radioButtonPublic.isChecked

            // Handle saving the ride here
            saveRide(comment, isPublic)

            // Dismiss the dialog
            dialog.dismiss()
        }
        // Show the dialog
        dialog.show()
        // Expand the width of the dialog
        val layoutParams = dialog.window?.attributes
        layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT // or a specific size
        dialog.window?.attributes = layoutParams

    }

    private fun saveRide(comment: String, isPublic: Boolean) {
        // Logic to save the ride based on the comment and privacy setting
        if (isPublic) {
            // Save as a public ride
        } else {
            // Save as a private ride
        }
    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED && isTracking
        ) {
            startLocationUpdates()
        }
        initializeLocationComponent() // Reinitialize the location component on resume
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates()
    }

    @SuppressLint("Lifecycle")
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("Lifecycle")
    override fun onStop() {
        super.onStop()
        mapView.onStop() // Ensure MapView stops
    }


    @SuppressLint("Lifecycle")
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }


    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                if (isInternetAvailable()) {
                    enableGPS()
                } else {
                    showToast("No internet connection. Please check your network settings.")
                }
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    private fun handlePermissionDenied() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            showRationaleDialog()
        } else {
            showPermissionSettingsDialog()
        }
    }

    private fun showRationaleDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Location Permission Needed")
            .setMessage("This app needs the Location permission to provide current location updates. Please grant this permission.")
            .setPositiveButton("OK") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun showPermissionSettingsDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Permission Required")
            .setMessage("Location permission is required for this app to function. Please enable it in the app settings.")
            .setPositiveButton("Open Settings") { _, _ ->
                openAppSettings()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    private fun enableGPS() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(requireActivity())
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            // Initialize location component after successful GPS check
            initializeLocationComponent()
        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(
                        requireActivity(),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun initializeLocationComponent() {
        if (::fusedLocationClient.isInitialized) {
            mapView.getMapboxMap()
                .loadStyleUri("mapbox://styles/systainadev/clzeswuev00bn01pl0whu8v9i") {
                    val locationComponent = mapView.location
                    locationComponent.updateSettings {
                        enabled = true
                        pulsingEnabled = true
                    }

                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        location?.let {
                            Log.d(
                                "TAG",
                                "Last known location: lat=${it.latitude}, lng=${it.longitude}"
                            )
                            startCameraAnimation(it.latitude, it.longitude)
                        } ?: Log.e("TAG", "Last known location is null.")
                    }
                }
        } else {
            Log.e("TAG", "fusedLocationClient is not initialized.")
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (::fusedLocationClient.isInitialized) {
            optimizeBatteryUsage() // Optimize battery before starting location updates
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } else {
            Log.e("TAG", "fusedLocationClient is not initialized.")
        }
    }

    private fun stopLocationUpdates() {
        if (::fusedLocationClient.isInitialized) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun optimizeBatteryUsage() {
        val newBuilder =
            LocationRequest.Builder(locationRequest.priority, balancedPowerUpdateInterval)
                .setMinUpdateIntervalMillis(locationRequest.minUpdateIntervalMillis)
                .setMaxUpdateDelayMillis(locationRequest.maxUpdateDelayMillis)
                .setMaxUpdates(locationRequest.maxUpdates)

        // Modify the priority based on battery saver mode
        if (isBatterySaverEnabled()) {
            newBuilder.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY)
        } else {
            newBuilder.setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        }

        locationRequest = newBuilder.build()
    }

    private fun isBatterySaverEnabled(): Boolean {
        val powerManager =
            requireContext().getSystemService(Context.POWER_SERVICE) as android.os.PowerManager
        return powerManager.isPowerSaveMode
    }

    private fun handleLocationUpdate(location: Location) {
        if (isTracking) {
            // Update UI
            Log.d(
                "TAG",
                "Handling location update: lat=$location.latitude, lng=$location.longitude"
            )
            // Update the map camera to the current location
            startCameraAnimation(location.latitude, location.longitude)
            // Calculate distance and speed
            calculateDistanceAndSpeed(location)
        }
        /*   val latitude = location.latitude
           val longitude = location.longitude


           startCameraAnimation(latitude, longitude)
           calculateDistanceAndSpeed(location)

           // Update last location
           lastLocation = location*/
    }

    @SuppressLint("MissingPermission")
    private fun startCameraAnimation(latitude: Double, longitude: Double) {
        handler.postDelayed({
            val cameraOptions = CameraOptions.Builder()
                .center(Point.fromLngLat(longitude, latitude))
                .zoom(16.0)
                .bearing(5.5)
                .pitch(0.0)
                .build()
            mapView.mapboxMap.setCamera(cameraOptions)

        }, updateInterval)
    }

    private fun calculateDistanceAndSpeed(currentLocation: Location) {
        if (lastLocation != null) {

            // Calculate the distance between the last and current locations
            val distanceInMeters = lastLocation!!.distanceTo(currentLocation)
            val distanceInKilometers = distanceInMeters / 1000.0
            totalDistance = distanceInKilometers// collect add distance

            // Calculate the speed (km/h)
            val currentTime = System.currentTimeMillis()
            val timeElapsed = (currentTime - startTimeForSpeed) / 1000.0 // Convert to seconds
            val speedMps = distanceInMeters / timeElapsed // Speed in meters per second
            val speedKph = speedMps * 3.6

            // Format the distance and speed
            distanceFormatted = String.format("%.2f", totalDistance)
            speedFormatted = String.format("%.2f", speedKph)
            val elapsedTimeMillis = currentTime - startTime
            elapsedTimeFormatted = formatElapsedTime(elapsedTimeMillis)

            Log.d(
                "TAG",
                "Distance: $distanceFormatted km, Speed: $speedFormatted km/h, Time: $elapsedTimeFormatted"
            )

            activity?.runOnUiThread {
                binding.distanceTextView.text = "$distanceFormatted km"
                binding.speedTextView.text = "$speedFormatted km/h"
                binding.timeTextView.text = elapsedTimeFormatted
            }

            lastLocation = currentLocation
            startTimeForSpeed = currentTime
        } else {
            // If this is the first location update, just set the lastLocation
            lastLocation = currentLocation
            startTimeForSpeed = System.currentTimeMillis()
        }
    }

    @SuppressLint("DefaultLocale")
    private fun formatElapsedTime(elapsedTimeMillis: Long): String {
        val hours = (elapsedTimeMillis / 3600000).toInt()
        val minutes = ((elapsedTimeMillis % 3600000) / 60000).toInt()
        val seconds = ((elapsedTimeMillis % 60000) / 1000).toInt()
        return String.format("%02d : %02d : %02d", hours, minutes, seconds)
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                checkLocationPermission()
            } else {
                handlePermissionDenied()
                showToast("Location permission is required to use this feature.")
            }
        }
}

