package com.snofed.publicapp.ui.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.snofed.publicapp.HomeDashBoardActivity
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentRecoverPasswordBinding
import com.snofed.publicapp.models.UserRecoverRequest
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.isEmailValid
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RecoverPasswordFragment : Fragment() {
    private var _binding: FragmentRecoverPasswordBinding? = null
    private val binding get() = _binding!!

    //private val authViewModel by activityViewModels<AuthViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecoverPasswordBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        bindObservers()

        binding.cancelText.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    private fun init() {
        binding.sendButton.setOnClickListener{
            if (validateFields()) {
                handleLogin() // Only attempt login if validation passes
            }
        }
    }

    // Validation logic to ensure fields are correctly filled out
    private fun validateFields(): Boolean {
        val email = binding.emailEditText.text.toString().trim()

        return when {
            email.isEmpty() -> {
                showToast(getString(R.string.please_enter_email_id))
                false
            }
            !email.isEmailValid() -> {
                showToast(getString(R.string.please_enter_valid_email_id))
                false
            }
            else -> true // Validation passed
        }
    }

    // Helper method to display toast messages
    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }
    // Handle login process after validation
    private fun handleLogin() {
        val email = binding.emailEditText.text.toString().trim()

        binding.progressBar.isVisible = true
        authViewModel.recoverPassword(
            userRequest = UserRecoverRequest(
                email = email)
        )
    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {

                    val message = getString(R.string.link_sent_email) + "\n" + getString(R.string.email_follow_instruction)
                    Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()

                    findNavController().popBackStack()

                }
                is NetworkResult.Error -> {

                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading ->{
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}