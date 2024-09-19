package com.snofed.publicapp.ui.feedback

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentFeedBackDefaultCategoryListBinding
import com.snofed.publicapp.ui.feedback.adapter.FeedBackAdapter
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedBackDefaultCategoryListFragment : Fragment(), FeedBackAdapter.OnItemClickListener {

    private var _binding: FragmentFeedBackDefaultCategoryListBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var feedAdapter: FeedBackAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_feed_back_default_category_list, container, false)
        _binding = FragmentFeedBackDefaultCategoryListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        fetchResponse()
        viewModel.feedBackTaskCategoriesLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data?.data
                    if (data.isNullOrEmpty()) {

                        feedAdapter = FeedBackAdapter(this)
                        binding.feedRecyclerView.layoutManager =
                            LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = feedAdapter
                        feedAdapter.setFeed(data)

                    } else {
                        // Normal case: data is present
                        Log.i("PraveenGallery22222", "Data: $data")
                        //  binding.tvSplashText.isVisible = false
                        feedAdapter = FeedBackAdapter(this)
                        binding.feedRecyclerView.layoutManager =
                            LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = feedAdapter
                        //Log.i("it.data?.feed","it.data?.feed "+it.data?.data)
                        feedAdapter.setFeed(data)

                    }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }
    private fun fetchResponse() {
        viewModel.getFeedBackTaskCategories()
    }

    override fun onItemClick(id: String, name: String) {
        // Create a Bundle to send data to the next fragment

        val bundle = Bundle().apply {
            putString("CATEGORY_ID", id) // Add the description to the bundle
            putString("CATEGORY_NAME", name) // Add the description to the bundle
        }
        // Navigate to the next fragment, passing the data
        findNavController().navigate(R.id.singleResortReportProblemFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}