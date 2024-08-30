package com.snofed.publicapp.ui.feedImage

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.snofed.publicapp.R

import com.snofed.publicapp.adapter.ImageAdapter
import com.snofed.publicapp.databinding.FragmentFeedViewImageBinding
import com.snofed.publicapp.models.workoutfeed.WorkoutImage
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FeedViewImageFragment : Fragment(){

    private var _binding: FragmentFeedViewImageBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    @Inject
    lateinit var tokenManager: TokenManager
    // Declare ViewPager2 as a lateinit property
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_feed_view_image, container, false)
        _binding = FragmentFeedViewImageBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = binding.viewPager

        // Set up ViewPager2
        setupViewPager()
    }

    private fun setupViewPager() {
        sharedViewModel.WorkoutActivites.observe(viewLifecycleOwner, Observer { response ->

            val data = response.data.workoutImages
            // Normal case: data is present
            Log.d("Tag_FeedGallery ", "Tag_FeedGallery.. $data")

            if (data.isEmpty()){
                // Handle empty image list case
                binding.noRecord.isVisible = true
                // val adapter = ImageAdapter(imageList)
                val adapter = ImageAdapter(data.size)
                viewPager.adapter = adapter
                adapter.setFeedImage(data)
                // Initialize the image count text view
                updateImageCount(1, data.size)

            }else{

                binding.noRecord.isVisible = false
                // val adapter = ImageAdapter(imageList)
                val adapter = ImageAdapter(data.size)
                viewPager.adapter = adapter
                adapter.setFeedImage(data)
                // Initialize the image count text view
                updateImageCount(1, data.size)
            }


            // Set a page change callback to update the image count text view
            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    // Update the image count text view
                    updateImageCount(position + 1, data.size)
                }
            })
        })
    }

    private fun updateImageCount(currentPage: Int, totalPages: Int) {
        binding.imageCount.text = "$currentPage/$totalPages"
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}