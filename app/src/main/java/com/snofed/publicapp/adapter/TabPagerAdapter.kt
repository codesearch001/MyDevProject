package com.snofed.publicapp.adapter

// TabPagerAdapter.kt
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.snofed.publicapp.ui.note.OrderHistoryFragment
import com.snofed.publicapp.ui.note.ProTrailsFragment

class TabPurchaseHistoryAdapter(
    private val fragment: Fragment,
    private val tabs: List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        // Create a new fragment for each tab
        return when (position) {
            0 -> OrderHistoryFragment()
            1 -> ProTrailsFragment()
            else -> OrderHistoryFragment()
        }
    }
}