package com.snofed.publicapp.membership

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentBacomeAMemberBinding
import com.snofed.publicapp.membership.dto.MembershipOrderDto
import com.snofed.publicapp.models.membership.Membership
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SnofedUtils
import com.snofed.publicapp.utils.TokenManager
import com.snofed.publicapp.utils.isEmailValid
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BacomeAMemberFragment : Fragment() {

    private var _binding: FragmentBacomeAMemberBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    var clientId: String = ""
    var useId: String = ""
    var membershipId: String = ""
    var membershipName: String = ""

    lateinit var membership: Membership

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_bacome_a_member, container, false)
        _binding = FragmentBacomeAMemberBinding.inflate(inflater, container, false)
        clientId = arguments?.getString("clientId").toString()
        membershipId = arguments?.getString("membershipId").toString()
        membershipName = arguments?.getString("membershipName").toString()
        useId = tokenManager.getUserId().toString()

        Log.i("BuyMembershipFragment" , "clientId " + clientId)
        Log.i("BuyMembershipFragment" , "useId " + useId)
        Log.i("BuyMembershipFragment" , "membershipId " + membershipId)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        binding.btnHamburger.setOnClickListener {
            it.findNavController().popBackStack()
        }


    }

    private fun init() {
        binding.txtMemberTitle.text = membershipName
        fetchResponse()
        viewModel.activeMembershipDetailsResponseLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data?.data

                    membership = data!!
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                }
            }
        })


        binding.btnPayWithSwish.setOnClickListener {
            handlePayment(true)  // Call with payment method flag for Swish

        }

        binding.btnPayWithCreditCard.setOnClickListener {

            handlePayment(false)  // Call with payment method flag for Credit Card

        }

    }

    private fun fetchResponse() {
        viewModel.getBenfitsMembership(membershipId)
    }

    private fun handlePayment(isPaymentMethodSwish: Boolean) {
        val fName = binding.etFirstName.text.toString().trim()
        val lName = binding.etLastName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        Log.e("membershipOrderDto", "membershipOrderDto $membership")

        val membershipOrderDto = MembershipOrderDto(membership,email,fName,lName,"sv",useId)

        Log.e("membershipOrderDto", "membershipOrderDto $membershipOrderDto")
            if (validateFields(fName, lName, email)) {
            buyTicket(isPaymentMethodSwish, fName, lName, email)
        }
    }

    private fun buyTicket(isPaymentMethodSwish: Boolean, firstName: String, lastName: String, email: String) {

        if (isPaymentMethodSwish) {
            initiateSwishPayment(firstName, lastName, email)
        }

       else if (SnofedUtils.isSwishAppInstalled(requireActivity())) {
            initiateCreditCardPayment(firstName, lastName, email)
        }
    }


    private fun initiateSwishPayment(firstName: String, lastName: String, email: String) {
        // Your Swish payment logic
        Toast.makeText(requireActivity(), "1", Toast.LENGTH_SHORT).show()
    }

    private fun initiateCreditCardPayment(firstName: String, lastName: String, email: String) {
        // Your credit card payment logic
        Toast.makeText(requireActivity(), "message2", Toast.LENGTH_SHORT).show()
        SnofedUtils.startSwish(requireActivity(), "token", "callbackUrl", 1)
    }



    // Validation logic to ensure fields are correctly filled out
    private fun validateFields(firstName: String, lastName: String, email: String): Boolean {
        val firstName = binding.etFirstName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        return when {
            firstName.isEmpty() -> {
                showToast(getString(R.string.t_h_enter_your_f_name))
                false
            }
            lastName.isEmpty() -> {
                showToast(getString(R.string.t_h_enter_your_l_name))
                false
            }
            email.isEmpty() -> {
                showToast(getString(R.string.t_h_enter_your_e_Mail))
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
}