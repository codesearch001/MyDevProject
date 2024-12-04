package com.snofed.publicapp.ui.feedImage

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.WorkoutFeedAdapter
import com.snofed.publicapp.databinding.FragmentMapFeedBinding
import com.snofed.publicapp.databinding.FragmentRideLogsBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.DrawerController
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.navigation.fragment.findNavController

@AndroidEntryPoint
class RideLogsFragment : Fragment(),WorkoutRideLogFeedAdapter.OnItemClickListener {

    private var _binding: FragmentRideLogsBinding? = null
    private val binding get() = _binding!!
    private val feedViewModel by viewModels<AuthViewModel>()
    private lateinit var feedAdapter: WorkoutRideLogFeedAdapter
    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_ride_logs, container, false)
        _binding = FragmentRideLogsBinding.inflate(inflater, container, false)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchResponse()
        feedViewModel.userDashBoardLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data?.data
                    if (data.isNullOrEmpty()) {
                        // Handle the "data not found" case
                        // Assuming this is inside a Fragment
                        it.data?.message?.let { message ->
                            binding.tvSplashText.apply {
                                isVisible = true
                                text = message
                            }
                        }

                        feedAdapter = WorkoutRideLogFeedAdapter(this)
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = feedAdapter
                        feedAdapter.setFeed(data)
                    } else {
                        // Normal case: data is present
                        binding.tvSplashText.isVisible = false
                        feedAdapter = WorkoutRideLogFeedAdapter(this)
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = feedAdapter
                        feedAdapter.setFeed(data)

                    }
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

    private fun fetchResponse() {
        feedViewModel.userDashBoardRequestUser(tokenManager.getUserId().toString())
    }

    override fun onItemClick(clientId: String) {
        val bundle = Bundle()
        bundle.putString("feedId", clientId)
        val destination = R.id.mapFeedFragment
        findNavController().navigate(destination, bundle)
    }
}