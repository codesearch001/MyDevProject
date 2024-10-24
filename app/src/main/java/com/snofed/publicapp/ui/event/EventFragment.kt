package com.snofed.publicapp.ui.event

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.EventFeedAdapter
import com.snofed.publicapp.databinding.FragmentEventBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment : Fragment(),EventFeedAdapter.OnItemClickListener {
    var lastName: String? = null
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private val eventViewModel by viewModels<AuthViewModel>()
    private lateinit var feedAdapter: EventFeedAdapter
    val currentDate = LocalDate.now() // Get current date
    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_event, container, false)
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Handle back press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        })
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        fetchResponse()
        eventViewModel.eventLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    // Filter the events to find those where the endDate matches the current date
                    val filteredEvents = it.data?.data?.filter { event ->
                        val eventEndDate = LocalDate.parse(event.endDate.substring(0, 10)) // Extract "yyyy-MM-dd" from endDate
                        eventEndDate == currentDate
                    }
                    Log.i("Event", "Event " + it.data?.data)
                    feedAdapter = EventFeedAdapter(this)
                    binding.eventRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                    binding.eventRecyclerView.adapter = feedAdapter
                    feedAdapter.setEvent(filteredEvents ?: emptyList())


                    // Check if there are no filtered events
                    if (filteredEvents.isNullOrEmpty()) {
                        // Show the "not found" text
                        binding.tvSplashText.visibility = View.VISIBLE
                    } else {
                        // Hide the "not found" text
                        binding.tvSplashText.visibility = View.GONE
                    }
                }

                is NetworkResult.Error -> {

                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }
       /* // Initialize RecyclerView and Adapter
        feedAdapter = EventFeedAdapter(this)
        binding.eventRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.eventRecyclerView.adapter = feedAdapter


        // Observe the SharedViewModel for data updates
        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner) { response ->
            val events = response?.data?.events ?: emptyList()

            Log.d("Tag_Events", "EventsSize: ${events.size}")

            if (events.isEmpty()) {

                // Show the "Data Not data" message and hide RecyclerView
                binding.tvSplashText.visibility = View.VISIBLE
                binding.eventRecyclerView.visibility = View.GONE

            } else {

                // Hide the "No data" message and show RecyclerView
                binding.tvSplashText.visibility = View.GONE
                binding.eventRecyclerView.visibility = View.VISIBLE
                feedAdapter.setEvent(events)
            }
        }
    }*/

    private fun fetchResponse() {
        eventViewModel.eventRequestUser()
    }
    override fun onItemClick(eventId: String) {
        //Log.i("Club","Id " + clientId )
        val bundle = Bundle()
        bundle.putString("eventId", eventId)
        val destination = R.id.singleEventDetailsFragment
        findNavController().navigate(destination, bundle)
    }
}