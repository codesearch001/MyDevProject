package com.snofed.publicapp.ui.clubsubmember

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentBrowseClubBinding
import com.snofed.publicapp.databinding.FragmentSubmembersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubmembersFragment : Fragment() {
    private var _binding: FragmentSubmembersBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_submembers, container, false)

    }


}