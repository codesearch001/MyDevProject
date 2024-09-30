package com.snofed.publicapp.membership

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentSupportingMemberBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class SupportingMemberFragment : Fragment() {

    private var _binding: FragmentSupportingMemberBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    var id: String = ""
    //private var  benefits: List<BenefitDetailsResponse> = listOf()
    //private val benefitsList: MutableList<BenefitDetailsResponse> = mutableListOf()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSupportingMemberBinding.inflate(inflater, container, false)
        id = arguments?.getString("id").toString()
        //Log.i("BuyMembershipFragment" , "id " + id)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHamburger.setOnClickListener {
            it.findNavController().popBackStack()
        }



        fetchResponse()
        viewModel.activeMembershipDetailsResponseLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data?.data

                    binding.txtBenefitName.text = data?.membershipName
                    val formattedDateto = convertDateTime(data!!.validTo)
                    val formattedDatefrom = convertDateTime(data!!.validFrom)
                    binding.txtValidTo.text = formattedDateto
                    binding.txtValidFrom.text = formattedDatefrom

                    if (data != null) {
                        val benefitsList = ArrayList(data.benefits)
                        Log.d("FirstFragment", "Benefits list: ${benefitsList.size}")
                    } else {
                        Log.e("FirstFragment", "Data is null!")
                    }


                    val benefitsList = ArrayList(data.benefits)
                    Log.d("FirstFragment", "Benefits list size: ${benefitsList.size}")

                    // Check if benefitsList is empty
                    if (benefitsList.isEmpty()) {
                        // Make the box not clickable
                        binding.boxId.isClickable = false
                        binding.boxId.alpha = 0.5f  // Optional: Change appearance to indicate it's disabled
                        Toast.makeText(requireContext(), "No benefits available", Toast.LENGTH_SHORT).show()
                    } else {
                        // Make the box clickable
                        binding.boxId.isClickable = true
                        binding.boxId.alpha = 1.0f  // Reset appearance

                        // Set the click listener
                        binding.boxId.setOnClickListener {
                            val bundle = Bundle()
                            bundle.putString("membershipName", data.membershipName)
                            bundle.putParcelableArrayList("benefits", benefitsList)

                            val destination = R.id.supportingMemberListFragment
                            view?.findNavController()?.navigate(destination, bundle)
                        }
                    }
                }

                    is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertDateTime(input: String): String {
        // Parse the input date-time string to LocalDateTime
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
        val localDateTime = LocalDateTime.parse(input, inputFormatter)

        // Define the output formatter to get the required format
        val outputFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy 'at' HH:mm")

        // Adjust the date-time to the local time zone (optional)
        val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault())

        // Return the formatted date-time string
        return outputFormatter.format(zonedDateTime)
    }

    private fun fetchResponse() {
        viewModel.getBenfitsMembership(id)

    }
}