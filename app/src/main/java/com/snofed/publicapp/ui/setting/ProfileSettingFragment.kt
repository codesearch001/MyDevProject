package com.snofed.publicapp.ui.setting


import RealmRepository
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.snofed.publicapp.databinding.FragmentProfileSettingBinding
import android.content.DialogInterface
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.snofed.publicapp.R
import com.snofed.publicapp.models.User
import com.snofed.publicapp.models.toUser
import com.snofed.publicapp.ui.User.UserViewModelRealm
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

    private val authViewModel by activityViewModels<AuthViewModel>()
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

        binding.selectProfileImage.setOnClickListener {
            showImageOptionsDialog()
        }

        binding.switchWifi.isChecked =
            ClientPrefrences.getSwitchState(requireContext(), SharedPreferenceKeys.SWITCH_WIFI_STATE)


        binding.switchCompat.isChecked =
            ClientPrefrences.getSwitchState(requireContext(), SharedPreferenceKeys.SWITCH_COMPAT_STATE)


        binding.switchWifi.setOnCheckedChangeListener { _, isChecked ->
            ClientPrefrences.saveSwitchState(requireContext(), SharedPreferenceKeys.SWITCH_COMPAT_STATE, isChecked)


            val userId = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_ID).toString()

            val realmRepository = RealmRepository()
            val userViewModelRealm = UserViewModelRealm(realmRepository)
            val userDTO = userViewModelRealm.getUserDTOById(userId)
            // Update settings before saving
            authViewModel.updateUser(userDTO!!.toUser());
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
        binding.txtFirstName.setOnClickListener {
            showEditablePopup( EditType.FIRST_NAME, "", binding.txtFirstName.text.toString()) { newValue ->
                binding.txtFirstName.text = newValue
            }
        }

        binding.txtLastName.setOnClickListener {
            showEditablePopup(EditType.LAST_NAME, "", binding.txtLastName.text.toString()) { newValue ->
                binding.txtLastName.text = newValue
            }
        }

        binding.txtUserGender.setOnClickListener {
            showGenderSelectionPopup(EditType.GENDER, "") { selectedGender ->
                binding.txtUserGender.text = selectedGender
            }
        }
    }

    private fun showEditablePopup(field: EditType, message: String, currentValue: String, onSave: (String) -> Unit) {
        val inputField = EditText(requireContext()).apply {
            setText(currentValue)
        }

        AlertDialog.Builder(requireContext())
            .setTitle(field.toString())
            .setMessage(message)
            .setView(inputField)
            .setPositiveButton("Save") { dialog, which ->
                val newValue = inputField.text.toString()
                onSave(newValue) // Update the field with the new value

                saveToRealmAndServer(newValue, field)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showGenderSelectionPopup(field: EditType, message: String, onSave: (String) -> Unit) {
        // Create a RadioGroup for gender options
        val radioGroup = RadioGroup(requireContext()).apply {
            orientation = RadioGroup.VERTICAL

            // Create radio buttons for Male, Female, Other
            val radioMale = RadioButton(requireContext()).apply {
                text = "Male"
            }
            val radioFemale = RadioButton(requireContext()).apply {
                text = "Female"
            }
            val radioOther = RadioButton(requireContext()).apply {
                text = "Other"
            }

            // Add radio buttons to the RadioGroup
            addView(radioMale)
            addView(radioFemale)
            addView(radioOther)
        }

        // Show the dialog with the radio buttons
        AlertDialog.Builder(requireContext())
            .setTitle(field.toString())
            .setMessage(message)
            .setView(radioGroup)
            .setPositiveButton("Save") { dialog, which ->
                // Get selected radio button text and pass it to onSave callback
                val selectedRadioButton = radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                onSave(selectedRadioButton?.text.toString()) // Update the gender field with the selected option
            //Save to Realm
            // Call the API
            //saveToRealmAndServer(selectedRadioButton?.text.toString(), title)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveToRealmAndServer(newValue: String, field: EditType) {
        val userId = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_ID).toString()

        val realmRepository = RealmRepository()
        val userViewModelRealm = UserViewModelRealm(realmRepository)
        val user = userViewModelRealm.getUserById(userId)
        //user!!.firstName = newValue
        userViewModelRealm.updateUser(userId, user!!)
        // Save the updated user back to Realm


        val userDTO = userViewModelRealm.getUserDTOById(userId)

        if(EditType.FIRST_NAME == field){
            userDTO!!.firstName = newValue
        }
        else if(EditType.LAST_NAME == field){
            userDTO!!.lastName = newValue
        }
        else if(EditType.GENDER == field){
            userDTO!!.gender = newValue?.toInt()
        }

        //Save to realm
        //val updatedUserRealm = userDTO.toUserRealm()
        //userViewModelRealm.updateUser(userId, updatedUserRealm)
        //userViewModelRealm.updateUser(userId, user!!)
        // Call the API

        authViewModel.updateUser(userDTO!!.toUser());
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
                    Log.e("ProfileSettingFragment", "Upload successful: $uploadMessage")
                    // Construct the URL for the uploaded image
                    val imageUrl = ServiceUtil.BASE_URL_IMAGE + result.data?.data.toString()
                    AppPreference.savePreference(context, SharedPreferenceKeys.PREFS_PROFILE_FILE, imageUrl)

                    mediaReader.setImageView(binding.profileImageView,imageUrl)

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

enum class EditType{
    FIRST_NAME,LAST_NAME,GENDER
}
