package com.snofed.publicapp.ui.dashboardFragment

import RealmRepository
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.BrowseClubListAdapter
import com.snofed.publicapp.adapter.ClubFavAdapter
import com.snofed.publicapp.databinding.FragmentBrowseAllClubBinding
import com.snofed.publicapp.databinding.FragmentBrowseFavBinding
import com.snofed.publicapp.models.realmModels.Client
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.ui.User.UserViewModelRealm
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.ClientPrefrences
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedPreferenceKeys
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BrowseFavFragment : Fragment() {//ClubFavAdapter.OnItemClickListener
    private var _binding: FragmentBrowseFavBinding? = null
    private val binding get() = _binding!!

    private val viewFavClubModel by viewModels<AuthViewModel>()
    private val clubViewModel by viewModels<AuthViewModel>()
    private lateinit var clubAdapter: ClubFavAdapter
    var clientIdList: List<String> = listOf()
    var allClubResponses = mutableListOf<NewClubData>()
    var favClubResponses = mutableListOf<Client>() // Replace `ClubFavResponseType` with the actual data type of `clubFavResponse.data`

    private lateinit var viewModelUserRealm: UserViewModelRealm

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_browse_fav, container, false)
        _binding = FragmentBrowseFavBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModelUserRealm = ViewModelProvider(this).get(UserViewModelRealm::class.java)
        fetchResponse()
        // Bind the adapter to the RecyclerView
        viewFavClubModel.clubLiveData.observe(viewLifecycleOwner) { result ->
            val allClubs = when (result) {
                is NetworkResult.Success -> {

                    val gson = Gson()
                    val json = gson.toJson(result.data) // Convert the object to JSON string
                    Log.d("BrowseClubResponse", json)
                    val userId = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_ID).toString()

//                    val realmRepository = RealmRepository()
//                    val userViewModelRealm = UserViewModelRealm(realmRepository)
                    val userRealm = viewModelUserRealm.getUserById(userId!!)
                    val getFavClients: List<String> = userRealm?.favouriteClients ?: emptyList()

//                    result.data?.let { data ->
//                        // Add the response to the list
//                        allClubResponses.add(data)
//                    }
                    val filteredClients = result.data?.data?.clients?.filter { client ->
                        client.visibility == 0 //true 1->false
                    }
                    Log.e("ClubResponses", "AllClubs $allClubResponses")
                    favClubResponses.clear()


                    for (club in filteredClients!!.toList()) {
                        // Check if any client in club.data.clients is in getFavClients
                            if (club.id in getFavClients) {
                                // If a match is found, add the club to favClubResponses
                                favClubResponses.add(club)
                        }
                    }

                    Log.e("ClubResponses", "AllClubs $favClubResponses")
                    // Initialize the adapter once
                    clubAdapter = ClubFavAdapter()
                    binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
                    binding.recyclerView.adapter = clubAdapter
                    binding.recyclerView.isVisible = true
                    clubAdapter.setClubs(favClubResponses)
                }

                is NetworkResult.Error -> {
                    Log.e("Error", "Failed to fetch clubs: ${result.message}")
                    favClubResponses
                }

                is NetworkResult.Loading -> {
                    Log.d("Loading", "Clubs are being loaded")
                    favClubResponses
                }

                null -> {
                    Log.e("Error", "LiveData is null")
                    favClubResponses
                }
            }
        }
        //favClubResponses = allClubResponses.filter { it.data.clients in getFavClients } as MutableList<NewClubData>
        // favClubResponses = allClubResponses.filter { it.data.clients.any { client -> client in getFavClients } } as MutableList<NewClubData>


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
    private fun fetchResponse() {
        clubViewModel.clubRequestUser()
    }

}