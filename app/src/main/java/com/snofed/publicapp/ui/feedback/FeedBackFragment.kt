package com.snofed.publicapp.ui.feedback

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.WorkoutFeedAdapter
import com.snofed.publicapp.databinding.FragmentEventBinding
import com.snofed.publicapp.databinding.FragmentFeedBackBinding
import com.snofed.publicapp.ui.feedback.adapter.FeedBackAdapter
import com.snofed.publicapp.ui.feedback.adapter.FeedBackCategoriesAdapter
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FeedBackFragment : Fragment(),FeedBackCategoriesAdapter.OnItemClickListener {

    private var _binding: FragmentFeedBackBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()

    private lateinit var feedAdapter: FeedBackCategoriesAdapter
    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_feed_back, container, false)
        _binding = FragmentFeedBackBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.callFeedBackLayout.setOnClickListener {
            it.findNavController().navigate(R.id.feedBackDefaultCategoryListFragment)
        }

        fetchResponse()
        viewModel.feedBackTaskByCategoriesIDLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {
                    val data = it.data?.data
                    if (data.isNullOrEmpty()) {
                        binding.tvSplashText.isVisible = true
                        feedAdapter = FeedBackCategoriesAdapter(this)
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = feedAdapter
                        feedAdapter.setFeed(data)
                    } else {
                        // Normal case: data is present
                        Log.i("PraveenGallery22222", "Data: $data")
                        binding.tvSplashText.isVisible = false
                        feedAdapter = FeedBackCategoriesAdapter(this)
                        binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
                        binding.feedRecyclerView.adapter = feedAdapter
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
        viewModel.getTaskByCategoriesID(tokenManager.getUserId().toString())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(id: String) {
        val bundle = Bundle().apply {
            putString("FEEDBACK_ID", id) // Add the description to the bundle
        }
        // Navigate to the next fragment, passing the data
        findNavController().navigate(R.id.feedBackDetailsFragment, bundle)
    }


}
