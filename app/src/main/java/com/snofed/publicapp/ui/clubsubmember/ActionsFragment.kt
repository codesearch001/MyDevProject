package com.snofed.publicapp.ui.clubsubmember

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentActionsBinding
import com.snofed.publicapp.databinding.FragmentClubSubMembersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActionsFragment : Fragment() {
    private var _binding: FragmentActionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_actions, container, false)
        _binding = FragmentActionsBinding.inflate(inflater, container, false)
        return binding.root
    }


}