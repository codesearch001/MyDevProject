package com.snofed.publicapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.snofed.publicapp.databinding.FragmentSignUpHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpHomeFragment : Fragment() {
    private var _binding: FragmentSignUpHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeSignUpButton.setOnClickListener {
            it.findNavController().navigate(R.id.action_signUpHomeFragment_to_registerFragment)
        }

        binding.loginText.setOnClickListener {
            it.findNavController().navigate(R.id.action_signUpHomeFragment_to_loginFragment)
        }
    }
}