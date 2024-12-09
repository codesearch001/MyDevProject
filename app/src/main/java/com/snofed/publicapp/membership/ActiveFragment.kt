package com.snofed.publicapp.ui.note

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentActvieBinding
import com.snofed.publicapp.databinding.FragmentMembershipBinding
import com.snofed.publicapp.membership.adapter.ActiveMembershipAdapter
import com.snofed.publicapp.membership.adapter.BuyMembershipAdapter
import com.snofed.publicapp.membership.model.BenefitResponse
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ActiveFragment : Fragment(), ActiveMembershipAdapter.OnItemClickListener {
    private var _binding: FragmentActvieBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var activeMembershipAdapter: ActiveMembershipAdapter

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentActvieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        fetchResponse()
        viewModel.activeMembershipResponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data?.data
                    if (data.isNullOrEmpty()) {
                        activeMembershipAdapter = ActiveMembershipAdapter(this)
                        binding.tvSplashText.isVisible = true
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = activeMembershipAdapter
                        activeMembershipAdapter.setFeed(data)
                    } else {
                        // Normal case: data is present
                        Log.i("buyMembershipAdapter", "Data: $data")
                        activeMembershipAdapter = ActiveMembershipAdapter(this)
                        binding.tvSplashText.isVisible = false
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = activeMembershipAdapter
                        activeMembershipAdapter.setFeed(data)
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
        viewModel.getActiveMembership(tokenManager.getUserId().toString())
    }

    //override fun onItemClick(id: String, benefits: List<BenefitResponse>)
    override fun onItemClick(id: String) {
        val bundle = Bundle()
        bundle.putString("id", id)
        val destination = R.id.supportingMemberFragment
        view?.findNavController()?.navigate(destination, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
