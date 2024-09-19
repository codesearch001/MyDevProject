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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentBuyMembershipBinding
import com.snofed.publicapp.databinding.FragmentOrderHistoryBinding
import com.snofed.publicapp.membership.adapter.BuyMembershipAdapter
import com.snofed.publicapp.ui.feedback.adapter.FeedBackCategoriesAdapter
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BuyMembershipFragment : Fragment(),BuyMembershipAdapter.OnItemClickListener {

    private var _binding: FragmentBuyMembershipBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var buyMembershipAdapter: BuyMembershipAdapter
    var clientId: String = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_buy_membership, container, false)
        _binding = FragmentBuyMembershipBinding.inflate(inflater, container, false)
        clientId = arguments?.getString("clientId").toString()
        Log.i("BuyMembershipFragment" , "clientId " + clientId)
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
                    val data = it.data?.data
                    if (data.isNullOrEmpty()) {
                        buyMembershipAdapter = BuyMembershipAdapter(this)
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = buyMembershipAdapter
                        buyMembershipAdapter.setFeed(data)
                    } else {
                        // Normal case: data is present
                        Log.i("buyMembershipAdapter", "Data: $data")
                        buyMembershipAdapter = BuyMembershipAdapter(this)
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

    private fun fetchResponse() {
        viewModel.getMembership(clientId)
    }

    override fun onItemClick(id: String) {
        val bundle = Bundle()
        bundle.putString("clientId", clientId)
        val destination = R.id.bacomeAMemberFragment
        findNavController().navigate(destination, bundle)
    }


}