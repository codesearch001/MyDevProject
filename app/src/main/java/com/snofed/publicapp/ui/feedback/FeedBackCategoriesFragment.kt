package com.snofed.publicapp.ui.feedback

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.snofed.publicapp.databinding.FragmentFeedBackCategoriesBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedBackCategoriesFragment : Fragment() {
    private var _binding: FragmentFeedBackCategoriesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_feed_back_categories, container, false)
        _binding = FragmentFeedBackCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}