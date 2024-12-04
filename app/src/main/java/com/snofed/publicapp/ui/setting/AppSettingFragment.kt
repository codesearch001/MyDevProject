package com.snofed.publicapp.ui.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentAppSettingBinding
import com.snofed.publicapp.databinding.FragmentProfileSettingBinding
import com.snofed.publicapp.utils.getAppVersion
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AppSettingFragment : Fragment() {
    private var _binding: FragmentAppSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_app_setting, container, false)
        _binding = FragmentAppSettingBinding.inflate(inflater, container, false)
        binding.appVersion.text = getAppVersion(requireActivity())?.versionName
        val view = binding.root
        return view
    }


}