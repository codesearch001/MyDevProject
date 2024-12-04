package com.snofed.publicapp.purchasehistory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentOrderHistoryBinding
import com.snofed.publicapp.databinding.FragmentPurchaseHistroryDeatisBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseHistroryDeatisFragment : Fragment() {

    private var _binding: FragmentPurchaseHistroryDeatisBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_purchase_histrory_deatis, container, false)
        _binding = FragmentPurchaseHistroryDeatisBinding.inflate(inflater, container, false)
        binding.btnHamburger.setOnClickListener {
            it.findNavController().popBackStack()
        }
        // Retrieve data from the arguments (Bundle)
        arguments?.let { bundle ->
            val createdDate = bundle.getString("createdDate")
            val totalPrice = bundle.getDouble("totalPrice")
            val status = bundle.getInt("status")
            val numberOfTickets = bundle.getInt("numberOfTickets")

            // Set the data to views using View Binding
//            binding.txtCreatedDate.text = createdDate
//            binding.txtTotalPrice.text = totalPrice.toString()
//            binding.txtStatus.text = status.toString()
//            binding.txtNumberOfTickets.text = numberOfTickets.toString()
        }
        return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Clean up binding
        _binding = null
    }
}