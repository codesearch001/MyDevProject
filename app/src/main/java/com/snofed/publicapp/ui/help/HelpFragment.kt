package com.snofed.publicapp.ui.help

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.snofed.publicapp.R
import com.snofed.publicapp.adapter.ExpandableAdapter
import com.snofed.publicapp.databinding.FragmentHelpBinding
import com.snofed.publicapp.databinding.FragmentPurchaseHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HelpFragment : Fragment() {
    var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_help, container, false)
        _binding = FragmentHelpBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        val categories = listOf(
            Category("Track4 Outdoors App - User Guide", listOf("Item 1.1", "Item 1.2")),
            Category("Buying tickets in the Track4 Outdoors App", listOf("Item 2.1", "Item 2.2")),
            Category("Installation of Track4 Outdoors App - Apple iOS", listOf("Item 2.1", "Item 2.2")),
            // Add more categories here
        )

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = ExpandableAdapter(categories)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

