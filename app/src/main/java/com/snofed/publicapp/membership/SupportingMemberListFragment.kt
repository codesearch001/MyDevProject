package com.snofed.publicapp.membership

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentMembershipBinding
import com.snofed.publicapp.databinding.FragmentSupportingMemberBinding
import com.snofed.publicapp.databinding.FragmentSupportingMemberListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupportingMemberListFragment : Fragment() {

    private var _binding: FragmentSupportingMemberListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSupportingMemberListBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnHamburger.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding.card1.setOnClickListener {
            it.findNavController().navigate(R.id.supportingMemDetailsFragment)
        }
  binding.card2.setOnClickListener {
            it.findNavController().navigate(R.id.supportingMemDetailsFragment)
        }

        binding.card3.setOnClickListener {
            it.findNavController().navigate(R.id.supportingMemDetailsFragment)
        }



    }
}