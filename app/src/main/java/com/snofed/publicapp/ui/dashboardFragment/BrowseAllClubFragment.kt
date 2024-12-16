package com.snofed.publicapp.ui.dashboardFragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.BrowseClubListAdapter
import com.snofed.publicapp.databinding.FragmentBrowseAllClubBinding
import com.snofed.publicapp.models.realmModels.Client
import com.snofed.publicapp.repository.UserRepository
import com.snofed.publicapp.ui.User.UserViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ActivityViewModelRealm
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.ClientPrefrences
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedPreferenceKeys
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.viewModel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BrowseAllClubFragment : Fragment(),BrowseClubListAdapter.OnItemClickListener {

    private var _binding: FragmentBrowseAllClubBinding? = null
    private val binding get() = _binding!!
    private val clubViewModel by viewModels<AuthViewModel>()
    private lateinit var clubAdapter: BrowseClubListAdapter
    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBrowseAllClubBinding.inflate(inflater, container, false)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Initialize Retrofit ApiService

        fetchResponse()
       // clubViewModel.fetchClients() // Trigger data fetch
        clubViewModel.clubLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val clubs :  List<Client> = it.data?.data?.clients ?: emptyList()
                    setupClubList(clubs)
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

    private fun setupClubList(clients: List<Client>?) {
        val userId = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_ID).toString()
        val userFavClients = userViewModel.getFavouriteClients(userId)
        val filteredClients = clients?.filter { it.visibility == 0 } ?: listOf()

        sharedViewModel.updateFavorites(userFavClients)

        filteredClients.forEach { client ->
            if (userFavClients.contains(client.id)) {
                client.isInWishlist = true
            }
        }

        clubAdapter = BrowseClubListAdapter(requireContext(), this, clubViewModel)
        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
        binding.recyclerView.adapter = clubAdapter
        clubAdapter.setClubs(filteredClients)

        sharedViewModel.favoriteClients.observe(viewLifecycleOwner, Observer { favClients ->
            filteredClients.forEach { client ->
                client.isInWishlist = favClients.contains(client.id)
            }
            clubAdapter.notifyDataSetChanged()
        })


        binding.editTextClubSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clubAdapter.getFilter().filter(s)
            }
            override fun afterTextChanged(s: Editable?) {
                clubAdapter.getFilter().filter(s)
            }
        })
    }

    private fun fetchResponse() {
        try {
            clubViewModel.clubRequestUser()
        }
        catch (e: Exception) {
            Log.e("Exception", e.toString())
        }
    }

    override fun onItemClick(clientId: String) {
        binding.editTextClubSearch.text?.clear()

        val bundle = Bundle()
        bundle.putString("clientId", clientId)
        val destination = R.id.clubSubMembersFragment
        findNavController().navigate(destination, bundle)

    }

    override fun onWishlistClick(clientId: String, isWishlisted : Boolean) {
        Log.d("TAG_WishList","Wishlist_Item " + clientId + " -- " + isWishlisted )
        val userId = AppPreference.getPreference(context, SharedPreferenceKeys.USER_USER_ID).toString()
        if (isWishlisted) {
            userViewModel.addFavouriteClient(userId,clientId)
            sharedViewModel.addToFavorites(clientId)

        } else {
            userViewModel.removeFavouriteClient(userId,clientId)
            sharedViewModel.removeFromFavorites(clientId)
        }
    }
}