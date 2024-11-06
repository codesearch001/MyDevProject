package com.snofed.publicapp.ui.feedback

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.android.gms.maps.MapView
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentFeedBackDetailsBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.enums.SyncActionEnum
import com.snofed.publicapp.utils.enums.TaskStatus
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FeedBackDetailsFragment : Fragment() {

    private var _binding: FragmentFeedBackDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()

    private var feedBackID: String? = null

    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_feed_back_details, container, false)
        _binding = FragmentFeedBackDetailsBinding.inflate(inflater, container, false)
        feedBackID = arguments?.getString("FEEDBACK_ID").toString()

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize the MapboxMap directly
        mapboxMap = binding.mapView.mapboxMap
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }


        fetchResponse()
        viewModel.feedBackTaskDetailsLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data?.data
                    Log.e("data", "data $data")
                    binding.txtTitle.text = data?.creatorFullName
                    binding.txtFeedBackDecription.text = data?.description

                    //Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    // Setup the map once it's ready
                    if (data != null) {
                        setupMap(data.latitude, data.longitude)
                    }
                   /* if (data?.status == 4){
                        binding.txtStatusProgress.text = TaskStatus.getDescription(TaskStatus.PublicAppReport)
                    }else{
                        binding.txtStatusProgress.text = "N/A"
                    }
*/
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun setupMap(latitude: Double, longitude: Double) {
        // Set the initial camera position using latitude and longitude from your Data class
        mapboxMap.loadStyle(Style.OUTDOORS) { style ->

            // Assuming this method provides the Data object
            val point = Point.fromLngLat(longitude, latitude)

            // Set the camera options and animate to the location
            mapboxMap.setCamera(
                CameraOptions.Builder()
                    .center(point)
                    .zoom(12.0)  // Set a zoom level
                    .build())
            // Add a marker at the specified location
            addMarker(point)
        }
    }

    private fun addMarker(point: Point?) {
        // Create an annotation manager for the map
        val annotationManager = binding.mapView.annotations.createPointAnnotationManager()

        // Define the custom icon for the marker
        val icon = BitmapFactory.decodeResource(resources, R.drawable.red_marker) // Your custom icon

        // Create a point annotation with the custom icon
        val pointAnnotationOptions = point?.let {
            PointAnnotationOptions()
                .withPoint(it)
                .withIconImage(icon)
                .withIconSize(0.5)
        }

        // Add the marker (point annotation) to the map
        if (pointAnnotationOptions != null) {
            annotationManager.create(pointAnnotationOptions)
        }
    }

    private fun fetchResponse() {
        viewModel.getTaskDetails(feedBackID.toString())
        //viewModel.getTaskDetails("C374DABB-5E40-49C9-BA08-DAF240E635FA")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}