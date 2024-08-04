package com.snofed.publicapp.ui.feedImage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.ImageAdapter
import com.snofed.publicapp.databinding.FragmentFeedViewImageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedViewImageFragment : Fragment() {
    private var _binding: FragmentFeedViewImageBinding? = null
    private val binding get() = _binding!!

    // Declare ViewPager2 as a lateinit property
    private lateinit var viewPager: ViewPager2
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_feed_view_image, container, false)
        _binding = FragmentFeedViewImageBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* binding.backBtn.setOnClickListener {
             it.findNavController().popBackStack()
         }*/

        // Initialize ViewPager2 property
        viewPager = binding.viewPager

        // Set up ViewPager2
        setupViewPager()
    }

    private fun setupViewPager() {
        // List of image resources
        val imageList = listOf(
            R.drawable.feed_view1,
            R.drawable.feed_view1,
            R.drawable.feed_view1,
            R.drawable.feed_view1,
            R.drawable.feed_view1
            // Add more images as needed
        )

       // val adapter = ImageAdapter(imageList)
        val adapter = ImageAdapter(imageList, imageList.size)
        viewPager.adapter = adapter
        // Initialize the image count text view
        updateImageCount(1, imageList.size)

        // Set a page change callback to update the image count text view
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // Update the image count text view
                updateImageCount(position + 1, imageList.size)
            }
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