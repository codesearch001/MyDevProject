package com.snofed.publicapp.ui.dashboardFragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.HomeDashBoardActivity
import com.snofed.publicapp.HomeNewActivity
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.RecentFeedAdpater
import com.snofed.publicapp.databinding.FragmentHomeBinding
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.models.workoutfeed.Daum
import com.snofed.publicapp.ui.feedImage.WorkoutRideLogFeedAdapter
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.DateTimeConverter
import com.snofed.publicapp.utils.DrawerController
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedPreferenceKeys
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val clubViewModel by viewModels<AuthViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    val dateTimeConverter = DateTimeConverter()
    private lateinit var recentFeedAdpater: RecentFeedAdpater
    private var lastDaysData: Long = 7
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        //for using status bar space
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }
        // adapter = NoteAdapter(::onNoteClicked)
        binding.humburger.setOnClickListener {
            (activity as? DrawerController)?.openDrawer()
        }
        return binding.root
        // val view = binding.root
        //  return view
    }



    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get ViewModel instance


        binding.nameTextView.text = tokenManager.getUser()
        binding.btnPurchase.setOnClickListener {
            findNavController().navigate(R.id.purchaseHistoryFragment2)
            //startActivity(Intent(requireActivity(), HomeNewActivity::class.java))

        }

        binding.btnMembership.setOnClickListener {
            findNavController().navigate(R.id.membershipFragment)
            //startActivity(Intent(requireActivity(), HomeNewActivity::class.java))

        }

        binding.startBtn.setOnClickListener {
            findNavController().navigate(R.id.startMapRideFragment)

        }
        binding.txtNoOfLog.setOnClickListener {
            findNavController().navigate(R.id.rideLogsFragment)

        }

        fetchResponse()
        clubViewModel.userDashBoardLiveData.observe(viewLifecycleOwner, Observer { it ->
            // binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    sharedViewModel.feedListResponse.value = it.data

                    val data = it.data?.data


                    binding.txtIdNoOfLog.text = (data?.size ?: 0).toString()



                    binding.txtIdTotalDistance.text = String.format("%.2f",
                        Helper.m2Km(data?.sumOf {
                            it.distance

                        })) + " km"

                    val totalSeconds = data?.sumOf { it.duration } ?: 0
                    binding.txtIdTotalTime.text = dateTimeConverter.formatSecondsToHMS(totalSeconds)

                    val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    // Parse and find the latest workout
                    val latestWorkout = data?.maxByOrNull { workout ->
                        LocalDateTime.parse(workout.startTime, formatter)
                    }

                    latestWorkout?.let {
                        it.distance
                    }
                    //binding.txtIdPreviousDistance.text = String.format("%.2f",latestWorkout?.distance)+"m"
                    binding.txtIdPreviousDistance.text = String.format("%.2f",
                        Helper.m2Km(
                            latestWorkout?.distance)) + " km"

                    /* binding.txtIdTotalTime.text = data?.sumOf {
                         it.duration
                     }.toString()*/
                    Log.d("TAG_Home_Fragment", "MSG${latestWorkout?.distance}")
                    // Filter data for the last 7 days
                    val filteredData = filterAndSortLast7Days(data)
                    if (filteredData.isNullOrEmpty()) {
                        recentFeedAdpater = RecentFeedAdpater()
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL, false)
                        binding.feedRecyclerView.adapter = recentFeedAdpater
                        recentFeedAdpater.setFeed(filteredData)
                    }else{
                        recentFeedAdpater = RecentFeedAdpater()
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL, false)
                        binding.feedRecyclerView.adapter = recentFeedAdpater
                        recentFeedAdpater.setFeed(filteredData)
                    }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    // binding.progressBar.isVisible = true
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterAndSortLast7Days(data: List<Daum>?): List<Daum> {
        val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
        val now = LocalDateTime.now()
        val sevenDaysAgo = now.minusDays(lastDaysData)

        // Filter data for the last 7 days
        val filteredData = data?.filter { workout ->
            val startDate = LocalDateTime.parse(workout.startTime, formatter)
            startDate.isAfter(sevenDaysAgo)
        } ?: emptyList()

        // Sort filtered data by date in descending order
        return filteredData.sortedByDescending { workout ->
            LocalDateTime.parse(workout.startTime, formatter)
        }
    }

    private fun fetchResponse() {
       // clubViewModel.userDashBoardRequestUser(tokenManager.getUserId().toString())
        clubViewModel.userDashBoardRequestUser(AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_ID).toString())
        //clubViewModel.userDashBoardRequestUser("38bf9f83-c07e-4ac1-9910-96a9a5f2977d")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}