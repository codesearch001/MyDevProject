package com.snofed.publicapp.ui.shop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentOrderTicketBinding
import com.snofed.publicapp.databinding.FragmentPurchaseOptionsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PurchaseOptionsFragment : Fragment() {

    private var _binding: FragmentPurchaseOptionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_purchase_options, container, false)
        _binding = FragmentPurchaseOptionsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.card1.setOnClickListener {
            it.findNavController().navigate(R.id.orderTicketFragment)
            //it.findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            /*val intent = Intent(requireActivity(), HomeDashBoardActivity::class.java)
            startActivity(intent)*/
        }
        binding.card2.setOnClickListener {
            it.findNavController().navigate(R.id.orderTicketFragment)
            //it.findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
            /*val intent = Intent(requireActivity(), HomeDashBoardActivity::class.java)
            startActivity(intent)*/
        }
    }
}