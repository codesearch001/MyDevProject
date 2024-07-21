package com.snofed.publicapp.ui.dashboardFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.snofed.publicapp.adapter.BrowseClubListAdapter
import com.snofed.publicapp.databinding.FragmentBrowseAllClubBinding
import com.snofed.publicapp.models.ClubListResponse
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.NoteResponse
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrowseAllClubFragment : Fragment() {

    private var _binding: FragmentBrowseAllClubBinding? = null

    private val binding get() = _binding!!

    private val clubViewModel by viewModels<AuthViewModel>()

    private lateinit var clubAdapter: BrowseClubListAdapter

    private var clubeList: NewClubData? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBrowseAllClubBinding.inflate(inflater, container, false)
        // adapter = NoteAdapter(::onNoteClicked)
        Log.i("1111","rrrrr")
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchResponse()
        //bindObservers()
//        clubViewModel.clubLiveData.observe(viewLifecycleOwner, Observer {
//            //initPopResListItem(it)
//
//        })

        //bindObservers()

        Log.i("1111111","rrrrr")

    }

    private fun fetchResponse() {
        clubViewModel.clubRequestUser()
    }


    private fun bindObservers() {
        clubViewModel.clubLiveData.observe(viewLifecycleOwner, Observer {
            /*when (it) {
                is NetworkResult.Success -> {
                    Log.i("brijesh ","test  "+it)
                   // findNavController().popBackStack()
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
            }*/
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
    }



}