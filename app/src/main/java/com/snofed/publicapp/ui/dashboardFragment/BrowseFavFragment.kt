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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.TokenManager
import com.snofed.publicapp.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BrowseFavFragment : Fragment(), ClubFavAdapter.OnItemClickListener {
    private var _binding: FragmentBrowseFavBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewFavClubModel: AuthViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var clubAdapter: ClubFavAdapter

    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBrowseFavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModels
        viewFavClubModel = ViewModelProvider(this).get(AuthViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        setupObservers()
        fetchResponseData() // Trigger API or use cached data
    }

    private fun setupObservers() {
        // Observe updates to the favorites list from the shared ViewModel
        sharedViewModel.favoriteClients.observe(viewLifecycleOwner) { isUpdated ->
                fetchResponseData()
        }

        // Observe data from `clubLiveData` in the AuthViewModel
        viewFavClubModel.clubLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    Log.d("BrowseFavFragment", "Data fetched successfully.")
                    val userId = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_ID).toString()

                    val favoriteClients = userViewModel.getFavouriteClients(userId) ?: emptyList()

                    // Filter only favorite and visible clients
                    val filteredClients = result.data?.data?.clients?.filter { client ->
                        client.visibility == 0 && client.id in favoriteClients
                    } ?: emptyList()

                    bindAdapter(filteredClients) // Update the adapter with new data
                }
                is NetworkResult.Error -> {
                    Log.e("BrowseFavFragment", "Error fetching data: ${result.message}")
                }
                is NetworkResult.Loading -> {
                    Log.d("BrowseFavFragment", "Loading data...")
                }
            }
        }
    }

    private fun fetchResponseData() {
        val currentData = viewFavClubModel.clubLiveData.value
        if (currentData is NetworkResult.Success && currentData.data != null) {
            Log.d("BrowseFavFragment", "Using cached data.")
            // Reuse cached data if available
            val userId = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_ID).toString()

            val favoriteClients = userViewModel.getFavouriteClients(userId) ?: emptyList()

            val filteredClients = currentData.data.data?.clients?.filter { client ->
                client.visibility == 0 && client.id in favoriteClients
            } ?: emptyList()

            bindAdapter(filteredClients)
        } else {
            try {
                viewFavClubModel.clubRequestUser() // Trigger API call
            } catch (e: Exception) {
                Log.e("BrowseFavFragment", "Exception during API call: ${e.message}")
            }
        }
    }

    private fun bindAdapter(favClients: List<Client>) {

        clubAdapter = ClubFavAdapter(requireContext(), this, viewFavClubModel)
        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.adapter = clubAdapter

        clubAdapter.setClubs(favClients)
        clubAdapter.notifyDataSetChanged() // Ensure the adapter updates its view
    }

    override fun onItemClick(clientId: String) {
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
