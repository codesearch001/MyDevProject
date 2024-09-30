package com.snofed.publicapp.ui.shop

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentOrderTicketBinding
import com.snofed.publicapp.databinding.FragmentPurchaseOptionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseOptionsFragment : Fragment() {

    private var _binding: FragmentPurchaseOptionsBinding? = null
    private val binding get() = _binding!!
    private var clientId: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_purchase_options, container, false)
        _binding = FragmentPurchaseOptionsBinding.inflate(inflater, container, false)
        clientId = getArguments()?.getString("clientId")
        Log.i("ggggg","Id " + clientId )
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.card1.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("clientId",clientId)
            bundle.putInt("ticketCategory",  1)
            val destination = R.id.newTicketFragment
            findNavController().navigate(destination, bundle)

        }

        binding.card2.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("clientId", clientId)
            bundle.putInt("ticketCategory", 1)
            bundle.putBoolean("isMultipleTicket", true)
            val destination = R.id.orderTicketFragment
            findNavController().navigate(destination, bundle)
        }

        binding.card3.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("clientId", clientId)
            bundle.putInt("ticketCategory",  3)
            val destination = R.id.newTicketFragment
            findNavController().navigate(destination, bundle)

        }
    }
}