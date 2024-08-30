package com.snofed.publicapp.membership

import android.os.Bundle
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bacome_a_member, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHamburger.setOnClickListener {
            it.findNavController().popBackStack()
        }

    }

}