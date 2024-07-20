package com.snofed.publicapp.membership

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentBuyMembershipBinding
import com.snofed.publicapp.databinding.FragmentOrderHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuyMembershipFragment : Fragment() {

    private var _binding: FragmentBuyMembershipBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_buy_membership, container, false)
        _binding = FragmentBuyMembershipBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnHamburger.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.buyMem.setOnClickListener {
            it.findNavController().navigate(R.id.bacomeAMemberFragment)
        }


    }

}