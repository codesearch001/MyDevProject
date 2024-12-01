package com.snofed.publicapp.maps

import StatusItem
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson

import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.Point.fromLngLat
import com.mapbox.geojson.Polygon
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.fillLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.animation.easeTo
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin
import com.mapbox.maps.plugin.locationcomponent.location
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.MapIntervalAdapter
import com.snofed.publicapp.databinding.BottomSheetApartmentsBinding
import com.snofed.publicapp.databinding.FragmentMapExploreBinding
import com.snofed.publicapp.databinding.MapFilterDetailsBinding
import com.snofed.publicapp.models.realmModels.Area
import com.snofed.publicapp.models.realmModels.Poi
import com.snofed.publicapp.models.realmModels.Resource
import com.snofed.publicapp.models.realmModels.Trail
import com.snofed.publicapp.models.realmModels.Zone
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.IntervalViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.PoisTypeViewModelRealm
import com.snofed.publicapp.utils.ServiceUtil
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.SnofedConstants
import com.snofed.publicapp.utils.enums.SyncActionEnum
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MapExploreFragment : Fragment(){
    private var _binding: FragmentMapExploreBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var sharedViewModell: SharedViewModel

    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private var cameraAnimationsPlugin: CameraAnimationsPlugin? = null // Nullable type
    private var pointAnnotationManager: PointAnnotationManager? = null
    val boundsBuilder = LatLngBounds.Builder()
    var hasCoordinates = false
    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation
    private var isOpen = false
    val gson = Gson()
    var defaultLat :String = ""
    var defaultLong :String = ""
    var clientId : String? =""

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private lateinit var viewModelPoisType: PoisTypeViewModelRealm

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    //Map Interval
    private lateinit var viewModelInterval: IntervalViewModelRealm
    private lateinit var mapIntervalAdapter: MapIntervalAdapter
   // var allClientMapInterval : List<StatusItem> = emptyList()
    var allClientMapInterval : MutableList<StatusItem> = mutableListOf()

//    val allTrails: MutableList<Trail> = mutableListOf()
//    val allPois: MutableList<Poi> = mutableListOf()
//    val allZones: MutableList<Zone> = mutableListOf()
//    val allResources: MutableList<Resource> = mutableListOf()

    // Client MAP Data
    var clientTrails    : MutableList<Trail> = mutableListOf()
    var clientPois      : MutableList<Poi> = mutableListOf()
    var clientZones     : MutableList<Zone> = mutableListOf()
    var clientResources : MutableList<Resource> = mutableListOf()
    var clientAreas     : MutableList<Area> = mutableListOf()

    var filterTrailsIds : List<String> = emptyList()
    var filterPoiIds : Set<String> = emptySet()
    var filterZoneIds : List<String> = emptyList()
    var filterAreaIds : List<String> = emptyList()
    var filterResourceIds : List<String> = emptyList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_map_explore, container, false)
        _binding = FragmentMapExploreBinding.inflate(inflater, container, false)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        sharedViewModell = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModelPoisType = ViewModelProvider(this).get(PoisTypeViewModelRealm::class.java)
        //Area
        sharedViewModell.selectedAreaIds.observe(viewLifecycleOwner, Observer { selectedIds ->
            // Update the UI with the selected IDs
            Log.d("selectedAreaIds", "Selected1 IDs: $selectedIds")
            // Use the selectedIds to update the UI as needed
            filterAreaIds = selectedIds
        })

        //Trails
        sharedViewModell.selectedTrailId.observe(viewLifecycleOwner, Observer { selectedIds ->
            // Update the UI with the selected IDs
            Log.d("selectedTrailId", "Selected2 IDs: $selectedIds")
            // Use the selectedIds to update the UI as needed
            filterTrailsIds = selectedIds
        })

        // Pass the selected POI IDs to the SharedViewModel
        sharedViewModell.selectedPoisIds.observe(viewLifecycleOwner, Observer { selectedIds ->
            // Update the UI with the selected IDs
            Log.d("selectedPoisIds", "Selected3 IDs: $selectedIds")
            // Use the selectedIds to update the UI as needed
            filterPoiIds = selectedIds.toSet()
            val filteredData = clientPois.filter { poi -> filterPoiIds.contains(poi.id) }
            val gson = Gson()
            val json = gson.toJson(clientPois)
            Log.e("payData"  ,"hello  "+ json)
            Log.e("filterPoiIds_JAGGU", "filterPoiIds NEW :: $clientPois")
            Log.e("filterPoiIds_JAGGU", "filterPoiIds DATA :: $filteredData")
            addPoisToMap(clientPois)
        })


       //Zone
        sharedViewModell.selectedZoneTypeId.observe(viewLifecycleOwner, Observer { selectedIds ->
            // Update the UI with the selected IDs
            Log.d("selectedZoneTypeId", "Selected4 IDs: $selectedIds")
            // Use the selectedIds to update the UI as needed
            filterZoneIds = selectedIds
            var filteredData = clientZones.filter { it.id in filterZoneIds }
            addZonesToMap(clientZones)
        })








        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        setupLocationRequest()
        clientId = arguments?.getString("clientId").toString()
        return binding.root
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

    private fun updateMapToCurrentLocation(location: android.location.Location) {
        val currentPoint = fromLngLat(location.longitude, location.latitude)

        val cameraOptions = CameraOptions.Builder()
            .center(currentPoint)
            .zoom(12.0) // Adjust zoom level as needed
            .build()

        mapboxMap.easeTo(cameraOptions)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

///////////////////////////////////////////////

        // Intervals
        viewModelInterval = ViewModelProvider(this).get(IntervalViewModelRealm::class.java)
        var allIntervals = viewModelInterval.getAllIntervals()

        allIntervals = allIntervals.filter { it.syncAction != SyncActionEnum.DELETED.getValue() }
        allClientMapInterval = allIntervals.map { interval ->
            StatusItem(
                id = interval.id!!,
                text = interval.name ?: "No Name",
                color = interval.color ?: "#FFFFFF" // Default color

            )
        }.toMutableList()

        /* text = "Closed",
           color = "Red"*/
        allClientMapInterval.add(StatusItem(
            id = "0",
            text = resources.getString(R.string.t_close),
            color = "#eb4034"
        ))

        //////////////////////////////////////
        binding.intervalsButton.setOnClickListener {


            // Show or hide the CardView with animation
            val cardView = binding.intervalCardView
            if (cardView.visibility == View.GONE || cardView.visibility == View.INVISIBLE) {
                cardView.visibility = View.VISIBLE
                cardView.alpha = 0f
                cardView.animate()
                    .alpha(1f)
                    .setDuration(300)
                    .start()
            } else {
                cardView.animate()
                    .alpha(0f)
                    .setDuration(300)
                    .withEndAction {
                        cardView.visibility = View.GONE
                    }
                    .start()
            }
        }

        //Map Interval type
        mapIntervalAdapter = MapIntervalAdapter(allClientMapInterval)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = mapIntervalAdapter


        val defaultLocationJson = arguments?.getString("DefaultClubLocation") ?: ""
        val gson = Gson()
        val data = gson.fromJson(defaultLocationJson, Map::class.java)

        defaultLat = "%.6f".format(Locale.US,data["Latitude"]?.toString()?.toDoubleOrNull() ?: SnofedConstants.CENTER_LAT)
        defaultLong = "%.6f".format(Locale.US,data["Longitude"]?.toString()?.toDoubleOrNull() ?: SnofedConstants.CENTER_LONG)


        // Initialize MapView and MapboxMap
        mapView = binding.mapView
        mapboxMap = mapView.mapboxMap
        // Retrieve the CameraAnimationsPlugin
        // Retrieve the CameraAnimationsPlugin
        cameraAnimationsPlugin = mapView.getPlugin(CameraAnimationsPlugin::class.java.toString())
        mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(defaultLong.toDouble(), defaultLat.toDouble())) // Set desired center
                .zoom(6.0) // Set desired zoom level
                .pitch(9.0)
                .build()
        )

        fetchMapTrailsData()
        binding.fab1.setOnClickListener {
            //zoomOut()
        }

        fabOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
        fabClose = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        rotateForward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_backward)

        binding.mapByFilter.setOnClickListener {
            showBottomMapFilter()
        }
        binding.fab.setOnClickListener {
            animateFab()
        }

        binding.fab1.setOnClickListener {
            animateFab()
            checkPermissionsAndGps()

            //Toast.makeText(requireContext(), "camera click", Toast.LENGTH_SHORT).show()
        }

        binding.fab2.setOnClickListener {
            animateFab()

            //Toast.makeText(requireContext(), "folder click", Toast.LENGTH_SHORT).show()
        }
        binding.fab3.setOnClickListener {
            animateFab()
            showCustomDialog2()
            //Toast.makeText(requireContext(), "folder click", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showBottomMapFilter() {

        // Show the BottomSheetFragment
        val bottomSheet = FilterMapBottomSheetFragment()
        val bundle = Bundle().apply {
            putString("clientId",clientId) // Replace with your data
        }
        bottomSheet.arguments = bundle
        bottomSheet.show(parentFragmentManager, "FilterMapBottomSheetFragment")

        /*val bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.TransparentBottomSheetDialog)
        val bottomSheetViewBinding = DataBindingUtil.inflate<MapFilterBinding>(
            layoutInflater, R.layout.map_filter, null, false)
        bottomSheetDialog.setContentView(bottomSheetViewBinding.root)*/
        //bottomSheetDialog.show()
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
    /* private fun zoomOut() {
         mapboxMap.getStyle { style ->
             val currentCameraPosition = mapboxMap.cameraState
             val newZoomLevel = currentCameraPosition.zoom - 1.0 // Adjust zoom level as needed
             cameraAnimationsPlugin?.easeTo(
                 CameraOptions.Builder()
                     .center(currentCameraPosition.center)
                     .zoom(newZoomLevel)
                     .build(),
                 MapAnimationOptions.Builder()
                     .duration(1000) // Duration in milliseconds
                     .build()
             )
         }
     }*/


    //trail data
    val trails: List<Trail> = listOf() // Replace with actual data

    private fun fetchMapTrailsData() {

        // Observe the SharedViewModel for data updates
        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner, Observer { response ->
//            val trails = response?.data?.trails?.filter { trail ->
//                trail.visibility?.toInt() == 1 //Assuming visibility is already an integer
//            }
//            val zones = response?.data?.zones ?: emptyList()
//            val pois = response?.data?.pois ?: emptyList()
//            val areas = response?.data?.areas ?: emptyList()


            clientTrails = (response?.data?.trails ?: emptyList()).toMutableList()
            clientZones = (response?.data?.zones ?: emptyList()).toMutableList()
            clientPois = (response?.data?.pois ?: emptyList()).toMutableList()
            clientResources = (response?.data?.resources ?: emptyList()).toMutableList()

            clientTrails = clientTrails.filter { it.visibility == 1 }.toMutableList()

            Log.d("Tag_Trails", "trails size: ${clientTrails?.size}")
            Log.d("Tag_Zones", "zones size: ${clientZones.size}")
            Log.d("Tag_POIs", "pois size: ${clientPois.size}")
            Log.d("Tag_Resources", "resources size: ${clientResources.size}")

            if (trails != null) {
                addTrailsToMap(clientTrails)
            }
//            if (clientZones.isNotEmpty()) {
//                addZonesToMap(clientZones)
//            }
//            if (clientPois.isNotEmpty()) {
//                addPoisToMap(clientPois)
//            }
//            if (areas.isNotEmpty()) {
//                addAreasToMap(areas)
//            }
        })
    }

    /////////////////////////

    private fun addTrailsToMap(trails: List<Trail>) {
        mapboxMap.getStyle { style ->
            trails.forEach { trail ->
                val coordinates = trail.polyLine?.features?.mapNotNull { feature ->
                    feature.geometry?.coordinates
                }

                if (!coordinates.isNullOrEmpty() && !coordinates[0].isNullOrEmpty()) {
                    hasCoordinates = true

                    // Include bounds
                    coordinates[0]?.forEach { coord ->
                        boundsBuilder.include(LatLng(coord[1], coord[0]))
                    }

                    // Create LineString and Feature
                    val lineString = LineString.fromLngLats(
                        coordinates[0].map { coord ->
                            Point.fromLngLat(coord[0], coord[1])
                        }
                    )
                    val feature = Feature.fromGeometry(lineString)
                    val featureCollection = FeatureCollection.fromFeatures(listOf(feature))

                    // Extract properties
                    val properties = trail.polyLine?.features?.firstOrNull()?.properties
                    val color = properties?.color ?: "#FF0000" // Default to red

                    // Define source and layer IDs
                    val sourceId = "line-source-${trail.id}"
                    val layerId = "line-layer-${trail.id}"

                    // Add GeoJSON source
                    if (style.getSource(sourceId) == null) {
                        style.addSource(geoJsonSource(sourceId) {
                            featureCollection(featureCollection)
                        })
                    }

                    // Add line layer
                    if (style.getLayer(layerId) == null) {
                        style.addLayer(lineLayer(layerId, sourceId) {
                            lineColor(color)
                            lineWidth(6.0)
                        })
                    }
                }
            }

            // Adjust camera bounds if coordinates exist
            if (hasCoordinates) {
                val latLngBounds = boundsBuilder.build()
                //val centerPoint = latLngBounds.center
                val centerPoint = Point.fromLngLat(latLngBounds.center.longitude, latLngBounds.center.latitude)
                mapView.camera.easeTo(
                    CameraOptions.Builder()
                        .center(centerPoint)
                        .zoom(8.0)
                        .build(),
                    MapAnimationOptions.Builder().duration(2000).build()
                )
            } else {
                Toast.makeText(requireContext(), "No coordinates available to adjust camera bounds.", Toast.LENGTH_SHORT).show()
                Log.e("MapError", "No coordinates available to adjust camera bounds.")
            }
        }
    }

    ///////////////////////
    /*private fun addTrailsToMap(trails: List<Trail>) {
        mapboxMap.getStyle { style ->

            trails.forEach { trail ->
                val polyline = trail.polyLine
                val coordinates = polyline.features.flatMap { feature ->
                    feature.geometry.coordinates
                }

                Log.d("coordinates", "coordinates size: ${coordinates}")
                if (coordinates.isNotEmpty()) {
                    hasCoordinates = true
                    coordinates.forEach { coord ->
                        Log.d("coordinates", "coordinates size: ${LatLng(coord[1], coord[0])}")
                        boundsBuilder.include(LatLng(coord[1], coord[0]))
                    }


                    // Create LineString from coordinates
                    val lineString = LineString.fromLngLats(
                        coordinates.map { coord ->
                            Point.fromLngLat(coord[0], coord[1])
                        }

                    )
                    Log.d("lineString", "lineString size: ${lineString}")
                    // Create Feature from LineString
                    val feature = Feature.fromGeometry(lineString)
                    val featureCollection = FeatureCollection.fromFeatures(listOf(feature))
                    //Extract properties from the first feature
                    val properties = trail.polyLine.features[0].properties

                    *//*  // Use GeoJsonSource.Builder to create the source
                      val lineSource = geoJsonSource("line-source-${trail.id}") {
                          featureCollection(featureCollection)
                      }*//*
                    // Define source and layer IDs
                    val sourceId = "line-source-${trail.id}"
                    val layerId = "line-layer-${trail.id}"

                    // Add source if it doesn't exist
                    if (style.getSource(sourceId) == null) {
                        style.addSource(geoJsonSource(sourceId) {
                            featureCollection(featureCollection)
                        })
                    }

                    // Add layer if it doesn't exist
                    if (style.getLayer(layerId) == null) {
                        style.addLayer(lineLayer(layerId, sourceId) {
                            lineColor(properties.color) // Customize color
                            lineWidth(6.0)
                        })
                    }
                    // Update the bounds with each trail's coordinates
                    coordinates.forEach { coord ->
                        boundsBuilder.include(LatLng(coord[1], coord[0])) // Note: LatLng requires (latitude, longitude)
                    }
                }
            }
            if (hasCoordinates) {
                // Use the plugin to animate the camera to fit all trails
                val latLngBounds = boundsBuilder.build()
                // Compute the center manually
                minLng = minOf(minLng, defaultLong.toDouble())
                minLat = minOf(minLat, defaultLat.toDouble())
                maxLng = maxOf(maxLng, defaultLong.toDouble())
                maxLat = maxOf(maxLat, defaultLat.toDouble())
                //val southwest = latLngBounds.southwest
                //val northeast = latLngBounds.northeast
                val centerLng = (minLng + maxLng) / 2
                val centerLat = (minLat + maxLat) / 2
                val centerPoint = Point.fromLngLat(centerLng, centerLat)
                val padding = EdgeInsets(50.0, 50.0, 50.0, 50.0) // Adjust as needed

                //val centerLatLng = LatLng(centerLat, centerLng)
                //  val centerPoint = Point.fromLngLat(centerLatLng.longitude, centerLatLng.latitude)
                val cameraAnimationsPlugin = mapView.camera
                cameraAnimationsPlugin?.easeTo(CameraOptions.Builder()
                    .center(centerPoint)
                    //.padding(padding)
                    .zoom(8.00)
                    .build(),
                    MapAnimationOptions.Builder()
                        .duration(2000) // Duration in milliseconds (e.g., 3 seconds)
                        .build())

            } else {
                Toast.makeText(requireContext(), "No coordinates available to adjust camera bounds.", Toast.LENGTH_SHORT).show()
                Log.e("MapError", "No coordinates available to adjust camera bounds.")
            }

            //cameraAnimationsPlugin!!.easeTo(cameraOptions, animationOptions) // Smooth animation to the bounds
        }
    }*/

    /*fun calculateZoomLevel(minLng: Double, maxLng: Double, minLat: Double, maxLat: Double, mapView: MapView): Double {
        // You can implement custom logic here, or use a basic approximation
        val lngDiff = maxLng - minLng
        val latDiff = maxLat - minLat
        val mapSize = max(lngDiff, latDiff)

        // Basic calculation for zoom level (you may need to adjust this scaling factor)
        return 14.0 - (mapSize * 5)
    }*/

    private fun addAreasToMap(areas: List<Area>) {
        mapboxMap.getStyle { style ->
            areas.forEach { area ->
                // Ensure you have a proper list of coordinates
                val coordinates = area.name // This should be a List<LatLng> or similar

                // Convert coordinates to a list of Point objects
                val points = coordinates?.map { coord ->
                    //Point.fromLngLat(coord.longitude, coord.latitude)
                }

                // Create a Polygon with a single ring (if it has holes, add them as additional rings)
               // val polygon = Polygon.fromLngLats(listOf(points))

               // val feature = Feature.fromGeometry(polygon)
                //val featureCollection = FeatureCollection.fromFeatures(listOf(feature))

                val sourceId = "area-source-${area.id}"
                val layerId = "area-layer-${area.id}"

                if (style.getSource(sourceId) == null) {
                    style.addSource(
                        geoJsonSource(sourceId) {
                           // featureCollection(featureCollection)
                        }
                    )
                }

                if (style.getLayer(layerId) == null) {
                    style.addLayer(lineLayer(layerId, sourceId) {
                        // Customize color
                        lineWidth(2.0)
                    })
                }
            }
        }
    }

    private fun addPoisToMap(pois: List<Poi>) {
        mapboxMap.getStyle { style ->
            // Ensure the icon image is added to the style
            if (style.getSource("poi-icon") == null) {
                val iconBitmap = BitmapFactory.decodeResource(resources, R.drawable.map_location_icon) // Replace with your drawable resource
                style.addImage("poi-icon", iconBitmap)
            }

            // Initialize PointAnnotationManager if not already initialized
            initializePointAnnotationManager(style)

            // Check if pointAnnotationManager is initialized
            pointAnnotationManager?.let { manager ->
                // Add annotations for each POI
                pois.forEach { poi ->
                    val point = Point.fromLngLat(poi.longitude!!, poi.latitude!!)


                    val options = PointAnnotationOptions()
                        .withPoint(point)
                        .withIconImage("poi-icon") // Use the icon image ID
                        .withIconSize(0.05) // Adjust icon size if needed

                   // manager.create(options)
                    ///////////////////////
                    val annotation = manager.create(options)
                    // Add click listener for the annotation
                    manager.addClickListener { clickedAnnotation ->
                        if (clickedAnnotation == annotation) {
                            Log.e("TAG_Pois", "Clicked on POI: ${poi.poiTypeId}")

                            showCustomPoisDialog(poi)
                            Toast.makeText(context, "Clicked POI: ${poi.name}", Toast.LENGTH_SHORT).show()
                            true
                        } else {
                            false
                        }
                    }

                }
            } ?: run {
                Log.e("MapError", "PointAnnotationManager is not initialized.")
            }
        }
    }

    private fun showCustomPoisDialog(poi: Poi) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.RoundedBottomSheetDialog)
        val bottomSheetViewBinding = DataBindingUtil.inflate<MapFilterDetailsBinding>(layoutInflater,
            R.layout.map_filter_details, null, false)

        bottomSheetViewBinding?.close?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        val lastPoiTypeStatusName = poi.poiStatusHistories
            ?.reversed()                      // Reverse the list to start from the last element
            ?.firstOrNull { it.poiTypeStatus != null } // Get the first non-null PoiTypeStatus
            ?.poiTypeStatus?.name             // Get the 'name' from the PoiTypeStatus


        val statusDates = poi.poiStatusHistories?.map { it.statusDate }

        val statusDatesText = statusDates?.joinToString(separator = "\n") { it.toString() }

        bottomSheetViewBinding.txtPoisStatusHostory.text = statusDatesText


        bottomSheetViewBinding?.idTxtStatusOpen?.text =lastPoiTypeStatusName
        bottomSheetViewBinding?.idTxtDateTime?.text = poi.lastUpdateDate
        bottomSheetViewBinding?.idTxtDescription?.text = poi.description
        //bottomSheetViewBinding?.txtPoisStatusHostory?.text =
        val poiTypeIcon = viewModelPoisType.getIconPathByPoiTypeId(poi.poiTypeId!!)

        val poisImages = poi.images?.map { it.path }
        // Get reference to the container (LinearLayout)
        val imagesContainer = bottomSheetViewBinding.imagesContainer

// Remove any existing views (to avoid duplicates if reloading)
        imagesContainer.removeAllViews()
        Log.d("sssss" ," sssss" +poisImages)
// Iterate through the images and add them dynamically to the layout
        poisImages?.forEach { imagePath ->
            val imageView = ImageView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._300sdp), // Adjust size as needed
                    resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._150sdp)
                ).apply {
                    setMargins(8, 0, 8, 0) // Adjust margins as needed
                }
                Glide.with(this)
                    .load(ServiceUtil.BASE_URL_IMAGE + imagePath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.clubs) // Default image if loading fails
                    .into(this)
            }

            // Add the ImageView to the container
            imagesContainer.addView(imageView)
        }
        /*      Log.d("poiImages" ," sssss" +poisImages)
              Glide.with(bottomSheetViewBinding.backgroundImage)
                  .load(poisImages?.get(0)).diskCacheStrategy(
                      DiskCacheStrategy.ALL
                  ).fitCenter().error(R.drawable.clubs)
                  .into(bottomSheetViewBinding.backgroundImage)*/
        Log.d("poiTypeIcon" ," sssss" +poiTypeIcon)

        Glide.with(bottomSheetViewBinding.poisTypeIcon)
            .load(poiTypeIcon).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).fitCenter().error(R.drawable.dinner)
            .into(bottomSheetViewBinding.poisTypeIcon)

        // Make sure the bottom sheet content fits properly on the screen
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet = (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)

            // Adjust peekHeight to control the collapsed view's height
            behavior.peekHeight = resources.getDimensionPixelSize(R.dimen._120sdp) // Example: 200dp

            // Set state to fully expanded, or collapsed to just show a portion of the sheet
            behavior.state = BottomSheetBehavior.STATE_EXPANDED  // Or STATE_HALF_EXPANDED based on your preference
        }

        bottomSheetDialog.setContentView(bottomSheetViewBinding.root)
        bottomSheetDialog.show()
    }


    private fun initializePointAnnotationManager(style: Style) {
        if (pointAnnotationManager == null) {
            pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
        }
    }

    private fun addZonesToMap(zones: List<Zone>) {
        mapboxMap.getStyle { style ->
            zones.forEach { zone ->
                val polygon = Polygon.fromLngLats(
                    listOf(zone.definitions?.map { definition ->
                            Point.fromLngLat(definition.longitude, definition.latitude)
                        }
                    )
                )

                val feature = Feature.fromGeometry(polygon)
                val featureCollection = FeatureCollection.fromFeatures(listOf(feature))

                val sourceId = "polygon-source-${zone.id}"
                val layerId = "polygon-layer-${zone.id}"

                if (style.getSource(sourceId) == null) {
                    style.addSource(geoJsonSource(sourceId) {
                        featureCollection(featureCollection)
                    })
                }

                if (style.getLayer(layerId) == null) {
                    style.addLayer(fillLayer(layerId, sourceId) {
                      //  lineColor(zone.color) // Customize color
                       // lineWidth(2.0)
                        fillColor(zone.color!!)
                        fillOpacity(0.7)
                    }
                    )
                }
            }
        }
    }






    private fun showCustomDialog() {
        // Inflate the custom layout
        val dialogView = layoutInflater.inflate(R.layout.approve_remove_dialog, null)
        // Reference the button from the custom layout
        val btnImgClose: ImageView = dialogView.findViewById(R.id.iv_close)
        val btnLeaveFeedback: Button = dialogView.findViewById(R.id.btn_approve)
        val btnCancel: Button = dialogView.findViewById(R.id.btn_remove)
        // Create the AlertDialog and set the custom layout
        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        // Set the dialog attributes
        dialog.show()
        // Add a click listener for the 'Leave Feedback' button
        btnImgClose.setOnClickListener {
            // Perform action for leaving feedback
            Toast.makeText(requireContext(), "btnImgClose", Toast.LENGTH_SHORT).show()
            dialog.dismiss()  // Close the dialog after feedback submission
        }

        // Add a click listener for the 'Leave Feedback' button
        btnLeaveFeedback.setOnClickListener {
            // Perform action for leaving feedback
            Toast.makeText(requireContext(), "Feedback Submitted", Toast.LENGTH_SHORT).show()
            dialog.dismiss()  // Close the dialog after feedback submission
        }

// Add a click listener for the 'Cancel' button
        btnCancel.setOnClickListener {
            // Perform action for canceling
            Toast.makeText(requireContext(), "Action Cancelled", Toast.LENGTH_SHORT).show()
            dialog.dismiss()  // Close the dialog on cancel
        }
    }

    private fun showCustomDialog2() {
//        val customDialog = CustomDialogFragmentFragment()
//        customDialog.show(parentFragmentManager, "CustomDialogTag")
        val bottomSheetDialog = BottomSheetDialog(requireContext(),R.style.TransparentBottomSheetDialog)
        val bottomSheetViewBinding = DataBindingUtil.inflate<BottomSheetApartmentsBinding>(
            layoutInflater, R.layout.bottom_sheet_apartments, null, false)
        bottomSheetViewBinding?.btnLeaveFeedback?.setOnClickListener {
            showCustomDialog()
            bottomSheetDialog.dismiss()
        }
        bottomSheetViewBinding?.btnCancel?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetViewBinding?.close?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetViewBinding.root)
        bottomSheetDialog.show()
    }

    private fun animateFab() {
        if (isOpen) {
            binding.fab.startAnimation(rotateForward)
            binding.fab1.startAnimation(fabClose)
            binding.fab2.startAnimation(fabClose)
            binding.fab3.startAnimation(fabClose)
            binding.fab1.isClickable = false
            binding.fab2.isClickable = false
            binding.fab3.isClickable = false
            isOpen = false
        } else {
            binding.fab.startAnimation(rotateBackward)
            binding.fab1.startAnimation(fabOpen)
            binding.fab2.startAnimation(fabOpen)
            binding.fab3.startAnimation(fabOpen)
            binding.fab1.isClickable = true
            binding.fab2.isClickable = true
            binding.fab3.isClickable = true
            isOpen = true
        }
    }





}