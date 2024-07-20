package com.snofed.publicapp.ui.dashboardFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentBrowseAllClubBinding
import com.snofed.publicapp.databinding.FragmentBrowseClubBinding
import com.snofed.publicapp.databinding.FragmentClubSubMembersBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BrowseAllClubFragment : Fragment() {

    private var _binding: FragmentBrowseAllClubBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentBrowseAllClubBinding.inflate(inflater, container, false)
        // adapter = NoteAdapter(::onNoteClicked)
        return binding.root
        // val view = binding.root
        //  return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.cardIdLayout.setOnClickListener {
            findNavController().navigate(R.id.clubSubMembersFragment)
            //startActivity(Intent(requireActivity(), HomeNewActivity::class.java))

        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}