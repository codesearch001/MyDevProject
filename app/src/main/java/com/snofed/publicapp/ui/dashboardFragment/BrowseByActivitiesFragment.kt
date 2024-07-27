package com.snofed.publicapp.ui.dashboardFragment

import android.os.Bundle
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
import com.snofed.publicapp.databinding.FragmentBrowseAllClubBinding
import com.snofed.publicapp.databinding.FragmentBrowseByActivitiesBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrowseByActivitiesFragment : Fragment() {

    private var _binding: FragmentBrowseByActivitiesBinding? = null
    private val binding get() = _binding!!
    private val clubViewModel by viewModels<AuthViewModel>()

    private lateinit var clubAdapter: BrowseClubListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_browse_by_activities, container, false)
        _binding = FragmentBrowseByActivitiesBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchResponse()
        clubViewModel.clubLiveData.observe(viewLifecycleOwner, Observer {
           // binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    //binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                    // Set up the RecyclerView with GridLayoutManager
                    /*clubAdapter = BrowseClubListAdapter()
                    binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
                    binding.recyclerView.adapter = clubAdapter
                    clubAdapter.setClubs(it.data?.data?.clients)*/
                    //Log.i("it.data?.clients","it.data?.clients "+it.data?.data?.clients)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                   // binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun fetchResponse() {
        clubViewModel.clubRequestUser()

    }

}