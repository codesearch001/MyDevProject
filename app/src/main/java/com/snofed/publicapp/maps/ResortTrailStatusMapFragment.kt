package com.snofed.publicapp.maps

import android.Manifest
import android.provider.Settings
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.Point.fromLngLat
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.animation.easeTo
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin
import com.mapbox.maps.plugin.locationcomponent.location
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentResortTrailStatusMapBinding
import com.snofed.publicapp.models.DataPolyResponse
import com.snofed.publicapp.models.PolyLine
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.Constants
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.SnofedConstants
import com.snofed.publicapp.utils.TokenManager
import com.snofed.publicapp.utils.enums.PageType
import dagger.hilt.android.AndroidEntryPoint
import java.lang.System.setProperties
import javax.inject.Inject


@AndroidEntryPoint
class ResortTrailStatusMapFragment : Fragment() {
    private var _binding: FragmentResortTrailStatusMapBinding? = null
    private val binding get() = _binding!!
    private val viewModelTrails by viewModels<AuthViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    val boundsBuilder = LatLngBounds.Builder()
    var hasCoordinates = false
    val trails: String = ""
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation
    private var isOpen = false
    // Define the coordinates where you want to center the map
    private val longitude = -73.935242
    private val latitude = 40.730610
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var polyLineDataSatelliteView: PolyLine? = null
    private var polyLinesResponseSatelliteView: DataPolyResponse? = null
    var isSatelliteViewClicked : Boolean = false

    private val pageType: PageType? by lazy {
        arguments?.getParcelable<PageType>("pageType") // Retrieve pageType
    }

    val specificTrailId: String? by lazy {
        arguments?.getString("trailId") // Retrieve pageType
    }
    @Inject
    lateinit var tokenManager: TokenManager

    private var currentStyleIndex = 0
    private val styles = listOf(
        Style.MAPBOX_STREETS,
        Style.SATELLITE,
        Style.OUTDOORS
    )
    companion object {
        const val GPS_ENABLE_REQUEST_CODE = 1002
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_resort_trail_status_map, container, false)
        _binding = FragmentResortTrailStatusMapBinding.inflate(inflater, container, false)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        setupLocationRequest()
        Log.d("P0000", "Adding GeoJsonSource and LineLayer$trails")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        fabOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
        fabClose = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        rotateForward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_backward)


        binding.fab.setOnClickListener {
            animateFab()
        }

        binding.currentLocationTrailStatus.setOnClickListener {
            animateFab()
            checkPermissionsAndGps()

            //Toast.makeText(requireContext(), "camera click", Toast.LENGTH_SHORT).show()
        }

        binding.satelliteViewToggle.setOnClickListener {
            animateFab()
            isSatelliteViewClicked = !isSatelliteViewClicked
            SatelliteView()

            //Toast.makeText(requireContext(), "folder click", Toast.LENGTH_SHORT).show()
        }
        binding.feedbackTrailStatus.setOnClickListener {
            animateFab()
            val bundle = Bundle()
           // bundle.putString("clientId", clientId)
            val destination = R.id.feedBackDefaultCategoryListFragment
            //val destination = R.id.feedBackFragment
            findNavController().navigate(destination, bundle)
            //showCustomDialog2()
           // Toast.makeText(requireContext(), "folder click", Toast.LENGTH_SHORT).show()
        }
        val trails = tokenManager.getTrailsId().toString()
        Log.d("P1111", "Adding GeoJsonSource and LineLayer$trails")

        // Initialize MapView and MapboxMap
        mapView = binding.mapView
        mapboxMap = mapView.mapboxMap

        mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(fromLngLat(SnofedConstants.CENTER_LONG, SnofedConstants.CENTER_LAT)) // Set desired center
                .zoom(9.0) // Set desired zoom level
                .build())

        // Use the pageType here
        when (pageType) {
            PageType.MAP -> {
                // Handle MAP page type
                Log.d("MAP_TAG", "MAP")
                mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS) { style ->
                    // Set the camera options to adjust zoom level
//                      mapView.mapboxMap.setCamera(
//                       CameraOptions.Builder()
//                           .zoom(8.0) // Set the desired zoom level here
//                           .center(fromLngLat(defaultLong, defaultLat))
//                           //.center(mapView.mapboxMap.cameraState.center) // Center the camera on the current location
//                           .build(),
//
//                   )
//                    val cameraAnimationsPlugin = mapView.camera
//                    cameraAnimationsPlugin?.easeTo(CameraOptions.Builder()
//                       // .center(fromLngLat(defaultLong, defaultLat))
//                        //.padding(padding)
//                        .zoom(8.00)
//                        .build(),
//                        MapAnimationOptions.Builder()
//                            .duration(3000) // Duration in milliseconds (e.g., 3 seconds)
//                            .build())
                    // Observe the SharedViewModel for data updates
                    fetchResponse()
                    viewModelTrails.trailsDrawPolyLinesByIDLiveData.observe(viewLifecycleOwner, Observer { response ->
                        // binding.trailsNameMap.text = response.data.name
                        Log.d("TAG_TRAILS_STAUS", "trailsDrawPolyLinesByIDLiveData ${response.data?.data?.features}")
                        if (response != null) {

                            val trailResponse = response.data?.data
                            polyLinesResponseSatelliteView = response.data?.data
                            Log.d("TAG_TRAIL_RESPONSE", "TAG_TRAIL_RESPONSE $trailResponse.")
                            trailResponse?.let {
                                drawPolyline(style, it)
                            }

                        } else {
                            // Handle the null case
                            Toast.makeText(requireContext(), response.toString(), Toast.LENGTH_SHORT).show()
                            Log.d("TAG_NULL", "Adding GeoJsonSource and LineLayer${response}")
                        }
                    })
                }
            }
            PageType.DETAIL -> {
                // Handle DETAIL page type
                Log.d("DETAIL", "DETAIL")
                mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS) { style ->
                    // Observe the SharedViewModel for data updates
                    sharedViewModel.TrailsDetilsResponse.observe(viewLifecycleOwner, Observer { response ->
                        binding.trailsNameMap.text = response.data.name
                        if (response != null) {
                            val polylineData = response.data.polyLine
                            polyLineDataSatelliteView = response.data.polyLine
                            Log.d("P3333", "Print Details trails Id ${response.data.id}")
                            Log.d("P2222", "Adding GeoJsonSource and LineLayer${polylineData.features.size}")
                            getDrawPolyline(style, polylineData)
                        } else {
                            // Handle the null case
                            Toast.makeText(requireContext(), response.toString(), Toast.LENGTH_SHORT).show()
                            Log.d("TAG_NULL", "Adding GeoJsonSource and LineLayer${response}")
                        }
                    })
                }
            }
            null -> {
                // Handle case where pageType is null
                Log.d("MAP_TAG_AA", "MAP")
            }
        }

    }

    private fun setupLocationRequest() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .setWaitForAccurateLocation(true)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                val location = locationResult.lastLocation
                if (location != null) {
                    updateMapToCurrentLocation(location)
                    fusedLocationClient.removeLocationUpdates(locationCallback) // Stop updates after first result
                }
            }
        }
    }

    private fun updateMapToCurrentLocation(location: Location) {
        val currentPoint = fromLngLat(location.longitude, location.latitude)

        val cameraOptions = CameraOptions.Builder()
            .center(currentPoint)
            .zoom(12.0) // Adjust zoom level as needed
            .build()

        val animationOptions = MapAnimationOptions.Builder()
            .duration(2000) // Duration in milliseconds
            .build()

        mapboxMap.easeTo(cameraOptions, animationOptions)
        enableLocationComponent()
    }

    private fun enableLocationComponent() {
        // Get the location component
        val locationComponent: LocationComponentPlugin = mapView.location

        // Activate the location component
        locationComponent.updateSettings {
            enabled = true
            pulsingEnabled = true // Pulsing effect around the current location
            pulsingColor = Color.BLUE // Customize pulsing color if needed
        }

        // Set the location puck to display an arrow indicating direction

    }

    private fun checkPermissionsAndGps() {
        // Check if the app has location permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {

            // Request location permission
            requestLocationPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            enableGPS() // If permission is already granted, enable GPS
        }


    }
    // Register the permission result launcher
    private val requestLocationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                enableGPS()
            } else {
                Toast.makeText(requireContext(), "Location permission is required", Toast.LENGTH_SHORT).show()
            }
        }

    private fun enableGPS() {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // If GPS is not enabled, open GPS settings screen
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            gpsSettingsLauncher.launch(intent)
        } else {
            //moveToCurrentLocation()
            requestLocationUpdates()
//            // If GPS is enabled, show a message or proceed with GPS-related tasks
//            Toast.makeText(requireContext(), "GPS is already enabled", Toast.LENGTH_SHORT).show()
        }
    }
    private fun requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }
    /*// Fetch and center map on current location
    private fun moveToCurrentLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val currentPoint = fromLngLat(it.longitude, it.latitude)

                // Animate map to current location
                val cameraOptions = CameraOptions.Builder()
                    .center(currentPoint)
                    .zoom(14.0) // Set preferred zoom level
                    .build()

                mapboxMap.easeTo(cameraOptions)
            } ?: run {
                Toast.makeText(requireContext(), "Unable to get current location", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Error fetching location: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }*/

    // Register the ActivityResultLauncher for GPS settings
    private val gpsSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(requireContext(), "GPS enabled", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please enable GPS", Toast.LENGTH_SHORT).show()
            }
        }


    private fun animateFab() {
        if (isOpen) {
            binding.fab.startAnimation(rotateForward)
            binding.currentLocationTrailStatus.startAnimation(fabClose)
            binding.satelliteViewToggle.startAnimation(fabClose)
            binding.feedbackTrailStatus.startAnimation(fabClose)
            binding.currentLocationTrailStatus.isClickable = false
            binding.satelliteViewToggle.isClickable = false
            binding.feedbackTrailStatus.isClickable = false
            isOpen = false
        } else {
            binding.fab.startAnimation(rotateBackward)
            binding.currentLocationTrailStatus.startAnimation(fabOpen)
            binding.satelliteViewToggle.startAnimation(fabOpen)
            binding.feedbackTrailStatus.startAnimation(fabOpen)
            binding.currentLocationTrailStatus.isClickable = true
            binding.satelliteViewToggle.isClickable = true
            binding.feedbackTrailStatus.isClickable = true
            isOpen = true
        }
    }

    private fun drawPolyline(style: Style, response: DataPolyResponse) {
        // Variables to track the bounding box
        var minLng = Double.MAX_VALUE
        var minLat = Double.MAX_VALUE
        var maxLng = Double.MIN_VALUE
        var maxLat = Double.MIN_VALUE

        response.features.forEach { feature ->
            val coordinates = feature.geometry.coordinates
            binding.trailsNameMap.text = feature.properties.trailName

            coordinates.forEach {
                val lng = it[0]
                val lat = it[1]
                if (lng < minLng) minLng = lng
                if (lat < minLat) minLat = lat
                if (lng > maxLng) maxLng = lng
                if (lat > maxLat) maxLat = lat
            }

            val points = coordinates.map { fromLngLat(it[0], it[1]) }
            val lineString = LineString.fromLngLats(points)
            val sourceId = "line-source-${feature.properties.trailId}"
            val layerId = "line-layer-${feature.properties.trailId}"

            if (style.getSource(sourceId) == null) {
                style.addSource(geoJsonSource(sourceId) { geometry(lineString) })
            }

            if (style.getLayer(layerId) == null) {
                style.addLayer(lineLayer(layerId, sourceId) {
                    lineColor(Color.parseColor(feature.properties.color))
                    lineWidth(5.0)
                })
            }
        }

        // Adjust camera to fit the bounding box
        if (response.features.isNotEmpty()) {
            // Create a list of Point objects for bounding box calculation
            val boundsCoordinates = listOf(
                Point.fromLngLat(minLng, minLat),
                Point.fromLngLat(maxLng, maxLat)
            )

            // Calculate camera options to fit the bounding box
            val cameraOptions = mapboxMap.cameraForCoordinates(
                boundsCoordinates,
                EdgeInsets(100.0, 50.0, 100.0, 50.0) // Padding: top, left, bottom, right
            )

            // Animate to the calculated camera position
            val animationOptions = MapAnimationOptions.Builder()
                .duration(2000) // Duration in milliseconds
                .build()
            mapboxMap.easeTo(cameraOptions, animationOptions)
        }
    }

    private fun fetchResponse() {
        viewModelTrails.trailsDrawPolyLinesByIDRequestUser(specificTrailId!!)
    }

    private fun getDrawPolyline(style: Style, polylineData: PolyLine) {
        val feature = polylineData.features.firstOrNull()
        val coordinates = feature?.geometry?.coordinates

        if (coordinates.isNullOrEmpty()) {
            Log.e("MapError", "No coordinates available in polyline data.")
            Toast.makeText(
                requireContext(),
                "No coordinates available to draw the polyline.",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        // Convert coordinates to Points
        val points = coordinates.map { coordinatePair ->
            fromLngLat(coordinatePair[0], coordinatePair[1])
        }

        // Create LineString from Points
        val lineString = LineString.fromLngLats(points)

        // Add a GeoJsonSource
        val geoJsonSourceId = "line-source"
        if (style.getSource(geoJsonSourceId) == null) {
            style.addSource(geoJsonSource(geoJsonSourceId) {
                geometry(lineString)
            })
        }

        // Add a LineLayer
        val lineLayerId = "line-layer"
        if (style.getLayer(lineLayerId) == null) {
            style.addLayer(lineLayer(lineLayerId, geoJsonSourceId) {
                lineColor(feature.properties.color ?: "#ff0000") // Default color: red
                lineWidth(5.0)
            })
        }

        // Calculate bounds using LatLngBounds.Builder
        val boundsBuilder = LatLngBounds.Builder()
        points.forEach { point ->
            boundsBuilder.include(LatLng(point.latitude(), point.longitude()))
        }
        val latLngBounds = boundsBuilder.build()

        // Animate camera to fit the bounds
        val cameraOptions = mapboxMap.cameraForCoordinates(
            points,
            EdgeInsets(100.0, 50.0, 100.0, 50.0) // Padding: top, left, bottom, right
        )

        val animationOptions = MapAnimationOptions.Builder()
            .duration(1500) // Duration in milliseconds
            .build()

        mapView.camera.easeTo(cameraOptions, animationOptions)
    }


    // MAP Toggle

    fun SatelliteView(){
        if (isSatelliteViewClicked) {
            toggleMapStyle(true)
        } else {
            toggleMapStyle(false)
        }
    }

    fun toggleMapStyle(isSatellite: Boolean) {
        val styleType = if (isSatellite) Style.SATELLITE else Style.OUTDOORS

        mapboxMap.loadStyle(styleType) { style ->
            // Remove old layers and sources (if necessary)
            if (polyLineDataSatelliteView != null) {
                    getDrawPolyline(style, polyLineDataSatelliteView!!)
            }

            if(polyLinesResponseSatelliteView != null){
                drawPolyline(style, polyLinesResponseSatelliteView!!)
            }
        }
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}



