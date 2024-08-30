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
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
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


        binding.txtSignUp.setOnClickListener {
            // it.findNavController().popBackStack()
            it.findNavController().navigate(R.id.registerFragment)
        }

        binding.forgotText.setOnClickListener {
            it.findNavController().navigate(R.id.recoverFragment)
            //it.findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            /*val intent = Intent(requireActivity(), HomeDashBoardActivity::class.java)
            startActivity(intent)*/
        }

        binding.btnLogin.setOnClickListener {
            Helper.hideKeyboard(it)
            val validationResult = validateUserInput()
            if (validationResult.first) {
                val userRequest = getUserRequest()
                authViewModel.loginUser(userRequest)
            } else {
                showValidationErrors(validationResult.second)
            }
        }
        bindObservers()
    }
       /* // Initialize Facebook SDK
        FacebookSdk.sdkInitialize(requireActivity().applicationContext)
        AppEventsLogger.activateApp(requireActivity().application)

        // Initialize Facebook Callback Manager
        callbackManager = com.facebook.CallbackManager.Factory.create()

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        // Set up Google Sign-In button
        binding.googleSignInButton.setOnClickListener {
            signInWithGoogle()
        }

        // Set up custom Facebook Login button
        binding.facebookLoginButton.setOnClickListener {
            loginWithFacebook()
        }
    }

    private fun signInWithGoogle() {
        try {
            val signInIntent: Intent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        } catch (e: Exception) {
            Log.e("GoogleSignIn", "Error starting Google Sign-In: ${e.message}")
            // Handle the error (e.g., show an error message to the user)
        }
    }

    private fun loginWithFacebook() {
        try {
            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile"))
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    // Handle successful Facebook login
                    handleFacebookSignInResult(loginResult)
                }

                override fun onCancel() {
                    Log.i("FacebookLogin", "Facebook login cancelled")
                    // Handle Facebook login cancel (e.g., show a message to the user)
                }

                override fun onError(exception: FacebookException) {
                    Log.e("FacebookLogin", "Facebook login error: ${exception.message}")
                    // Handle Facebook login error (e.g., show an error message to the user)
                }
            })
        } catch (e: Exception) {
            Log.e("FacebookLogin", "Error starting Facebook Login: ${e.message}")
            // Handle the error (e.g., show an error message to the user)
        }
    }

    private fun handleFacebookSignInResult(loginResult: LoginResult) {
        // Get the access token
        val accessToken = loginResult.accessToken

        // Create a new GraphRequest
        val request = GraphRequest.newMeRequest(
            accessToken
        ) { jsonObject, response ->
            // Handle the response from the Graph API
            try {
                if (response.error != null) {
                    Log.e("FacebookGraphRequest", "GraphRequest error: ${response.error.errorMessage}")
                    // Handle GraphRequest error
                } else {
                    // Extract user data from jsonObject
                    val id = jsonObject.optString("id")
                    val name = jsonObject.optString("name")
                    val email = jsonObject.optString("email")

                    Log.i("FacebookGraphRequest", "ID: $id")
                    Log.i("FacebookGraphRequest", "Name: $name")
                    Log.i("FacebookGraphRequest", "Email: $email")

                    // Handle the user data (e.g., update UI or send it to your backend)
                }
            } catch (e: Exception) {
                Log.e("FacebookGraphRequest", "Error parsing GraphRequest response: ${e.message}")
                // Handle parsing error
            }
        }

        // Set the parameters for the GraphRequest
        val parameters = Bundle()
        parameters.putString("fields", "id,name,email")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
                handleGoogleSignInResult(account)
            } catch (e: ApiException) {
                Log.w("GoogleSignIn", "signInResult:failed code=${e.statusCode}")
                // Handle the error (e.g., show an error message to the user)
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)






    }

    private fun handleGoogleSignInResult(account: GoogleSignInAccount) {
        // Extract user information from account
        Log.i("GoogleSignIn", "User ID: ${account.id}")
        Log.i("GoogleSignIn", "Email: ${account.email}")
        Log.i("GoogleSignIn", "Display Name: ${account.displayName}")
        // You can use the user information to authenticate with your backend or update the UI
    }
*/

    private fun getUserRequest(): UserRequest {
        return binding.run {
            UserRequest(
                txtEmail.text.toString(),
                txtPassword.text.toString(),
                true)
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun showValidationErrors(error: String) {
        binding.txtError.text = String.format(resources.getString(R.string.txt_error_message, error))
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        return authViewModel.validateLoginCredentials(emailAddress,  password, true)
    }

    private fun bindObservers() {
        authViewModel.userResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    // tokenManager.saveToken(it.data!!.token)
                    println("loginSuccess... " + it.data.toString())
                    Toast.makeText(requireActivity(), it.data?.success.toString(), Toast.LENGTH_SHORT).show()
                    // findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                    val intent = Intent(requireActivity(), HomeDashBoardActivity::class.java)
                    startActivity(intent)
                }
                is NetworkResult.Error -> {
                    // showValidationErrors(it.message.toString())

                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()

                    println("login-response... " + it.message.toString())
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