package com.snofed.publicapp.ui.feedback

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentSingleResortReportProblemBinding
import com.snofed.publicapp.databinding.FragmentSingleResortReportProblemChooseLocationBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.isEmailValid
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleResortReportProblemFragment : Fragment() {
    private var _binding: FragmentSingleResortReportProblemBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    private var categoryID: String? = null
    private var categoryName: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_single_resort_report_problem, container, false)
        _binding = FragmentSingleResortReportProblemBinding.inflate(inflater, container, false)
        categoryID = arguments?.getString("CATEGORY_ID").toString()
        categoryName= arguments?.getString("CATEGORY_NAME").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding.txtReportProblem.text = categoryName
        init()
    }

    private fun init() {
        binding.chooseLocationBtn.setOnClickListener {
            if (validateFields()) {
                handleLogin() // Only attempt login if validation passes
            }
        }
    }
    private fun validateFields(): Boolean {
        val edtReportProblem = binding.edtReportProblem.text.toString().trim()
        val etFirstName = binding.etFirstName.text.toString().trim()
        val etLastName = binding.etLastName.text.toString().trim()
        val etMobileNumber = binding.etMobileNumber.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()

        return when {
            edtReportProblem.isEmpty() -> {
                showToast(getString(R.string.t_please_enter_description))
                false
            }

            etFirstName.isEmpty() -> {
                showToast(getString(R.string.t_please_enter_first_name))
                false
            }
            etLastName.isEmpty() -> {
                showToast(getString(R.string.t_please_enter_last_name))
                false
            }
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

    private fun handleLogin() {
        var edtReportProblem = binding.edtReportProblem.text.toString().trim()
        val etFirstName = binding.etFirstName.text.toString().trim()
        val etLastName = binding.etLastName.text.toString().trim()
        val etMobileNumber = binding.etMobileNumber.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        // Create a Bundle to send data to the next fragment
        val bundle = Bundle().apply {
            putString("description", edtReportProblem) // Add the description to the bundle
            putString("CATEGORY_ID", categoryID) // Add the description to the bundle
            putString("CATEGORY_F_NAME", etFirstName) // Add the description to the bundle
            putString("CATEGORY_L_NAME", etLastName) // Add the description to the bundle
            putString("CATEGORY_M_NUMBER", etMobileNumber) // Add the description to the bundle
            putString("CATEGORY_EMAIL_ID", email) // Add the description to the bundle
        }
        edtReportProblem = ""

        // Navigate to the next fragment, passing the data
        findNavController().navigate(R.id.singleResortReportProblemChooseLocationFragment, bundle)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}