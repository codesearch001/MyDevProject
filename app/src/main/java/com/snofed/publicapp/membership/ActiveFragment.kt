package com.snofed.publicapp.ui.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentActvieBinding
import com.snofed.publicapp.databinding.FragmentMembershipBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ActiveFragment : Fragment() {
    private var _binding: FragmentActvieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentActvieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardid.setOnClickListener {
            it.findNavController().navigate(R.id.supportingMemberFragment)
        }



    }
}