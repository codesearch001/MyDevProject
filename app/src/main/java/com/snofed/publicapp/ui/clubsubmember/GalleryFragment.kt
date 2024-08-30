package com.snofed.publicapp.ui.clubsubmember

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.snofed.publicapp.adapter.GalleryAdapter
import com.snofed.publicapp.databinding.FragmentGalleryBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val galleryViewModel by viewModels<AuthViewModel>()
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private lateinit var galleryAdapter: GalleryAdapter
    @Inject
    lateinit var tokenManager: TokenManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_gallery, container, false)
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
//        Log.i("galleryFragment..1", "galleryFragmentImages..1 ")
//        Log.e("size of array","bkgllery111 "+ galleryViewModel.mutableData.galleryImage.value)
//        /*galleryViewModel.mutableData.dataa.observe(viewLifecycleOwner, Observer { neitem->
//            Log.e("size of array","bkgllery222"+ neitem.size)
//            // galleryAdapter.setGallery(galleryViewModel.mutableData.galleryImage.value)
//        })*/
//        galleryAdapter= GalleryAdapter()
//        //binding.galleryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//        binding.galleryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
//        binding.galleryRecyclerView.adapter = galleryAdapter

     /*   fetchResponse()
        galleryViewModel.subClubLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {

                    // Example of setting data
                    //galleryViewModel.mutableData.galleryImage.postValue(it.data?.data?.publicData)
                    //clubViewModel.mutableData.updateData(it.data?.data?.publicData?.images!!)
                    Log.i("PraveenGallery22222" , "gggggg222 " +it.data?.data?.publicData?.images)
                    //Log.i("it.data?.clients/publicData", "it.data?.clients.publicData " + it.data?.data?.publicData?.images)

//                    galleryAdapter = GalleryAdapter()
//                    //binding.galleryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//                    binding.galleryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
//                    binding.galleryRecyclerView.adapter = galleryAdapter
//                    galleryAdapter.setGallery(it.data?.data?.publicData?.images)
                    // Check if the data is null or empty
                    val images = it.data?.data?.publicData?.images
                    if (images.isNullOrEmpty()) {
                        // Handle the "data not found" case
                        binding.tvSplashText.isVisible = true
                        //Toast.makeText(requireActivity(), "No data available", Toast.LENGTH_SHORT).show()
                        galleryAdapter = GalleryAdapter()
                        binding.galleryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                        binding.galleryRecyclerView.adapter = galleryAdapter
                        galleryAdapter.setGallery(emptyList()) // Pass an empty list to the adapter
                    } else {
                        // Normal case: data is present
                        Log.i("PraveenGallery22222", "Data: $images")
                        binding.tvSplashText.isVisible = false
                        galleryAdapter = GalleryAdapter()
                        binding.galleryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
                        binding.galleryRecyclerView.adapter = galleryAdapter
                        galleryAdapter.setGallery(images)
                    }
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), it.message.toString(), Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

            }
        })*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView and Adapter
        galleryAdapter = GalleryAdapter()
        binding.galleryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.galleryRecyclerView.adapter = galleryAdapter


        // Observe the SharedViewModel for data updates
        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner) { response ->
            val images = response?.data?.publicData?.images ?: emptyList()

            Log.d("Tag_Gallery", "GalleryImages size: ${images.size}")

            if (images.isEmpty()) {

                // Show the "Data Not data" message and hide RecyclerView
                binding.tvSplashText.visibility = View.VISIBLE
                binding.galleryRecyclerView.visibility = View.GONE

            } else {

                // Hide the "No data" message and show RecyclerView
                binding.tvSplashText.visibility = View.GONE
                binding.galleryRecyclerView.visibility = View.VISIBLE
                galleryAdapter.setGallery(images) // Pass the list to the adapter
            }
        }
    }
  /*  private fun fetchResponse() {
        galleryViewModel.subClubRequestUser(tokenManager.getClientId().toString())
    }*/

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}