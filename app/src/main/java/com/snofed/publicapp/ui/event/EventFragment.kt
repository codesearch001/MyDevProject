package com.snofed.publicapp.ui.event

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.EventFeedAdapter
import com.snofed.publicapp.adapter.WorkoutFeedAdapter
import com.snofed.publicapp.databinding.FragmentEventBinding
import com.snofed.publicapp.databinding.FragmentFeedBinding
import com.snofed.publicapp.databinding.FragmentSupportingMemDetailsBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EventFragment : Fragment(),EventFeedAdapter.OnItemClickListener {

    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!
    private val eventViewModel by viewModels<AuthViewModel>()

    private lateinit var feedAdapter: EventFeedAdapter
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

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        fetchResponse()
        eventViewModel.eventLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    Log.i("Event", "Event " + it.data?.data)
                    feedAdapter = EventFeedAdapter(this)
                    binding.eventRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                    binding.eventRecyclerView.adapter = feedAdapter
                    feedAdapter.setEvent(it.data?.data)

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