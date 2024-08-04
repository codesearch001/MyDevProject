package com.snofed.publicapp.ui.feedImage

import PolylineManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.snofed.publicapp.databinding.FragmentMapFeedBinding
import com.snofed.publicapp.utils.DrawerController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapFeedFragment : Fragment() {
    private var _binding: FragmentMapFeedBinding? = null
    private val binding get() = _binding!!
    private lateinit var polylineManager: PolylineManager
    private lateinit var mapView: MapView
    private val route = listOf(
        Point.fromLngLat(86.2516482, 22.7897874), // Point A
        Point.fromLngLat( 86.2516321,  22.7898061), // Point B28°33'17.3"N 77°03'05.2"E
        Point.fromLngLat( 86.2516321,  22.7898061), // Point B28°33'17.3"N 77°03'05.2"E
        Point.fromLngLat( 86.2516322,  22.7898061), // Point B28°33'17.3"N 77°03'05.2"E
        Point.fromLngLat( 86.2516323,  22.7898061), // Point B28°33'17.3"N 77°03'05.2"E
        Point.fromLngLat( 86.2516324,  22.7898061), // Point B28°33'17.3"N 77°03'05.2"E
        Point.fromLngLat( 86.251635,  22.7898061), // Point B28°33'17.3"N 77°03'05.2"E
        Point.fromLngLat( 86.2516326,  22.7898061), // Point B28°33'17.3"N 77°03'05.2"E
        Point.fromLngLat( 86.2516327,  22.7898061), // Point B28°33'17.3"N 77°03'05.2"E
        Point.fromLngLat(86.2516138, 22.7898143), // Point C
        Point.fromLngLat( 86.2516308, 22.7898066), // Point D
//        Point.fromLngLat(77.2351, 28.5834), // Point E
//        Point.fromLngLat(77.2361, 28.5836), // Point F
//        Point.fromLngLat(77.2371, 28.5838), // Point G
//        Point.fromLngLat(77.2381, 28.5840), // Point H
//        Point.fromLngLat(77.2391, 28.5842)  // Point I
    )
    private var routeIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval: Long = 1000 // Update interval in milliseconds
    private var isGraphVisible = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapFeedBinding.inflate(inflater, container, false)
        binding.humburger.setOnClickListener {
            (activity as? DrawerController)?.openDrawer()
        }
        return binding.root
    }

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

        mapView = binding.mapView
        polylineManager = PolylineManager(requireActivity(), mapView)
        mapView.getMapboxMap()
            .loadStyleUri("mapbox://styles/systainadev/clzeswuev00bn01pl0whu8v9i") {
                // mapView.getMapboxMap().loadStyleUri(Style.OUTDOORS) {
                // Add the polyline to the map
                polylineManager.addPolyline(route)

                // Start the camera animation
                startCameraAnimation()

            }

        val lineChart: LineChart = binding.chartWorkout

        // Sample data
        val entries = mutableListOf<Entry>()
        entries.add(Entry(-3500f, 6f))
        entries.add(Entry(-3400f, 6.5f))
        entries.add(Entry(-3300f, 5.5f))
        entries.add(Entry(-3200f, 8f))
        entries.add(Entry(-3100f, 4f))
        entries.add(Entry(-3000f, 6f))
        entries.add(Entry(-2900f, 6.2f))

        val lineDataSet = LineDataSet(entries, "Speed")
        lineDataSet.color = Color.parseColor("#42a5f5") // Change color to light blue
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth = 2f
        lineDataSet.setDrawValues(false)
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER

        val lineData = LineData(lineDataSet)
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
        yAxisLeft.axisMinimum = 0f // Minimum value for YAxis
        yAxisLeft.axisMaximum = 10f // Maximum value for YAxis
        yAxisLeft.granularity = 2f // Interval between values
        yAxisLeft.setLabelCount(6, true) // Set number of labels to 6 (0, 2, 4, 6, 8, 10)


        // YAxis customization (right)
        val yAxisRight: YAxis = lineChart.axisRight
        yAxisRight.textColor = Color.WHITE
        yAxisRight.setDrawGridLines(false)
        yAxisRight.axisMinimum = 0f // Minimum value for YAxis
        yAxisRight.axisMaximum = 10f // Maximum value for YAxis
        yAxisRight.granularity = 2f // Interval between values
        yAxisRight.labelCount = 6 // Set number of labels to 6 (0, 2, 4, 6, 8, 10)

        lineChart.invalidate() // refresh
    }

    private fun startCameraAnimation() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (routeIndex < route.size) {
                    val point = route[routeIndex]
                    val cameraOptions = CameraOptions.Builder()
                        .center(point)
                        .zoom(14.0)
                        .build()
                    mapView.getMapboxMap().setCamera(cameraOptions)
                    routeIndex++

                }
            }
        }, updateInterval)
    }


    override fun onStop() {
        super.onStop()
        mapView.onStop() // Ensure MapView stops
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onStart()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null) // Stop the handler when the fragment is paused
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        _binding = null
    }


    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}