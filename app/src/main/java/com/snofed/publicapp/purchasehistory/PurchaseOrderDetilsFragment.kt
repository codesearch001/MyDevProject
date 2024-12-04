package com.snofed.publicapp.purchasehistory

import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentPurchaseHistroryDeatisBinding
import com.snofed.publicapp.databinding.FragmentPurchaseOrderDetilsBinding
import com.snofed.publicapp.purchasehistory.adapter.OrderDetailsAdapter
import com.snofed.publicapp.purchasehistory.adapter.OrderHistoryAdapter
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ClientViewModelRealm
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.DateTimeConverter
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.enums.PaymentOrderStatus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseOrderDetilsFragment : Fragment() {

    private var _binding: FragmentPurchaseOrderDetilsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var orderDetailsAdapter: OrderDetailsAdapter
    private lateinit var vmClientRealm: ClientViewModelRealm
    var ticketOrderID: String = ""
    var clientId:String = ""
    var clientNameRef:String? = ""
    val dateTimeConverter = DateTimeConverter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_purchase_order_detils, container, false)
        _binding = FragmentPurchaseOrderDetilsBinding.inflate(inflater, container, false)
        ticketOrderID = arguments?.getString("ticketOrderID").toString()
        clientId = arguments?.getString("clientId").toString()
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmClientRealm = ViewModelProvider(this).get(ClientViewModelRealm::class.java)
        clientNameRef = vmClientRealm.getClientNameById(clientId)
        fetchResponse()
        viewModel.purchaseOrderHistoryDetailsResponseLiveData.observe(viewLifecycleOwner, Observer {
                binding.progressBar.isVisible = false
                when (it) {
                    is NetworkResult.Success -> {
                        val data = it.data?.data?.tickets

                        // Set initial button text and visibility of RecyclerView
                        binding.btnHideShow.text = getString(R.string.show_tickets) + " (${data?.size ?: 0})"
                        binding.recyclerView.visibility = View.GONE
                        Log.e("data","printData" + data)
                        if (it.data?.data?.createdDate != null) {
                            dateTimeConverter.convertDateTime(it.data.data.createdDate)
                        }
                        binding.tvCreatedDate.text =  dateTimeConverter.outputFormatterOnlyDate
                        binding.txtNumberOfTickets.text = it.data?.data?.tickets?.count().toString()
                        binding.txtTotalPrice.text = it.data?.data?.totalPrice.toString()
                        binding.txtOrderTitle.text = clientNameRef

                        val ticketOrderStatus: Int = it.data?.data?.ticketOrderStatus?.toInt() ?: -1

                        val statusText = PaymentOrderStatus.fromStatus(ticketOrderStatus)
                        binding.tvStatusApproved.text = statusText

                        when (ticketOrderStatus) {
                            PaymentOrderStatus.Canceled.status -> {
                                binding.statusCancelled.visibility = View.VISIBLE
                                binding.statusApproved.visibility = View.GONE
                            }
                            PaymentOrderStatus.Approved.status -> {
                                binding.statusApproved.visibility = View.VISIBLE
                                binding.statusCancelled.visibility = View.GONE
                            }
                            else -> {
                                binding.statusCancelled.visibility = View.GONE
                                binding.statusApproved.visibility = View.GONE
                            }
                        }
                        if(data.isNullOrEmpty()) {
                            binding.tvSplashText.isVisible = true
                        } else {
                            binding.tvSplashText.isVisible = false
                        }
                        // Initialize adapter with the click listener
                        orderDetailsAdapter = OrderDetailsAdapter()
                        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.recyclerView.adapter = orderDetailsAdapter
                        orderDetailsAdapter.setFeed(data, clientNameRef)

                        // Handle button toggle for showing/hiding the RecyclerView
                        var isExpanded = false
                        binding.btnHideShow.setOnClickListener {
                            isExpanded = !isExpanded
                            binding.recyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
                            val buttonText = if (isExpanded) {
                                getString(R.string.hide_tickets) + " (${data?.size ?: 0})"
                            } else {
                                getString(R.string.show_tickets) + " (${data?.size ?: 0})"
                            }
                            binding.btnHideShow.text = buttonText
                                // Create a SpannableString with text
                            val spannable = SpannableString("$buttonText ")
                            // Use smaller icons and scale dynamically
                            val iconResId = if (isExpanded) R.drawable.up else R.drawable.down
                            val drawable = ContextCompat.getDrawable(requireContext(), iconResId)?.apply {
                                val iconSize = (binding.btnHideShow.lineHeight * 0.9).toInt() // Adjust icon size based on button text height
                                setBounds(0, 8, iconSize, iconSize)
                            }

                            // Ensure the icon is loaded and visible
                            if (drawable != null) {
                                val imageSpan = ImageSpan(drawable, ImageSpan.ALIGN_BASELINE)
                                spannable.setSpan(imageSpan, spannable.length - 1, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                            }

// Set the SpannableString as the button's text
                            binding.btnHideShow.text = spannable
                        }
                    }

                    is NetworkResult.Error -> {
                        Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                    }

                    is NetworkResult.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                }
            })
    }

    private fun fetchResponse() {
        viewModel.getOrderById(ticketOrderID)
    }
}