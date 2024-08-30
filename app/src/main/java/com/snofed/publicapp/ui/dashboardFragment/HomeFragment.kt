package com.snofed.publicapp.ui.dashboardFragment

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.snofed.publicapp.HomeDashBoardActivity
import com.snofed.publicapp.HomeNewActivity
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentHomeBinding
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.DrawerController
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedViewModel
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        //for using status bar space
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            );
        }
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
        // Get ViewModel instance


        binding.nameTextView.text = tokenManager.getUser()
        binding.btnPurchase.setOnClickListener {
            findNavController().navigate(R.id.purchaseHistoryFragment2)
            //startActivity(Intent(requireActivity(), HomeNewActivity::class.java))

        }

        binding.btnMembership.setOnClickListener {
            findNavController().navigate(R.id.membershipFragment)
            //startActivity(Intent(requireActivity(), HomeNewActivity::class.java))

        }

        binding.startBtn.setOnClickListener {
            findNavController().navigate(R.id.startMapRideFragment)

        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}