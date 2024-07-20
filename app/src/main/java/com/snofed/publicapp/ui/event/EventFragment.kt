package com.snofed.publicapp.ui.event

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentEventBinding
import com.snofed.publicapp.databinding.FragmentSupportingMemDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFragment : Fragment() {
    private var _binding: FragmentEventBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_event, container, false)
        _binding = FragmentEventBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.eventCardId.setOnClickListener {
            it.findNavController().navigate(R.id.singleEventDetailsFragment)
        }




    }
}