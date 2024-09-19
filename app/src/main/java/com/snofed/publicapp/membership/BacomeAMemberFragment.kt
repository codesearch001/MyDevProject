package com.snofed.publicapp.membership

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentBacomeAMemberBinding
import com.snofed.publicapp.databinding.FragmentBuyMembershipBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BacomeAMemberFragment : Fragment() {

    private var _binding: FragmentBacomeAMemberBinding? = null
    private val binding get() = _binding!!
    var clientId: String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_bacome_a_member, container, false)
        _binding = FragmentBacomeAMemberBinding.inflate(inflater, container, false)
        clientId = arguments?.getString("clientId").toString()
        Log.i("BuyMembershipFragment" , "clientId " + clientId)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHamburger.setOnClickListener {
            it.findNavController().popBackStack()
        }

    }

}