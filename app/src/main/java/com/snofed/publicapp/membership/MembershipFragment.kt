package com.snofed.publicapp.membership

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.TabMembershipAdapter
import com.snofed.publicapp.adapter.TabPurchaseHistoryAdapter
import com.snofed.publicapp.databinding.FragmentMembershipBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MembershipFragment : Fragment() {
    private var _binding: FragmentMembershipBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMembershipBinding.inflate(inflater, container, false)

        // Initialize the tab layout and ViewPager
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        // Create a list of tabs
        val tabs = listOf("Active", "Expired")

        // Create a ViewPager adapter
        val adapter = TabMembershipAdapter(this, tabs)
        viewPager.adapter = adapter

        // Set up the tab layout with the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnHamburger.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding.btnBuyMemberships.setOnClickListener {
            it.findNavController().navigate(R.id.buyMembershipFragment)
        }
        binding.btnActivateMemberships.setOnClickListener {
          //  it.findNavController().navigate(R.id.action_membershipFragment_to_buyMembershipFragment)
        }


    }

}