package com.snofed.publicapp.ui.clubsubmember

import android.os.Bundle
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
import com.google.android.material.tabs.TabLayoutMediator
import com.snofed.publicapp.adapter.TabClubsSubMemAdapter
import com.snofed.publicapp.databinding.FragmentClubSubMembersBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClubSubMembersFragment : Fragment() {

    private var _binding: FragmentClubSubMembersBinding? = null
    private val binding get() = _binding!!
    private val clubViewModel by viewModels<AuthViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    var clientId: String = ""
    var description: String = ""

    @Inject lateinit var tokenManager: TokenManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentClubSubMembersBinding.inflate(inflater, container, false)
        // Initialize the tab layout and ViewPager
        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        // Create a list of tabs
        val tabs = listOf("Action", "Gallery", "About", "Submembers")

        // Create a ViewPager adapter
        val adapter = TabClubsSubMemAdapter(this, tabs)
        viewPager.adapter = adapter

        // Set up the tab layout with the ViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        // Retrieve data from arguments
        clientId = arguments?.getString("clientId").toString()
        clubViewModel.mutableData.clientId.set(clientId)
        tokenManager.saveClientId(clientId)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        fetchResponse()
        clubViewModel.subClubLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    binding.progressBar.isVisible = false
                    sharedViewModel.browseSubClubResponse.value = it.data
                    Log.i("PraveenALL" , "ALL " + sharedViewModel.browseSubClubResponse.value)

                    // Example of setting data
                    //clubViewModel.mutableData.galleryImage.postValue(it.data?.data?.publicData)
                    //clubViewModel.mutableData.updateData(it.data?.data?.publicData?.images!!)
                    //Log.i("PraveenGallery" , "hhhhhh " +it.data?.data?.publicData)
                    //Log.i("it.data?.clients/publicData", "it.data?.clients.publicData " + it.data?.data?.publicData?.images)

                    if (it.data?.data?.publicName == null){
                        binding.idPublicName.text = ""
                    }else{
                        binding.idPublicName.text = it.data.data.publicName.toString()
                    }
                    binding.idTotalTrails.text = it.data?.data?.totalTrails.toString()
                    binding.idTotalTrailsLength.text = Helper.m2Km(it.data?.data?.totalTrailsLength?.toDouble()).toString() + " km"
                    binding.idClientRating.text = it.data?.data?.clientRating.toString()
                    binding.idTotalRatings.text = it.data?.data?.totalRatings.toString()
                    description = it.data?.data?.publicData?.description.toString()

                    if (it.data?.data?.publicData?.description == null){
                        tokenManager.saveDesc("No Data Found")
                    }else{
                        tokenManager.saveDesc(description)

                    }
                }

                is NetworkResult.Error -> {
                    binding.progressBar.isVisible = false
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun fetchResponse() {
        clubViewModel.subClubRequestUser(tokenManager.getClientId().toString())
       // clubViewModel.subClubRequestUser(clubViewModel.mutableData.clientId.get().toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

