package com.snofed.publicapp.ui.resortsinglectivities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snofed.publicapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleResortsActivitiesFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_single_resorts_activities, container, false)
    }

}