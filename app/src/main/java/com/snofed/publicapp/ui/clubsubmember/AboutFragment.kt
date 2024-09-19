package com.snofed.publicapp.ui.clubsubmember

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentAboutBinding
import com.snofed.publicapp.databinding.FragmentBrowseAllClubBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.AccessFunctions
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AboutFragment : Fragment() {

    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager
    // Create an instance of MyFunctions
    private val myFunctions = AccessFunctions()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_about, container, false)
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        // adapter = NoteAdapter(::onNoteClicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val htmlString  = tokenManager.getDesc().toString()
        val strippedString = myFunctions.removePTags(htmlString)
        binding.idDescription.text = strippedString
        println(strippedString) // Output: "This is a TEST RESORT"
    }


}