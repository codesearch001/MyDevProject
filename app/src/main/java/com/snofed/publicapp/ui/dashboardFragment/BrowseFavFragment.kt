package com.snofed.publicapp.ui.dashboardFragment

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.GridLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.BrowseClubListAdapter
import com.snofed.publicapp.adapter.ClubFavAdapter
import com.snofed.publicapp.databinding.FragmentBrowseAllClubBinding
import com.snofed.publicapp.databinding.FragmentBrowseFavBinding
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.ClientPrefrences
import com.snofed.publicapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BrowseFavFragment : Fragment() {//ClubFavAdapter.OnItemClickListener
    private var _binding: FragmentBrowseFavBinding? = null
    private val binding get() = _binding!!

    private val viewFavClubModel by viewModels<AuthViewModel>()
    private lateinit var clubAdapter: ClubFavAdapter
    var clientIdList: List<String> = listOf()

    val favClubResponses = mutableListOf<BrowseSubClubResponse>() // Replace `ClubFavResponseType` with the actual data type of `clubFavResponse.data`

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_browse_fav, container, false)
        _binding = FragmentBrowseFavBinding.inflate(inflater, container, false)

     /*   val getAllClientIds = ClientPrefrences.getClientIds(requireContext())
        Log.e("BrowseFavFragment","getALLIDS " + getAllClientIds)
        val idList: List<String> = listOf(getAllClientIds)*/

        val getAllClientIds = ClientPrefrences.getClientIds(requireContext())
        clientIdList = getAllClientIds.toList()
        //val idList: List<String> = clientIdList.toString().split(",")


       // Log.e("idList" ,"praveenFAVXYZ" + idList)



        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favClubResponses.clear()
        // Initialize the adapter once
        val clubAdapter = ClubFavAdapter()
        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.adapter = clubAdapter
        binding.recyclerView.isVisible = true
        clubAdapter.setClubs(favClubResponses)

// Mutable list to hold all responses
     //   val favClubResponses = mutableListOf<ClubFavResponseType>() // Replace `ClubFavResponseType` with the actual type

        for (favClientId in clientIdList) {
            /*fetchResponse(favClientId)
            viewFavClubModel.clubFavLiveData.observe(viewLifecycleOwner, Observer { clubFavResponse ->
                when (clubFavResponse) {
                    is NetworkResult.Success -> {

                        clubFavResponse.data?.let { data ->
                            // Add the response to the list
                            favClubResponses.add(data)


                            // Update the adapter data
                            clubAdapter.setClubs(favClubResponses)

                            //clubAdapter.notifyDataSetChanged()
                        }
                        Log.e("PraveenMSG", "Praveen $favClubResponses")
                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(requireActivity(), clubFavResponse.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Loading -> {
                        // Handle loading state if necessary
                    }
                }
            })
            Log.e("idList", "praveenFAV111 $favClientId")*/
        }




       /* for (favClientId in clientIdList) {
            fetchResponse(favClientId)
            viewFavClubModel.clubFavLiveData.observe(viewLifecycleOwner, Observer {clubFavResponse->
                when (clubFavResponse) {
                    is NetworkResult.Success -> {

                        clubFavResponse.data?.let { data ->
                            favClubResponses.add(data) // Store the response in the list
                        }

                        clubAdapter = ClubFavAdapter(this)
                        // Set up the RecyclerView with GridLayoutManager
                        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
                        binding.recyclerView.adapter = clubAdapter
                        binding.recyclerView.isVisible = true
                        clubAdapter.setClubs(clubFavResponse?.data!!)
                        clubAdapter.notifyDataSetChanged()

                        Log.e("PraveenMSG" ,"Praveen" + clubFavResponse?.data!!)

                    }
                    is NetworkResult.Error -> {
                        Toast.makeText(requireActivity(), clubFavResponse.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is NetworkResult.Loading -> {

                    }
                }
            })
            Log.e("idList" ,"praveenFAV111" + favClientId)
        }*/
    }



    private fun fetchResponse(favClientId: String) {
        viewFavClubModel.getFavClubResponse(favClientId)

    }


}