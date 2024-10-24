package com.snofed.publicapp.ui.trailsstatus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.TabPurchaseHistoryAdapter
import com.snofed.publicapp.adapter.TabTrailsTileStatusAdapter
import com.snofed.publicapp.databinding.FragmentPurchaseHistoryBinding
import com.snofed.publicapp.databinding.FragmentTrailsStatusBinding
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrailsStatusFragment : Fragment() {
    lateinit var _binding: FragmentTrailsStatusBinding
    private val binding get() = _binding!!
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_trails_status, container, false)
        _binding = FragmentTrailsStatusBinding.inflate(inflater, container, false)

        //Initialize the tab layout and ViewPager
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        // Create a list of tabs
        val tabs = listOf(resources.getString(R.string.t_tile_view), resources.getString(R.string.t_list_view))
        // val tabs = listOf("Tile view")

        // Create a ViewPager adapter
        val adapter = TabTrailsTileStatusAdapter(this, tabs)
        viewPager.adapter = adapter

        // Set up the tab layout with the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Observe the SharedViewModel for data updates
        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner) { response ->
            response?.data?.trails.let {
                binding.txtTotalTrails.text = response.data.totalTrails.toString()
                //binding.txtTotalLength.text = response.data.totalTrailsLength.toString()
                binding.txtTotalLength.text = Helper.m2Km(response.data.totalTrailsLength?.toDouble()).toString() + " km"

            }
        }
    }
}
