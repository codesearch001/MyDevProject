package com.snofed.publicapp.maps

import PolylineManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.bottomsheet.BottomSheetDialog

import com.mapbox.geojson.BoundingBox
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.geojson.Polygon
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.LineLayer
import com.mapbox.maps.extension.style.layers.generated.fillLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.createAnnotationPlugin
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.BottomSheetApartmentsBinding
import com.snofed.publicapp.databinding.FragmentMapExploreBinding
import com.snofed.publicapp.databinding.FragmentMapFeedBinding
import com.snofed.publicapp.models.browseSubClub.Area
import com.snofed.publicapp.models.browseSubClub.Poi
import com.snofed.publicapp.models.browseSubClub.Properties
import com.snofed.publicapp.models.browseSubClub.Trail
import com.snofed.publicapp.models.browseSubClub.Zone
import com.snofed.publicapp.models.workoutfeed.WorkoutPointResponse
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.DrawerController
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.lang.System.setProperties

@AndroidEntryPoint
class MapExploreFragment : Fragment() {
    private var _binding: FragmentMapExploreBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by activityViewModels<SharedViewModel>()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_map_explore, container, false)
        _binding = FragmentMapExploreBinding.inflate(inflater, container, false)

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize MapView and MapboxMap
        mapView = binding.mapView
        mapboxMap = mapView.mapboxMap
        // Retrieve the CameraAnimationsPlugin
        // Retrieve the CameraAnimationsPlugin
        cameraAnimationsPlugin = mapView.getPlugin(CameraAnimationsPlugin::class.java.toString())


        fetchMapTrailsData()
        binding.fab1.setOnClickListener {
            zoomOut()
        }

        fabOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_out)
        fabClose = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in)
        rotateForward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_forward)
        rotateBackward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_backward)

        binding.fab.setOnClickListener {
            animateFab()
        }

        binding.fab1.setOnClickListener {
            animateFab()
            Toast.makeText(requireContext(), "camera click", Toast.LENGTH_SHORT).show()
        }

        binding.fab2.setOnClickListener {
            animateFab()

            Toast.makeText(requireContext(), "folder click", Toast.LENGTH_SHORT).show()
        }
        binding.fab3.setOnClickListener {
            animateFab()
            showCustomDialog2()
            Toast.makeText(requireContext(), "folder click", Toast.LENGTH_SHORT).show()
        }
    }

    private fun zoomOut() {
        /* mapboxMap.getStyle { style ->
             val currentCameraPosition = mapboxMap.cameraPosition
             val newZoomLevel = currentCameraPosition.zoom - 1.0 // Adjust zoom level as needed

             mapboxMap.setCamera(
                 CameraOptions.Builder()
                     .center(Point.fromLngLat(currentCameraPosition.center.longitude, currentCameraPosition.center.latitude))
                     .zoom(newZoomLevel)
                     .build()
             )
         }*/
    }

    //trail data
    val trails: List<Trail> = listOf() // Replace with actual data

    private fun fetchMapTrailsData() {

        // Observe the SharedViewModel for data updates
        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner, Observer { response ->
            val trails = response?.data?.trails?.filter { trail ->
                trail.visibility.toInt() == 1 //Assuming visibility is already an integer
            }
            val zones = response?.data?.zones ?: emptyList()//1
            val pois = response?.data?.pois ?: emptyList()//1
            val areas = response?.data?.areas ?: emptyList()
            Log.d("Tag_Trails", "trails size: ${trails?.size}")
            Log.d("Tag_Zones", "zones size: ${zones.size}")
            Log.d("Tag_POIs", "pois size: ${pois.size}")
            Log.d("Tag_Areas", "areas size: ${areas.size}")


            if (trails != null) {
                addTrailsToMap(trails)
            }
            if (zones.isNotEmpty()) {
                addZonesToMap(zones)
            }
            if (pois.isNotEmpty()) {
                addPoisToMap(pois)
            }
            if (areas.isNotEmpty()) {
                addAreasToMap(areas)
            }
        })
    }

    private fun addTrailsToMap(trails: List<Trail>) {
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

                    /*  // Use GeoJsonSource.Builder to create the source
                      val lineSource = geoJsonSource("line-source-${trail.id}") {
                          featureCollection(featureCollection)
                      }*/
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
                val southwest = latLngBounds.southwest
                val northeast = latLngBounds.northeast
                val centerLat = (southwest.latitude + northeast.latitude) / 2
                val centerLng = (southwest.longitude + northeast.longitude) / 2
                //val centerLatLng = LatLng(centerLat, centerLng)
                //  val centerPoint = Point.fromLngLat(centerLatLng.longitude, centerLatLng.latitude)
                val cameraAnimationsPlugin = mapView.camera
                cameraAnimationsPlugin.easeTo(CameraOptions.Builder()
                    .center(Point.fromLngLat(centerLng, centerLat)) // Note: Longitude, Latitude
                    .zoom(9.0)
                    .padding(EdgeInsets(10.0, 10.0, 10.0, 10.0)) // Optional: add padding to the edges// Adjust zoom level as needed
                    .build(),
                    MapAnimationOptions.Builder()
                        .duration(3000) // Duration in milliseconds (e.g., 3 seconds)
                        .build())

            } else {
                Toast.makeText(requireContext(), "No coordinates available to adjust camera bounds.", Toast.LENGTH_SHORT).show()
                Log.e("MapError", "No coordinates available to adjust camera bounds.")
            }

            //cameraAnimationsPlugin!!.easeTo(cameraOptions, animationOptions) // Smooth animation to the bounds
        }
    }

    private fun addAreasToMap(areas: List<Area>) {
        mapboxMap.getStyle { style ->
            areas.forEach { area ->
                // Ensure you have a proper list of coordinates
                val coordinates = area.name // This should be a List<LatLng> or similar

                // Convert coordinates to a list of Point objects
                val points = coordinates.map { coord ->
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
                    val point = Point.fromLngLat(poi.longitude, poi.latitude)

                    val options = PointAnnotationOptions()
                        .withPoint(point)
                        .withIconImage("poi-icon") // Use the icon image ID
                        .withIconSize(0.05) // Adjust icon size if needed

                    manager.create(options)
                }
            } ?: run {
                Log.e("MapError", "PointAnnotationManager is not initialized.")
            }
        }
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
                    listOf(zone.definitions.map { definition ->
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
                        fillColor(zone.color)
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
            layoutInflater,
            R.layout.bottom_sheet_apartments,
            null,
            false)
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