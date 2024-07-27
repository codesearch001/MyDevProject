package com.snofed.publicapp.ui.login

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snofed.publicapp.models.NewClubData

import com.snofed.publicapp.models.UserRegRequest
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.models.UserResponse
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.models.workoutfeed.FeedListResponse
import com.snofed.publicapp.repository.UserRepository
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.MutableData
import com.snofed.publicapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    var mutableData =MutableData()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    val clubLiveData: LiveData<NetworkResult<NewClubData>>
        get() = userRepository.clubLiveData

  val subClubLiveData: LiveData<NetworkResult<BrowseSubClubResponse>>
        get() = userRepository.subClubLiveData

    val feedLiveData: LiveData<NetworkResult<FeedListResponse>>
        get() = userRepository.feedLiveData

//    val clubLiveData get() = userRepository.clubLiveData
  /*  val clubResponseLiveData: LiveData<NewClubData>
        get() = userRepository.clubListResponseLiveData*/

    fun registerUser(userRequest: UserRegRequest) {
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun clubRequestUser() {
        viewModelScope.launch {
            userRepository.getClub()
            //userRepository.saveData(userRepository)
        }
    }

    fun subClubRequestUser(clientId: String) {
        viewModelScope.launch {
            userRepository.getSubClub(clientId)
        }
    }
    fun feedRequestUser() {
        viewModelScope.launch {
            userRepository.getFeedClub()
        }
    }

    fun validateLoginCredentials(
        emailAddress: String,
        password: String,
        isLogin: Boolean
    ): Pair<Boolean, String> {

        var result = Pair(true, "")
        if (TextUtils.isEmpty(emailAddress) || (!isLogin && TextUtils.isEmpty(emailAddress)) || TextUtils.isEmpty(
                password
            )
        ) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Helper.isValidEmail(emailAddress)) {
            result = Pair(false, "Email is invalid")
        } else if (!TextUtils.isEmpty(password) && password.length <= 5) {
            result = Pair(false, "Password length should be greater than 5")
        }
        return result
    }


    fun validateCredentials(
        userName: String, emailAddress: String, password: String, txtRePassword: String,
        isLogin: Boolean
    ): Pair<Boolean, String> {

        var result = Pair(true, "")
        if (TextUtils.isEmpty(emailAddress) || (!isLogin && TextUtils.isEmpty(userName)) || TextUtils.isEmpty(
                password
            ) || TextUtils.isEmpty(txtRePassword)
        ) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Helper.isValidEmail(emailAddress)) {
            result = Pair(false, "Email is invalid")
        } else if (!TextUtils.isEmpty(password) && password.length <= 5) {
            result = Pair(false, "Password length should be greater than 5")
        } else if (password != txtRePassword) {
            result = Pair(false, "Passwords do not match")
        }
        return result
    }

}