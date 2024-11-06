package com.snofed.publicapp.ui.setting


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.snofed.publicapp.databinding.FragmentProfileSettingBinding
import android.app.Activity
import android.content.DialogInterface
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.ImageUriCallback
import com.snofed.publicapp.utils.MediaReader
import com.snofed.publicapp.utils.SharedPreferenceKeys
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProfileSettingFragment : Fragment(),ImageUriCallback {
    private var _binding: FragmentProfileSettingBinding? = null
    private val binding get() = _binding!!

    private lateinit var mediaReader: MediaReader
    private var imageView: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_profile_setting, container, false)
        _binding = FragmentProfileSettingBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize MediaReader with this fragment
        mediaReader = MediaReader(this,this)
        mediaReader.setImageView(binding.profileImageView)

        // Set click listener on ImageView
        binding.profileImageView.setOnClickListener {
            showImageOptionsDialog()
        }
        binding.changePassword.setOnClickListener {
            it.findNavController().navigate(R.id.recoverFragment)
        }

            binding.txtFirstName.text =  AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_FIRST_NAME)
            binding.txtLastName.text =AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_LAST_NAME)
            binding.txtUserAge.text = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_AGE)
            binding.txtUserWeight.text = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_WEIGHT) + " Kg"
            binding.txtUserGender.text = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_GENDER_TYPE)


        return view
    }

    private fun showImageOptionsDialog() {
        val options = arrayOf(resources.getString(R.string.take_photo), resources.getString(R.string.choose_from_gallery))
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.t_select_image_source))
        builder.setItems(options) { _: DialogInterface, which: Int ->
            when (which) {
                0 -> mediaReader.checkPermissionsAndOpenCamera() // Take Photo
                1 -> mediaReader.checkPermissionsAndOpenGallery() // Choose from Gallery
            }
        }
        builder.show()
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onImageUriReceived(uri: Uri) {
        imageView?.setImageURI(uri)
    }
}