package com.snofed.publicapp.ui.socialmedia

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.snofed.publicapp.databinding.FragmentTwitterBinding
import com.snofed.publicapp.ui.links.LinksDocAdapter
import com.snofed.publicapp.utils.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TwitterFragment : Fragment() {
    private var _binding: FragmentTwitterBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var linksAdapter: LinksDocAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_twitter, container, false)
        _binding = FragmentTwitterBinding.inflate(inflater, container, false)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner) { response ->
            val socialMediaLinks = response?.data?.publicData?.socialMediaLinks


            // Set up click listeners for each social media icon
            socialMediaLinks?.let { links ->
                // Twitter link
                binding.twitterImage.setOnClickListener {
                    links.twitterLink.takeIf { it.isNotEmpty() }?.let { twitterLink ->
                        openLink(twitterLink)
                    } ?: showToast("Twitter link not available")
                }

                // Facebook link
                binding.facebookImage.setOnClickListener {
                    links.facebookLink.takeIf { it.isNotEmpty() }?.let { facebookLink ->
                        openLink(facebookLink)
                    } ?: showToast("Facebook link not available")
                }

                // Instagram link
                binding.instagramImage.setOnClickListener {
                    links.instagramLink.takeIf { it.isNotEmpty() }?.let { instagramLink ->
                        openLink(instagramLink)
                    } ?: showToast("Instagram link not available")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // Function to open a link in a browser or app
    private fun openLink(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            showToast("No application found to open this link")
        }
    }




}