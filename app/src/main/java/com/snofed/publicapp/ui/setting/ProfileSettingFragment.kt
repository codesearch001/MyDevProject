package com.snofed.publicapp.ui.setting


import RealmRepository
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.snofed.publicapp.R
import com.snofed.publicapp.databinding.FragmentProfileSettingBinding
import com.snofed.publicapp.dto.PublicUserSettingsDTO
import com.snofed.publicapp.models.toUser
import com.snofed.publicapp.ui.User.UserViewModelRealm
import com.snofed.publicapp.ui.login.AuthViewModel
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.MediaReader
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.ServiceUtil
import com.snofed.publicapp.utils.SharedPreferenceKeys
import com.snofed.publicapp.utils.SnofedUtils.Companion.getAgeFromBirthYear
import com.snofed.publicapp.utils.SnofedUtils.Companion.getBirthYearFromAge
import com.snofed.publicapp.utils.SnofedUtils.Companion.getYearFromDate
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ProfileSettingFragment : Fragment(), MediaReader.OnImageUriReceivedListener{
    private var _binding: FragmentProfileSettingBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by activityViewModels<AuthViewModel>()
    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var mediaReader: MediaReader

    private lateinit var viewModelUserRealm: UserViewModelRealm
    // String arrays
    var autoPauseSpeedsArray: Array<String> = emptyArray()
    var unitsArray: Array<String> = emptyArray()
    var gendersArray: Array<String> = emptyArray()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_profile_setting, container, false)
        _binding = FragmentProfileSettingBinding.inflate(inflater, container, false)
        val view = binding.root

        // Initialize MediaReader with this fragment
        mediaReader = MediaReader(this,this)


        // Set click listener on ImageView
        binding.profileImageView.setOnClickListener {
            showImageOptionsDialog()
        }

        binding.selectProfileImage.setOnClickListener {
            showImageOptionsDialog()
        }

        binding.changePassword.setOnClickListener {
            it.findNavController().navigate(R.id.recoverFragment)
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_ID).toString()

        // Retrieve the saved image URL
        val savedImageUrl = ServiceUtil.BASE_URL_IMAGE + viewModelUserRealm.getPublicUserSettingValue(userId,"Image")
        //Log.e("IMAGE","IMAGE"+savedImageUrl)
        savedImageUrl?.let {
            // Use Glide to set the image URL to the ImageView
            mediaReader.setImageView(binding.profileImageView, it)
        }
        fillStringArrays()

        var userGender = if (viewModelUserRealm.getUserById(userId)?.gender.toString().equals("1", ignoreCase = true))  gendersArray[1] else gendersArray[0]
        var userweight = viewModelUserRealm.getUserById(userId)?.weight.toString()
        var userAge = viewModelUserRealm.getUserById(userId)?.age.toString()

        binding.txtFirstName.text =  viewModelUserRealm.getUserById(userId)?.firstName
        binding.txtLastName.text = viewModelUserRealm.getUserById(userId)?.lastName
        binding.txtUserAge.text = if(userAge.isNullOrEmpty()) "0" else getAgeFromBirthYear(userAge.toInt()).toString()
        binding.txtUserWeight.text =  if(userweight.isNullOrEmpty()) "0" else "$userweight Kg"
        binding.txtUserGender.text = userGender

        Log.e("GENDER","GENDER_GET_REALM "+ viewModelUserRealm.getUserById(userId)?.gender.toString())

        binding.txtFirstName.setOnClickListener {
            showEditablePopup( EditType.FIRSTNAME, "", binding.txtFirstName.text.toString()) { newValue ->
                binding.txtFirstName.text = newValue
            }
        }

        binding.txtLastName.setOnClickListener {
            showEditablePopup(EditType.LASTNAME, "", binding.txtLastName.text.toString()) { newValue ->
                binding.txtLastName.text = newValue
            }
        }

        binding.txtUserAgeArrow.setOnClickListener {
            showAgeSelectionPopup(EditType.AGE, resources.getString(R.string.app_age_settings), binding.txtUserAge.text.toString()) { selectedAge ->
                binding.txtUserAge.text = getAgeFromBirthYear(selectedAge!!.toInt()).toString()
            }
        }

        binding.txtUserWeightArrow.setOnClickListener {
            showWeightSelectionPopup(EditType.WEIGHT, resources.getString(R.string.app_Weight_settings), userweight.toString()) { selectedWeight ->
                binding.txtUserWeight.text = selectedWeight + " Kg"
            }
        }
        binding.txtUserGenderArrow.setOnClickListener {
            showGenderSelectionPopup(EditType.GENDER, resources.getString(R.string.pref_gender_title), binding.txtUserGender.text.toString()) { selectedGender ->
                binding.txtUserGender.text = selectedGender
            }
        }

        val switchScreen = viewModelUserRealm.getPublicUserSettingValue(userId,"ScreenAlwaysOn")
        val switchWifiNew = viewModelUserRealm.getPublicUserSettingValue(userId,"SyncOnWifi")
        val switchAutoSpeedTxt = viewModelUserRealm.getPublicUserSettingValue(userId,"AutoPauseSpeed")
        //val finalAutoSpeedTxt = if(switchAutoSpeedTxt.isNullOrEmpty()) "0" else if(switchAutoSpeedTxt == "0") "2" else "4"

        Log.e("AUTOSPEED","switch auto speed "+switchAutoSpeedTxt)

        // Set Log
        binding.switchWifi.isChecked = if (switchWifiNew.equals("true", ignoreCase = true)) true else false

        binding.switchAutoSpeedTxt.text = switchAutoSpeedTxt

        binding.switchScreenOn.isChecked = if (switchScreen.equals("true", ignoreCase = true)) true else false

        binding.switchWifi.setOnCheckedChangeListener { _, isChecked ->

//            val realmRepository = RealmRepository()
//            val userViewModelRealm = UserViewModelRealm(realmRepository)

            // Update settings before saving
            viewModelUserRealm.updatePublicUserSetting(userId, PublicUserSettingsDTO("SyncOnWifi", isChecked.toString()))

            val userDTO = viewModelUserRealm.getUserDTOById(userId)

            authViewModel.updateUser(userDTO!!.toUser())
        }


        binding.switchScreenOn.setOnCheckedChangeListener { _, isChecked ->

//            val realmRepository = RealmRepository()
//            val userViewModelRealm = UserViewModelRealm(realmRepository)

            // Update settings before saving
            viewModelUserRealm.updatePublicUserSetting(userId, PublicUserSettingsDTO("ScreenAlwaysOn", isChecked.toString()))

            var userDTO = viewModelUserRealm.getUserDTOById(userId)

            authViewModel.updateUser(userDTO!!.toUser())
        }

        binding.switchAutoSpeedArrow.setOnClickListener{
            showLogSpeedSelectionPopup(EditType.AUTOPAUSESPEED, resources.getString(R.string.speed), switchAutoSpeedTxt.toString()) { selectedSpeed ->
                binding.switchAutoSpeedTxt.text = selectedSpeed
            }
        }

    }

    private fun fillStringArrays() {
        autoPauseSpeedsArray = resources.getStringArray(R.array.pref_autopause_speed_items)
        gendersArray = resources.getStringArray(R.array.pref_gender_items)
    }

    private fun showEditablePopup(field: EditType, message: String, currentValue: String, onSave: (String) -> Unit) {
        // Create an EditText for user input
        val inputField = TextInputEditText(requireContext()).apply {
            setText(currentValue)
            //hint = "Enter ${field.toString().replace("_", " ").lowercase()}" // Add a helpful hint
            inputType = InputType.TYPE_CLASS_TEXT // Adjust input type based on expected input
            setPadding(20, 10, 20, 40) // Add padding for better spacing
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            background = ContextCompat.getDrawable(requireContext(), android.R.color.transparent)
        }

        // Wrap EditText in a TextInputLayout for better visual appearance
        val textInputLayout = TextInputLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(20,0,20,0)
            //isHintEnabled = true // Enable hint above input field
            addView(inputField)
        }

        // Create a parent layout for spacing and alignment
        val parentLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            setPadding(50, 20, 50, 20) // Add margins around the dialog box content
            addView(textInputLayout)
        }


        AlertDialog.Builder(requireContext())
            //.setTitle(field.toString().replace("_"," "))
            .setMessage(message)
            .setView(parentLayout)
            .setPositiveButton(resources.getString(R.string.Save)) { dialog, which ->
                val newValue = inputField.text.toString().trim()
                if (newValue.isEmpty()) {
                    Toast.makeText(requireContext(), resources.getString(R.string.field_not_empty), Toast.LENGTH_SHORT).show()
                }
                else if(newValue.length < 3){
                    Toast.makeText(requireContext(), resources.getString(R.string.name_min_length), Toast.LENGTH_SHORT).show()
                }
                else {
                    onSave(newValue) // Update the field with the new value
                    saveToRealmAndServer(newValue, field)
                }
            }
            .setNegativeButton(resources.getString(R.string.cancel), null)
            .show()
    }

    private fun showAgeSelectionPopup(field: EditType, message: String,currentValue: String,  onSave: (String) -> Unit) {
        val newCal = if (currentValue.isNullOrEmpty()) 2000 else getBirthYearFromAge(currentValue.toInt())
        val numberPicker = NumberPicker(requireContext()).apply {
            minValue = 1900 // Set the minimum value
            maxValue = getYearFromDate() - 4// Set the maximum value
            value = newCal // Set default value (optional)
            wrapSelectorWheel = true // Enable or disable cycling of values
            setPadding(20, 10, 20, 40) // Add padding for better spacing
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        // Show the dialog with the NumberPicker
        AlertDialog.Builder(requireContext())
            //.setTitle(field.toString())
            .setMessage(message)
            .setView(numberPicker)
            .setPositiveButton(resources.getString(R.string.Save)) { dialog, which ->
                // Pass the selected value to the onSave callback
                onSave(numberPicker.value.toString())
                // Save to Realm and server
                saveToRealmAndServer(numberPicker.value.toString(), field)
            }
            .setNegativeButton(resources.getString(R.string.cancel), null)
            .show()
    }

    private fun showWeightSelectionPopup(field: EditType, message: String,currentValue: String?,  onSave: (String) -> Unit) {
        val newCal = if (currentValue.isNullOrEmpty()) 2000 else currentValue.toInt()
        val numberPicker = NumberPicker(requireContext()).apply {
            minValue = 10 // Set the minimum value
            maxValue = 300 // Set the maximum value
            value = newCal // Set default value (optional)
            wrapSelectorWheel = true // Enable or disable cycling of values
            setPadding(20, 10, 20, 40) // Add padding for better spacing
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }

        // Show the dialog with the NumberPicker
        AlertDialog.Builder(requireContext())
            //.setTitle(field.toString())
            .setMessage(message)
            .setView(numberPicker)
            .setPositiveButton(resources.getString(R.string.Save)) { dialog, which ->
                // Pass the selected value to the onSave callback
                onSave(numberPicker.value.toString())
                // Save to Realm and server
                saveToRealmAndServer(numberPicker.value.toString(), field)
            }
            .setNegativeButton(resources.getString(R.string.cancel), null)
            .show()
    }

    private fun showGenderSelectionPopup(field: EditType, message: String,currentValue: String,  onSave: (String) -> Unit) {
        // Create a RadioGroup for gender options
        val radioGroup = RadioGroup(requireContext()).apply {
            orientation = RadioGroup.VERTICAL
            // Create radio buttons for Male, Female, Other
            val radioMale = RadioButton(requireContext()).apply {
                text = gendersArray[0]
                id = View.generateViewId()
            }
            val radioFemale = RadioButton(requireContext()).apply {
                text = gendersArray[1]
                id = View.generateViewId()
            }
            setPadding(40, 10, 20, 40) // Add padding for better spacing
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            val txtMale = gendersArray[0]
            val txtFemale = gendersArray[1]
            // Add radio buttons to the RadioGroup
            addView(radioMale)
            addView(radioFemale)
            when (currentValue.lowercase()) {
                txtMale -> check(radioMale.id) // Select "Male" if currentValue is "male"
                txtFemale -> check(radioFemale.id) // Select "Female" if currentValue is "female"
                else -> check(radioMale.id) // Default to "Male" if no valid currentValue
            }
        }

        // Show the dialog with the radio buttons
        AlertDialog.Builder(requireContext())
            //.setTitle(field.toString())
            .setMessage(message)
            .setView(radioGroup)
            .setPositiveButton(resources.getString(R.string.Save)) { dialog, which ->
                // Get selected radio button text and pass it to onSave callback
                val selectedRadioButton = radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                onSave(selectedRadioButton?.text.toString()) // Update the gender field with the selected option
            //SAVE TO REALM AND SERVER
            saveToRealmAndServer(selectedRadioButton?.text.toString(), field)
            }
            .setNegativeButton(resources.getString(R.string.cancel), null)
            .show()
    }

    private fun showLogSpeedSelectionPopup(field: EditType, message: String,currentValue: String,  onSave: (String) -> Unit) {
        // Create a RadioGroup for gender options
        val radioGroup = RadioGroup(requireContext()).apply {
            orientation = RadioGroup.VERTICAL
            // Create radio buttons for Male, Female, Other
            val radio2 = RadioButton(requireContext()).apply {
                text = "2 km/h"
                id = View.generateViewId()
            }
            val radio4 = RadioButton(requireContext()).apply {
                text = "4 km/h"
                id = View.generateViewId()
            }
            setPadding(40, 10, 20, 40) // Add padding for better spacing
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            // Add radio buttons to the RadioGroup
            addView(radio2)
            addView(radio4)
            when (currentValue.lowercase()) {
                "2 km/h" -> check(radio2.id)
                "4 km/h" -> check(radio4.id)
                else -> check(radio2.id)
            }
        }

        // Show the dialog with the radio buttons
        AlertDialog.Builder(requireContext())
            //.setTitle(field.toString())
            .setMessage(message)
            .setView(radioGroup)
            .setPositiveButton(resources.getString(R.string.Save)) { dialog, which ->
                // Get selected radio button text and pass it to onSave callback
                val selectedRadioButton = radioGroup.findViewById<RadioButton>(radioGroup.checkedRadioButtonId)
                onSave(selectedRadioButton?.text.toString()) // Update the gender field with the selected option
                //SAVE TO REALM AND SERVER
                saveToRealmAndServer(selectedRadioButton?.text.toString(), field)
            }
            .setNegativeButton(resources.getString((R.string.cancel)), null)
            .show()
    }

    private fun saveToRealmAndServer(newValue: String, field: EditType) {
        val userId = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_ID).toString()

//        val realmRepository = RealmRepository()
//        val userViewModelRealm = UserViewModelRealm(realmRepository)
//        val realm = realmRepository.getRealmInstance() // Get a Realm instance from your repository

        val userDTO = viewModelUserRealm.getUserDTOById(userId)

        if(EditType.FIRSTNAME == field){
            userDTO!!.firstName = newValue
        }
        else if(EditType.LASTNAME == field){
            userDTO!!.lastName = newValue
        }
        else if(EditType.GENDER == field){
            var checkText = gendersArray[0]
            userDTO?.gender = if (newValue.equals(checkText, ignoreCase = true)) 0 else 1
        }
        else if(EditType.AGE == field){
            userDTO?.age = newValue?.toInt()
        }
        else if(EditType.WEIGHT == field){
            userDTO?.weight = newValue?.toInt()
        }
        else if(EditType.AUTOPAUSESPEED == field){
            //val updateValue = if (newValue.equals("2 km/h", ignoreCase = true)) 0 else 1
            viewModelUserRealm.updatePublicUserSetting(userId, PublicUserSettingsDTO("AutoPauseSpeed", newValue.toString()))
        }

        //Update the UserRealm
        if(EditType.AUTOPAUSESPEED != field) {
            viewModelUserRealm.updateUser(userId, userDTO!!)
        }

        var sendUserDTO = viewModelUserRealm.getUserDTOById(userId)
        

        // Call the API to save to Server
        authViewModel.updateUser(sendUserDTO!!.toUser());
    }

    fun SaveUserPublicSettings(type: EditType, value: String,realmRepository: RealmRepository,userViewModelRealm: UserViewModelRealm){
        val userId = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_ID).toString()
        val realm = realmRepository.getRealmInstance() // Get a Realm instance from your repository
        realm.executeTransaction { transactionRealm ->
            val userRealm = userViewModelRealm.getUserById(userId!!)
            userRealm?.let {
                // Find and update the setting with a specific key
                it.publicUserSettings?.forEach { setting ->
                    if (setting.key == "desiredKey") {
                        setting.value = value  // Modify the value of the matching setting
                    }
                }
                transactionRealm.insertOrUpdate(it)
            }
        }
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
                    //("ProfileSettingFragment", "Upload successful: $uploadMessage")
                    // Construct the URL for the uploaded image
                    val imageResponseUri = result.data?.data.toString()
                    val imageUrl = ServiceUtil.BASE_URL_IMAGE + imageResponseUri
                    //AppPreference.savePreference(context, SharedPreferenceKeys.PREFS_PROFILE_FILE, imageUrl)
                    //Log.e("IMAGE_UPLOAD","IMAGE NEW"+imageUrl)
                    //Update UserRealm Settings
                    val userId = AppPreference.getPreference(requireActivity(), SharedPreferenceKeys.USER_USER_ID).toString()
//                    val realmRepository = RealmRepository()
//                    val userViewModelRealm = UserViewModelRealm(realmRepository)

                    // Update image uri to publicUserSettings in UserRealm
                    viewModelUserRealm.updatePublicUserSetting(userId, PublicUserSettingsDTO("Image", imageResponseUri))

                    //Commented, no need to bind the image
                    //mediaReader.setImageView(binding.profileImageView,imageUrl)

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
    FIRSTNAME,LASTNAME,AGE,WEIGHT,GENDER, AUTOPAUSESPEED
}

enum class GenderType{
    MALE,FEMALE
}

enum class PublicUserSettings{

}
