package com.snofed.publicapp.ui.note

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentLoginBinding
import com.snofed.publicapp.databinding.FragmentOrderHistoryBinding
import com.snofed.publicapp.membership.adapter.ActiveMembershipAdapter
import com.snofed.publicapp.purchasehistory.adapter.OrderHistoryAdapter
import com.snofed.publicapp.purchasehistory.model.Daum
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OrderHistoryFragment : Fragment(), OrderHistoryAdapter.OnItemClickListener {
    private var _binding: FragmentOrderHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var orderHistoryAdapter: OrderHistoryAdapter

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_order_history, container, false)
        _binding = FragmentOrderHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchResponse()
        viewModel.purchaseOrderHistoryMembershipResponseLiveData.observe(
            viewLifecycleOwner,
            Observer {
                binding.progressBar.isVisible = false
                when (it) {
                    is NetworkResult.Success -> {
                        val data = it.data?.data
                        if(data.isNullOrEmpty()) {
                            binding.tvSplashText.isVisible = true
                        } else {
                            binding.tvSplashText.isVisible = false
                        }
                        // Initialize adapter with the click listener
                        orderHistoryAdapter = OrderHistoryAdapter(this)
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = orderHistoryAdapter
                        orderHistoryAdapter.setFeed(data)
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
        viewModel.getPurchaseOrderHistory(tokenManager.getUserId().toString())
    }

    override fun onItemClick(daum: Daum) {
        //Toast.makeText(context, "Clicked on ${daum.id}", Toast.LENGTH_SHORT).show()
        val bundle = Bundle().apply {
            putString("ticketOrderID", daum.id)
            putString("createdDate", daum.createdDate)
            putDouble("totalPrice", daum.totalPrice)
            putInt("status", daum.ticketOrderStatus.toInt())
            putInt("numberOfTickets", daum.tickets.count())
        }
        findNavController().navigate(R.id.purchaseOrderDetilsFragment, bundle)
        //findNavController().navigate(R.id.purchaseHistroryDeatisFragment, bundle)
    }
}