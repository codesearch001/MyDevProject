package com.snofed.publicapp.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentRegisterBinding
import com.snofed.publicapp.models.UserRegRequest
import com.snofed.publicapp.models.UserResponse
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()
    // Define a nullable property for the observer
    private var loginObserver: Observer<NetworkResult<UserResponse>>? = null

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        
        binding.txtLogin.setOnClickListener {
            it.findNavController().navigate(R.id.loginFragment)
        }
    }

    private fun init() {
        binding.btnSignUp.setOnClickListener{
            if (validateSignUpFields()) {
                handleLogin() // Only attempt login if validation passes
            }
        }
    }

    private fun handleLogin() {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        binding.progressBar.isVisible = true

        // Initialize and register the observer
        loginObserver = createSignUpObserver()
        loginObserver?.let { observer ->
            authViewModel.userResponseLiveData.observe(viewLifecycleOwner, observer)
        }

        authViewModel.registerUser(
            userRequest = UserRegRequest(
                firstName = name,
                email = email,
                password = password,
                lastName = ""
            )
        )
    }

    private fun validateSignUpFields(): Boolean {
        val name = binding.nameEditText.text.toString().trim()
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val repeatPassword = binding.repeatPasswordEditText.text.toString().trim()

        return when {
            // Check if name is empty
            name.isEmpty() -> {
                showToast(getString(R.string.t_h_enter_your_name))
                false
            }
            // Check if email is empty
            email.isEmpty() -> {
                showToast(getString(R.string.t_h_enter_your_email))
                false
            }
            // Check if email is valid
            !email.isEmailValid() -> {
                showToast(getString(R.string.please_enter_valid_email_id))
                false
            }
            // Check if password is empty
            password.isEmpty() -> {
                showToast(getString(R.string.t_h_enter_your_new_password)) // Empty password message
                false
            }
            // Check if password is less than 6 characters
            password.length < 8 -> {
                showToast(getString(R.string.password_must_be_8_chars)) // Password length message
                false
            }
            // Check if repeat password matches password
            repeatPassword.isEmpty() -> {
                showToast(getString(R.string.t_h_enter_your_re_password)) // Empty repeat password
                false
            }
            password != repeatPassword -> {
                showToast(getString(R.string.t_h_passwords_do_not_match)) // Password mismatch message
                false
            }
            else -> true // All validations passed
        }
    }

    // Helper method for email validation
    private fun String.isEmailValid(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    // Helper method to display toast messages
    private fun showToast(message: String) {
        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show()
    }


    // Method to create the observer
    private fun createSignUpObserver(): Observer<NetworkResult<UserResponse>> {
        return Observer { response ->
            binding.progressBar.isVisible = false
            when (response) {
                is NetworkResult.Success -> {

                    Toast.makeText(requireActivity(), response.data!!.message, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.loginFragment)
                    // Remove the observer after handling the result
                    authViewModel.userResponseLiveData.removeObserver(loginObserver!!)
                    loginObserver = null
                }
                is NetworkResult.Error -> {

                    Toast.makeText(requireActivity(), response.message.toString(), Toast.LENGTH_SHORT).show()
                    println("res-response... " + response.message.toString())
                    authViewModel.userResponseLiveData.removeObserver(loginObserver!!)
                    loginObserver = null
                }
                is NetworkResult.Loading ->{
                    binding.progressBar.isVisible = true
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}