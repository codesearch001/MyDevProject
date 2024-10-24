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
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.viewannotation.ViewAnnotationManager
import com.mapbox.maps.viewannotation.geometry
import com.mapbox.maps.viewannotation.viewAnnotationOptions

@AndroidEntryPoint
class BrowseClubMapFragment : Fragment() {
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

            // Set the default camera settings, including zoom level
            setDefaultCamera()

            setupAnnotationManager() // Ensure the annotation manager is created here
        }
    }

    private fun setDefaultCamera() {
        // Set the initial camera position and zoom level
        val initialPoint = Point.fromLngLat(0.0, 0.0) // Default center point (you can change this)
        val initialZoom = 8.0 // Default zoom level

        mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(initialPoint)
                .zoom(initialZoom)
                .build()
        )
    }

    private fun setupAnnotationManager() {
        // Initialize PointAnnotationManager
        val annotationPlugin = mapView.annotations
        pointAnnotationManager = annotationPlugin.createPointAnnotationManager(
            AnnotationConfig(layerId = LAYER_ID)
        )
        Log.d("BrowseClubMapFragment", "PointAnnotationManager created")

        // Set up click and drag listeners for the annotations
        pointAnnotationManager.addClickListener { clickedAnnotation ->
            Log.d("BrowseClubMapFragment", "Annotation clicked at: ${clickedAnnotation.point.longitude()}, ${clickedAnnotation.point.latitude()}")
            showPopup(clickedAnnotation)
            true
        }

      /*  pointAnnotationManager.addDragListener(object : OnPointAnnotationDragListener {
            override fun onAnnotationDrag(annotation: Annotation<*>) {
                val pointAnnotation = annotation as PointAnnotation
                Log.d("AnnotationDrag", "Dragging annotation at: ${pointAnnotation.point.longitude()}, ${pointAnnotation.point.latitude()}")
            }

            override fun onAnnotationDragFinished(annotation: Annotation<*>) {
                val pointAnnotation = annotation as PointAnnotation
                Log.d("AnnotationDragFinished", "Drag finished at: ${pointAnnotation.point.longitude()}, ${pointAnnotation.point.latitude()}")
                Toast.makeText(
                    requireActivity(),
                    "Drag finished at: ${pointAnnotation.point.longitude()}, ${pointAnnotation.point.latitude()}",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onAnnotationDragStarted(annotation: Annotation<*>) {
                val pointAnnotation = annotation as PointAnnotation
                Log.d("AnnotationDragStarted", "Drag started at: ${pointAnnotation.point.longitude()}, ${pointAnnotation.point.latitude()}")
            }
        })*/

        // Create the initial annotations
        updateMapWithPoints()
    }
  /*  private fun showPopup(annotation: PointAnnotation) {
        val coordinates = annotation.point

        // Inflate the custom popup layout
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_layout, null)

        // Set up the TextViews in the popup
        val titleTextView = popupView.findViewById<TextView>(R.id.popup_title)
        val descriptionTextView = popupView.findViewById<TextView>(R.id.popup_description)

        // Set the content for the popup (you can customize this based on your data)
        titleTextView.text = "Marker Title" // Set a dynamic title based on your data
        descriptionTextView.text = "Longitude: ${coordinates.longitude()}, Latitude: ${coordinates.latitude()}" // Set a dynamic description

        // Create a PopupWindow
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        // Show the popup at the marker's position
        val location = binding.mapView.projection.toScreenLocation(coordinates)
        popupWindow.showAtLocation(binding.mapView, Gravity.NO_GRAVITY, location.x, location.y)

        // Dismiss the popup when clicked outside
        popupView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                popupWindow.dismiss()
                true
            } else {
                false
            }
        }
    }*/

    private fun showPopup(annotation: PointAnnotation) {
        val coordinates = annotation.point
        Toast.makeText(
            requireActivity(),
            "Marker clicked at: ${coordinates.longitude()}, ${coordinates.latitude()}",
            Toast.LENGTH_SHORT
        ).show()
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
            // Clear any previous annotations
            pointAnnotationManager.deleteAll()
            // Add new annotations
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

    private companion object {
        const val LAYER_ID = "layer-id"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}





