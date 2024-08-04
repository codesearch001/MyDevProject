package com.snofed.publicapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.adapter.BrowseClubListAdapter
import com.snofed.publicapp.adapter.GalleryAdapter
import com.snofed.publicapp.adapter.WorkoutFeedAdapter
import com.snofed.publicapp.databinding.FragmentBrowseClubBinding
import com.snofed.publicapp.databinding.FragmentFeedBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.DrawerController
import com.snofed.publicapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment(),WorkoutFeedAdapter.OnItemClickListener {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val feedViewModel by viewModels<AuthViewModel>()

    private lateinit var feedAdapter: WorkoutFeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_feed, container, false)
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        binding.humburger.setOnClickListener {
            (activity as? DrawerController)?.openDrawer()
        }
        binding.imgFeedId.setOnClickListener {
            it.findNavController().navigate(R.id.feedViewImageFragment)
        }

        binding.imgFeedIdChart.setOnClickListener {
            it.findNavController().navigate(R.id.mapFeedFragment)
        }
       return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchResponse()
        feedViewModel.feedLiveData.observe(viewLifecycleOwner, Observer {
             binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data?.data
                    if (data.isNullOrEmpty()) {
                        //Log.i("it.data?.feed","it.data?.feed "+it.data?.data)
                        // Handle the "data not found" case
                        // Assuming this is inside a Fragment
                        it.data?.message?.let { message ->
                            binding.tvSplashText.apply {
                                isVisible = true
                                text = message
                            }
                        }

                        feedAdapter = WorkoutFeedAdapter(this)
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = feedAdapter
                        feedAdapter.setFeed(data)
                    } else {
                        // Normal case: data is present
                        Log.i("PraveenGallery22222", "Data: $data")
                        binding.tvSplashText.isVisible = false
                        feedAdapter = WorkoutFeedAdapter(this)
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = feedAdapter
                        //Log.i("it.data?.feed","it.data?.feed "+it.data?.data)
                        feedAdapter.setFeed(data)

                    }

//                    feedAdapter = WorkoutFeedAdapter(this)
//                    binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
//                    binding.feedRecyclerView.adapter = feedAdapter
//                    //Log.i("it.data?.feed","it.data?.feed "+it.data?.data)
//                    feedAdapter.setFeed(it.data?.data)

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
        feedViewModel.feedRequestUser()
    }

    override fun onItemClick(clientId: String) {
        val bundle = Bundle()
        bundle.putString("eventId", clientId)
        val destination = R.id.mapFeedFragment
        findNavController().navigate(destination, bundle)
    }
}