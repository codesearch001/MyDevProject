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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.BrowseClubListAdapter
import com.snofed.publicapp.databinding.FragmentBrowseAllClubBinding
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrowseAllClubFragment : Fragment(),BrowseClubListAdapter.OnItemClickListener {

    private var _binding: FragmentBrowseAllClubBinding? = null
    private val binding get() = _binding!!
    private val clubViewModel by viewModels<AuthViewModel>()

    private lateinit var clubAdapter: BrowseClubListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBrowseAllClubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchResponse()
        clubViewModel.clubLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {

                    // Set up the RecyclerView with GridLayoutManager
                    //binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
                    clubAdapter = BrowseClubListAdapter(this)
                    binding.recyclerView.layoutManager = GridLayoutManager(requireActivity(), 2)
                    binding.recyclerView.adapter = clubAdapter
                    clubAdapter.setClubs(it.data?.data?.clients)
                   // Log.i("it.data?.clients","it.data?.clients "+it.data?.data?.clients)
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
        val bundle = Bundle()
        bundle.putString("clientId", clientId)
        Log.i("Club","Id " + clientId )
        val destination = R.id.clubSubMembersFragment
        findNavController().navigate(destination, bundle)

    }


    /*   private fun bindObservers() {
           clubViewModel.clubLiveData.observe(viewLifecycleOwner, Observer {
               when (it) {
                   is NetworkResult.Success -> {
                       Log.i("brijesh ","test  "+it)
                      // findNavController().popBackStack()
                   }
                   is NetworkResult.Error -> {

                   }
                   is NetworkResult.Loading -> {

                   }
               }
           })
       }
       private fun initPopResListItem(items: List<NewClubData>) {
           //initialize groupie's group adapter class and add the list of items
           Log.e("dddd " ," "+items)
   //        val groupAdapter = GroupAdapter<RecyclerView.ViewHolder>().apply {
   //            addAll(items)
   //        }
   //        binding.recyclerView?.apply {
   //            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
   //            adapter = groupAdapter
   //        }
       }*/
}