package com.snofed.publicapp.ui.dashboardFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.snofed.publicapp.HomeDashBoardActivity
import com.snofed.publicapp.HomeNewActivity
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentHomeBinding
import com.snofed.publicapp.utils.DrawerController
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
  //  private val noteViewModel by viewModels<NoteViewModel>()

   // private lateinit var adapter: NoteAdapter
   @Inject
   lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
       // adapter = NoteAdapter(::onNoteClicked)
        binding.humburger.setOnClickListener {
            (activity as? DrawerController)?.openDrawer()
        }
        return binding.root
       // val view = binding.root
      //  return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.nameTextView.text =tokenManager.getUser()
        binding.btnPurchase.setOnClickListener {
            findNavController().navigate(R.id.purchaseHistoryFragment2)
            //startActivity(Intent(requireActivity(), HomeNewActivity::class.java))

        }

        binding.btnMembership.setOnClickListener {
            findNavController().navigate(R.id.membershipFragment)
            //startActivity(Intent(requireActivity(), HomeNewActivity::class.java))

        }

//        noteViewModel.getAllNotes()
//        binding.noteList.layoutManager =
//            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
//        binding.noteList.adapter = adapter



//        binding.btnMembership.setOnClickListener {
//          // findNavController().navigate(R.id.action_mainFragment_to_membershipFragment)//membershipFragment
//            // navController.navigate(R.id.action_mainFragment_to_membershipFragment)//membershipFragment
//           //navController.navigate(R.id.membershipFragment)
//           // Navigation.findNavController(it).navigate(R.id.membershipFragment)
//
//
//        }
//        bindObservers()
    }


//    private fun bindObservers() {
//        noteViewModel.notesLiveData.observe(viewLifecycleOwner, Observer {
//            binding.progressBar.isVisible = false
//            when (it) {
//                is NetworkResult.Success -> {
//                    adapter.submitList(it.data)
//                }
//                is NetworkResult.Error -> {
//                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
//                        .show()
//                }
//                is NetworkResult.Loading -> {
//                    binding.progressBar.isVisible = true
//                }
//            }
//        })
//    }
//
//    private fun onNoteClicked(noteResponse: NoteResponse){
//        val bundle = Bundle()
//        bundle.putString("note", Gson().toJson(noteResponse))
//        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
//    }
//
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}