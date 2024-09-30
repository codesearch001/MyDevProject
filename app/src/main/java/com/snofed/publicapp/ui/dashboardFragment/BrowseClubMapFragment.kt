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
class BrowseClubMapFragment : Fragment()  {
    private var _binding: FragmentBrowseClubMapBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var viewAnnotationManager: ViewAnnotationManager
    private lateinit var pointAnnotationManager: PointAnnotationManager
    private lateinit var pointAnnotation: PointAnnotation
    private lateinit var viewAnnotation: View
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    private var pointsList: List<Point> = emptyList()


    private lateinit var iconBitmap : Bitmap
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_browse_club_map, container, false)
       _binding = FragmentBrowseClubMapBinding.inflate(inflater, container, false)

      /*  binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }*/
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize MapView and MapboxMap
        mapView = binding.mapView
        mapboxMap = mapView.mapboxMap

        /*val iconBitmap = BitmapUtils.bitmapFromDrawableRes(requireContext(), R.drawable.red_marker)!!*/
        getResponse()
        iconBitmap = bitmapFromDrawableRes(requireContext(), R.drawable.red_marker)!!
        viewAnnotationManager = binding.mapView.viewAnnotationManager

        resetCamera()

        binding.mapView.mapboxMap.loadStyle(Style.STANDARD) {
            prepareAnnotationMarker(binding.mapView, iconBitmap,pointsList)
           // prepareViewAnnotation()

            pointAnnotationManager.addClickListener { clickedAnnotation ->
                if (pointAnnotation == clickedAnnotation) {
                   // viewAnnotation.toggleViewVisibility()
                }
                true
            }

          /*  binding.fabStyleToggle.setOnClickListener {
                if (pointAnnotation.iconImage == null) {
                    pointAnnotation.iconImageBitmap = iconBitmap
                    viewAnnotation.isVisible = true
                } else {
                    pointAnnotation.iconImageBitmap = null
                    viewAnnotation.isVisible = false
                }
                pointAnnotationManager.update(pointAnnotation)
            }*/

          /*  binding.fabReframe.setOnClickListener {
                resetCamera()
                pointAnnotation.point = POINT
                pointAnnotationManager.update(pointAnnotation)
                syncAnnotationPosition()
            }
*/
            pointAnnotationManager.addDragListener(object : OnPointAnnotationDragListener {
                override fun onAnnotationDrag(annotation: Annotation<*>) {
                    TODO("Not yet implemented")
                }

                override fun onAnnotationDragFinished(annotation: Annotation<*>) {
                    TODO("Not yet implemented")
                }

                override fun onAnnotationDragStarted(annotation: Annotation<*>) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    private fun getResponse() {
        // Show loader before the API call starts
        showLoader()

        sharedViewModel.browseClubResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                val data = response.data.clients
                // Normal case: data is present
                Log.d("browseClubResponse ", "browseClubResponse.. $data")
                val filteredClients = data?.filter { client ->
                    client.visibility == 0 // true 1->false
                }

                filteredClients?.let { apiPoints ->
                    // Convert ApiPoint to Point
                    pointsList = apiPoints.map {
                        Point.fromLngLat(it.startLongitude.toDouble(), it.startLatitude.toDouble())
                    }

                    // Update map with the points
                    updateMapWithPoints()

                    // Hide loader after processing
                    hideLoader()
                } ?: run {
                    // If filteredClients is null, hide loader
                    hideLoader()
                }
            } else {
                // Error or null case, hide loader
                hideLoader()
                Log.e("browseClubResponse", "Failed to fetch response")
            }
        })
    }

    // Function to show the loader
    private fun showLoader() {
        binding.progressBar.visibility = View.VISIBLE
    }

    // Function to hide the loader
    private fun hideLoader() {
        binding.progressBar.visibility = View.GONE
    }

    private fun updateMapWithPoints() {
        resetCamera() // Reset camera to center on new points
        prepareAnnotationMarker(binding.mapView, iconBitmap, pointsList) // Update markers on the map
    }


    private fun bitmapFromDrawableRes(context: Context, resId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, resId) ?: return null
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        drawable.setBounds(0, 0, 80, 80)
        drawable.draw(canvas)
        return bitmap
    }

    private fun resetCamera() {
        if (pointsList.isEmpty()) return

        // Calculate the center point
        val centerPoint = Point.fromLngLat(
            pointsList.map { it.longitude() }.average(),
            pointsList.map { it.latitude() }.average()
        )

        binding.mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(centerPoint)
                .pitch(45.0)
                .zoom(8.0)
                .bearing(-17.6)
                .build()
        )
    }

    /*private fun resetCamera() {
        if (POINTS.isEmpty()) return

        // Use the first point as the center
        val centerPoint = POINTS[0]

        binding.mapView.mapboxMap.setCamera(
            CameraOptions.Builder()
                .center(centerPoint)
                .pitch(45.0)
                .zoom(12.5)
                .bearing(-17.6)
                .build()
        )
    }*/


    private fun syncAnnotationPosition() {
        viewAnnotationManager.updateViewAnnotation(
            viewAnnotation,
            viewAnnotationOptions {
                geometry(pointAnnotation.geometry)
            }
        )
        /*ItemCalloutViewBinding.bind(viewAnnotation).apply {
            textNativeView.text = "lat=%.2f\nlon=%.2f".format(
                pointAnnotation.geometry.latitude(),
                pointAnnotation.geometry.longitude()
            )
        }*/
    }

   /* @SuppressLint("SetTextI18n")
    private fun prepareViewAnnotation() {
        viewAnnotation = viewAnnotationManager.addViewAnnotation(
            resId = R.layout.item_callout_view,
            options = viewAnnotationOptions {
                geometry(pointAnnotation.geometry)
                annotationAnchor {
                    anchor(ViewAnnotationAnchor.BOTTOM)
                    offsetY((pointAnnotation.iconImageBitmap?.height!!.toDouble()))
                }
            }
        )
        ItemCalloutViewBinding.bind(viewAnnotation).apply {
            textNativeView.text = "lat=%.2f\nlon=%.2f".format(POINT.latitude(), POINT.longitude())
            closeNativeView.setOnClickListener {
                viewAnnotationManager.removeViewAnnotation(viewAnnotation)
            }
            selectButton.setOnClickListener { b ->
                val button = b as Button
                val isSelected = button.text.toString().equals("SELECT", true)
                val pxDelta = if (isSelected) SELECTED_ADD_COEF_PX else -SELECTED_ADD_COEF_PX
                button.text = if (isSelected) "DESELECT" else "SELECT"
                viewAnnotationManager.updateViewAnnotation(
                    viewAnnotation,
                    viewAnnotationOptions {
                        selected(isSelected)
                    }
                )
                (button.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    bottomMargin += pxDelta
                    rightMargin += pxDelta
                    leftMargin += pxDelta
                }
                button.requestLayout()
            }
        }
    }*/

    private fun prepareAnnotationMarker(
        mapView: MapView, iconBitmap: Bitmap,
        pointsList: List<Point>
    ) {
        val annotationPlugin = mapView.annotations
        pointAnnotationManager = annotationPlugin.createPointAnnotationManager(
            AnnotationConfig(layerId = LAYER_ID)
        )

        for (point in pointsList) {
            val pointAnnotationOptions: PointAnnotationOptions = PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(iconBitmap)
                .withIconAnchor(IconAnchor.BOTTOM)
                .withDraggable(true)
            pointAnnotationManager.create(pointAnnotationOptions)
        }
    }

    private companion object {
        const val SELECTED_ADD_COEF_PX = 25

        val LAYER_ID = "layer-id"
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

}


