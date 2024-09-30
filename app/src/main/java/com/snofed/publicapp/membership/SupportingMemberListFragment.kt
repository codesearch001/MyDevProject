package com.snofed.publicapp.membership

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentSupportingMemberListBinding
import com.snofed.publicapp.membership.adapter.SupportMembershipAdapterr
import com.snofed.publicapp.models.membership.Benefit
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SupportingMemberListFragment : Fragment(), SupportMembershipAdapterr.OnItemClickListener {

    private var _binding: FragmentSupportingMemberListBinding? = null
    private val binding get() = _binding!!

    private var benefits: ArrayList<Benefit>? = null
    private lateinit var supportMemberAdapter: SupportMembershipAdapterr
    var membershipName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSupportingMemberListBinding.inflate(inflater, container, false)

        membershipName = arguments?.getString("membershipName").toString()
        benefits = arguments?.getParcelableArrayList("benefits")

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*  binding.card1.setOnClickListener {
            it.findNavController().navigate(R.id.supportingMemDetailsFragment)
        }*/
        binding.btnHamburger.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.txtSupportMemberName.text = membershipName
        benefits?.let {
            supportMemberAdapter = SupportMembershipAdapterr(emptyList(), this)
            binding.feedRecyclerView.layoutManager = LinearLayoutManager(requireActivity())
            binding.feedRecyclerView.adapter = supportMemberAdapter
            supportMemberAdapter.setFeed(it)
        }
    }

    override fun onItemClick(
        id: String,
        name: String,
        description: String,
        partnerLogoPath: String,
        partnerWebLink: String
    ) {
        val bundle = Bundle()
        // Put the id in the bundle
        bundle.putString("id", id)
        bundle.putString("name", name)
        bundle.putString("description", description)
        bundle.putString("partnerLogoPath", partnerLogoPath)
        bundle.putString("partnerWebLink", partnerWebLink)
        val destination = R.id.supportingMemDetailsFragment
        view?.findNavController()?.navigate(destination, bundle)
    }
}