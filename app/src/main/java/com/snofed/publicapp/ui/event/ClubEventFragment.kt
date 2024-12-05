package com.snofed.publicapp.ui.event

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.EventClubFeedAdapter
import com.snofed.publicapp.adapter.EventFeedAdapter
import com.snofed.publicapp.databinding.FragmentClubEventBinding
import com.snofed.publicapp.databinding.FragmentEventBinding
import com.snofed.publicapp.models.events.EventResponseList
import com.snofed.publicapp.models.realmModels.Event
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class ClubEventFragment : Fragment() , EventClubFeedAdapter.OnItemClickListener {
    private var _binding: FragmentClubEventBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var feedAdapter: EventClubFeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentClubEventBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Handle back press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Check if there's any fragment in the back stack
                if (requireFragmentManager().backStackEntryCount > 0) {
                    // Pop the fragment from the back stack
                    requireFragmentManager().popBackStack()
                } else {
                    // If no fragments in the back stack, you can exit the activity or perform another action
                    // For example, exit the app:
                    requireActivity().finish()
                    // Or handle navigation to a specific fragment or screen
                    // findNavController().navigate(R.id.someOtherFragment)
                }
            }
        })
         // Initialize RecyclerView and Adapter
       feedAdapter = EventClubFeedAdapter(emptyList(),this)
       binding.eventRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
       binding.eventRecyclerView.adapter = feedAdapter


        //Apply search Filter
        binding.editTextClubSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                feedAdapter.getFilter().filter(s)
            }

            override fun afterTextChanged(s: Editable?) {
                // Trigger filter on the adapter based on the updated text
                feedAdapter.getFilter().filter(s)

            }
        })

        // Observe the SharedViewModel for data updates
       sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner) { response ->
           val events = response?.data?.events ?: emptyList()

           val activeEvents = filterActiveEvents(events?: emptyList()).sortedBy {
               it.startDate
           }

           Log.d("Tag_Events", "EventsSize: ${activeEvents.size}")

           if (activeEvents.isEmpty()) {

               // Show the "Data Not data" message and hide RecyclerView
               binding.tvSplashText.visibility = View.VISIBLE
               binding.eventRecyclerView.visibility = View.GONE

           } else {

               // Hide the "No data" message and show RecyclerView
               binding.tvSplashText.visibility = View.GONE
               binding.eventRecyclerView.visibility = View.VISIBLE
               feedAdapter.setEvent(activeEvents)
           }
       }
   }

    fun filterActiveEvents(events: List<Event>): List<Event> {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()

        return events.filter { event ->
            val eventEndDate = dateFormat.parse(event.endDate!!)
            eventEndDate?.after(currentDate) == true
        }
    }

    override fun onItemClick(eventId: String) {
        //Log.i("Club","Id " + clientId )
        val bundle = Bundle()
        bundle.putString("eventId", eventId)
        val destination = R.id.singleEventDetailsFragment
        findNavController().navigate(destination, bundle)
    }

}