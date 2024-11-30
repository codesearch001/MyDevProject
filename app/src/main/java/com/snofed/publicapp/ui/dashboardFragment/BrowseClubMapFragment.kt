package com.snofed.publicapp.ui.dashboardFragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
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
import com.snofed.publicapp.models.Client


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

    // Desired width and height for the icon in pixels
    val iconWidth = 50  // for example, 50px
    val iconHeight = 85 // for example, 50px
    var clubName :String? =null

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


        //val iconBitmap = bitmapFromDrawableRes(requireContext(), R.drawable.red_marker, iconWidth, iconHeight)

        iconBitmap = bitmapFromDrawableRes(requireContext(), R.drawable.red_marker,iconWidth,iconHeight)!!
        viewAnnotationManager = binding.mapView.viewAnnotationManager

        // Fetch data and set up map
        getResponse()


        binding.mapView.mapboxMap.loadStyle(Style.STANDARD) {
            Log.d("BrowseClubMapFragment", "Map style loaded successfully")

            // Set the default camera settings
            setDefaultCamera()


        }

    }




        private fun setDefaultCamera() {
        val initialPoint = Point.fromLngLat( pointsList.map {
            it.longitude() }.average(),
            pointsList.map { it.latitude() }.average()) // Default center point


        mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(initialPoint)
                .pitch(35.0)
                .zoom(5.0)
                .build()
        )
            // Setup annotation manager
            setupAnnotationManager()
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
        popupView.findViewById<TextView>(R.id.club_main_name).text =clubName
           // "Longitude: ${coordinates.longitude()}, Latitude: ${coordinates.latitude()}"

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

                response.data.clients.forEach {
                  clubName=  it.publicName
                }
                pointsList = apiPoints.map {

                    Point.fromLngLat(
                        it.startLongitude.toDouble(),
                        it.startLatitude.toDouble())
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
            prepareAnnotationMarker(pointsList,)
        } else {
            Log.e("BrowseClubMapFragment", "PointAnnotationManager not initialized")
        }
    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int, width: Int, height: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, resourceId) ?: return null
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
        return bitmap
    }


    // Load the custom icon
    //val iconBitmap = bitmapFromDrawableRes(requireContext(), R.drawable.red_marker)


    private fun prepareAnnotationMarker(pointsList: List<Point>) {
        pointsList.forEach { point ->
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point) // Set the marker position
                .withIconImage(iconBitmap) // Set the resized icon
                .withIconAnchor(IconAnchor.BOTTOM) // Set anchor point at the bottom of the icon
                .withDraggable(false) // Prevent dragging the marker
            pointAnnotationManager.create(pointAnnotationOptions)


            Log.d("PrepareAnnotationMarker", "Added marker at: ${point.latitude()}, ${point.longitude()}")
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


}








