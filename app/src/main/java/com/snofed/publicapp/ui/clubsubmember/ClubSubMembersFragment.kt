package com.snofed.publicapp.ui.clubsubmember

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.TabBrowseClubsAdapter
import com.snofed.publicapp.adapter.TabClubsSubMemAdapter
import com.snofed.publicapp.databinding.FragmentClubSubMembersBinding
import com.snofed.publicapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubSubMembersFragment : Fragment() {

    private var _binding: FragmentClubSubMembersBinding? = null
    private val binding get() = _binding!!
    //  private val noteViewModel by viewModels<NoteViewModel>()

    // private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentClubSubMembersBinding.inflate(inflater, container, false)
        // Initialize the tab layout and ViewPager
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        // Create a list of tabs
        val tabs = listOf("Action", "Gallery", "About", "Submembers")

        // Create a ViewPager adapter
        val adapter = TabClubsSubMemAdapter(this, tabs)
        viewPager.adapter = adapter

        // Set up the tab layout with the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}