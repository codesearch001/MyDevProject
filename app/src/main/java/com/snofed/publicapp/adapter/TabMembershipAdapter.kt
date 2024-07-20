package com.snofed.publicapp.adapter

// TabPagerAdapter.kt
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.snofed.publicapp.ui.note.ActiveFragment
import com.snofed.publicapp.membership.ExpriedFragment

class TabMembershipAdapter(private val fragment: Fragment,private val tabs: List<String>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        // Create a new fragment for each tab
        return when (position) {
            0 -> ActiveFragment()
            1 -> ExpriedFragment()
            else -> ActiveFragment()
        }


    }
}