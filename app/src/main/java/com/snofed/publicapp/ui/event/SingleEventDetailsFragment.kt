package com.snofed.publicapp.ui.event

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.snofed.publicapp.databinding.FragmentSingleEventDetailsBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.DateTimeConverter
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint
import android.text.Spannable
import androidx.activity.OnBackPressedCallback
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.snofed.publicapp.R
import com.snofed.publicapp.utils.Constants
import com.snofed.publicapp.utils.ServiceUtil

@AndroidEntryPoint
class SingleEventDetailsFragment : Fragment() {

    private var _binding: FragmentSingleEventDetailsBinding? = null
    private val binding get() = _binding!!
    private val eventDetailsViewModel by viewModels<AuthViewModel>()
    var eventId: String = ""
    val dateTimeConverter = DateTimeConverter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //  return inflater.inflate(R.layout.fragment_single_event_details, container, false)
        _binding = FragmentSingleEventDetailsBinding.inflate(inflater, container, false)
        // Retrieve data from arguments
        eventId = arguments?.getString("eventId").toString()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Handle back press
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Check if there's any fragment in the back stack
                if (requireFragmentManager().backStackEntryCount > 0) {
                    // Pop the fragment from the back stack
                    requireFragmentManager().popBackStack()
                } else {
                    // If no fragments in the back stack, you can exit the activity or perform another action
                    // For example, exit the app:
                    requireActivity().finish()
                    // Or handle navigation to a specific fragment or screen
                    // findNavController().navigate(R.id.someOtherFragment)
                }
            }
        })
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding.btnBuyEventTicket.setOnClickListener {
            it.findNavController().navigate(R.id.purchaseOptionsFragment)
            //it.findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            /*val intent = Intent(requireActivity(), HomeDashBoardActivity::class.java)
            startActivity(intent)*/
        }

        fetchResponse()
        eventDetailsViewModel.eventDetailsLiveData.observe(viewLifecycleOwner, Observer {
            //binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {

                    Log.d("EventDetails", "EventDetails... " + it.data?.data)

                    if (it.data?.data == null) {

                        binding.eventLayout.isVisible = false
                        binding.txtIdNoRecordFound.isVisible = true

                    } else {

                        binding.eventLayout.isVisible = true
                        binding.txtIdNoRecordFound.isVisible = false

                        dateTimeConverter.convertDateTime(it.data.data.startDate)//convert data
                        val getDate=dateTimeConverter.datePartOnly
                        val getMonthOnly=dateTimeConverter.dateOfMonthPartOnly
                        binding.textStartEventDetailsDate.text = getDate
                        binding.textMonth.text = getMonthOnly
                        binding.txtEventBannerDate.text = getMonthOnly + " Event"
                        binding.txtEventName.text = it.data.data.name

                        if (it.data.data.coverImagePath == null) {
                            Glide.with(binding.imgEventBannerImagePath).load(R.drawable.event_banner_details)
                                .into(binding.imgEventBannerImagePath)
                        } else {
                            Glide.with(binding.imgEventBannerImagePath)
                                .load(ServiceUtil.BASE_URL_IMAGE + it.data.data.coverImagePath )
                                .diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(binding.imgEventBannerImagePath)
                        }

                        if (it.data.data.location == "") {
                            binding.txtEventLocation.text = "N/A"
                        } else {
                            binding.txtEventLocation.text = it.data.data.location
                        }

                        if (it.data.data.ticketPrice.equals(0.0)){
                            binding.txtEventPrice.text = "Free Ticket"
                        }else{
                            binding.txtEventPrice.text = it.data.data.ticketPrice.toString()
                        }

                        binding.txtEventdescription.text = it.data.data.description
                        binding.txtMaxAttendees.text = it.data.data.maxAttendees.toString()
                        // Example date strings
                        val startDate = it.data.data.startDate
                        val endDate =  it.data.data.endDate
                        // Log the formatted date range
                        // Format the date range
                        val (formattedStartDate, formattedEndDate) = Helper.formatDateRange(startDate, endDate)
                        binding.txtEventStartDate.text = formattedStartDate
                        binding.txtEventEndDate.text = formattedEndDate
                        if (it.data.data.link == null){
                            binding.txtEventLink.text = "N/A"
                        }else{

                            val spannableString = SpannableString(it.data.data.linkName.toString())

                            val linkSpan = object : ClickableSpan() {
                                override fun onClick(widget: View) {
                                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(it.data.data.link.toString()))
                                    startActivity(browserIntent)
                                }
                            }

                            spannableString.setSpan(linkSpan, 0, it.data.data.linkName.toString().length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                            binding.txtEventLink.text = spannableString
                            binding.txtEventLink.movementMethod = LinkMovementMethod.getInstance()
                            binding.txtEventLink.text = spannableString
                        }
                        if (it.data.data.sponsors == ""){
                            binding.txtEventSponsors.text = "N/A"
                        }else{
                            binding.txtEventSponsors.text =it.data.data.sponsors
                        }
                        if (it.data.data.ticketPrice == 0.0 ) {
                            binding.btnBuyEventTicket.isVisible = false
                        } else {
                            binding.btnBuyEventTicket.isVisible = true
                        }


                    }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    // binding.progressBar.isVisible = true
                }

            }
        })
    }

    private fun fetchResponse() {
        eventDetailsViewModel.eventDetailsRequestUser(eventId)
    }
}

