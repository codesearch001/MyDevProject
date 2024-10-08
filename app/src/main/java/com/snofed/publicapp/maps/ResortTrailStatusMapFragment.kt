package com.snofed.publicapp.maps

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
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
import com.mapbox.maps.extension.style.layers.generated.LineLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.animation.easeTo
import com.snofed.publicapp.databinding.FragmentResortTrailStatusMapBinding
import com.snofed.publicapp.models.DataPolyResponse
import com.snofed.publicapp.models.PolyLine
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.SharedViewModel
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
    // Define the coordinates where you want to center the map
    private val longitude = -73.935242
    private val latitude = 40.730610
    private val pageType: PageType? by lazy {
        arguments?.getParcelable<PageType>("pageType") // Retrieve pageType
    }

    val specificTrailId: String? by lazy {
        arguments?.getString("trailId") // Retrieve pageType
    }
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_resort_trail_status_map, container, false)
        _binding = FragmentResortTrailStatusMapBinding.inflate(inflater, container, false)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        Log.d("P0000", "Adding GeoJsonSource and LineLayer$trails")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val trails = tokenManager.getTrailsId().toString()
        Log.d("P1111", "Adding GeoJsonSource and LineLayer$trails")

        // Initialize MapView and MapboxMap
        mapView = binding.mapView
        mapboxMap = mapView.mapboxMap

        // Use the pageType here
        when (pageType) {
            PageType.DETAIL -> {
                // Handle DETAIL page type
                Log.d("DETAIL", "DETAIL")
                mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS) { style ->
                    // Observe the SharedViewModel for data updates
                    sharedViewModel.TrailsDetilsResponse.observe(viewLifecycleOwner, Observer { response ->
                        binding.trailsNameMap.text = response.data.name
                        if (response != null) {
                            val polylineData = response.data.polyLine
                            Log.d("P3333", "Print Details trails Id ${response.data.id}")
                            Log.d("P2222", "Adding GeoJsonSource and LineLayer${polylineData.features.size}")
                            getDrawPolyline(polylineData)
                        } else {
                            // Handle the null case
                            Toast.makeText(requireContext(), response.toString(), Toast.LENGTH_SHORT).show()
                            Log.d("TAG_NULL", "Adding GeoJsonSource and LineLayer${response}")
                        }
                    })
                }
            }
            PageType.MAP -> {
                // Handle MAP page type
                Log.d("MAP_TAG", "MAP")
                mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS) { style ->
                    // Set the camera options to adjust zoom level
                      mapView.mapboxMap.setCamera(
                       CameraOptions.Builder()
                           .zoom(14.0) // Set the desired zoom level here
                           .center(fromLngLat(longitude, latitude))
                           //.center(mapView.mapboxMap.cameraState.center) // Center the camera on the current location
                           .build()
                   )
                    // Observe the SharedViewModel for data updates
                    fetchResponse()
                    viewModelTrails.trailsDrawPolyLinesByIDLiveData.observe(viewLifecycleOwner, Observer { response ->
                       // binding.trailsNameMap.text = response.data.name
                        Log.d("TAG_TRAILS_STAUS", "trailsDrawPolyLinesByIDLiveData ${response.data?.data?.features}")
                        if (response != null) {

                            val trailResponse = response.data?.data
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
            null -> {
                // Handle case where pageType is null
                Log.d("MAP_TAG_AA", "MAP")
            }
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

            // Add GeoJsonSource if it doesn't already exist
            if (style.getSource(sourceId) == null) {
                style.addSource(geoJsonSource(sourceId) {
                    geometry(lineString)
                })
            }
            // Add LineLayer if it doesn't already exist
            if (style.getLayer(layerId) == null) {
                style.addLayer(
                    lineLayer(layerId, sourceId) {
                        lineColor(Color.parseColor(feature.properties.color))
                        lineWidth(5.0)
                    }
                )
            }
        }

        // Update camera to fit the bounding box
        if (response.features.isNotEmpty()) {
            val latLngBounds = LatLngBounds.Builder()
                .include(LatLng(minLat, minLng))
                .include(LatLng(maxLat, maxLng))
                .build()
// Convert LatLng center to Point
            val centerPoint = Point.fromLngLat(latLngBounds.center.longitude, latLngBounds.center.latitude)

            // Calculate zoom level based on bounding box
            val baseZoomLevel = calculateZoomLevel(minLng, minLat, maxLng, maxLat)
            // Adjust zoom level to zoom out slightly (e.g., decrease by 1.0)
            val zoomOutAdjustment = 1.0 // You can adjust this value as needed
            val adjustedZoomLevel = Math.min(15.0, baseZoomLevel + zoomOutAdjustment)
            // Create CameraOptions
            val cameraOptions = CameraOptions.Builder()
                .center(centerPoint) // Center on the bounding box center (converted to Point)
                .zoom(adjustedZoomLevel) // Set the calculated zoom level
                .build()


            // Update camera position with CameraOptions
            mapboxMap.easeTo(cameraOptions) // Duration of the animation in milliseconds

        }
    }

    private fun calculateZoomLevel(minLng: Double, minLat: Double, maxLng: Double, maxLat: Double): Double {
        // Define padding (in pixels) to give some space around the bounding box
        val padding = 50.0

        // Convert the bounding box width and height to map coordinates
        val bboxWidth = maxLng - minLng
        val bboxHeight = maxLat - minLat

        // Map dimensions in pixels
        val mapWidth = 200.0 // Width of the map at zoom level 0 (default tile size)
        val mapHeight = 100.0 // Height of the map at zoom level 0 (default tile size)

        // Calculate the zoom level based on the map's tile dimensions
        // Maximum zoom level
        val zoomLevel = Math.min(15.0, Math.log(Math.max(mapWidth / bboxWidth, mapHeight / bboxHeight)) / Math.log(2.0))
        return zoomLevel
    }



    private fun fetchResponse() {
        viewModelTrails.trailsDrawPolyLinesByIDRequestUser(specificTrailId!!)
    }

    private fun getDrawPolyline(polylineData: PolyLine) {
        mapView.mapboxMap.loadStyle(Style.MAPBOX_STREETS) { style ->
            // Get the feature and its coordinates
            val feature = polylineData.features.firstOrNull() ?: return@loadStyle
            val coordinates = feature.geometry.coordinates
            Log.e("TAG_TRAIL MAP", "HHHHH" + coordinates)
            if (coordinates.isEmpty()) {
                Log.e("MapboxError", "No coordinates found.")
                return@loadStyle
            }

            // Convert coordinates to Points
            val points = coordinates.map { coordinatePair ->
                fromLngLat(coordinatePair[0], coordinatePair[1])
            }

            // Create LineString from Points
            val lineString = LineString.fromLngLats(points)

            // Add a GeoJsonSource
            val geoJsonSource = geoJsonSource("line-source") {
                geometry(lineString)
            }
            style.addSource(geoJsonSource)

            // Add a LineLayer
            val lineLayer = lineLayer("line-layer", "line-source") {
                lineColor(feature.properties.color ?: "#ff0000") // Default to red if no color specified
                lineWidth(5.0)
            }
            style.addLayer(lineLayer)

            // Calculate the bounding box of the LineString
            val boundsBuilder = LatLngBounds.Builder()
            points.forEach { point ->
                boundsBuilder.include(LatLng(point.latitude(), point.longitude()))
            }

            // Build bounds from your boundsBuilder
            val latLngBounds = boundsBuilder.build()

// Check if bounds are valid
            if (latLngBounds.southwest != null && latLngBounds.northeast != null) {
                // Extract southwest and northeast points
                val southwest = latLngBounds.southwest
                val northeast = latLngBounds.northeast

                // Calculate the center latitude and longitude
                val centerLat = (southwest.latitude + northeast.latitude) / 2
                val centerLng = (southwest.longitude + northeast.longitude) / 2

                // Calculate the bounding box width and height in degrees
                val latSpan = northeast.latitude - southwest.latitude
                val lngSpan = northeast.longitude - southwest.longitude

                // Adjust zoom level based on bounding box size
                // You might need to tweak these values to fit your requirements
                val zoomLevel = when {
                    latSpan > 0.1 || lngSpan > 0.1 -> 10.0  // Example value for larger areas
                    latSpan > 0.05 || lngSpan > 0.05 -> 12.0  // Example value for medium areas
                    else -> 14.0  // Example value for smaller areas
                }

                // Configure camera options
                val cameraOptions = CameraOptions.Builder()
                    .center(fromLngLat(centerLng, centerLat))
                    .zoom(zoomLevel) // Dynamically adjusted zoom level
                    .padding(EdgeInsets(10.0, 10.0, 10.0, 10.0)) // Optional padding
                    .build()

                // Configure animation options
                val animationOptions = MapAnimationOptions.Builder()
                    .duration(3000) // Duration in milliseconds
                    .build()

                // Animate camera to fit bounds
                mapView.camera.easeTo(cameraOptions, animationOptions)




            /*// Add a GeoJsonSource to the map using the builder pattern
            style.addSource(
                geoJsonSource("line-source") {
                    geoJsonSource(lineString.toString())
                }
            )

            // Add a LineLayer to visualize the polyline
            style.addLayer(
                LineLayer("line-layer", "line-source").apply {
                    lineColor("#ff0000")  // Set the line color
                    lineWidth(5.0)        // Set the line width
                }
            )*/

                // Zoom to a specific coordinate (e.g., the first point of the polyline)
                // val targetPoint = lineCoordinates.first() // or choose any other coordinate
                /* val position = CameraPosition.Builder()
                .target(LatLng(centerLat, centerLng)) // Center of your bounding box
                .zoom(zoomLevel) // Adjust zoom level as needed
                .build()*/

                // mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))

                // Calculate the bounding box of the LineString

                /*if (hasCoordinates) {
                    // Use the plugin to animate the camera to fit all trails
                    val latLngBounds = boundsBuilder.build()
                    // Compute the center manually
                    val southwest = latLngBounds.southwest
                    val northeast = latLngBounds.northeast
                    val centerLat = (southwest.latitude + northeast.latitude) / 2
                    val centerLng = (southwest.longitude + northeast.longitude) / 2
                    //val centerLatLng = LatLng(centerLat, centerLng)
                    //  val centerPoint = Point.fromLngLat(centerLatLng.longitude, centerLatLng.latitude)
                    val cameraAnimationsPlugin = mapView.camera
                    cameraAnimationsPlugin.easeTo(
                        CameraOptions.Builder()
                            .center(fromLngLat(centerLng, centerLat)) // Note: Longitude, Latitude
                            .zoom(9.0)
                            .padding(
                                EdgeInsets(10.0, 10.0, 10.0, 10.0)) // Optional: add padding to the edges// Adjust zoom level as needed
                            .build(),
                        MapAnimationOptions.Builder()
                            .duration(3000) // Duration in milliseconds (e.g., 3 seconds)
                            .build()
                    )

                } else {
                    Toast.makeText(
                        requireContext(),
                        "No coordinates available to adjust camera bounds.",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("MapError", "No coordinates available to adjust camera bounds.")
                }*/

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



