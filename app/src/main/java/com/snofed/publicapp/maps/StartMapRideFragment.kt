package com.snofed.publicapp.maps

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.Priority
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task
import com.google.gson.Gson
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.SymbolLayer
import com.mapbox.maps.plugin.locationcomponent.location
import com.snofed.publicapp.R
import com.snofed.publicapp.SnofedApplication
import com.snofed.publicapp.databinding.DialogSaveRideBinding
import com.snofed.publicapp.databinding.FragmentStartMapRideBinding
import com.snofed.publicapp.db.WorkoutResponse
import com.snofed.publicapp.ridelog.NewRideWorkout
import com.snofed.publicapp.ridelog.NewWorkoutImage
import com.snofed.publicapp.ridelog.NewWorkoutPoint
import com.snofed.publicapp.ui.User.UserViewModelRealm
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.ui.setting.WorkoutDataImages
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.Constants
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.MediaReader
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.ServiceUtil
import com.snofed.publicapp.utils.SharedPreferenceKeys
import com.snofed.publicapp.utils.TokenManager
import com.snofed.publicapp.utils.WorkoutMediaReader
import com.snofed.publicapp.utils.enums.SyncActionEnum
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject


@AndroidEntryPoint
class StartMapRideFragment : Fragment(),WorkoutMediaReader.OnImageUriReceivedListener {
    private var _binding: FragmentStartMapRideBinding? = null
    private val binding get() = _binding!!
    private val feedWorkoutViewModel by viewModels<AuthViewModel>()

    private lateinit var workoutViewModel: WorkoutViewModel
    private val viewModel by viewModels<AuthViewModel>()

    private lateinit var mediaReader: WorkoutMediaReader
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    //private val updateInterval: Long = 1000 // Update interval in milliseconds
    private val updateInterval: Long = 200 // Update interval in milliseconds
    private var lastLocation: Location? = null
    private var startTimeForSpeed: Long = 0 // Start time for speed calculation
    private var startTime: Long = 0
    private var totalDistance = 0.0f // Variable to store total distance in kilometers
    private var totalDistanceTest = 0.0 // Variable to store total distance in kilometers
    private var isRunning = false

    private var distanceFormatted: String = ""
    private var distanceFormattedd: String = ""
    private var speedFormatted: String = ""
    private var speedFormattedDouble: Double = 0.0
    private var elapsedTimeFormatted: String = ""

    // Battery optimization intervals
    private val highAccuracyUpdateInterval: Long = 1000 // 10 seconds
    private val balancedPowerUpdateInterval: Long = 1000 // 10 seconds

    private lateinit var imageView: ImageView
    private var uri: Uri? = null // Store the current URI
    private var isPublicRide: Boolean = true // Default value

    private var isFirstReceivedLocation: Boolean = true
    private var previousLocation: Location? = null
    private var distance: Float = 0f
    val ACTIVE: Boolean = false

    // private var workout: List<NewRideWorkout> = listOf()
    // Is the stopwatch running?
    private var running = false
    private var workout: NewRideWorkout? = null
    private var workoutPoints: NewWorkoutPoint? = null
    private var getWorkoutImage: NewWorkoutImage? = null
    private var userId: String? = null
    private var workout_UDID: String? = null
   // private var workout_Point_UDID: String? = null
    private var userName: String? = null
    private var getWorkoutID: String? = null
    private var workouttest: String? = null
    private val isAutoPauseON = false
    private var Houre = 0
    private var Seconds = 0.0
    private var Minutes = 0
    private var avgPace = 0.0

    private val CAMERA_PERMISSION_REQUEST = 101
    private val CAMERA_REQUEST_CODE = 102
    private val capturedImages = mutableListOf<String>() // List to store image paths
    private var photoFile: File? = null
   // private var workoutImgTemp : List<String> = listOf()
    private var workoutImgTemp: MutableList<String> = mutableListOf()

    private lateinit var viewModelUserRealm: UserViewModelRealm
    private var lastClickTime = 0L

    @Inject
    lateinit var tokenManager: TokenManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_start_map_ride, container, false)
        _binding = FragmentStartMapRideBinding.inflate(inflater, container, false)
        // Initialize MediaReader with this fragment
        //mediaReader = MediaReader(this)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        // Initialize MediaReader with this fragment
        workoutViewModel = ViewModelProvider(this)[WorkoutViewModel::class.java]
        viewModelUserRealm = ViewModelProvider(this).get(UserViewModelRealm::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Insert or fetch data from Realm
        /* realm.executeTransaction { realm ->
             // Example: Insert data into Realm
             val myData = realm.createObject(NewWorkoutPoint::class.java)

         }*/
        mediaReader = WorkoutMediaReader(this,this)
        userId = tokenManager.getUserId()

        var userSettingScreenOn = viewModelUserRealm.getPublicUserSettingValue(userId!!,"ScreenAlwaysOn")
        var isScreenOn = if(userSettingScreenOn.isNullOrEmpty()) false else if(userSettingScreenOn.toBoolean()) true else false

        if (isScreenOn) {
            requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        userName = tokenManager.getFullName()

        getWorkoutID = tokenManager.getWorkoutUdId()


        workout_UDID = UUID.randomUUID().toString()
       // workout_Point_UDID = UUID.randomUUID().toString()

        Log.e("TAG_StartMapRideFragment", "workout_UDID: start$workout_UDID")

       // init()


        //mediaReader = MediaReader(this)

        handler = Handler(Looper.getMainLooper())
        mapView = binding.mapView
        // Initialize TextViews
        binding.timeTextView.text = "00:00:00"
        binding.speedTextView.text = "0.00 km/h"
        binding.distanceTextView.text = "0.00 km"


        if (handler != null) {
            // handler.removeCallbacks(runnable)
        }


        bindObservers()
        /// Button Click Listeners
        binding.bStart.setOnClickListener {
            startTracking()
        }
        binding.bEnd.setOnClickListener {
            stopTracking()
        }
        binding.bPause.setOnClickListener {
            pauseTracking()
        }
        binding.bResume.setOnClickListener {
            resumeTracking()
        }
        // Set click listener on ImageView
        binding.rideCameraMap.setOnClickListener {
            showImageOptionsDialog()
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, highAccuracyUpdateInterval)
                .setMinUpdateIntervalMillis(highAccuracyUpdateInterval).build()

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

    private fun showImageOptionsDialog() {
        val options = arrayOf(resources.getString(R.string.take_photo), resources.getString(R.string.choose_from_gallery))
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.t_select_image_source))
        builder.setItems(options) { _: DialogInterface, which: Int ->
            when (which) {
                0 -> mediaReader.checkPermissionsAndOpenCamera() // Take Photo
                1 -> mediaReader.checkPermissionsAndOpenGallery() // Choose from Gallery
            }
        }
        builder.show()
    }


    private fun bindObservers() {
        feedWorkoutViewModel.userWorkoutRideLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {


                    if (workoutImgTemp.isNotEmpty()){

                        sendWorkoutImages()
                        Toast.makeText(requireActivity(), "Ride Data Send Successfully Please wait for Images to save ", Toast.LENGTH_LONG).show()
                    }else{
                        findNavController().popBackStack()
                        Toast.makeText(requireActivity(), "Ride Data Saved Successfully", Toast.LENGTH_LONG).show()
                    }


                }

                is NetworkResult.Error -> {

                    Toast.makeText(requireActivity(), it.data?.message.toString(), Toast.LENGTH_LONG).show()

                }

                is NetworkResult.Loading -> {

                }
            }
        })
    }


    private fun imageResponse(uri: MutableList<String>, getWorkoutID: String) {
        val imageFile = uriToFile(uri)
        viewModel.uploadWorkOutIdImage(getWorkoutID, imageFile)
        Log.e("TAG_ProfileSettingFragment", "onImageUriReceived: File path1111111 - ${imageFile}")
    }

    private fun uriToFile(uris: List<String>): List<File> {
        val fileList = mutableListOf<File>()
        //val contentResolver = requireContext().contentResolver
        uris.forEach { uriString->

            //File(uriString)
            fileList.add(File(uriString))
            Log.e("TAG", "Error converting URI to File   $uriString")
        }
        return fileList
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
        enableGPS()
        Log.e("TAG_StartMapRideFragment", "onStartRecordingClick: start")

        //first time initialization when before start button click
        workout = NewRideWorkout(
            id = workout_UDID.toString(),
            syncAction = SyncActionEnum.NEW.getValue(),
            publicUserId = userId.toString(),
            publisherFullname = userName.toString(),
            activityId = Constants.ACTIVITIES_ID,
            startTime = Helper.getDateNow(Constants.DATETIME_FORMAT),
            isNewlyCreated = true
        )

        tokenManager.saveWorkoutUdId(workout!!.id)

        workoutViewModel.addNewRideWorkout(workout!!)


        isRunning = true
        startTime = System.currentTimeMillis()
        startTimeForSpeed = startTime
        startLocationUpdates()
        binding.rideCameraMap.visibility = View.VISIBLE
        binding.bStart.visibility = View.GONE
        binding.bConatiner.visibility = View.VISIBLE
        binding.bEnd.visibility = View.VISIBLE
        binding.bResume.visibility = View.GONE
        binding.bPause.visibility = View.VISIBLE
        running = true
        //handler.postDelayed(runnable, 1000)
    }

    private fun pauseTracking() {
        isRunning = false
        stopLocationUpdates()
        binding.bPause.visibility = View.GONE
        binding.bResume.visibility = View.VISIBLE

        running = false
        //handler.removeCallbacks(runnable)
    }

    private fun resumeTracking() {
        isRunning = true
        startTimeForSpeed = System.currentTimeMillis()
        startLocationUpdates()
        binding.bResume.visibility = View.GONE
        // binding.bEnd.visibility = View.VISIBLE
        binding.bPause.visibility = View.VISIBLE
        running = true
        //handler.postDelayed(runnable, 1000)
    }

    private fun stopTracking() {
        isRunning = false
        binding.timeTextView.text = "00:00:00"
        binding.speedTextView.text = "0.00 km/h"
        binding.distanceTextView.text = "0.00 km"
        elapsedTimeFormatted = ""
        distanceFormatted = ""
        speedFormatted = ""
        speedFormattedDouble = 0.0
        Log.e("TAG", "onStopRecordingClick")
        Log.d(
            "TAG",
            "Time: $elapsedTimeFormatted ,Distance: $distanceFormatted m, Speed: $speedFormatted km/h, "
        )
        SnofedApplication.isRecordingWorkoutProcessing = false
        Houre = 0
        Seconds = 0.0
        Minutes = 0
        running = false
        stopLocationUpdates()
        binding.bConatiner.visibility = View.GONE
        binding.bStart.visibility = View.VISIBLE
        binding.bEnd.visibility = View.GONE
        binding.bPause.visibility = View.GONE
        binding.bResume.visibility = View.GONE
        //binding.bStart.is

        Toast.makeText(requireContext(), resources.getString(R.string.t_s_save_ride), Toast.LENGTH_SHORT).show()

        // Show the custom dialog when End button is clicked
        showSaveRideDialog()

    }

    private fun showSaveRideDialog() {
        // Use view binding for the custom dialog
        val dialogBinding = DialogSaveRideBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())
        dialog.setContentView(dialogBinding.root)

        // Set dialog title (optional, already set in XML)
        dialogBinding.dialogTitle.text = resources.getString(R.string.t_you_finished_your_ride)

        // Set default selection for radioButtonPrivate
        dialogBinding.radioButtonPrivate.isChecked = true
        isPublicRide = false // Since Private is selected by default

        // Set click listener for Save button
        dialogBinding.buttonSaveRide.setOnClickListener {

            val comment = dialogBinding.editTextComment.text.toString()

            // Update global variable based on selected radio button
            isPublicRide = dialogBinding.radioButtonPublic.isChecked

            // Print selected radio button value for debugging
            if (isPublicRide) {
                println("Selected: Public")
            } else {
                println("Selected: Private")
            }

            // Validate comment is not empty
            if (comment.isBlank()) {
                // Show a toast message as well
                Toast.makeText(requireContext(), resources.getString(R.string.t_please_enter_a_comment), Toast.LENGTH_SHORT).show()
            } else {
                // Remove any previous error message
                dialogBinding.editTextComment.error = null

                // Handle saving the ride here

                //val magedata  = workoutImgTemp
                // Check the time of the last click and prevent rapid clicks
                if (System.currentTimeMillis() - lastClickTime > 500) {
                    lastClickTime = System.currentTimeMillis()
                    saveRide(comment, isPublicRide)
                }


                // Dismiss the dialog
                dialog.dismiss()
            }
        }

        // Show the dialog
        dialog.show()

        // Expand the width of the dialog
        val layoutParams = dialog.window?.attributes
        layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT // or a specific size
        dialog.window?.attributes = layoutParams
    }

    //var newWorkoutId :String? = ""
    private fun saveRide(comment: String, isPublic: Boolean) {
        // Perform necessary actions to save the ride
        val rideType = if (isPublic) "public" else "private"
        Log.e("idprint", "praveen: start1 $" +tokenManager.getWorkoutUdId())


        println("Saving ride as $rideType with comment: $comment")
        println("Saving ride Two $rideType with WID:" + tokenManager.getWorkoutUdId().toString())

        sendRideToServer(comment, isPublic)
    }

    private fun sendRideToServer(comment: String, isPublic: Boolean) {

        workoutViewModel.addDescription(description = comment, isPublic = isPublic)

        println("Saving ride as $isPublic with comment: $comment")

        //Get data from REALM
        val workoutJsonList: List<WorkoutResponse> =
            workoutViewModel.fetchWorkoutByIdAsJsonList(tokenManager.getWorkoutUdId().toString())
        

        // FINAL DATA SEND OVER SERVER
        feedWorkoutViewModel.workOutRideRequest(workoutJsonList)



//        val gson = Gson()
//        val json = gson.toJson(workoutJsonList)
//
//        Log.d("received from realm db ride as", "sendReport: " + json)
//        println("received from realm db ride $workoutJsonList")

    }

    private fun sendWorkoutImages(){
        imageResponse(workoutImgTemp, tokenManager.getWorkoutUdId()!!)
        Log.e("idprint", "praveen: start2 $" +tokenManager.getWorkoutUdId())

        viewModel.uploadWorkoutResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    //val uploadMessage = result.data?.message ?: resources.getString(R.string.upload_successful)
                    //val images  = ServiceUtil.BASE_URL_IMAGE  + result.data?.data
                    val images : List<WorkoutDataImages>? = result.data?.data
                    val gson = Gson()
                    val json = gson.toJson(images)
                    Toast.makeText(requireActivity(), "Ride Data Saved Successfully", Toast.LENGTH_LONG).show()


                   // onImageUriReceived(images)
                    Log.e("TAG_Praveen", "json - $json")
                    findNavController().popBackStack()
                   // Toast.makeText(requireActivity(), images, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Error -> {
                    Log.e("TAG_ProfileSettingFragment", "result.message.toString() -" +result.message)
                    Toast.makeText(requireActivity(), result.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    // Handle loading state
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED && isRunning
        ) {
            startLocationUpdates()
        }
        initializeLocationComponent() // Reinitialize the location component on resume
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
                    showToast(resources.getString(R.string.t_no_internet_connection))
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
            mapView.mapboxMap
                .loadStyle(Style.OUTDOORS) { style ->
                    val locationComponent = mapView.location
                    locationComponent.updateSettings {
                        enabled = true
                        pulsingEnabled = true
                    }// Add a custom icon for the location component
                    // Define the custom icon name and URL
                    val iconName = "your_custom_icon" // Replace with your custom icon name

                    // Add the icon image to the map style
                    style.addImage(
                        iconName, // Custom icon name
                        BitmapFactory.decodeResource(
                            mapView.context.resources,
                            R.drawable.red_marker
                        ) // Replace with your actual image
                    )

                    // Create and configure the symbol layer
                    val symbolLayer = SymbolLayer("location-icon-layer", "location").apply {
                        iconImage(iconName) // Use the custom icon name
                        iconAllowOverlap(true) // Optional: Allow the icon to overlap with other symbols
                    }

                    // Add the symbol layer to the map style
                    style.addLayer(symbolLayer)


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

    @SuppressLint("SuspiciousIndentation")
    private fun handleLocationUpdate(location: Location) {
        if (isRunning) {
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
    /*
        val runnable = object : Runnable {
            override fun run() {
                if (workout == null) {
                    val workoutId = tokenManager.getWorkoutUdId()
                    workout = workoutViewModel.getWorkoutById(workoutId?.isNullOrEmpty().toString())
                }

               */
    /* if (isAutoPauseON) {
        workout?.let { calculateNotMovingInAutoPause(it.distance) }
    }*//*


            avgPace = SnofedUtils.calculateAvgPace(Seconds, workout?.distance ?: 0.0)
            Seconds = workout?.duration ?: 0.0
            val minutes = Seconds / 60
            val secs = Seconds % 60
            Seconds++
            //lastDistance = workout?.distance ?: 0.0

            workoutViewModel.updateWorkoutDurationAndAvgPace(workout, Seconds, avgPace)
            elapsedTimeFormatted = String.format("%.2f", minutes, secs)
            //SnofedUtils.showSpeedOrAvgPace(tvAVGPace, avgPace)

            handler.postDelayed(this, 1000)
        }
    }
*/


    @SuppressLint("SuspiciousIndentation")
    private fun calculateDistanceAndSpeed(currentLocation: Location) {
        if (lastLocation != null) {
            //val currentLatLng = LatLng(currentLocation.latitude, currentLocation.longitude)
            val currentLatLng = Location("").apply {
                latitude = currentLocation.latitude
                longitude = currentLocation.longitude
            }
            // MEASURE DISTANCE BETWEEN TWO POINTS (SKIP FIRST ONE)
            if (isFirstReceivedLocation) {
                Log.e("Praveen", "recordingTrail: First Received Location")
                isFirstReceivedLocation = false
            } else {
                distance = currentLatLng.distanceTo(previousLocation!!)
            }
            /*****************Create a new WorkoutPoint object*****************/

            //val udID = UUID.randomUUID().toString()
            // print("udID+praveen " + udID)
            workoutPoints = NewWorkoutPoint(
                UUID.randomUUID().toString(),
                currentLocation.latitude,
                currentLocation.longitude,
                speedFormattedDouble,
                Helper.getDateNow(Constants.DATETIME_FORMAT),
                distance.toDouble(),
                SyncActionEnum.NEW.getValue(),
                tokenManager.getWorkoutUdId().toString()
            )

            /***************** ADD REALM  the new WorkoutPoint to the ViewModel*****************/
            workoutViewModel.addWorkoutPoint(workoutPoints!!)


            // Calculate the distance between the last and current locations
            val distanceInMeters = lastLocation!!.distanceTo(currentLocation)
            // val distanceInKilometers = distanceInMeters / 1000.0
            val distanceInKilometers = distanceInMeters
            //totalDistance = distanceInKilometers// collect add distance
            totalDistance += distanceInKilometers// collect add distance
            // Calculate the speed (km/h)
            val currentTime = System.currentTimeMillis()
            val timeElapsed = (currentTime - startTimeForSpeed) / 1000.0 // Convert to seconds
            val speedMps = distanceInMeters / timeElapsed // Speed in meters per second
            val speedKph = speedMps * 3.6

            // Format the distance and speed
            distanceFormatted = String.format(Locale.US, "%.2f", totalDistance)
            //distanceFormattedd = String.format("%.2f", totalDistanceTest)
            speedFormatted = String.format(Locale.US, "%.2f", speedKph)
            speedFormattedDouble = speedFormatted.toDouble()
            val elapsedTimeMillis = currentTime - startTime
            elapsedTimeFormatted = formatElapsedTime(elapsedTimeMillis)
            val totalSeconds = convertTimeStringToSeconds(elapsedTimeFormatted)

            Log.d(
                "TAG_1",
                "Time1: $elapsedTimeFormatted ,Distance: $distanceFormatted m,  Speed: $speedFormatted km/h, "
            )
            Log.d(
                "TAG_2",
                "Time2: $totalSeconds ,Distance: $distanceFormatted m,  Speed: $speedFormatted km/h, "
            )

            activity?.runOnUiThread {
                //TIME
                binding.timeTextView.text = elapsedTimeFormatted
                //DISTANCE
                binding.distanceTextView.text = "$distanceFormatted m"
                //SPEED
                binding.speedTextView.text = "$speedFormattedDouble km/h"
            }

            //ADD REALM DB WORKOUT
            //workout = NewRideWorkout(duration = totalSeconds, distance = distanceFormatted.toInt(), averagePace = speedFormattedDouble.toInt())
            // workout = NewRideWorkout(duration = totalSeconds, distance =distanceFormatted.toDouble(), averagePace = speedFormattedDouble)
            //workoutViewModel.addWorkoutDurationAndAvgPace(workout!!)

            // Call the method with individual parameters
            workoutViewModel.addWorkoutDurationAndAvgPace(
                duration = totalSeconds,
                distance = distanceFormatted.toDouble(),
                averagePace = speedFormattedDouble
            )
            lastLocation = currentLocation
            startTimeForSpeed = currentTime

            previousLocation = currentLocation
        } else {
            // If this is the first location update, just set the lastLocation
            lastLocation = currentLocation
            startTimeForSpeed = System.currentTimeMillis()
        }
    }

    fun convertTimeStringToSeconds(timeString: String): Int {
        // Split the string by " : " to get hours, minutes, and seconds
        val parts = timeString.split(" : ").map { it.trim() }

        // Ensure that there are exactly three parts (hours, minutes, seconds)
        if (parts.size != 3) {
            throw IllegalArgumentException("Invalid time format: $timeString")
        }

        // Parse each component as an integer
        val hours = parts[0].toIntOrNull() ?: throw IllegalArgumentException("Invalid hours format")
        val minutes =
            parts[1].toIntOrNull() ?: throw IllegalArgumentException("Invalid minutes format")
        val seconds =
            parts[2].toIntOrNull() ?: throw IllegalArgumentException("Invalid seconds format")

        // Calculate the total number of seconds
        return (hours * 3600) + (minutes * 60) + seconds
    }


    @SuppressLint("DefaultLocale")
    private fun formatElapsedTime(elapsedTimeMillis: Long): String {
        val hours = (elapsedTimeMillis / 3600000).toInt()
        val minutes = ((elapsedTimeMillis % 3600000) / 60000).toInt()
        val seconds = ((elapsedTimeMillis % 60000) / 1000).toInt()
        return String.format("%02d : %02d : %02d", hours, minutes, seconds)
    }

    /* @SuppressLint("DefaultLocale")
     private fun formatElapsedTime(elapsedTimeMillis: Long): String {
         // Ensure the time is non-negative
         if (elapsedTimeMillis < 0) return "00 : 00 : 00"

         // Calculate hours, minutes, and seconds
         val hours = (elapsedTimeMillis / 3600000).toInt()
         val minutes = ((elapsedTimeMillis % 3600000) / 60000).toInt()
         val seconds = ((elapsedTimeMillis % 60000) / 1000).toInt()

         // Format the time string
         return "%02d : %02d : %02d".format(hours, minutes, seconds)
     }*/


    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                checkLocationPermission()
            } else {
                handlePermissionDenied()
                showToast(resources.getString(R.string.loacation_feature))
            }
        }



    private fun onImageUriReceived(uri: String) {
        print("Saving get ride as iMAGE:${uri}")
         getWorkoutImage = NewWorkoutImage(UUID.randomUUID().toString(),1, uri,"Test",tokenManager.getWorkoutUdId().toString())
         workoutViewModel.addWorkoutImage(getWorkoutImage!!)
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


    @SuppressLint("Lifecycle")
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onImageUriReceived(uri: Uri) {
        imageWorkoutResponse(uri)
    }

    private fun imageWorkoutResponse(uri: Uri) {
        val imageFile = uriToFile(uri)

        if (imageFile != null) {
            workoutImgTemp.add(imageFile.absolutePath)

            Log.e("TAG_", "onImageUriReceived: File path - ${imageFile.absolutePath}")
        } else {
            Log.e("TAG_", "onImageUriReceived: Error converting URI to File")
        }
    }

    private fun uriToFile(uri: Uri): File? {
        val contentResolver = requireContext().contentResolver
        val tempFile = File.createTempFile("workoutimages", ".jpg", requireContext().cacheDir)

        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            Log.e("MyFragment", "Error converting URI to File: ${e.message}")
            return null
        }

        return tempFile
    }

}

