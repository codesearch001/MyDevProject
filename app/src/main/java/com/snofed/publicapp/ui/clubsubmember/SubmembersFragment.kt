package com.snofed.publicapp.ui.clubsubmember

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.BrowseClubListAdapter
import com.snofed.publicapp.adapter.GalleryAdapter
import com.snofed.publicapp.adapter.SubMemberListAdapter
import com.snofed.publicapp.databinding.FragmentBrowseClubBinding
import com.snofed.publicapp.databinding.FragmentClubSubMembersBinding
import com.snofed.publicapp.databinding.FragmentSubmembersBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SubmembersFragment() : Fragment(),SubMemberListAdapter.OnItemClickListener {

    private var _binding: FragmentSubmembersBinding? = null
    private val binding get() = _binding!!
    private val subMemberViewModel by viewModels<AuthViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private lateinit var clubAdapter: SubMemberListAdapter
    @Inject lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_submembers, container, false)
        _binding = FragmentSubmembersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Initialize Retrofit ApiService
       /* fetchResponse()
        subMemberViewModel.subClubLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {

                    Log.i("PraveenGallery22222" , "gggggg222 " +it.data?.data?.subOrganisations?.size)

                    val data = it.data?.data?.subOrganisations
                        // Handle the "data not found" case
                        if (data.isNullOrEmpty()){

                            clubAdapter = SubMemberListAdapter(this)
                            // Set up the RecyclerView with GridLayoutManager
                            binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
                            binding.recyclerView.adapter = clubAdapter
                            clubAdapter.setSubMemClubs(emptyList())

                        }else{
                            clubAdapter = SubMemberListAdapter(this)
                            // Set up the RecyclerView with GridLayoutManager
                            binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
                            binding.recyclerView.adapter = clubAdapter
                            clubAdapter.setSubMemClubs(data)
                        }

                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

            }
        })*/

        // Initialize RecyclerView and Adapter
        clubAdapter = SubMemberListAdapter(this)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = clubAdapter


        // Observe the SharedViewModel for data updates
        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner) { response ->
            val subOrganisations = response?.data?.subOrganisations ?: emptyList()

            Log.d("Tag_SubMember", "SubMembersSize: ${subOrganisations.size}")

            if (subOrganisations.isEmpty()) {

                // Show the "Data Not data" message and hide RecyclerView
                binding.tvSplashText.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE

            } else {

                // Hide the "No data" message and show RecyclerView
                binding.tvSplashText.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                clubAdapter.setSubMemClubs(subOrganisations)
            }
        }

    }


  /*  private fun fetchResponse() {
        subMemberViewModel.subClubRequestUser(tokenManager.getClientId().toString())
    }*/

    override fun onItemClick(clientId: String) {
        // Hide the "Submembers" tab

        val bundle = Bundle()
        bundle.putString("clientId", clientId)
        val destination = R.id.clubSubMembersFragment
        findNavController().navigate(destination, bundle)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}