package com.snofed.publicapp.ui.clubsubmember

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.GalleryAdapter
import com.snofed.publicapp.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_gallery, container, false)
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemList = listOf(
            GalleryAdapter.MyItem(R.drawable.gallery1),
            GalleryAdapter.MyItem(R.drawable.gallery2),
            GalleryAdapter.MyItem(R.drawable.gallery1),
            GalleryAdapter.MyItem(R.drawable.gallery1),
            GalleryAdapter.MyItem(R.drawable.gallery2),
            GalleryAdapter.MyItem(R.drawable.gallery1),
            GalleryAdapter.MyItem(R.drawable.gallery1),
            GalleryAdapter.MyItem(R.drawable.gallery2),
            GalleryAdapter.MyItem(R.drawable.gallery1),
            GalleryAdapter.MyItem(R.drawable.gallery1),
            GalleryAdapter.MyItem(R.drawable.gallery1),
            GalleryAdapter.MyItem(R.drawable.gallery2),
        )

        val adapter = GalleryAdapter(itemList)
       // binding.galleryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.galleryRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.galleryRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}