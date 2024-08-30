package com.snofed.publicapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.snofed.publicapp.ui.trailsstatus.TrailsStatusListViewFragment
import com.snofed.publicapp.ui.trailsstatus.TrailsStatusTileViewFragment

class TabTrailsTileStatusAdapter(private val fragment: Fragment, private val tabs: List<String>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        // Create a new fragment for each tab
        return when (position) {
            0 -> TrailsStatusTileViewFragment()
            1 -> TrailsStatusListViewFragment()
            else -> TrailsStatusTileViewFragment()
        }
    }
}
