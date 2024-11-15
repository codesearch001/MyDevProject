package com.snofed.publicapp.ui.setting


import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.snofed.publicapp.databinding.FragmentProfileSettingBinding
import android.content.DialogInterface
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.snofed.publicapp.R
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.ClientPrefrences
import com.snofed.publicapp.utils.MediaReader
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.ServiceUtil
import com.snofed.publicapp.utils.SharedPreferenceKeys
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ProfileSettingFragment : Fragment(), MediaReader.OnImageUriReceivedListener{
    private var _binding: FragmentProfileSettingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var mediaReader: MediaReader
    private var imageView: ImageView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_profile_setting, container, false)
        _binding = FragmentProfileSettingBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize MediaReader with this fragment
        mediaReader = MediaReader(this,this)

        // Retrieve the saved image URL
        val savedImageUrl = AppPreference.getPreference(context, SharedPreferenceKeys.PREFS_PROFILE_FILE)
        savedImageUrl?.let {
            // Use Glide to set the image URL to the ImageView
            mediaReader.setImageView(binding.profileImageView, it)
        }
        // Set click listener on ImageView
        binding.profileImageView.setOnClickListener {
            showImageOptionsDialog()
        }


        binding.switchWifi.isChecked = ClientPrefrences.getSwitchState(requireContext(), SharedPreferenceKeys.SWITCH_WIFI_STATE)


        binding.switchCompat.isChecked = ClientPrefrences.getSwitchState(requireContext(), SharedPreferenceKeys.SWITCH_COMPAT_STATE)


        binding.switchWifi.setOnCheckedChangeListener { _, isChecked ->
            ClientPrefrences.saveSwitchState(requireContext(), SharedPreferenceKeys.SWITCH_COMPAT_STATE, isChecked)
        }


        binding.switchCompat.setOnCheckedChangeListener { _, isChecked ->
            ClientPrefrences.saveSwitchState(requireContext(), SharedPreferenceKeys.SWITCH_COMPAT_STATE, isChecked)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        imageResponse(uri)
        // Observe upload result
        viewModel.uploadResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkResult.Success -> {
                    val uploadMessage = result.data?.message ?: resources.getString(R.string.upload_successful)
                    // Construct the URL for the uploaded image
                    val imageUrl = ServiceUtil.BASE_URL_IMAGE + result.data?.data.toString()
                    AppPreference.savePreference(context, SharedPreferenceKeys.PREFS_PROFILE_FILE, imageUrl)


                   // Log.e("ProfileSettingFragment", "Upload successful: $uploadMessage")
                    Toast.makeText(requireActivity(), uploadMessage, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), result.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    // Show loading indicator if necessary
                    // binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun imageResponse(uri: Uri) {
        val userId = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_ID).toString()
        val imageFile = uriToFile(uri)

        if (imageFile != null) {
            viewModel.uploadProfileImage(userId, imageFile)

            Log.e("TAG_ProfileSettingFragment", "onImageUriReceived: File path - ${imageFile.absolutePath}")
        } else {
            Log.e("TAG_ProfileSettingFragment", "onImageUriReceived: Error converting URI to File")
        }
    }


    private fun uriToFile(uri: Uri): File? {
        val contentResolver = requireContext().contentResolver
        val tempFile = File.createTempFile("profile_image", ".jpg", requireContext().cacheDir)

        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                tempFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: Exception) {
            Log.e("MyFragment", "Error converting URI to File: ${e.message}")
            return null
        }

        return tempFile
    }

}