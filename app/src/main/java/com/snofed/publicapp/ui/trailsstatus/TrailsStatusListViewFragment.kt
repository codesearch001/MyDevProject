package com.snofed.publicapp.ui.trailsstatus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentResortTrailStatusMapBinding
import com.snofed.publicapp.databinding.TrailsStatusListViewBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrailsStatusListViewFragment : Fragment() {
    private var _binding: TrailsStatusListViewBinding? = null
    private val binding get() = _binding!!
    private val feedWorkoutViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_trails_status_list_view, container, false)
        _binding = TrailsStatusListViewBinding.inflate(inflater, container, false)
        /*binding.trailsMaps.setOnClickListener {
            it.findNavController().navigate(R.id.resortTrailStatusMapFragment)
        }

        binding.idtrailsList.setOnClickListener {
            it.findNavController().navigate(R.id.resortSingleTrailsStatusDetailsFragment)
        }*/
        return  binding.root
    }
}