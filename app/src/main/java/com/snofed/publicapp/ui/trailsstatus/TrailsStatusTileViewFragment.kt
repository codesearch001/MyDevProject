package com.snofed.publicapp.ui.trailsstatus

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentTrailsStatusTileViewBinding
import com.snofed.publicapp.models.browseSubClub.Trail
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.enums.PageType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrailsStatusTileViewFragment : Fragment() {
    private var _binding: FragmentTrailsStatusTileViewBinding? = null
    private val binding get() = _binding!!
    private val feedWorkoutViewModel by viewModels<AuthViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var trailsAdapter: TrailListAdapter
    private val pageType: PageType = PageType.MAP // Example page type, set as needed
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_trails_status_tile_view, container, false)
        _binding = FragmentTrailsStatusTileViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and Adapter
        //trailsAdapter = TrailListAdapter(emptyList())
        // Initialize RecyclerView and Adapter
        // Initialize RecyclerView and Adapter with both click listeners
        trailsAdapter = TrailListAdapter(emptyList(), { clickedTrail ->
            showTrailDetails(clickedTrail)
        }, { clickedTrail ->
            // Handle map click
            showTrailMap(clickedTrail)
        }, pageType)


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = trailsAdapter

        // Observe the SharedViewModel for data updates
        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner) { response ->
          /*  response?.data?.trails?.let { trails ->
                Log.d("Tag_Trails", "TrailsList: ${trails.size}")
                trailsAdapter.updateTrails(trails) // Update the adapter with new data
            }*/

            val trail = response?.data?.trails ?: emptyList()

            Log.d("Tag_Trails", "TrailsSize: ${trail.size}")

            if (trail.isEmpty()) {

                // Show the "Data Not data" message and hide RecyclerView
                binding.tvSplashText.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE

            } else {

                // Hide the "No data" message and show RecyclerView
                binding.tvSplashText.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                trailsAdapter.updateTrails(trail) // Update the adapter with new data
            }
        }
    }

    private fun showTrailMap(clickedTrail: Trail) {
        val bundle = Bundle().apply {
            putString("trailId", clickedTrail.id)
            putParcelable("pageType", pageType) // Use putParcelable
        }
        val destination = R.id.resortTrailStatusMapFragment
        findNavController().navigate(destination,bundle)
    }

    private fun showTrailDetails(clickedTrail: Trail) {
        val bundle = Bundle().apply {
            putString("trailId", clickedTrail.id)
            putParcelable("pageType", pageType) // Use putParcelable
        }
        val destination = R.id.resortSingleTrailsStatusDetailsFragment
        findNavController().navigate(destination,bundle)
    }
}