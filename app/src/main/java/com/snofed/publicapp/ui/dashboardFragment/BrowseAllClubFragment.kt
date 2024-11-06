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
import com.snofed.publicapp.models.Client
import com.snofed.publicapp.repository.UserRepository
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedViewModel
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBrowseAllClubBinding.inflate(inflater, container, false)
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
                    // Log.i("it.data?.clients","it.data?.clients "+it.data?.data?.clients)
                    //val data = it.data?.data?.clients
                    sharedViewModel.browseClubResponse.value = it.data

                    val filteredClients = it.data?.data?.clients?.filter { client ->
                        client.visibility == 0 //true 1->false
                    }

                    Log.e("filter","filterSize " +filteredClients?.size)

                    if (filteredClients.isNullOrEmpty()){
                        clubAdapter = BrowseClubListAdapter(this)
                        // Set up the RecyclerView with GridLayoutManager
                        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
                        binding.recyclerView.adapter = clubAdapter
                        clubAdapter.setClubs(filteredClients)
                    }else{
                        clubAdapter = BrowseClubListAdapter(this)
                        // Set up the RecyclerView with GridLayoutManager
                        binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
                        binding.recyclerView.adapter = clubAdapter
                        clubAdapter.setClubs(filteredClients)
                    }

                    //Apply search Filter
                    binding.editTextClubSearch.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                            clubAdapter.getFilter().filter(s)
                        }

                        override fun afterTextChanged(s: Editable?) {
                            // Trigger filter on the adapter based on the updated text
                            clubAdapter.getFilter().filter(s)

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
        clubViewModel.clubRequestUser()
    }

    override fun onItemClick(clientId: String) {
        binding.editTextClubSearch.text?.clear()
        //Log.i("dddddd","Id " + clientId )
        val bundle = Bundle()
        bundle.putString("clientId", clientId)
        val destination = R.id.clubSubMembersFragment
        findNavController().navigate(destination, bundle)

    }

    override fun onWishlistClick(clientId: String) {
        Log.d("TAG_WishList","Wishlist_Item " + clientId )
       // clubViewModel.toggleWishlistStatus(clientId)

    }
}