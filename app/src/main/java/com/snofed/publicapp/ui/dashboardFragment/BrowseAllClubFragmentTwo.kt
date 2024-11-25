package com.snofed.publicapp.ui.dashboardFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.snofed.publicapp.adapter.BrowseClubListAdapter
import com.snofed.publicapp.databinding.FragmentBrowseAllClubTwoBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrowseAllClubFragmentTwo : Fragment(),BrowseClubListAdapter.OnItemClickListener {

    private var _binding: FragmentBrowseAllClubTwoBinding? = null
    private val binding get() = _binding!!
    private lateinit var clubAdapter: BrowseClubListAdapter
    private val clubViewModel by viewModels<AuthViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBrowseAllClubTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        //clubViewModel.fetchClients() // Trigger data fetch
    }

    private fun setupRecyclerView() {
        //clubAdapter = BrowseClubListAdapter(rquireContext(),this)
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recyclerView.adapter = clubAdapter
    }

    private fun observeViewModel() {
       /* clubViewModel.clients.observe(viewLifecycleOwner, { clientList ->
            binding.progressBar.isVisible = false
            if (clientList.isNotEmpty()) {
                clubAdapter.setClubs(clientList)
            } else {
                // Handle empty list or show a message
                Toast.makeText(requireContext(), "No clients available", Toast.LENGTH_SHORT).show()
            }
        })*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(clientId: String) {
        TODO("Not yet implemented")
    }

    override fun onWishlistClick(clientId: String) {
        TODO("Not yet implemented")
    }
}
