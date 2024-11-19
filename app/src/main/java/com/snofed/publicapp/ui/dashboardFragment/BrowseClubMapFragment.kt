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
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentBrowseClubMapBinding
import com.snofed.publicapp.utils.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.mapbox.maps.Style
import com.mapbox.maps.ViewAnnotationAnchor
import com.mapbox.maps.extension.style.layers.properties.generated.IconAnchor
import com.mapbox.maps.plugin.animation.CameraAnimationsPlugin
import com.mapbox.maps.plugin.animation.MapAnimationOptions
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
import com.snofed.publicapp.models.ClientResponse


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

    private lateinit var fabOpen: Animation
    private lateinit var fabClose: Animation
    private lateinit var rotateForward: Animation
    private lateinit var rotateBackward: Animation
    private var isOpen = false
    private var cameraAnimationsPlugin: CameraAnimationsPlugin? = null
    // Desired width and height for the icon in pixels
    val iconWidth = 50  // for example, 50px
    val iconHeight = 85 // for example, 50px

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
        //iconBitmap = bitmapFromDrawableRes(requireContext(), R.drawable.red_marker)!!
        iconBitmap = bitmapFromDrawableRes(requireContext(), R.drawable.red_marker,iconWidth,iconHeight)!!
        viewAnnotationManager = binding.mapView.viewAnnotationManager

        // Fetch data and set up map
        getResponse()
        binding.fab1.setOnClickListener {
            //zoomOut()
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
//            zoomToCurrentLocation()

            //Toast.makeText(requireContext(), "camera click", Toast.LENGTH_SHORT).show()
        }

        binding.fab2.setOnClickListener {
            animateFab()

            //Toast.makeText(requireContext(), "folder click", Toast.LENGTH_SHORT).show()
        }
        binding.fab3.setOnClickListener {
            animateFab()
            //showCustomDialog2()
            //Toast.makeText(requireContext(), "folder click", Toast.LENGTH_SHORT).show()
        }
        binding.mapView.mapboxMap.loadStyle(Style.STANDARD) {
            Log.d("BrowseClubMapFragment", "Map style loaded successfully")

            // Set the default camera settings
            //setDefaultCamera()

            // Setup annotation manager
            //setupAnnotationManager()
        }


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


    private fun setDefaultCamera() {
        val initialPoint = Point.fromLngLat( pointsList.map { it.longitude() }.average(),
            pointsList.map { it.latitude() }.average()) // Default center point
        //val initialZoom = 18.0

        mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(initialPoint)
                .pitch(20.0)
                .zoom(5.0)
                .build()
        )
    }

    private fun setupAnnotationManager(clients: List<Client>) {
        val annotationPlugin = mapView.annotations
       pointAnnotationManager = annotationPlugin.createPointAnnotationManager(AnnotationConfig(layerId = LAYER_ID))

       // Click listener to show the custom info window
       pointAnnotationManager.addClickListener { clickedAnnotation ->
            showCustomInfoWindow(clickedAnnotation)
          true
      }

        // Add markers to the map
        updateMapWithPoints(clients)
    }

    @SuppressLint("InflateParams")
    private fun showCustomInfoWindow(annotation: PointAnnotation) {
        val coordinates = annotation.point
        val client  = annotation.getData()
        val gson = Gson()
        val clientData = gson.fromJson(client.toString(), Client::class.java)
        // Remove any existing annotation views
       // viewAnnotationManager.removeAllViewAnnotations()

        // Create and configure the custom info window
        // Inflate your custom layout properly
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.map_popup_layout, null)
        val displayMetrics = resources.displayMetrics
        val widthInDp = 250 // Desired width in dp
        val heightInDp = 170 // Desired height in dp

        val widthInPx = (widthInDp * displayMetrics.density).toInt()
        val heightInPx = (heightInDp * displayMetrics.density).toInt()

        popupView.layoutParams = ViewGroup.LayoutParams(widthInPx, heightInPx)

        //val view = LayoutInflater.from(requireContext()).inflate(R.layout.map_popup_layout, null)
        popupView.findViewById<TextView>(R.id.club_name).text = clientData?.publicName?.trimStart()?.trimEnd()
       // popupView.findViewById<TextView>(R.id.club_main_name).text =
        popupView.findViewById<TextView>(R.id.rating_text).text =clientData.clientRating
        popupView.findViewById<TextView>(R.id.review_count).text ="(${clientData.totalRatings})"
        popupView.findViewById<TextView>(R.id.view_club_page_button).setOnClickListener {
            val bundle = Bundle()
            bundle.putString("clientId", clientData.id)
            val destination = R.id.clubSubMembersFragment
            findNavController().navigate(destination, bundle)
        }


            /*"Longitude: ${coordinates.longitude()}, Latitude: ${coordinates.latitude()}"*/

        // Add the custom view as an annotation
        viewAnnotationManager.addViewAnnotation(
            popupView,
            viewAnnotationOptions {
                geometry(coordinates)
                //anchor(ViewAnnotationAnchor.BOTTOM) // Attach popup at the bottom of the POI
                allowOverlap(true)     // Allow overlapping with other annotations (optional)
                ignoreCameraPadding(true)  // Optionally ignore camera padding
                allowOverlapWithPuck(true)
            }
        )
        cameraAnimationsPlugin = mapView.getPlugin(CameraAnimationsPlugin::class.java.toString())
        mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(coordinates)
                .zoom(6.0) // Set desired zoom level
                .pitch(9.0)
                .build()
        )
        MapAnimationOptions.mapAnimationOptions { duration(3000) }
    }

    private fun getResponse() {
        showLoader()
        sharedViewModel.browseClubResponse.observe(viewLifecycleOwner, Observer { response ->
            hideLoader()
            response?.data?.clients?.filter { it.visibility == 0 }?.let { clients ->
                pointsList = clients.map {
                    Point.fromLngLat(it.startLongitude.toDouble(), it.startLatitude.toDouble())
                }
                //updateMapWithPoints()
                setDefaultCamera()
                setupAnnotationManager(clients)
            } ?: Log.e("browseClubResponse", "Failed to fetch response")
        })
    }

    private fun showLoader() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        binding.progressBar.visibility = View.GONE
    }

    private fun updateMapWithPoints(clients: List<Client>) {
        if (this::pointAnnotationManager.isInitialized) {
            pointAnnotationManager.deleteAll()
            prepareAnnotationMarker(clients)
        } else {
            Log.e("BrowseClubMapFragment", "PointAnnotationManager not initialized")
        }
    }

//    private fun bitmapFromDrawableRes(context: Context, resId: Int): Bitmap? {
//        val drawable = ContextCompat.getDrawable(context, resId) ?: return null
//        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
//        val canvas = android.graphics.Canvas(bitmap)
//        drawable.setBounds(0, 0, 80, 80)
//        drawable.draw(canvas)
//        return bitmap
//    }

    private fun bitmapFromDrawableRes(context: Context, @DrawableRes resourceId: Int, width: Int, height: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, resourceId) ?: return null
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
        return bitmap
    }

    private fun prepareAnnotationMarker(clients: List<Client>) {
        clients.forEach { client ->
            val pointAnnotationOptions = PointAnnotationOptions()
                .withPoint(Point.fromLngLat(client.startLongitude.toDouble(), client.startLatitude.toDouble()))
                .withIconImage(iconBitmap)
                .withIconAnchor(IconAnchor.BOTTOM)
                .withDraggable(false)
           // pointAnnotationManager.create(pointAnnotationOptions)
            // Create the annotation and attach the client data as metadata
            val pointAnnotation = pointAnnotationManager.create(pointAnnotationOptions)


            val gson = Gson()
            // Convert Client object to JsonElement
            val clientJson: JsonElement = gson.toJsonTree(client)
            pointAnnotation.setData(clientJson)
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
        dismissPopup()
        print("click")
        return true
    }
    private fun dismissPopup() {

        viewAnnotationManager.removeAllViewAnnotations()
        Log.d("BrowseClubMapFragment", "Popup dismissed")
    }

}








