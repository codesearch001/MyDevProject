package com.snofed.publicapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.snofed.publicapp.ui.setting.AppSettingFragment
import com.snofed.publicapp.ui.setting.ProfileSettingFragment

class TabSettingAdapter(private val fragment: Fragment, private val tabs: List<String>) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        // Create a new fragment for each tab
        return when (position) {
            0 -> AppSettingFragment()
            1 -> ProfileSettingFragment()

            else -> AppSettingFragment()
        }


    }
}