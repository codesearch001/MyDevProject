package com.snofed.publicapp.ui.trailsstatus

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.EventClubFeedAdapter
import com.snofed.publicapp.databinding.FragmentResortSingleTrailsStatusDetailsBinding
import com.snofed.publicapp.ui.event.EventTrailsFeedAdapter
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.DateTimeConverter
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.TokenManager
import com.snofed.publicapp.utils.enums.PageType
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ResortSingleTrailsStatusDetailsFragment : Fragment(),EventTrailsFeedAdapter.OnItemClickListener {

    private var _binding: FragmentResortSingleTrailsStatusDetailsBinding? = null
    private val binding get() = _binding!!
    private val trailsDetailsViewModel by viewModels<AuthViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private var trailId: String = ""
    private var textForLighting: String = ""
    private val dateTimeConverter = DateTimeConverter()
    private val pageType: PageType = PageType.DETAIL // Example page type, set as needed

    private lateinit var feedAdapter: EventTrailsFeedAdapter
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_resort_single_trails_status_details, container, false)
        _binding = FragmentResortSingleTrailsStatusDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trailId = arguments?.getString("trailId").toString()
        Log.d("ResortSingleTrails", "trailId $trailId")
        tokenManager.saveTrailsId(trailId)

        binding.trailsMaps.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("pageType", pageType) // Use putParcelable
            }
            it.findNavController().navigate(R.id.resortTrailStatusMapFragment,bundle)
        }

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.feedback.setOnClickListener {
            it.findNavController().navigate(R.id.feedBackDefaultCategoryListFragment)
        }
        /*  binding.eventCardId.setOnClickListener {
              it.findNavController().navigate(R.id.singleEventDetailsFragment)
          }*/
        getTrailsDetails()
        getTrailGraphData()
        getEventList()
    }

    private fun getEventList() {
        // Initialize RecyclerView and Adapter
        feedAdapter = EventTrailsFeedAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = feedAdapter


        // Observe the SharedViewModel for data updates
        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner) { response ->
            val events = response?.data?.events ?: emptyList()

            Log.d("Tag_Events", "EventsSize: ${events.size}")

            if (events.isEmpty()) {

                // Show the "Data Not data" message and hide RecyclerView
               // binding.tvSplashText.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE

            } else {

                // Hide the "No data" message and show RecyclerView
               // binding.tvSplashText.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                binding.recyclerView.layoutManager = layoutManager
                feedAdapter.setEvent(events)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTrailsDetails() {
        fetchResponse()
        trailsDetailsViewModel.trailsDetailsLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    sharedViewModel.TrailsDetilsResponse.value = it.data
                    //binding.progressBar.isVisible = false
                    // Assuming `data` is an instance of the `Data` class and `nameTranslates` is a property of `data`
                    val name = when {
                        it.data?.data?.nameTranslates?.en != null -> it.data.data.nameTranslates.en
                        it.data?.data?.nameTranslates?.sv != null -> it.data.data.nameTranslates.sv
                        it.data?.data?.nameTranslates?.de != null -> it.data.data.nameTranslates.de
                        it.data?.data?.nameTranslates?.no != null -> it.data.data.nameTranslates.no
                        else -> "N/A"
                    }

                    binding.trailsName.text = name.toString()
                    //binding.idTxtLength.text = it.data?.data?.length.toString() + "m"
                    binding.idTxtLength.text = Helper.m2Km(it.data?.data?.length?.toDouble()).toString() + resources.getString(R.string.t_km)

                    dateTimeConverter.convertDateTime(it.data?.data?.lastPreparedDate!!)//convert data
                    val getDate = dateTimeConverter.dateandtimePart

                    if (getDate== null){
                        binding.idTxtDateTime.text =resources.getString(R.string.trail_info_preparation_datete_not_yet_prepared)
                    }else{
                        binding.idTxtDateTime.text = getDate
                    }
                   // binding.idTxtDateTime.text = getDate
                    binding.idtxtRating.text = it.data.data.averageRating.toString()
                   binding.idRating.rating = it.data?.data?.averageRating!!.toFloat()
                    binding.tvReviewCount.text = "(" + it.data.data.trailRatings.count().toString() + ")"
                   // binding.tvReviewCount.text = "(" + it.data.data.averageRating.toString() + ")"
                    if (it.data.data.trailQuality == null) {
                        binding.idTxttrailQuality.text = "N/A"
                    } else {
                        binding.idTxttrailQuality.text = it.data.data.trailQuality
                    }
                    binding.idtxtMinHeight.text = String.format("%.2f m", it.data.data.minAlt)
                    binding.idTxtMaxHeight.text = String.format("%.2f m", it.data.data.maxAlt)
                    binding.idTxtDuration.text = it.data.data.duration.toString() + "h"
                    if (it.data.data.difficulty == null) {
                        binding.idtxtDifficulty.text = "N/A"
                       // binding.idtxtDifficulty.setTextColor(Color.parseColor(Color.BLACK.toString()))
                    } else {
                        binding.idtxtDifficulty.text = it.data.data.difficulty.name
                        binding.idtxtDifficulty.setTextColor(Color.parseColor( it.data.data.difficulty.color))
                    }

                    binding.idTxtShortMsg.text = it.data.data.shortMessage.takeIf { it.isNotEmpty() } ?: "N/A"
                    binding.idTxtAvgRating.text = "(" + it.data.data.averageRating.toString() + ")"
                    if (it.data.data.description.isNullOrEmpty()) {
                        binding.idTxtDescription.text = "N/A"
                    } else {
                        binding.idTxtDescription.text = it.data.data.description
                    }

                    if (it.data.data.trailIconPath == null) {
                        Glide.with(binding.backgroundImage).load(R.drawable.resort_card_bg)
                            .into(binding.backgroundImage)
                        binding.idImgCardLL.visibility = View.GONE
                    } else {

                        Glide.with(binding.backgroundImage).load(it.data.data.trailIconPath)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter()
                            .into(binding.backgroundImage)
                        binding.idImgCardLL.visibility = View.VISIBLE
                    }

                    if (it.data.data.hasLights) {
                        textForLighting = resources.getString(R.string.yes)
                    } else {
                        textForLighting = resources.getString(R.string.no)
                    }

                    if (it.data.data.timespanLightsOn != ""){
                        textForLighting = textForLighting + " , " +it.data.data.timespanLightsOn
                    }
                    binding.idtxtLighting.text = textForLighting


                    if (it.data.data.status.toInt() == 1) {
                        binding.idTxtStatusOpen.isVisible = true
                        binding.idTxtStatusClose.isVisible = false
                    } else {
                        binding.idTxtStatusOpen.isVisible = false
                        binding.idTxtStatusClose.isVisible = true
                    }
                }

                is NetworkResult.Error -> {

                }

                is NetworkResult.Loading -> {
                    //binding.progressBar.isVisible = true
                    binding.root.isEnabled = false  // Disable UI interactions
                }
            }
        })
    }

    private fun getTrailGraphData() {
        fetchResponse2()
        trailsDetailsViewModel.trailsDetailsGraphData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {

                    Log.d("Tag1", "length11 " + it.data?.data)
                    Log.d("Tag2", "height22 " + it.data?.data?.size)

                    // Use binding to access the line chart
                    val lineChart = binding.lineChart
                    if (it.data?.data != null && it.data.data.isNotEmpty()) {
                        val entries = ArrayList<Entry>()
                        it.data.data.forEachIndexed { _, graphResponse ->
                            // Check that values are valid and non-negative
                            if (graphResponse.height >= 0 && graphResponse.length >= 0) {
                                entries.add(Entry(graphResponse.length.toFloat(), graphResponse.height.toFloat()))
                            }
                        }
                            // Create a dataset only if entries are valid
                        if (entries.isNotEmpty()) {
                            // Create a dataset and configure its appearance
                            val dataSet = LineDataSet(entries, "X - length, Y - height")
                            dataSet.color = Color.parseColor("#42a5f5") // Change color to light blue
                            dataSet.color = Color.BLUE
                            dataSet.valueTextColor = Color.WHITE
                            dataSet.setDrawCircles(false)
                            dataSet.setDrawValues(false)
                            dataSet.lineWidth = 2f
                            dataSet.fillAlpha = 110
                            dataSet.mode=LineDataSet.Mode.CUBIC_BEZIER
                            // Create a LineData object with the dataset
                            val lineData = LineData(dataSet)


                            // Configure X-Axis
                            val xAxis: XAxis = lineChart.xAxis
                            xAxis.position = XAxis.XAxisPosition.BOTTOM
                            xAxis.setDrawGridLines(false)
                            xAxis.textColor = Color.WHITE

                            // Configure Y-Axis
                            val leftAxis: YAxis = lineChart.axisLeft
                            val rightAxis: YAxis = lineChart.axisRight
                            leftAxis.textColor = Color.WHITE
                            rightAxis.textColor = Color.WHITE

                            // Configure the Legend
                            val legend: Legend = lineChart.legend
                            legend.textColor = Color.WHITE
                            lineChart.data = lineData
                            // Set description text to an empty string
                            // Configure chart description
                            val chartDescription = Description().apply {
                                text = ""
                            }
                            lineChart.description = chartDescription
                            lineChart.description.isEnabled = false
                            lineChart.setDrawGridBackground(false)
                            lineChart.setTouchEnabled(true)
                            lineChart.axisLeft.setDrawAxisLine(false)
                            lineChart.axisRight.setDrawAxisLine(false)
                            //lineChart.setPinchZoom(true)
                            lineChart.setDrawBorders(false)
                            lineChart.zoom(1f, 0f, 0f, 0f) // Zooms in by 50% on both X and Y axes, centered on (0, 0)
                            lineChart.invalidate()
                            lineChart.setBackgroundColor(Color.DKGRAY)
                            lineChart.animateX(1000)
                        }
                    } else {
                        // Handle case where entries is empty, if necessary
                        Log.d("LineChart", "Entries list is empty. No data to display.")
                    }

                }

                is NetworkResult.Error -> {
                    // Handle case where entries is empty, if necessary
                    Log.d("LineChart", "Entries list is empty. No data to display.")
                }

                is NetworkResult.Loading -> {
                    //binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun fetchResponse2() {
        trailsDetailsViewModel.trailsDetailsGraph(trailId)
    }

    private fun fetchResponse() {
        trailsDetailsViewModel.trailsDetailsRequestUser(trailId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(eventId: String) {
        val bundle = Bundle()
        bundle.putString("eventId", eventId)
        val destination = R.id.singleEventDetailsFragment
        findNavController().navigate(destination, bundle)
    }
}