package com.snofed.publicapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.snofed.publicapp.ui.clubsubmember.AboutFragment
import com.snofed.publicapp.ui.clubsubmember.ActionsFragment
import com.snofed.publicapp.ui.clubsubmember.GalleryFragment
import com.snofed.publicapp.ui.clubsubmember.SubmembersFragment

class TabClubsSubMemAdapter (private val fragment: Fragment, private val tabs: List<String>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        // Create a new fragment for each tab
        return when (position) {
            0 -> ActionsFragment()
            1 -> GalleryFragment()
            2 -> AboutFragment()
            3 -> SubmembersFragment()
            else -> ActionsFragment()
        }
    }
}