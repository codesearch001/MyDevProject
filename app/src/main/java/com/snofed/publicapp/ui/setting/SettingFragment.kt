package com.snofed.publicapp.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.snofed.publicapp.adapter.TabSettingAdapter
import com.snofed.publicapp.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_setting, container, false)
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        // Initialize the tab layout and ViewPager
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        // Create a list of tabs
        val tabs = listOf("App Settings", "Profile Settings")

        // Create a ViewPager adapter
        val adapter = TabSettingAdapter(this, tabs)
        viewPager.adapter = adapter

        // Set up the tab layout with the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

    }

}