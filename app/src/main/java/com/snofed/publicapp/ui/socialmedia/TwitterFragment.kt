package com.snofed.publicapp.ui.socialmedia

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.snofed.publicapp.databinding.FragmentTwitterBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TwitterFragment : Fragment() {
    private var _binding: FragmentTwitterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_twitter, container, false)
        _binding = FragmentTwitterBinding.inflate(inflater, container, false)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        return binding.root
    }

}