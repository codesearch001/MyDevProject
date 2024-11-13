package com.snofed.publicapp.ui.dashboardFragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentBrowseAllClubBinding
import com.snofed.publicapp.databinding.FragmentBrowseFavBinding
import com.snofed.publicapp.utils.ClientPrefrences
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BrowseFavFragment : Fragment() {
    private var _binding: FragmentBrowseFavBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_browse_fav, container, false)
        _binding = FragmentBrowseFavBinding.inflate(inflater, container, false)

        val getAllClientIds = ClientPrefrences.getClientIds(requireContext())
        Log.e("BrowseFavFragment","getALLIDS " + getAllClientIds)
        return binding.root
    }
}