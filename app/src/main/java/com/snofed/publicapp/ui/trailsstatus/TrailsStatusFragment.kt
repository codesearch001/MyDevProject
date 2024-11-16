package com.snofed.publicapp.ui.trailsstatus

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.TabPurchaseHistoryAdapter
import com.snofed.publicapp.adapter.TabTrailsTileStatusAdapter
import com.snofed.publicapp.databinding.FragmentPurchaseHistoryBinding
import com.snofed.publicapp.databinding.FragmentTrailsStatusBinding
import com.snofed.publicapp.databinding.FragmentTrailsStatusTileViewBinding
import com.snofed.publicapp.models.browseSubClub.Trail
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.enums.PageType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrailsStatusFragment : Fragment() {

    lateinit var _binding: FragmentTrailsStatusBinding
    private val binding get() = _binding!!
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var trailsAdapter: TrailListAdapter
    private val pageType: PageType = PageType.MAP // Example page type, set as needed

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_trails_status, container, false)
        _binding = FragmentTrailsStatusBinding.inflate(inflater, container, false)

//        //Initialize the tab layout and ViewPager
//        val tabLayout = binding.tabLayout
//        val viewPager = binding.viewPager
//
//        // Create a list of tabs
//        //val tabs = listOf(resources.getString(R.string.t_tile_view), resources.getString(R.string.t_list_view))
//         val tabs = listOf("Tile view")
//
//        // Create a ViewPager adapter
//        val adapter = TabTrailsTileStatusAdapter(this, tabs)
//        viewPager.adapter = adapter
//
//
//        // Set up the tab layout with the ViewPager
//        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
//            tab.text = tabs[position]
//        }.attach()
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
//        // Hide TabLayout
//        binding.tabLayout.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Observe the SharedViewModel for data updates
//        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner) { response ->
//            response?.data?.trails.let {
//                binding.txtTotalTrails.text = response.data.totalTrails.toString()
//                //binding.txtTotalLength.text = response.data.totalTrailsLength.toString()
//                binding.txtTotalLength.text =
//                    Helper.m2Km(response.data.totalTrailsLength?.toDouble()).toString() + " km"
//
//            }
//        }

        trailsAdapter = TrailListAdapter(emptyList(), { clickedTrail ->
            showTrailDetails(clickedTrail)
        }, { clickedTrail ->
            // Handle map click
            showTrailMap(clickedTrail)
        }, { upgradeClick ->
            upgradeProTrails(upgradeClick)
        }, pageType)


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = trailsAdapter

        // Observe the SharedViewModel for data updates
        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner) { response ->
            response?.data?.trails.let {
                binding.txtTotalTrails.text = response.data.totalTrails.toString()
                //binding.txtTotalLength.text = response.data.totalTrailsLength.toString()
                binding.txtTotalLength.text =
                    Helper.m2Km(response.data.totalTrailsLength?.toDouble()).toString() + " km"

            }

            val trail = response?.data?.trails ?: emptyList()

            Log.d("Tag_Trails", "TrailsSize: ${trail}")

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

            //Apply search Filter
            binding.editTextClubSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    trailsAdapter.getFilter().filter(s)
                }

                override fun afterTextChanged(s: Editable?) {
                    // Trigger filter on the adapter based on the updated text
                    trailsAdapter.getFilter().filter(s)

                }
            })
        }
    }

    private fun upgradeProTrails(upgradeClick: Trail) {
        val bundle = Bundle().apply {
            putString("trailId", upgradeClick.id)
            putParcelable("pageType", pageType) // Use putParcelable
        }
        val destination = R.id.newTicketFragment
        findNavController().navigate(destination, bundle)
    }

    private fun showTrailMap(clickedTrail: Trail) {
        val bundle = Bundle().apply {
            putString("trailId", clickedTrail.id)
            putParcelable("pageType", pageType) // Use putParcelable
        }
        val destination = R.id.resortTrailStatusMapFragment
        findNavController().navigate(destination, bundle)
    }

    private fun showTrailDetails(clickedTrail: Trail) {
        val bundle = Bundle().apply {
            putString("trailId", clickedTrail.id)
            putParcelable("pageType", pageType) // Use putParcelable
        }
        val destination = R.id.resortSingleTrailsStatusDetailsFragment
        findNavController().navigate(destination, bundle)
    }
}

