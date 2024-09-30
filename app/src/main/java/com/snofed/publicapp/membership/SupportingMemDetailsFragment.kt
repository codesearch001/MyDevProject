package com.snofed.publicapp.membership

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentSupportingMemDetailsBinding
import com.snofed.publicapp.databinding.FragmentSupportingMemberListBinding
import com.snofed.publicapp.utils.ServiceUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupportingMemDetailsFragment : Fragment() {

    private var _binding: FragmentSupportingMemDetailsBinding? = null
    private val binding get() = _binding!!

    private var id: String? = null
    private var name: String? = null
    private var description: String? = null
    private var partnerLogoPath: String? = null
    private var partnerWebLink: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_supporting_mem_details, container, false)
        _binding = FragmentSupportingMemDetailsBinding.inflate(inflater, container, false)

        id = arguments?.getString("id").toString()
        name = arguments?.getString("name").toString()
        description = arguments?.getString("description").toString()
        partnerLogoPath = arguments?.getString("partnerLogoPath").toString()
        partnerWebLink = arguments?.getString("partnerWebLink").toString()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHamburger.setOnClickListener {
            it.findNavController().popBackStack()
        }
        // Optionally, load images using Glide or similar if you have partner logos
        if (partnerLogoPath == null) {
            Glide.with(binding.imgLogo).load(R.drawable.logo).into(binding.imgLogo)
        } else {
            Glide.with(binding.imgLogo)
                .load(ServiceUtil.BASE_URL_MEMB_IMAGE + "/" + partnerLogoPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(binding.imgLogo)
        }

        binding.toolbarTitle.text = name
        binding.txtSupportMemberName.text = name
        binding.txtSupportMemberDescription.text = description

        binding.btnBuyMemberships.setOnClickListener {
            if (partnerWebLink == "") {
                Toast.makeText(context, "Partner link not found", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(partnerWebLink))
                startActivity(intent)
            }
        }
    }
}