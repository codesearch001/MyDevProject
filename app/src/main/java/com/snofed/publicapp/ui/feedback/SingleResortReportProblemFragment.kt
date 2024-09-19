package com.snofed.publicapp.ui.feedback

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentSingleResortReportProblemBinding
import com.snofed.publicapp.databinding.FragmentSingleResortReportProblemChooseLocationBinding
import com.snofed.publicapp.ui.login.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleResortReportProblemFragment : Fragment() {
    private var _binding: FragmentSingleResortReportProblemBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<AuthViewModel>()
    private var categoryID: String? = null
    private var categoryName: String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_single_resort_report_problem, container, false)
        _binding = FragmentSingleResortReportProblemBinding.inflate(inflater, container, false)
        categoryID = arguments?.getString("CATEGORY_ID").toString()
        categoryName= arguments?.getString("CATEGORY_NAME").toString()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }
        binding.txtReportProblem.text = categoryName
        init()
    }

    private fun init() {
        binding.chooseLocationBtn.setOnClickListener {
            var description = binding.edtReportProblem.text.toString().trim()
            if (description.isEmpty()) {
                Toast.makeText(requireActivity(), getString(R.string.t_please_enter_description), Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            // Create a Bundle to send data to the next fragment
            val bundle = Bundle().apply {
                putString("description", description) // Add the description to the bundle
                putString("CATEGORY_ID", categoryID) // Add the description to the bundle
            }
            description = ""

            // Navigate to the next fragment, passing the data
            it.findNavController().navigate(R.id.singleResortReportProblemChooseLocationFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}