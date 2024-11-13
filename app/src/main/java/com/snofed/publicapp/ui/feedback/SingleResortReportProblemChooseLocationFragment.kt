package com.snofed.publicapp.ui.feedback


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotation
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentSingleResortReportProblemChooseLocationBinding
import com.snofed.publicapp.models.TaskMedia
import com.snofed.publicapp.models.TaskNote
import com.snofed.publicapp.models.UserReport
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.Constants
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
import com.snofed.publicapp.utils.enums.SyncActionEnum
import com.snofed.publicapp.utils.enums.TaskStatus
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class SingleResortReportProblemChooseLocationFragment : Fragment() {
    private var _binding: FragmentSingleResortReportProblemChooseLocationBinding? = null
    private val binding get() = _binding!!
    private val feedViewModel by viewModels<AuthViewModel>()

    private lateinit var mapView: MapView
    private var pointAnnotationManager: PointAnnotationManager? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var fixedPointAnnotation: PointAnnotation
    private var taskNote: List<TaskNote> = listOf()
    private var taskMedia: List<TaskMedia> = listOf()
    private var description: String? = null
    private var categoryName: String? = null
    private var categoryID: String? = null
    private var fName: String? = null
    private var lName: String? = null
    private var mPhone: String? = null
    private var eMailId: String? = null
    private var contactInformation: String? = null
    private var taskId = UUID.randomUUID().toString()
    private var noteUDID = UUID.randomUUID().toString()

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSingleResortReportProblemChooseLocationBinding.inflate(inflater, container, false)

        description = arguments?.getString("description").toString()
        categoryName = arguments?.getString("categoryName").toString()
        categoryID = arguments?.getString("CATEGORY_ID").toString()
        fName = arguments?.getString("CATEGORY_F_NAME").toString()
        lName = arguments?.getString("CATEGORY_L_NAME").toString()
        mPhone = arguments?.getString("CATEGORY_M_NUMBER").toString()
        eMailId = arguments?.getString("CATEGORY_EMAIL_ID").toString()

        Log.e("MapView", "Location is null.$description")
        Log.e("MapView", "Location is null.$categoryID")

        contactInformation = createContactInformation()

        return binding.root
    }

    private fun createContactInformation(): String {
        fName = arguments?.getString("CATEGORY_F_NAME").toString()
        lName = arguments?.getString("CATEGORY_L_NAME").toString()
        mPhone = arguments?.getString("CATEGORY_M_NUMBER").toString()
        eMailId = arguments?.getString("CATEGORY_EMAIL_ID").toString()

        return fName + "\n" + lName + "\n" + mPhone + "\n" + eMailId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        initializeFusedLocationClient()
        checkLocationPermissions()
        bindObservers()
        initializeMapView()
    }

    private fun bindObservers() {
        feedViewModel.userFeedBackResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    Toast.makeText(requireActivity(),
                        resources.getString(R.string.feedback_saved_successfully),
                        Toast.LENGTH_SHORT
                    ).show()
                    //findNavController().navigate(R.id.feedBackFragment)
                    findNavController().popBackStack(R.id.feedBackFragment, false)
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is NetworkResult.Loading -> {
                    // Handle loading state
                }
            }
        })
    }

    private fun initializeFusedLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    private fun checkLocationPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    moveCameraToLocation(location)
                    logAndToastLocation(location)
                } else {
                    Log.e("MapView", "Location is null.")
                }
            }
            .addOnFailureListener { e ->
                Log.e("MapView", "Failed to get location: ${e.message}")
            }
    }

    private fun logAndToastLocation(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        Log.d("MapView", "Latitude: $latitude, Longitude: $longitude")
        Toast.makeText(
            requireContext(),
            "Latitude: $latitude, Longitude: $longitude",
            Toast.LENGTH_LONG
        ).show()


    }

    private fun handleClicks(latitude: Double, longitude: Double) {
        val userReport = UserReport(
            taskId,
            tokenManager.getClientId().toString(),
            categoryID.toString(),
            categoryName.toString(),
            description.toString(),
            longitude,
            latitude,
            tokenManager.getUserId().toString(),
            tokenManager.getFullName().toString(),
            TaskStatus.PublicAppReport.statusValue,
            SyncActionEnum.NEW.statusValue,
            taskNote
        )
        val gson = Gson()
        val json = gson.toJson(userReport)
        //Log.d("SingleResortReportProblemChooseLocationFragment", "sendReport: " +json)
        Log.d("SingleResortReportProblemChooseLocationFragment", "sendReport: " +userReport)

        feedViewModel.userReportRequest(listOf(userReport))
    }

    private fun moveCameraToLocation(location: Location) {
        val cameraOptions = CameraOptions.Builder()
            .center(Point.fromLngLat(location.longitude, location.latitude))
            .zoom(14.0) // Adjust zoom level as needed
            .build()

        mapView.getMapboxMap().setCamera(cameraOptions)
    }

    private fun initializeMapView() {
        mapView = binding.mapView
        mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) { style ->

            val originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.red_marker)
            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, 100, 130, false)
            style.addImage("YOUR_ICON_BITMAP", scaledBitmap)

            // Initialize PointAnnotationManager after the style has been loaded
            if (pointAnnotationManager == null) {
                pointAnnotationManager = mapView.annotations.createPointAnnotationManager()
            }

            pointAnnotationManager = mapView.annotations.createPointAnnotationManager()

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return@loadStyleUri
            }
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val currentPoint = Point.fromLngLat(location.longitude, location.latitude)
                    addFixedPointAnnotation(currentPoint) // Add fixed marker
                } else {
                    addFixedPointAnnotation(Point.fromLngLat(0.0, 0.0)) // Placeholder
                }
            }

            // Listen to map movements and update the marker position
            mapView.getMapboxMap().addOnCameraChangeListener {
                val center = mapView.getMapboxMap().cameraState.center
                val latitude = center.latitude()
                val longitude = center.longitude()
                updateFixedPointAnnotation(Point.fromLngLat(center.longitude(), center.latitude()))
                // Print and Toast the updated map center
                Log.d(
                    "MapView",
                    "Map Center after drag - Latitude: $latitude, Longitude: $longitude"
                )
                //Toast.makeText(requireContext(), "Map Center - Latitude: $latitude, Longitude: $longitude", Toast.LENGTH_LONG).show()
                taskNote = listOf(
                    TaskNote(
                        noteUDID,
                        contactInformation.toString(),
                        tokenManager.getUserId().toString(),
                        tokenManager.getFullName().toString(),
                        taskId,
                        SyncActionEnum.NEW.statusValue,
                        taskMedia
                    )
                )

                /*taskMedia = listOf(
                    TaskMedia(
                        UUID.randomUUID().toString(),
                        "",
                        "",
                        "",
                        0,
                        SyncActionEnum.NEW.statusValue,
                        noteUDID
                    )
                )*/

                binding.confirmSendButton.setOnClickListener {
                    handleClicks(latitude, longitude)
                }

            }
        }

    }

    private fun addFixedPointAnnotation(point: Point) {
        val pointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage("YOUR_ICON_BITMAP")
            .withDraggable(false) // No dragging for the marker

        // Initialize fixedPointAnnotation safely
        fixedPointAnnotation = pointAnnotationManager?.create(pointAnnotationOptions) ?: return

    }

    private fun updateFixedPointAnnotation(point: Point) {
        if (::fixedPointAnnotation.isInitialized) {
            fixedPointAnnotation.point = point
            pointAnnotationManager?.update(fixedPointAnnotation)
        } else {
            Log.e("MapView", "fixedPointAnnotation is not initialized.")
        }
    }
    /*private fun addDraggablePointAnnotation(point: Point) {
        val pointAnnotationOptions = PointAnnotationOptions()
            .withPoint(point)
            .withIconImage("YOUR_ICON_BITMAP")
            .withDraggable(true) // Set the annotation to be draggable

        // Create the fixed annotation (marker)
        pointAnnotationManager?.create(pointAnnotationOptions)

        // Optionally handle drag events
        pointAnnotationManager?.addDragListener(object : OnPointAnnotationDragListener {
//            override fun onAnnotationDragStarted(annotation: PointAnnotation) {
//                Log.d("MapView", "Drag started")
//            }
//
//            override fun onAnnotationDrag(annotation: PointAnnotation) {
//                Log.d("MapView", "Dragging at: ${annotation.point.latitude()}, ${annotation.point.longitude()}")
//            }
//
//            override fun onAnnotationDragFinished(annotation: PointAnnotation) {
//                Log.d("MapView", "Drag finished at: ${annotation.point.latitude()}, ${annotation.point.longitude()}")
//                handleClicks(annotation.point.latitude(), annotation.point.longitude()) // Update feedback with the new location
//            }
//
            override fun onAnnotationDrag(annotation: Annotation<*>) {
                TODO("Not yet implemented")
            }

            override fun onAnnotationDragFinished(annotation: Annotation<*>) {
                if (annotation is PointAnnotation) {
                    Log.d("MapView", "Drag finished at: ${annotation.point.latitude()}, ${annotation.point.longitude()}")

                    // Update feedback or do something with the new location
                   // handleClicks(annotation.point.latitude(), annotation.point.longitude())
                }
            }

            override fun onAnnotationDragStarted(annotation: Annotation<*>) {
                TODO("Not yet implemented")
            }
        })
    }*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                // Handle permission denial
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //getCurrentLocation()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.mapView.onDestroy()
        _binding = null
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }
}



