package com.snofed.publicapp.ui.dashboardFragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentBrowseClubMapBinding
import com.snofed.publicapp.utils.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.annotation.Annotation
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.OnPointAnnotationDragListener
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.viewannotation.viewAnnotationOptions

import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions


@AndroidEntryPoint
class BrowseClubMapFragment : Fragment(), OnMapClickListener {
    private var _binding: FragmentBrowseClubMapBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var viewAnnotationManager: ViewAnnotationManager
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private var pointsList: List<Point> = emptyList()
    private lateinit var iconBitmap: Bitmap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBrowseClubMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize MapView and MapboxMap
        mapView = binding.mapView
        mapboxMap = mapView.mapboxMap
        iconBitmap = bitmapFromDrawableRes(requireContext(), R.drawable.red_marker)!!
        viewAnnotationManager = binding.mapView.viewAnnotationManager

        // Fetch data and set up map
        getResponse()

        binding.mapView.mapboxMap.loadStyle(Style.STANDARD) {
            Log.d("BrowseClubMapFragment", "Map style loaded successfully")

            // Set the default camera settings
            setDefaultCamera()

            // Setup annotation manager
            setupAnnotationManager()
        }
    }

    private fun setDefaultCamera() {
        val initialPoint = Point.fromLngLat(0.0, 0.0) // Default center point
        val initialZoom = 18.0

        mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(initialPoint)
                .zoom(initialZoom)
                .build()
        )
    }

    private fun setupAnnotationManager() {
        val annotationPlugin = mapView.annotations
        pointAnnotationManager = annotationPlugin.createPointAnnotationManager(AnnotationConfig(layerId = LAYER_ID))

        // Click listener to show the custom info window
        pointAnnotationManager.addClickListener { clickedAnnotation ->
            showCustomInfoWindow(clickedAnnotation)
            true
        }

        // Add markers to the map
        updateMapWithPoints()
    }

    @SuppressLint("InflateParams")
    private fun showCustomInfoWindow(annotation: PointAnnotation) {
        val coordinates = annotation.point

        // Remove any existing annotation views
        viewAnnotationManager.removeAllViewAnnotations()

        // Create and configure the custom info window
        // Inflate your custom layout properly
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.map_popup_layout, null)
        val displayMetrics = resources.displayMetrics
        val widthInDp = 250 // Desired width in dp
        val heightInDp = 170 // Desired height in dp

        val widthInPx = (widthInDp * displayMetrics.density).toInt()
        val heightInPx = (heightInDp * displayMetrics.density).toInt()

        popupView.layoutParams = ViewGroup.LayoutParams(
            widthInPx,
            heightInPx
        )

        //val view = LayoutInflater.from(requireContext()).inflate(R.layout.map_popup_layout, null)
        popupView.findViewById<TextView>(R.id.club_name).text = "Marker Title"
        popupView.findViewById<TextView>(R.id.club_main_name).text =
            "Longitude: ${coordinates.longitude()}, Latitude: ${coordinates.latitude()}"

        // Add the custom view as an annotation
        viewAnnotationManager.addViewAnnotation(
            popupView,
            viewAnnotationOptions {
                geometry(coordinates)
                allowOverlap(true)
                ignoreCameraPadding(true)
                allowOverlapWithPuck(true)
            }
        )
    }

    private fun getResponse() {
        showLoader()
        sharedViewModel.browseClubResponse.observe(viewLifecycleOwner, Observer { response ->
            hideLoader()
            response?.data?.clients?.filter { it.visibility == 0 }?.let { apiPoints ->
                pointsList = apiPoints.map {
                    Point.fromLngLat(it.startLongitude.toDouble(), it.startLatitude.toDouble())
                }
                updateMapWithPoints()
            } ?: Log.e("browseClubResponse", "Failed to fetch response")
        })
    }

    private fun showLoader() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.progressBar.visibility = View.GONE
    }

    private fun updateMapWithPoints() {
        if (this::pointAnnotationManager.isInitialized) {
            pointAnnotationManager.deleteAll()
            prepareAnnotationMarker(pointsList)
        } else {
            Log.e("BrowseClubMapFragment", "PointAnnotationManager not initialized")
        }
    }

    private fun bitmapFromDrawableRes(context: Context, resId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, resId) ?: return null
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, 80, 80)
        drawable.draw(canvas)
        return bitmap
    }

    private fun prepareAnnotationMarker(pointsList: List<Point>) {
        pointsList.forEach { point ->
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(iconBitmap)
                .withIconAnchor(IconAnchor.BOTTOM)
                .withDraggable(false)
            pointAnnotationManager.create(pointAnnotationOptions)
        }
        Log.d("BrowseClubMapFragment", "Annotations added: ${pointsList.size}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private companion object {
        const val LAYER_ID = "layer-id"
    }

    override fun onMapClick(point: Point): Boolean {
        TODO("Not yet implemented")
    }
}








