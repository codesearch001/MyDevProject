package com.snofed.publicapp.purchasehistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.snofed.publicapp.adapter.TabPurchaseHistoryAdapter
import com.snofed.publicapp.databinding.FragmentPurchaseHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseHistoryFragment : Fragment() {
    lateinit var _binding: FragmentPurchaseHistoryBinding
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_purchase_history, container, false)
        _binding = FragmentPurchaseHistoryBinding.inflate(inflater, container, false)

         //Initialize the tab layout and ViewPager
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        // Create a list of tabs
        val tabs = listOf("Orders", "Pro Trails")

        // Create a ViewPager adapter
        val adapter = TabPurchaseHistoryAdapter(this, tabs)
        viewPager.adapter = adapter

        // Set up the tab layout with the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        return binding.root
//        val view = binding.root
//        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

    }

}