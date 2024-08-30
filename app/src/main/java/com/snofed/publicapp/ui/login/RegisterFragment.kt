package com.snofed.publicapp.ui.login

import android.annotation.SuppressLint
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
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.utils.Helper.Companion.hideKeyboard
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
       /* if (tokenManager.getToken() != null) {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }*/
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtLogin.setOnClickListener {
            it.findNavController().navigate(R.id.loginFragment)
        }
        binding.btnSignUp.setOnClickListener {
            hideKeyboard(it)
            val validationResult = validateUserInput()
            if (validationResult.first) {
                val userRequest = getUserRequest()
                authViewModel.registerUser(userRequest)
            } else {
                showValidationErrors(validationResult.second)
            }
        }
        bindObservers()
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userName = binding.txtUsername.text.toString()
        val emailAddress = binding.txtEmail.text.toString()

        val password = binding.txtPassword.text.toString()
        val retpassword = binding.txtRePassword.text.toString()
        return authViewModel.validateCredentials(userName, emailAddress,  password, retpassword,false)
    }

    @SuppressLint("StringFormatMatches")
    private fun showValidationErrors(error: String) {
        binding.txtError.text = String.format(resources.getString(R.string.txt_error_message, error))
    }


    private fun getUserRequest(): UserRegRequest {
        return binding.run {
            UserRegRequest(
                txtUsername.text.toString(),
                txtEmail.text.toString(),
                txtPassword.text.toString(),
                txtRePassword.text.toString(),

                )
        }
    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    // tokenManager.saveToken(it.data!!.token)
                    // Toast.makeText(requireActivity(), it.data!!.token, Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.loginFragment)
                }
                is NetworkResult.Error -> {
                    //showValidationErrors(it.message.toString())
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    println("res-response... " + it.message.toString())
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