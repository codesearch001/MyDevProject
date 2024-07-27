package com.snofed.publicapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.snofed.publicapp.ui.dashboardFragment.BrowseAllClubFragment
import com.snofed.publicapp.ui.dashboardFragment.BrowseByActivitiesFragment
import com.snofed.publicapp.ui.dashboardFragment.BrowseFavFragment

class TabBrowseClubsAdapter(private val fragment: Fragment, private val tabs: List<String>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        // Create a new fragment for each tab
        return when (position) {
            0 -> BrowseAllClubFragment()
            /*1 -> BrowseByActivitiesFragment()*/
            1 -> BrowseFavFragment()
            else -> BrowseAllClubFragment()
        }


    }
}