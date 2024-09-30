package com.snofed.publicapp.ui.newticket


import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentNewTicketBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.ui.order.model.TicketType

import com.snofed.publicapp.ui.order.ticketing.TicketDTO
import com.snofed.publicapp.ui.order.ticketing.TicketTypeDTO
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedPreferenceKeys

import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar


@AndroidEntryPoint
class NewTicketFragment : Fragment() {
    var _binding: FragmentNewTicketBinding? = null
    private val binding get() = _binding!!
    private val viewTicketModel by viewModels<AuthViewModel>()
    private lateinit var viewModel: TicketViewModel

    private var clientId: String? = null
    private var ticketTypes: List<TicketType> = emptyList() // Initialize to an empty list
    private var ticketTypeNames: List<String> = emptyList() // Initialize to an empty list
    private var ticketTypeIds: List<String> = emptyList() // Initialize to an empty list

    // Local variables to store selected ticket info
    private lateinit var selectedTicket: TicketType
    private var selectedTicketName: String = ""
    private var selectedTicketId: String = ""
    private var selectedTicketPrice: Double = 0.0
    private var ticketCategory: Int? = null // To store the passed category value
    private var isMultipleTicket: Boolean? = false // To store the passed category value
    private val MOBILE_REGEX = "^[0-9]{10}$" // Regex for a 10-digit mobile number
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // return inflater.inflate(R.layout.fragment_new_ticket, container, false)
        _binding = FragmentNewTicketBinding.inflate(inflater, container, false)
        clientId = arguments?.getString("clientId")
        Log.i("ClientId", "Id: $clientId")

        // Retrieve the passed category argument
        ticketCategory = arguments?.getInt("ticketCategory")
        isMultipleTicket = arguments?.getBoolean("isMultipleTicket")

        Log.d("NewTicketFragment", "Ticket category received: $ticketCategory")
        Log.d("NewTicketFragment", "Ticket isMultipleTicket received: $isMultipleTicket")

        viewModel = ViewModelProvider(requireActivity()).get(TicketViewModel::class.java)


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.removeAllTicket()
                findNavController().popBackStack()
            }
        })

        binding.btnCreateTicket.setOnClickListener {
            validateInputs()
        }

        return binding.root
    }

    private fun init() {
        val rememberCbChecked =
            AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.IS_CREDENTIAL_REMEMBER_CB)
        if (rememberCbChecked != "") {
            binding?.emailEditText?.setText(
                AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_EMAIL_ID))
            binding?.startDateEditText?.setText(
                AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_START_DATE)
            )
            binding?.checkboxRememberMe?.isChecked = true
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view,savedInstanceState)
        init()
        binding.backBtn.setOnClickListener {
            viewModel.removeAllTicket()
            it.findNavController().popBackStack()
        }
        fetchResponse()
        binding.startDateEditText.setOnClickListener {
            showDatePickerDialog()
        }
        viewTicketModel.getTicketTypeResponseLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    val data = result.data?.data
                    if (data != null) {
                        ticketTypes = data
                       // ticketTypeNames = data.map { it.name }
                       // ticketTypeIds = data.map { it.id }

                        // Now filter based on ticketCategory
                       /* val filteredTicketTypes = ticketTypes.filter {
                             it.ticketCategory.toInt() == ticketCategory
                        }*/

                        val filteredTicketTypes : List<TicketType>
                        if (isMultipleTicket == true){
                            filteredTicketTypes = ticketTypes.filter {
                                isMultipleTicket == true && it.ticketCategory.toInt() == ticketCategory
                            }
                        }else{
                            filteredTicketTypes = ticketTypes.filter {
                                isMultipleTicket == false && it.ticketCategory.toInt() == ticketCategory
                            }
                        }
                        // If no data matches, show a message
                        if (filteredTicketTypes.isEmpty()) {
                            Toast.makeText(requireContext(), "No tickets available for this category", Toast.LENGTH_SHORT).show()
                        } else {
                            ticketTypeNames = filteredTicketTypes.map { it.name }
                            ticketTypeIds = filteredTicketTypes.map { it.id }

                            // Set up spinner with the filtered data
                            setupSpinner(ticketTypeNames, filteredTicketTypes)
                        }
                       // setupSpinner(ticketTypeNames)

                    }
                    Log.d("NewTicketFragment", "ticketTypeNames: $ticketTypeNames")
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), result.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    // Show loading indicator if needed
                }
            }
        }
    }



    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                // Set the selected date to the EditText
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                binding.startDateEditText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }
    private fun validateEmail(email: String): Boolean {
        // Simple regex to check if email is valid
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateMobile(mobile: String): Boolean {
        return mobile.matches(MOBILE_REGEX.toRegex())
    }
    private fun setupSpinner(ticketTypeNames: List<String>, filteredTicketTypes: List<TicketType>) {
        // Create a mutable list to add the default item
        val spinnerItems = mutableListOf("Choose ticket type") // Default item
        spinnerItems.addAll(ticketTypeNames) // Add the filtered ticket type names to the list

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.ticketTypeSpinner.adapter = adapter
        binding.ticketTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                // Check if the fragment is added and view is not null
                if (isAdded && view != null) {
                    if (position == 0) {
                        // The user selected the default item; reset the selected ticket info
                        selectedTicketName = ""
                        selectedTicketId = ""
                        selectedTicketPrice = 0.0
                    } else {
                        // Get the selected ticket from the filtered list
                        // Adjust index due to default item
                        selectedTicket = filteredTicketTypes[position - 1]
                        selectedTicketName = selectedTicket.name
                        selectedTicketId = selectedTicket.id
                        selectedTicketPrice = selectedTicket.totalPrice

                        // Print the selected ticket details
                        Log.d("TicketSelection", "Selected Ticket: Name: $selectedTicketName, ID: $selectedTicketId, Price: $selectedTicketPrice")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle case when nothing is selected
            }
        }
    }

    private fun fetchResponse() {
        viewTicketModel.getTicketType(clientId.toString())
    }

    private fun validateInputs() {
        // Retrieve input values
        val startDate = binding.startDateEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val mobile = binding.mobileEditText.text.toString()

        // Retrieve input values
        val ticket = TicketDTO(
            ticketType =  TicketTypeDTO(selectedTicket.id, selectedTicket.name, selectedTicket.totalPrice),
            ticketStartDate = startDate,
            buyerFirstName = binding.firstNameEditText.text.toString(),
            buyerLastName = binding.lastNameEditText.text.toString(),
            buyerEmail = email,
            buyerMobileNumber = mobile
        )

        // Check if the selected ticket's category is valid (1 or 3)
        if (ticketCategory != 1 && ticketCategory != 3) {
            Toast.makeText(requireContext(), "Only tickets of category 1 or 3 can be added.", Toast.LENGTH_SHORT).show()
            return
        }
        // Validate ticket selection before adding to ViewModel
        if (selectedTicketName.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a ticket type.", Toast.LENGTH_SHORT).show()
            return
        }
        // Validate email
        if (!validateEmail(email)) {
            Toast.makeText(requireContext(), "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
            return
        }

     /*   // Validate mobile
        if (!validateMobile(mobile)) {
            Toast.makeText(requireContext(), "Please enter a valid mobile number (10 digits).", Toast.LENGTH_SHORT).show()
            return
        }*/

        // Validate date selection
        if (startDate.isEmpty()) {
            Toast.makeText(requireContext(), "Please select a start date.", Toast.LENGTH_SHORT).show()
            return
        }
            if (binding?.checkboxRememberMe?.isChecked == true) {
                AppPreference.savePreference(context, SharedPreferenceKeys.USER_START_DATE,
                    startDate)
                AppPreference.savePreference(context, SharedPreferenceKeys.USER_EMAIL_ID,
                    email)
                AppPreference.savePreference(context, SharedPreferenceKeys.MOBILE,
                    mobile)
                AppPreference.savePreference(context, SharedPreferenceKeys.IS_CREDENTIAL_REMEMBER_CB, "true")

            }else{

                AppPreference.savePreference(
                        context,
                        SharedPreferenceKeys.USER_EMAIL_ID,
                        ""
                    )
            }

        if (!isMultipleTicket!!){
            viewModel.removeAllTicket()
        }
        // Add the new ticket to the ViewModel
        viewModel.addTicket(ticket)

        // Further validation logic...
        Toast.makeText(requireContext(), "Ticket created successfully!", Toast.LENGTH_SHORT).show()
        // Create a Bundle to send the ticketCategory or pageType back to OrderTicketFragment
        val bundle = Bundle().apply {
            putInt("ticketCategory", ticketCategory ?: 1) // Assuming ticketCategory is stored in this fragment
            putBoolean("isMultipleTicket", isMultipleTicket!!) // Assuming ticketCategory is stored in this fragment
            putString("clientId", clientId) // Assuming ticketCategory is stored in this fragment

        }
        findNavController().navigate(R.id.orderTicketFragment, bundle)

}
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Prevent memory leaks
    }
}