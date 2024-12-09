package com.snofed.publicapp.maps

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
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
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.addLayerAbove
import com.mapbox.maps.extension.style.layers.generated.SymbolLayer
import com.mapbox.maps.extension.style.layers.generated.fillLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
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
import com.snofed.publicapp.models.interval.StatusItem
import com.snofed.publicapp.models.realmModels.Area
import com.snofed.publicapp.models.realmModels.Poi
import com.snofed.publicapp.models.realmModels.Resource
import com.snofed.publicapp.models.realmModels.Trail
import com.snofed.publicapp.models.realmModels.Zone
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.IntervalViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.PoisTypeViewModelRealm
import com.snofed.publicapp.utils.DateTimeConverter
import com.snofed.publicapp.utils.ServiceUtil
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.SnofedConstants
import com.snofed.publicapp.utils.enums.SyncActionEnum
import com.snofed.publicapp.utils.enums.VisibilityEnum
import dagger.hilt.android.AndroidEntryPoint
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale
import java.util.concurrent.Executors

@AndroidEntryPoint
class MapExploreFragment : Fragment() {
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
    var defaultLat: String = "%.6f".format(Locale.US,SnofedConstants.CENTER_LAT)
    var defaultLong: String = "%.6f".format(Locale.US,SnofedConstants.CENTER_LONG)
    var clientId: String? = ""
    private var lastClickTime = 0L
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

    var allClientMapInterval: MutableList<StatusItem> = mutableListOf()

    // Client MAP Data
    var clientTrails: MutableList<Trail> = mutableListOf()
    var clientPois: MutableList<Poi> = mutableListOf()
    var clientZones: MutableList<Zone> = mutableListOf()
    var clientResources: MutableList<Resource> = mutableListOf()
    var clientAreas: MutableList<Area> = mutableListOf()

    var selectedAreaId: String = "0"

    var selectedTrailsIds: Set<String> = emptySet()
    var selectedPoiIds: Set<String> = emptySet()
    var selectedZoneIds: Set<String> = emptySet()
    var selectedResourceIds: Set<String> = emptySet()

    var filteredTrails : List<Trail> = emptyList()
    var filteredZones : List<Zone> = emptyList()
    var filteredPois : List<Poi> = emptyList()
    private val dateTimeConverter = DateTimeConverter()

    var isSatelliteViewClicked : Boolean = false
    var lastTrailLayerId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_map_explore, container, false)
        _binding = FragmentMapExploreBinding.inflate(inflater, container, false)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
            clearSelectedIds()
        }

        sharedViewModell = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModelPoisType = ViewModelProvider(this).get(PoisTypeViewModelRealm::class.java)
        //Area
        sharedViewModell.selectedAreaId.observe(viewLifecycleOwner, Observer { selectedId ->
            // Update the UI with the selected IDs
            Log.d("selectedId", "SelectedArea IDs: ${selectedId.id}")
            // Use the selectedIds to update the UI as needed
            selectedAreaId = selectedId.id

            // filter trails and draw again
            removeTrailsOnMap(clientTrails)
            if (selectedAreaId != "0") {
                if(selectedTrailsIds.contains("0"))
                    filteredTrails = clientTrails.filter { trail -> trail.areaId == selectedAreaId }
                else
                    filteredTrails = clientTrails.filter { trail -> trail.areaId == selectedAreaId && selectedTrailsIds.contains(trail.activity?.id)}
            }
            else {
                if(selectedTrailsIds.contains("0"))
                    filteredTrails = clientTrails
                else
                    filteredTrails = clientTrails.filter { trail -> selectedTrailsIds.contains(trail.activity?.id)}
            }
            if(selectedTrailsIds.isNotEmpty())
                addTrailsToMap(filteredTrails)


            // filter pois and draw again
            removePoisOnMap(filteredPois)
            // filter pois again and draw again
            if (selectedAreaId != "0") {
                if(selectedPoiIds.contains("0"))
                    filteredPois = clientPois.filter { poi -> poi.areaId == selectedAreaId }
                else
                    filteredPois = clientPois.filter { poi -> poi.areaId == selectedAreaId && selectedPoiIds.contains(poi.poiTypeId)}
            }
            else {
                if(selectedPoiIds.contains("0"))
                    filteredPois = clientPois
                else
                    filteredPois = clientPois.filter { poi -> selectedPoiIds.contains(poi.poiTypeId)}
            }

            addPoisToMap(filteredPois)


            // filter Zone again and draw again
            removeZonesOnMap(clientZones)
            if (selectedAreaId != "0") {
                if(selectedZoneIds.contains("0"))
                    filteredZones = clientZones.filter { zone -> zone.areaId == selectedAreaId }
                else
                    filteredZones = clientZones.filter { zone -> zone.areaId == selectedAreaId && selectedZoneIds.contains(zone.zoneTypeId)}
            }
            else {
                if(selectedZoneIds.contains("0"))
                    filteredZones = clientZones
                else
                    filteredZones = clientZones.filter { zone -> selectedZoneIds.contains(zone.zoneTypeId)}
            }

            addZonesToMap(filteredZones)
        })

        //TRAILS
        sharedViewModell.selectedTrailId.observe(viewLifecycleOwner, Observer { selectedIds ->
            // Update the UI with the selected IDs
            Log.d("selectedId", "Selected Activity IDs: $selectedIds")
            // Use the selectedIds to update the UI as needed
            selectedTrailsIds = selectedIds.toSet()

            if(selectedTrailsIds.isNotEmpty())
                filteredTrails = clientTrails.filter { trail -> selectedTrailsIds.contains(trail.activity?.id) }

            if(selectedTrailsIds.contains("0"))
                filteredTrails =  clientTrails
            else if(selectedTrailsIds.isNotEmpty() && !selectedTrailsIds.contains("0"))
                filteredTrails =  clientTrails.filter { trail -> selectedTrailsIds.contains(trail.activity?.id) }

            //filter with areaId
            if (selectedAreaId != "0") {
                filteredTrails = filteredTrails.filter { trail -> trail.areaId == selectedAreaId }
            }

            removeTrailsOnMap(clientTrails)
            if(selectedTrailsIds.isNotEmpty())
                addTrailsToMap(filteredTrails)

            // for overlap issue rerender pois also
            removePoisOnMap(filteredPois)
            // Add POIs
            addPoisToMap(filteredPois)
        })

        //  POI
        sharedViewModell.selectedPoisIds.observe(viewLifecycleOwner, Observer { selectedIds ->
            // Update the UI with the selected IDs
            Log.d("selectedId", "Selected POIs IDs: $selectedIds")
            // Use the selectedIds to update the UI as needed
            selectedPoiIds = selectedIds.toSet()

            filteredPois = clientPois.filter { poi -> selectedPoiIds.contains(poi.poiTypeId) }

            if(selectedPoiIds.contains("0"))
                filteredPois =  clientPois
            else if(selectedPoiIds.isNotEmpty() && !selectedPoiIds.contains("0"))
                filteredPois =  clientPois.filter { poi -> selectedPoiIds.contains(poi.poiTypeId) }

            //filter with areaId
            if (selectedAreaId != "0") {
                filteredPois = filteredPois.filter { poi -> poi.areaId == selectedAreaId }
            }
            removePoisOnMap(clientPois)
            addPoisToMap(filteredPois)
        })

        //Zone
        sharedViewModell.selectedZoneTypeId.observe(viewLifecycleOwner, Observer { selectedIds ->
            // Update the UI with the selected IDs
            Log.d("selectedId", "Selected Zone IDs: $selectedIds")
            // Use the selectedIds to update the UI as needed
            selectedZoneIds = selectedIds.toSet()

            filteredZones = clientZones.filter { zone -> selectedZoneIds.contains(zone.zoneTypeId) }

            if(selectedZoneIds.contains("0"))
                filteredZones =  clientZones
            else if(selectedZoneIds.isNotEmpty() && !selectedZoneIds.contains("0"))
                filteredZones =  clientZones.filter { zone -> selectedZoneIds.contains(zone.zoneTypeId) }

            //filter with areaId
            if (selectedAreaId != "0") {
                filteredZones = filteredZones.filter { zone -> zone.areaId == selectedAreaId }
            }

            removeZonesOnMap(clientZones)

            addZonesToMap(filteredZones)
        })

        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        setupLocationRequest()
        clientId = arguments?.getString("clientId").toString()
        return binding.root
    }


    private fun clearSelectedIds() {
        sharedViewModel.updateSelectedIds(emptyList()) // Clear POIs
        sharedViewModel.updateSelectedTrailsIds(emptyList()) // Clear Trails
        //sharedViewModel.updateSelectedTrailsIds(listOf("0")) // Set Default
        sharedViewModel.updateSelectedZoneIds(emptyList()) // Clear Zones
        sharedViewModel.updateSelectedAreaIds("0","")
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
        allClientMapInterval.add(
            StatusItem(
                id = "0",
                text = resources.getString(R.string.t_close),
                color = "#eb4034"
            )
        )

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

       if(!data.isNullOrEmpty()) {
           defaultLat = "%.6f".format(
               Locale.US,
               data["Latitude"]?.toString()?.toDoubleOrNull() ?: SnofedConstants.CENTER_LAT
           )
           defaultLong = "%.6f".format(
               Locale.US,
               data["Longitude"]?.toString()?.toDoubleOrNull() ?: SnofedConstants.CENTER_LONG
           )
       }

        // Initialize MapView and MapboxMap
        mapView = binding.mapView
        mapboxMap = mapView.mapboxMap

        mapboxMap.loadStyle(Style.OUTDOORS) {
            // Callback when the style has been fully loaded
            Log.d("Mapbox", "Style OUTDOORS loaded successfully.")

            // Initialize Camera options
            mapboxMap.setCamera(
                CameraOptions.Builder()
                    .center(
                        Point.fromLngLat(
                            defaultLong.toDouble(),
                            defaultLat.toDouble()
                        )
                    ) // Set desired center
                    .zoom(6.0) // Set desired zoom level
                    .pitch(9.0) // Optional: set pitch for a tilted view
                    .build()
            )

            // Retrieve the CameraAnimationsPlugin if needed
            cameraAnimationsPlugin =
                mapView.getPlugin(CameraAnimationsPlugin::class.java.toString())
        }

        fetchMapTrailsData()

        fabOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
        fabClose = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        rotateForward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_backward)

        binding.mapByFilter.setOnClickListener {
            // Check the time of the last click and prevent rapid clicks
            if (System.currentTimeMillis() - lastClickTime > 500) { // 500ms debounce time
                lastClickTime = System.currentTimeMillis()
                showBottomMapFilter()
            }
        }
        binding.fab.setOnClickListener {
            animateFab()
        }

        binding.currentLocation.setOnClickListener {
            animateFab()
            checkPermissionsAndGps()

            //Toast.makeText(requireContext(), "camera click", Toast.LENGTH_SHORT).show()
        }

        binding.satelliteToggle.setOnClickListener {
            animateFab()
            isSatelliteViewClicked = !isSatelliteViewClicked
            SatelliteView()
            //Toast.makeText(requireContext(), "folder click", Toast.LENGTH_SHORT).show()
        }
        binding.feedbackBtn.setOnClickListener {
            animateFab()
            //val bundle = Bundle()
            //val destination = R.id.feedBackDefaultCategoryListFragment
            //val destination = R.id.feedBackFragment
            //findNavController().navigate(destination, bundle)
            showCustomDialog2()

        }
    }

    private fun showBottomMapFilter() {
       /* // Avoid disabling the button unintentionally
        binding.mapByFilter.isEnabled = true*/
        // Show the BottomSheetFragment
        val bottomSheet = FilterMapBottomSheetFragment()
        val bundle = Bundle().apply {
            putString("clientId", clientId) // Replace with your data
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
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

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
                Toast.makeText(
                    requireContext(),
                    "Location permission is required",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun enableGPS() {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    // Register the ActivityResultLauncher for GPS settings
    private val gpsSettingsLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
    //val trails: List<Trail> = listOf() // Replace with actual data

    private fun fetchMapTrailsData() {

        // Observe the SharedViewModel for data updates
        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner, Observer { response ->

            clientTrails = (response?.data?.trails ?: emptyList()).toMutableList()
            clientZones = (response?.data?.zones ?: emptyList()).toMutableList()
            clientPois = (response?.data?.pois ?: emptyList()).toMutableList()
            clientResources = (response?.data?.resources ?: emptyList()).toMutableList()

            clientTrails = clientTrails.filter { it.visibility == VisibilityEnum.PUBLIC.getValue() }.toMutableList()
            clientPois = clientPois.filter { it.poiVisibility == VisibilityEnum.PUBLIC.getValue() }.toMutableList()
            // Check zone for always show on map option
            //clientZones = clientZones.filter { it.visibility == VisibilityEnum.PUBLIC.getValue() }.toMutableList()
            clientResources = clientResources.filter { it.isActive == true && it.isPublic == true }.toMutableList()

            Log.d("Tag_Trails", "trails size: ${clientTrails?.size}")
            Log.d("Tag_Zones", "zones size: ${clientZones.size}")
            Log.d("Tag_POIs", "pois size: ${clientPois.size}")
            Log.d("Tag_Resources", "resources size: ${clientResources.size}")

            if (clientTrails.size > 0) {
                addTrailsToMap(clientTrails)
                sharedViewModel.updateSelectedTrailsIds(listOf("0"))
                //filteredTrails = clientTrails
            }
        })
    }

    /////////////////////////

    private fun addTrailsToMap(trails: List<Trail>) {
        mapboxMap.getStyle { style ->
            val lastLayerId = style.styleLayers.lastOrNull()?.id
            val allCoordinates = mutableListOf<Point>()

            trails.forEach { trail ->
                val coordinates = trail.polyLine?.features?.mapNotNull { feature ->
                    feature.geometry?.coordinates
                }

                if (!coordinates.isNullOrEmpty() && !coordinates[0].isNullOrEmpty()) {
                    hasCoordinates = true

                    // Include bounds
                    coordinates[0]?.forEach { coord ->
                        boundsBuilder.include(LatLng(coord[1], coord[0]))
                        allCoordinates.add(Point.fromLngLat(coord[0], coord[1]))
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
                        style.addLayerAbove(lineLayer(layerId, sourceId) {
                            lineColor(color)
                            lineWidth(6.0)
                        },lastLayerId)

                    }

                    // Add starting point POI
                    val startingPoint = coordinates[0]?.firstOrNull()
                    if (startingPoint != null) {
                        val poiSourceId = "start-poi-source-${trail.id}"
                        val poiLayerId = "start-poi-layer-${trail.id}"

                        val poiFeature = Feature.fromGeometry(
                            Point.fromLngLat(startingPoint[0], startingPoint[1])
                        )
                        val poiFeatureCollection =
                            FeatureCollection.fromFeatures(listOf(poiFeature))

                        if (style.getSource(poiSourceId) == null) {
                            style.addSource(geoJsonSource(poiSourceId) {
                                featureCollection(poiFeatureCollection)
                            })
                        }
                        val poiIconBitmap = BitmapFactory.decodeResource(resources, R.drawable.start_pin)
                        style.addImage("start-poi-icon", poiIconBitmap)

                        if (style.getLayer(poiLayerId) == null) {
                            style.addLayerAbove(symbolLayer(poiLayerId, poiSourceId) {
                                iconImage("start-poi-icon") // Use an icon from your sprite or custom one
                                iconSize(1.0)
                            }, layerId)
                        }
                    }
                    //Update the last added trail layer ID
                    lastTrailLayerId = layerId
                }
            }

            // Adjust camera bounds if coordinates exist
            if (allCoordinates.isNotEmpty()) {
                val cameraOptions = mapboxMap.cameraForCoordinates(
                    allCoordinates,
                    EdgeInsets(100.0, 100.0, 100.0, 100.0) // Padding around the edges
                )
                mapView.camera.easeTo(
                    cameraOptions,
                    MapAnimationOptions.Builder().duration(2000).build()
                )
                
               /* val latLngBounds = boundsBuilder.build()
                //val centerPoint = latLngBounds.center
                val centerPoint =
                    Point.fromLngLat(latLngBounds.center.longitude, latLngBounds.center.latitude)
                mapView.camera.easeTo(
                    CameraOptions.Builder()
                        .center(centerPoint)
                        .zoom(8.0)
                        .build(),
                    MapAnimationOptions.Builder().duration(2000).build()
                )*/
            } else {
                /*Toast.makeText(
                    requireContext(),
                    "No trails available for these selections.",
                    Toast.LENGTH_SHORT
                ).show()*/
                Log.e("MapError", "No coordinates available to adjust camera bounds.")
            }
        }
    }

    ///////////////////////

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

            // Retrieve the last layer ID dynamically
            val lastLayerId = style.styleLayers.lastOrNull()?.id

            val markerCoordinates: List<Feature> = ArrayList()
            pointAnnotationManager?.let { manager ->
                // Clear existing annotations
                manager.deleteAll()

                pois.forEach { poi ->
                    val point = Point.fromLngLat(poi.longitude!!, poi.latitude!!)
                    val poisTypeImage = viewModelPoisType.getIconPathByPoiTypeId(poi.poiTypeId!!)
                    //val poisColor = poi.lastPoiStatus?.color ?: "#97D1FF"

                    val poiStatusOpenOrCloseColor = poi.poiStatusHistories
                        ?.sortedByDescending { it.statusDate } // Sort by status date in descending order
                        ?.firstOrNull { it.poiTypeStatus != null } // Get the first non-null PoiTypeStatus
                        ?.poiTypeStatus?.color

                    var poiStatusColor = when (poiStatusOpenOrCloseColor) {
                        "#00FF00" -> "#97D1FF" // If green, return a specific blue color
                        "#FF0000" -> "#FFA5C9" // If red, return a specific pink color
                        null -> "#CDD8E1"      // If null, default to grey
                        else -> poiStatusOpenOrCloseColor // Otherwise, keep the original color
                    }

                    // Fetch and create custom POI icons asynchronously
                    if (!poisTypeImage.isNullOrEmpty()) {
                        val executor = Executors.newSingleThreadExecutor()
                        val handler = Handler(Looper.getMainLooper())
                        executor.execute {
                            try {
                                val urlBitmap = fetchBitmapFromURL(poisTypeImage)
                                val markerBase = when (poiStatusOpenOrCloseColor) {
                                    "#00FF00" -> BitmapFactory.decodeResource(resources, R.drawable.location_blue)
                                    "#FF0000" -> BitmapFactory.decodeResource(resources, R.drawable.location_red)
                                    null -> BitmapFactory.decodeResource(resources, R.drawable.location_grey)
                                    else -> BitmapFactory.decodeResource(resources, R.drawable.location_blue)
                                }

                                val finalBitmap = createCustomPoiBitmap(markerBase, urlBitmap, poiStatusColor)
                                handler.post {
                                    style.addImage("poi-icon-${poi.id}", finalBitmap)
                                }
                            } catch (e: Exception) {
                                Log.e("POI Error", "Error fetching image: ${e.message}")
                            }
                        }
                    }

                    val feature = Feature.fromGeometry(fromLngLat(poi.longitude!!, poi.latitude!!))
                    feature.addStringProperty("MARKER_ICONS", poi.name)
                    feature.addStringProperty("POI_ID", poi.id.toString())

                    // Add POI layer
                    val poiLayerId = "poi-layer-${poi.id}"
                    val poiSourceId = "poi-source-${poi.id}"
                    if (style.getSource(poiSourceId) == null) {
                        style.addSource(geoJsonSource(poiSourceId) {
                            feature(feature)
                        })
                    }

                    if (style.getLayer(poiLayerId) == null) {
                        val poiLayer = SymbolLayer(poiLayerId, poiSourceId)
                            .iconImage("poi-icon-${poi.id}")
                            .iconAllowOverlap(true)
                            .iconAnchor(IconAnchor.BOTTOM)
                            .iconSize(0.101)

                        // Add POI layer above the last existing layer dynamically
                        if (lastLayerId != null) {
                            style.addLayerAbove(poiLayer, lastLayerId)
                        } else {
                            style.addLayer(poiLayer) // If no layers exist, add normally
                        }
                    }

                    // Add POI as a PointAnnotation
                    val options = PointAnnotationOptions()
                        .withPoint(point)
                        .withIconImage("poi-icon-${poi.id}")
                        .withIconSize(0.101)
                        .withIconAnchor(IconAnchor.BOTTOM)
                        .withDraggable(false)

                    val annotation = manager.create(options)
                    manager.addClickListener { clickedAnnotation ->
                        if (clickedAnnotation == annotation) {
                            showCustomPoisDialog(poi)
                            true
                        } else false
                    }
                }
            } ?: run {
                Log.e("MapError", "PointAnnotationManager is not initialized.")
            }

            initializePointAnnotationManager(style)
        }
    }

    private fun fetchAndAddPoiIcon(style: Style, iconUrl: String, color: String, poiId: String) {
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        executor.execute {
            try {
                val url = URL(iconUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                val urlBitmap = BitmapFactory.decodeStream(inputStream)

                // Customize icon (optional, based on `color`)
                val markerBitmap = customizePoiIcon(urlBitmap, color)

                handler.post {
                    style.addImage("poi-icon-$poiId", markerBitmap)
                    Log.d("TAG_POI", "Image added for POI: $poiId")
                }
            } catch (e: Exception) {
                Log.e("TAG_POI", "Error fetching image for POI $poiId: ${e.message}")
            }
        }
    }

    private fun customizePoiIcon(baseIcon: Bitmap, color: String): Bitmap {
        val resultBitmap = Bitmap.createBitmap(baseIcon.width, baseIcon.height, baseIcon.config)
        val canvas = Canvas(resultBitmap)
        val paint = Paint().apply {
            this.color = Color.parseColor(color)
            this.style = Paint.Style.FILL
        }
        canvas.drawCircle(resultBitmap.width / 2f, resultBitmap.height / 2f, resultBitmap.width / 2f, paint)
        canvas.drawBitmap(baseIcon, 0f, 0f, null)
        return resultBitmap
    }

    private fun addPoiLayer(style: Style, poi: Poi) {
        val poiLayerId = "poi-layer-${poi.id}"
        val sourceId = "poi-source-${poi.id}"

        // Add the layer if it doesn't exist
        if (style.getLayer(poiLayerId) == null) {
            val poiLayer = SymbolLayer(poiLayerId, sourceId)
                .iconImage("poi-icon-${poi.id}")
                .iconAllowOverlap(true)

            style.addLayerAbove(poiLayer, lastTrailLayerId)
        }
    }

    private fun fetchBitmapFromURL(urlString: String): Bitmap {
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.doInput = true
        connection.connect()
        return BitmapFactory.decodeStream(connection.inputStream)
    }

    private fun createCustomPoiBitmap(base: Bitmap, overlay: Bitmap, color: String): Bitmap {
        val finalBitmap = Bitmap.createBitmap(950, 1200, base.config)
        val canvas = Canvas(finalBitmap)
        val scaledBase = Bitmap.createScaledBitmap(base, 950, 1200, true)
        canvas.drawBitmap(scaledBase, 0f, 0f, null)

        val paint = Paint()
        paint.color = Color.parseColor(color)
        paint.style = Paint.Style.FILL
        canvas.drawCircle(470f, 400f, 350f, paint)

        val scaledOverlay = Bitmap.createScaledBitmap(overlay, 450, 450, true)
        canvas.drawBitmap(scaledOverlay, 470f - 225, 400f - 225, null)

        return finalBitmap
    }
  
    private fun showCustomPoisDialog(poi: Poi) {
        val bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.RoundedBottomSheetDialog)
        val bottomSheetViewBinding = DataBindingUtil.inflate<MapFilterDetailsBinding>(
            layoutInflater,
            R.layout.map_filter_details, null, false
        )

        bottomSheetViewBinding?.close?.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        val lastPoiTypeStatusName = poi.poiStatusHistories
            ?.sortedByDescending { it.statusDate } // Create a new list sorted by 'statusDate' in descending order
            ?.firstOrNull { it.poiTypeStatus != null } // Get the first non-null 'PoiTypeStatus'
            ?.poiTypeStatus?.name // Get the 'name' from 'PoiTypeStatus


        val statusDates = poi.poiStatusHistories
                            ?.sortedByDescending { it.statusDate }
                            ?.map { it.statusDate}

        val statusDatesText = statusDates
                            ?.map { dateTimeConverter.ConvertToDateTime(it!!) }
                            ?.joinToString(separator = "\n") { it.toString() }

        bottomSheetViewBinding.txtPoisStatusHostory.text = statusDatesText

        bottomSheetViewBinding?.idTxtPoisName?.text = poi.name
        bottomSheetViewBinding?.idTxtStatusOpen?.text = lastPoiTypeStatusName
        bottomSheetViewBinding?.idTxtDateTime?.text = dateTimeConverter.ConvertToDateTime((poi.lastUpdateDate!!))
        bottomSheetViewBinding?.idTxtDescription?.text = poi.description
        //bottomSheetViewBinding?.txtPoisStatusHostory?.text =
        val poiTypeIcon = viewModelPoisType.getIconPathByPoiTypeId(poi.poiTypeId!!)

        val poisImages = poi.images?.map { it.path }
        // Get reference to the container (LinearLayout)
        val imagesContainer = bottomSheetViewBinding.imagesContainer

// Remove any existing views (to avoid duplicates if reloading)
        imagesContainer.removeAllViews()
        Log.d("sssss", " sssss" + poisImages)
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
        Log.d("poiTypeIcon", " sssss" + poiTypeIcon)

        Glide.with(bottomSheetViewBinding.poisTypeIcon)
            .load(poiTypeIcon).diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).fitCenter().error(R.drawable.dinner)
            .into(bottomSheetViewBinding.poisTypeIcon)

        // Make sure the bottom sheet content fits properly on the screen
        bottomSheetDialog.setOnShowListener { dialog ->
            val bottomSheet =
                (dialog as BottomSheetDialog).findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)

            // Adjust peekHeight to control the collapsed view's height
            behavior.peekHeight = resources.getDimensionPixelSize(R.dimen._120sdp) // Example: 200dp

            // Set state to fully expanded, or collapsed to just show a portion of the sheet
            behavior.state =
                BottomSheetBehavior.STATE_EXPANDED  // Or STATE_HALF_EXPANDED based on your preference
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

    ////////////////////

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
        val bottomSheetDialog =
            BottomSheetDialog(requireContext(), R.style.TransparentBottomSheetDialog)
        val bottomSheetViewBinding = DataBindingUtil.inflate<BottomSheetApartmentsBinding>(
            layoutInflater, R.layout.bottom_sheet_apartments, null, false
        )
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
            binding.currentLocation.startAnimation(fabClose)
            binding.satelliteToggle.startAnimation(fabClose)
            binding.feedbackBtn.startAnimation(fabClose)
            binding.currentLocation.isClickable = false
            binding.satelliteToggle.isClickable = false
            binding.feedbackBtn.isClickable = false
            isOpen = false
        } else {
            binding.fab.startAnimation(rotateBackward)
            binding.currentLocation.startAnimation(fabOpen)
            binding.satelliteToggle.startAnimation(fabOpen)
            binding.feedbackBtn.startAnimation(fabOpen)
            binding.currentLocation.isClickable = true
            binding.satelliteToggle.isClickable = true
            binding.feedbackBtn.isClickable = true
            isOpen = true
        }
    }

    ////MAP

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

            // Remove Trails
            removeTrailsOnMap(clientTrails)
            // Add Trails

            if(selectedTrailsIds.isNotEmpty())
                addTrailsToMap(filteredTrails)

            // Remove Pois
            removePoisOnMap(filteredPois)
            // Add POIs
            addPoisToMap(filteredPois)

            // Remove Zones
            removeZonesOnMap(clientZones)
            // Add Zones
            addZonesToMap(filteredZones)
        }
    }

    private fun removeTrailsOnMap(clientTrails: MutableList<Trail>) {
        mapboxMap.getStyle { style ->
            clientTrails.forEach { trail ->
                // Remove Trails from MAP
                val sourceId = "line-source-${trail.id}"
                val layerId = "line-layer-${trail.id}"

                style.getLayer(layerId)?.let {
                    style.removeStyleLayer(it.layerId)
                }
                style.getSource(sourceId)?.let {
                    style.removeStyleSource(it.sourceId)
                }

                val poiLayerId = "start-poi-layer-${trail.id}"
                val poiSourceId = "start-poi-source-${trail.id}"

                style.getLayer(poiLayerId)?.let { style.removeStyleLayer(it.layerId) }
                style.getSource(poiSourceId)?.let { style.removeStyleSource(it.sourceId) }
            }
        }
    }

    private fun removePoisOnMap(clientPois: List<Poi>) {
        mapboxMap.getStyle { style ->
            clientPois.forEach { poi ->
                val poiLayerId = "poi-layer-${poi.id}"
                val poiSourceId = "poi-source-${poi.id}"

                style.getLayer(poiLayerId)?.let {
                    style.removeStyleLayer(it.layerId)
                }
                style.getSource(poiSourceId)?.let {
                    style.removeStyleSource(it.sourceId)
                }
            }
        }
    }

    private fun removeZonesOnMap(clientZones: MutableList<Zone>) {
        mapboxMap.getStyle { style ->
            clientZones.forEach { zone ->
                val sourceId = "polygon-source-${zone.id}"
                val layerId = "polygon-layer-${zone.id}"

                if (style.getSource(sourceId) != null) {
                    style.removeStyleSource(sourceId)
                }

                if (style.getLayer(layerId) != null) {
                    style.removeStyleLayer(layerId)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearSelectedIds()
    }

}