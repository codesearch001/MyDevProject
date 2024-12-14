package com.snofed.publicapp.membership

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentBuyMembershipBinding
import com.snofed.publicapp.membership.adapter.BuyMembershipAdapter
import com.snofed.publicapp.membership.model.BuyMembershipResponse
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class BuyMembershipFragment : Fragment(),BuyMembershipAdapter.OnItemClickListener {

    private var _binding: FragmentBuyMembershipBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var buyMembershipAdapter: BuyMembershipAdapter
    private var clientId: String = ""
    private var isAdmin :Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_buy_membership, container, false)
        _binding = FragmentBuyMembershipBinding.inflate(inflater, container, false)
        clientId = arguments?.getString("clientId") ?: ""
        isAdmin = arguments?.getBoolean("isAdmin") ?: false
        Log.i("BuyMembershipFragment", "clientId " + clientId)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnHamburger.setOnClickListener {
            it.findNavController().popBackStack()
        }
        fetchResponse()
        viewModel.membershipResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    var data = it.data?.data

                    data= filterWithValidity(data)
                    if(clientId == ""){
                        if(isAdmin)
                            data = data?.filter { it.isAdminMembership }
                    }
                    else{
                        data = data?.filter { !it.isAdminMembership }
                    }

                    //Uncomment to see only active membership
                    //data = data?.filter { it.isActive }

                    if (data.isNullOrEmpty()) {
                        buyMembershipAdapter = BuyMembershipAdapter(this)
                        binding.tvSplashText.isVisible = true
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = buyMembershipAdapter
                        buyMembershipAdapter.setFeed(data)
                    } else {
                        // Normal case: data is present
                        Log.i("buyMembershipAdapter", "Data: $data")
                        buyMembershipAdapter = BuyMembershipAdapter(this)
                        binding.tvSplashText.isVisible = false
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = buyMembershipAdapter
                        buyMembershipAdapter.setFeed(data)

                    }

                    // Search filter implementation
                    binding.editTextSearch.addTextChangedListener(object : TextWatcher {
                        override fun afterTextChanged(s: Editable?) {
                            buyMembershipAdapter.getFilter().filter(s)
                        }

                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                            buyMembershipAdapter.getFilter().filter(s)
                        }

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            buyMembershipAdapter.getFilter().filter(s)

                        }
                    })
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

    private fun filterWithValidity(data: List<BuyMembershipResponse>?): List<BuyMembershipResponse>? {

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()

        return data?.filter { memberShip ->
            val eventEndDate = dateFormat.parse(memberShip.activeTo)
            eventEndDate?.after(currentDate) == true
        }
    }

    private fun fetchResponse() {
        viewModel.getMembership(clientId) // Use the client ID to make the API call
    }

    override fun onItemClick(id: String, name: String) {
        val bundle = Bundle()
        bundle.putString("membershipId", id)
        bundle.putString("clientId", clientId)
        bundle.putString("membershipName", name)
        val destination = R.id.bacomeAMemberFragment
        findNavController().navigate(destination, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}