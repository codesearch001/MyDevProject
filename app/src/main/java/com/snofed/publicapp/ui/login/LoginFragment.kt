package com.snofed.publicapp.ui.login

import android.annotation.SuppressLint
import android.content.Intent
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
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.snofed.publicapp.HomeDashBoardActivity
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentLoginBinding
import com.snofed.publicapp.models.UserRecoverRequest
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.models.UserResponse
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
import com.snofed.publicapp.utils.isEmailValid
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001
    private lateinit var callbackManager: CallbackManager
    // Define a nullable property for the observer
    private var loginObserver: Observer<NetworkResult<UserResponse>>? = null

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        //bindObservers() // Move observer binding here to ensure it's ready to listen

        binding.txtSignUp.setOnClickListener {
            // it.findNavController().popBackStack()
            it.findNavController().navigate(R.id.registerFragment)
        }

        binding.forgotText.setOnClickListener {
            it.findNavController().navigate(R.id.recoverFragment)

        }

    }

    private fun init() {
        binding.btnLogin.setOnClickListener{
            if (validateFields()) {
                handleLogin() // Only attempt login if validation passes
            }
        }
    }

    // Validation logic to ensure fields are correctly filled out
    private fun validateFields(): Boolean {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        return when {
            email.isEmpty() -> {
                showToast(getString(R.string.please_enter_email_id))
                false
            }
            !email.isEmailValid() -> {
                showToast(getString(R.string.please_enter_valid_email_id))
                false
            }
            password.isEmpty() -> {
                showToast(getString(R.string.t_h_enter_your_password)) // Empty password message
                false
            }
            password.length < 6 -> {
                showToast(getString(R.string.password_must_be_6_chars)) // Password length message
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
        val password = binding.passwordEditText.text.toString().trim()

        binding.progressBar.isVisible = true
        // Initialize and register the observer
        loginObserver = createLoginObserver()
        loginObserver?.let { observer ->
            authViewModel.userResponseLiveData.observe(viewLifecycleOwner, observer)
        }

        authViewModel.loginUser(
            userRequest = UserRequest(
                email = email,
                password = password,
                rememberMe = true
            )
        )

    }

    // Method to create the observer
    private fun createLoginObserver(): Observer<NetworkResult<UserResponse>> {
        return Observer { response ->
            binding.progressBar.isVisible = false
            when (response) {
                is NetworkResult.Success -> {
                    Toast.makeText(requireActivity(), response.data?.success.toString(), Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), HomeDashBoardActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()

                    // Remove the observer after handling the result
                    authViewModel.userResponseLiveData.removeObserver(loginObserver!!)
                    loginObserver = null
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), response.message.toString(), Toast.LENGTH_SHORT).show()
                    authViewModel.userResponseLiveData.removeObserver(loginObserver!!)
                    loginObserver = null
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    /*  private fun bindObservers() {
          authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
              binding.progressBar.isVisible = false
              when (it) {
                  is NetworkResult.Success -> {

                      println("loginSuccess... " + it.data.toString())
                      Toast.makeText(requireActivity(), it.data?.success.toString(), Toast.LENGTH_SHORT).show()

                      val intent = Intent(requireActivity(), HomeDashBoardActivity::class.java)
                      intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                      startActivity(intent)

                      // Optionally, finish the current activity to remove it from the back stack
                      requireActivity().finish()
                  }
                  is NetworkResult.Error -> {

                      Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()

                      println("login-response... " + it.message.toString())
                  }
                  is NetworkResult.Loading ->{
                      binding.progressBar.isVisible = true
                  }
              }
          })
      }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}