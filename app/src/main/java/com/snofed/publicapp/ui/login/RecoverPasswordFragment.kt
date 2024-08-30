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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecoverPasswordFragment : Fragment() {
    private var _binding: FragmentRecoverPasswordBinding? = null
    private val binding get() = _binding!!

    //private val authViewModel by activityViewModels<AuthViewModel>()
    private val authViewModel by viewModels<AuthViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRecoverPasswordBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.cancelText.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.sendButton.setOnClickListener {
            Helper.hideKeyboard(it)
            val validationResult = validateUserInput()
            if (validationResult.first) {
                val userRequest = getUserRequest()
                authViewModel.recoverPassword(userRequest)
            } else {
                showValidationErrors(validationResult.second)
            }
        }
        bindObservers()
    }

    private fun getUserRequest(): UserRecoverRequest {
        return binding.run {
            UserRecoverRequest(
                emailEditText.text.toString())
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun showValidationErrors(error: String) {
        Toast.makeText(requireActivity(), String.format(resources.getString(R.string.txt_error_message, error)), Toast.LENGTH_SHORT).show()
        //binding.txtError.text = String.format(resources.getString(R.string.txt_error_message, error))
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val emailAddress = binding.emailEditText.text.toString()
        return authViewModel.validateEmailForPasswordReset(emailAddress)
    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    // tokenManager.saveToken(it.data!!.token)
                    println("RecoverSuccess... " + it.data.toString())
                    Toast.makeText(requireActivity(), "Link is sent to your Email address.\n" +
                            "Please follow the instructions in the email to access your account.", Toast.LENGTH_LONG).show()

                    findNavController().navigate(R.id.action_recoverFragment_to_loginFragment)
                   /* val intent = Intent(requireActivity(), HomeDashBoardActivity::class.java)
                    startActivity(intent)*/
                }
                is NetworkResult.Error -> {
                    // showValidationErrors(it.message.toString())

                    Toast.makeText(requireActivity(), "User Does Not Exist", Toast.LENGTH_SHORT).show()

                    println("RecoverSuccess-response... " + it.message.toString())
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