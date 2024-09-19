package com.snofed.publicapp.ui.dashboardFragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.ImageAdapter
import com.snofed.publicapp.databinding.FragmentBrowseClubMapBinding
import com.snofed.publicapp.databinding.FragmentMapExploreBinding
import com.snofed.publicapp.utils.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.symbolLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.plugin.gestures.OnMapClickListener
import com.mapbox.maps.plugin.gestures.addOnMapClickListener

@AndroidEntryPoint
class BrowseClubMapFragment : Fragment()  {
    private var _binding: FragmentBrowseClubMapBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
      //  return inflater.inflate(R.layout.fragment_browse_club_map, container, false)
       _binding = FragmentBrowseClubMapBinding.inflate(inflater, container, false)

      /*  binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize MapView and MapboxMap
        mapView = binding.mapView
        mapboxMap = mapView.mapboxMap

        // Set up setupClubMap
        setupClubMap()
        // Set the initial camera position and zoom level
        setInitialCameraPosition()
        // Add location icon marker
        addLocationMarker()
    }

    private fun addLocationMarker() {
        mapView.mapboxMap.getStyle { style ->
            // Add a source for markers
            val source = geoJsonSource("markers-source") {
                featureCollection(
                    FeatureCollection.fromFeatures(
                        listOf(
                            Feature.fromGeometry(Point.fromLngLat(-73.9856, 40.7484)),
                            Feature.fromGeometry(Point.fromLngLat(-74.9856, 41.7484)),
                            // Add more features here
                        )
                    )
                )
            }
            style.addSource(source)

            // Convert the drawable to a bitmap and reduce size
            val markerIcon = ResourcesCompat.getDrawable(resources, R.drawable.map_location_icon, null)
            val bitmap = markerIcon?.toBitmap(size = 65) // Reduced size (e.g., 48x48 pixels)

            // Add the marker icon to the style if the bitmap is not null
            bitmap?.let {
                style.addImage("marker-icon", it)
            }

            // Add a layer for the markers
            style.addLayer(
                symbolLayer("markers-layer", "markers-source") {
                    iconImage("marker-icon")
                    iconSize(0.5) // Adjust size factor as needed
                    iconAllowOverlap(true)
                }
            )


        }
    }

    private fun Drawable.toBitmap(size: Int): Bitmap? {
        val width = size
        val height = size
        if (width <= 0 || height <= 0) {
            return null
        }
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        this.setBounds(0, 0, canvas.width, canvas.height)
        this.draw(canvas)
        return bitmap
    }

    private fun showInfoWindow() {
        AlertDialog.Builder(requireContext())
            .setTitle("Marker Info")
            .setMessage("This is the info for the marker.")
            .setPositiveButton("OK", null)
            .show()
    }


    private fun setInitialCameraPosition() {
        // Set the camera position and zoom level
        mapView.getMapboxMap().setCamera(
            CameraOptions.Builder()
                .center(Point.fromLngLat(-73.9856, 40.7484)) // Example coordinates
                .zoom(8.0) // Example zoom level
                .build()
        )
    }

    private fun setupClubMap() {
        sharedViewModel.browseClubResponse.observe(viewLifecycleOwner, Observer { response ->

            val data = response.data.systemData.poiTypes.size
            // Normal case: data is present
            Log.d("Tag_FeedGallery ", "Tag_FeedGallery.. $data")



        })
    }


}


