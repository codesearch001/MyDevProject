package com.snofed.publicapp.ui.resortsinglectivities

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentClubSubMembersBinding
import com.snofed.publicapp.databinding.FragmentSingleResortsActivitiesBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleResortsActivitiesFragment : Fragment() {

    private var _binding: FragmentSingleResortsActivitiesBinding? = null
    private val binding get() = _binding!!
    private val activitiesViewModel by viewModels<AuthViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_single_resorts_activities, container, false)
        _binding = FragmentSingleResortsActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }

}