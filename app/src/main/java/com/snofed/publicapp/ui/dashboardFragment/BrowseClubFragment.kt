package com.snofed.publicapp.ui.dashboardFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.snofed.publicapp.HomeDashBoardActivity
import com.snofed.publicapp.adapter.TabBrowseClubsAdapter
import com.snofed.publicapp.databinding.FragmentBrowseClubBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.DrawerController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrowseClubFragment : Fragment() {

    private var _binding: FragmentBrowseClubBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_browse_clubs, container, false)
        _binding = FragmentBrowseClubBinding.inflate(inflater, container, false)
        binding.humburger.setOnClickListener {
            (activity as? DrawerController)?.openDrawer()
        }
        // Initialize the tab layout and ViewPager
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        // Create a list of tabs
        /*val tabs = listOf("All", "By Activities", "Favourites")*/
        val tabs = listOf("All","Favourites")

        // Create a ViewPager adapter
        val adapter = TabBrowseClubsAdapter(this, tabs)
        viewPager.adapter = adapter

        // Set up the tab layout with the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
        return binding.root
    }
}
