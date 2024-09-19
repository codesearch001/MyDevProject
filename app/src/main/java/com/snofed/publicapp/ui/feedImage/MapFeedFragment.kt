package com.snofed.publicapp.ui.feedImage

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.mapbox.geojson.LineString
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.extension.style.layers.addLayer
import com.mapbox.maps.extension.style.layers.generated.lineLayer
import com.mapbox.maps.extension.style.layers.getLayer
import com.mapbox.maps.extension.style.sources.addSource
import com.mapbox.maps.extension.style.sources.generated.GeoJsonSource
import com.mapbox.maps.extension.style.sources.generated.geoJsonSource
import com.mapbox.maps.extension.style.sources.getSource
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentMapFeedBinding
import com.snofed.publicapp.models.workoutfeed.WorkoutPointResponse
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.DateTimeConverter
import com.snofed.publicapp.utils.DrawerController
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class MapFeedFragment : Fragment(){
    private var _binding: FragmentMapFeedBinding? = null
    private val binding get() = _binding!!
    private val feedWorkoutViewModel by viewModels<AuthViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private var isGraphVisible = false
    private var publicUserId: String = ""
    private lateinit var mapView: MapView
    private lateinit var mapboxMap: MapboxMap
    val dateTimeConverter = DateTimeConverter()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapFeedBinding.inflate(inflater, container, false)

        binding.humburger.setOnClickListener {
            (activity as? DrawerController)?.openDrawer()
        }
        // Retrieve data from arguments
        // Ensure that the arguments and initialization are valid
        publicUserId = arguments?.getString("feedId") ?: ""
        tokenManager.savePublicUserId(publicUserId)

        return binding.root
    }

    @SuppressLint("SuspiciousIndentation", "DefaultLocale")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.imgIdChartIcon.setOnClickListener {
            isGraphVisible = !isGraphVisible
            binding.rlGraphContainer.visibility = if (isGraphVisible) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        // Initialize MapView and MapboxMap
        mapView = binding.mapView
        mapboxMap = mapView.mapboxMap

        // Load Map Style
        mapView.mapboxMap.loadStyle(Style.OUTDOORS)


        fetchResponse()
        feedWorkoutViewModel.feedWorkoutLiveData.observe(viewLifecycleOwner, Observer { it ->
            when (it) {
                is NetworkResult.Success -> {
                    sharedViewModel.WorkoutActivites.value = it.data

                    val data = it.data?.data
                    // Normal case: data is present
                    Log.d("feedWorkoutViewModel ", "Data: $data")
                    binding.feedName.text = data?.publisherFullname ?: "N/A"

                    binding.feedWorkoutDec.text = data?.description ?: "N/A"

                    val formattedDateTimeHMS = dateTimeConverter.formatSecondsToHMS(data?.duration!!.toLong())
                    binding.txtIdStartTime.text = formattedDateTimeHMS

                    binding.txtIdWorkoutType.text = data?.activity?.name ?: "N/A"

                    binding.txtIdDistance.text = String.format("%.2f", (data?.distance?.div(1000))).toDouble().toString() + "km"

                    binding.txtCaloriesId.text = data?.calories.toString() ?: "N/A"

                    binding.txtIdAveragePace.text = String.format("%.2f", data?.averagePace).toDouble().toString() + " min/km"

                    //binding.feedSpeed.text = dateTimeConverter.calculateSpeedFromPace(String.format("%.2f",data.averagePace).toDouble()).toString() + " km/h"

                    val averageSpeed = calculateAverage(data.workoutPoints)
                    // Assuming `data.averagePace` is a number you want to format
                    val formattedPace = String.format("%.2f", data.averagePace)

                    // Convert the formatted string to Double for calculation
                    val paceAsDouble = formattedPace.toDouble()

                    // Calculate speed and format the result
                    val speed = dateTimeConverter.calculateSpeedFromPace(paceAsDouble)

                    // Set the text with the speed formatted to two decimal places and "km/h"
                    binding.feedSpeed.text = String.format("%.2f", averageSpeed) + " km/h"


                    if (data.activity.iconPath == "") {
                        Glide.with(binding.imgActivitiesIcon).load(R.drawable.bicycle).into(binding.imgActivitiesIcon)
                    } else {
                        Glide.with(binding.imgActivitiesIcon).load(data.activity.iconPath).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(binding.imgActivitiesIcon)
                    }

                    binding.imgFeedId.setOnClickListener {
                        it.findNavController().navigate(R.id.feedViewImageFragment)
                    }

                    val workoutPoints = data?.workoutPoints?.map {
                        Point.fromLngLat(it.longitude, it.latitude)
                    }

                    Log.d("feedWorkoutViewModel2 ", "workoutPoints" + workoutPoints)
                   /* workoutPoints?.let { points ->
                        drawRoute(points)
                    }*/

                    workoutPoints?.let { points ->
                        if (points.isNotEmpty()) {
                            drawRoute(points)
                        } else {
                            Log.e("MapFeedFragment", "Workout points list is empty")
                        }
                    }
                    val workoutStartDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(it.data.data.startTime) ?: Date()

                    setUpChart(it.data.data.workoutPoints,workoutStartDate)

                 //   processWorkoutPoints(it.data.data.workoutPoints, workoutStartDate)

                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {

                }
            }
        })
    }

    private fun calculateAverage(workoutPoints: List<WorkoutPointResponse>): Double {
        val sum = workoutPoints.sumOf { it.speed }
        val average = if (workoutPoints.isNotEmpty()) sum / workoutPoints.size else 0.0

        return Helper.round(average, 2)
    }


    private fun setUpChart(workoutPoints: List<WorkoutPointResponse>, workoutStartDate: Date) {
        val entries = mutableListOf<Entry>()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val comparator = Comparator<WorkoutPointResponse> { c1, c2 -> c1.timestamp.compareTo(c2.timestamp) }
        workoutPoints.sortedWith(comparator).forEach { point ->
            try {
                // Parse the timestamp string to a Date object
                val pointDate = sdf.parse(point.timestamp) ?: return@forEach
                // Calculate the difference in milliseconds between the point's timestamp and the workout start date
                val current = pointDate.time - workoutStartDate.time
                if (point.speed <= 100 && current > 0) {
                    // Add the entry to the list with time in seconds and speed
                    entries.add(Entry((current / 1000).toFloat(), point.speed.toFloat()))
                }
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val lineChart: LineChart = binding.chartWorkout
            val lineDataSet = LineDataSet(entries, "Speed")
            lineDataSet.color = Color.parseColor("#42a5f5") // Change color to light blue
            lineDataSet.setDrawCircles(false)
            lineDataSet.lineWidth = 2f
            lineDataSet.setDrawValues(false)
            lineDataSet.setDrawValues(false)
            lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

            val lineData = LineData(lineDataSet)
            val chartDesc = Description()
            chartDesc.text = ""

            lineChart.data = lineData

            // Customization
            lineChart.setBackgroundColor(Color.parseColor("#1a1a2e"))
            lineChart.setDrawGridBackground(false)
            lineChart.setDrawBorders(false) // Disable drawing borders
            lineChart.description.isEnabled = false
            lineChart.legend.textColor = Color.WHITE

            // XAxis customization
            val xAxis: XAxis = lineChart.xAxis
            xAxis.textColor = Color.WHITE
            xAxis.position = XAxis.XAxisPosition.TOP
            xAxis.setDrawGridLines(false)

            xAxis.setAvoidFirstLastClipping(true)

            // YAxis customization (left)
            val yAxisLeft: YAxis = lineChart.axisLeft
            yAxisLeft.textColor = Color.WHITE
            yAxisLeft.setDrawGridLines(false)
            /*yAxisLeft.axisMinimum = 0f // Minimum value for YAxis
            yAxisLeft.axisMaximum = 10f // Maximum value for YAxis*/
            yAxisLeft.granularity = 1f // Interval between values
            yAxisLeft.setLabelCount(6, true) // Set number of labels to 6 (0, 2, 4, 6, 8, 10)


            // YAxis customization (right)
            val yAxisRight: YAxis = lineChart.axisRight
            yAxisRight.textColor = Color.WHITE
            yAxisRight.setDrawGridLines(false)
            //yAxisRight.axisMinimum = 0f // Minimum value for YAxis
            //yAxisRight.axisMaximum = 10f // Maximum value for YAxis
            yAxisRight.granularity = 1f // Interval between values
            yAxisRight.setLabelCount(6, true) // Set number of labels to 6 (0, 2, 4, 6, 8, 10)

            lineChart.invalidate() // refresh
        }

    }


    private fun drawRoute(workoutPoints: List<Point>) {
        mapView.mapboxMap.getStyle { style ->
            // Create a LineString from the workout points
            val lineString = LineString.fromLngLats(workoutPoints)

            /*// Add a GeoJsonSource for the line
            style.addSource(
                geoJsonSource("route-source") {
                    geometry(lineString)
                }
            )*/

            val sourceId = "route-source"
            if (style.getSource(sourceId) != null) {
                (style.getSource(sourceId) as GeoJsonSource).geometry(lineString)
            } else {
                style.addSource(
                    geoJsonSource(sourceId) {
                        geometry(lineString)
                    }
                )
            }

            /*val sourceId = "route-source"
            if (style.getSource(sourceId) != null) {
                style.removeSource(sourceId)
            }

            style.addSource(
                geoJsonSource(sourceId) {
                    geometry(lineString)
                }
            )*/


            // Add a LineLayer to display the line
            /*style.addLayer(
                lineLayer("route-layer", "route-source") {
                    lineColor("#052650") // Set the color of the route
                    lineWidth(2.0) // Set the width of the route
                    lineDasharray(listOf(3.0, 3.0)) // Create a dashed line pattern
                }
            )*/

            val layerId = "route-layer"
            if (style.getLayer(layerId) != null) {
                style.removeStyleLayer(layerId)
            }

            style.addLayer(
                lineLayer(layerId, "route-source") {
                    lineColor(Color.RED)
                    lineWidth(2.0) // Set the width of the route
                    lineDasharray(listOf(3.0, 3.0)) // Create a dashed line pattern
                }
            )

            /*  // Add sources and layers for start and end points
              val startPoint = workoutPoints.first()
              val endPoint = workoutPoints.last()

              // Create GeoJSON sources for start and end points
              style.addSource(
                  geoJsonSource("start-point-source") {
                      geometry(startPoint)
                  }
              )
              style.addSource(
                  geoJsonSource("end-point-source") {
                      geometry(endPoint)
                  }
              )

              // Add SymbolLayer for start point with circle icon
              style.addLayer(
                  symbolLayer("start-point-layer", "start-point-source") {
                      iconImage("circle-icon") // Use a predefined or custom circle icon
                      iconSize(1.0) // Size of the icon
                      iconAllowOverlap(true) // Allow overlap
                  }
              )

              // Add SymbolLayer for end point with star icon
              style.addLayer(
                  symbolLayer("end-point-layer", "end-point-source") {
                      iconImage("star-icon") // Use a predefined or custom star icon
                      iconSize(1.0) // Size of the icon
                      iconAllowOverlap(true) // Allow overlap
                  }
              )
  */
            // Add images to the map style
            /* addImageToStyle(style, "circle-icon", R.drawable.star,12,12)
            addImageToStyle(style, "star-icon", R.drawable.star,12,12)*/

            // Calculate the bounding box to include the start and end points
            if (workoutPoints.isNotEmpty()) {
                val startPoint = workoutPoints.first()
                val endPoint = workoutPoints.last()

                val boundsBuilder = LatLngBounds.Builder()
                boundsBuilder.include(startPoint.toLatLng())
                boundsBuilder.include(endPoint.toLatLng())
                val bounds = boundsBuilder.build()

                // Add margin to the bounding box
                val marginRatio = 1.0 // Adjust this ratio to fit your margin needs
                val expandedBounds = expandBounds(bounds, marginRatio)
                // Access the CameraAnimationsPlugin
                val cameraAnimationsPlugin = mapView.camera
                cameraAnimationsPlugin.easeTo(
                    CameraOptions.Builder()
                        .center(expandedBounds.center.toPoint()) // Center the camera on the expanded bounding box center
                        .zoom(calculateZoomLevel(expandedBounds)) // Set zoom level to fit the expanded bounding box
                        .build(),
                    MapAnimationOptions.Builder()
                        .startDelay(500)
                        .duration(800) // Duration in milliseconds (e.g., 3 seconds)
                        .build()
                )
            }
        }
    }

    // Function to expand the bounding box by a given margin ratio
    private fun expandBounds(bounds: LatLngBounds, marginRatio: Double): LatLngBounds {
        val width = bounds.northeast.longitude - bounds.southwest.longitude
        val height = bounds.northeast.latitude - bounds.southwest.latitude

        // Calculate the margin based on the ratio
        val marginHeight = height * marginRatio

        val expandedSouthwest = LatLng(
            bounds.southwest.latitude - marginHeight, // Extend bottom edge
            bounds.southwest.longitude
        )
        val expandedNortheast = LatLng(
            bounds.northeast.latitude,
            bounds.northeast.longitude
        )

        return LatLngBounds.Builder()
            .include(expandedSouthwest)
            .include(expandedNortheast)
            .build()
    }

    // Extension function to convert LatLng to Point
    private fun LatLng.toPoint(): Point {
        return Point.fromLngLat(longitude, latitude)
    }

    // Function to calculate an appropriate zoom level for the bounding box
    private fun calculateZoomLevel(bounds: LatLngBounds): Double {
        // Adjust these values as needed to fit the bounding box
        val width = bounds.northeast.longitude - bounds.southwest.longitude
        val height = bounds.northeast.latitude - bounds.southwest.latitude

        // Calculate the zoom level based on bounding box dimensions
        // This is a simplified example; you may need a more complex calculation based on your needs
        return when {
            width > 0.1 || height > 0.1 -> 18.0 // Zoom out a bit for large areas
            width > 0.05 || height > 0.05 -> 18.0 // Zoom out further for medium areas
            else -> 21.0 // Zoom in the closest for small areas
        }
    }
    // Extension function to convert Point to LatLng
    private fun Point.toLatLng(): LatLng {
        return LatLng(latitude(), longitude())
    }

    private fun fetchResponse() {
        feedWorkoutViewModel.feedWorkoutRequestUser(tokenManager.getPublicUserId().toString())
    }

    @SuppressLint("Lifecycle")
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    @SuppressLint("Lifecycle")
    override fun onStop() {
        super.onStop()
        mapView.onStop() // Ensure MapView stops
    }

    @SuppressLint("Lifecycle")
    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up binding to avoid memory leaks
        _binding = null
    }

    override fun onDetach() {
        super.onDetach()
        mapView.onStart()
    }


}