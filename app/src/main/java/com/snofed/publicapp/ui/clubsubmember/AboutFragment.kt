package com.snofed.publicapp.ui.clubsubmember

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentAboutBinding
import com.snofed.publicapp.databinding.FragmentBrowseAllClubBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_about, container, false)
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        // adapter = NoteAdapter(::onNoteClicked)
        return binding.root
    }


}