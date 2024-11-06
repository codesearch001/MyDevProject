package com.snofed.publicapp.ui.links

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.EventClubFeedAdapter
import com.snofed.publicapp.databinding.FragmentLinksBinding
import com.snofed.publicapp.databinding.FragmentMapExploreBinding
import com.snofed.publicapp.ui.event.EventTrailsFeedAdapter
import com.snofed.publicapp.utils.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LinksFragment : Fragment(), LinksDocAdapter.OnItemClickListener {

    private var _binding: FragmentLinksBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel by activityViewModels<SharedViewModel>()
    private lateinit var linksAdapter: LinksDocAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_links, container, false)
        _binding = FragmentLinksBinding.inflate(inflater, container, false)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        linksAdapter = LinksDocAdapter(this)
        binding.eventRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.eventRecyclerView.adapter = linksAdapter
        // Observe the SharedViewModel for data updates
        sharedViewModel.browseSubClubResponse.observe(viewLifecycleOwner) { response ->
            val links = response?.data?.publicData?.links ?: emptyList()

            Log.d("LinksFragment", "links: ${links.size}")

            if (links.isEmpty()) {
                // Show the "Data Not data" message and hide RecyclerView
                binding.tvSplashText.visibility = View.VISIBLE
                binding.eventRecyclerView.visibility = View.GONE

            } else {
                // Hide the "No data" message and show RecyclerView
                binding.tvSplashText.visibility = View.GONE
                binding.eventRecyclerView.visibility = View.VISIBLE
                linksAdapter.setEvent(links)
            }
        }
    }

    override fun onItemClick(eventId: String, link: String) {
        val spannableString = SpannableString(link)

        val linkSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                startActivity(browserIntent)
            }
        }
        spannableString.setSpan(linkSpan, 0, link.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

    }

}