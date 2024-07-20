package com.snofed.publicapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snofed.publicapp.api.UserAPI
import com.snofed.publicapp.models.UserRegRequest
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.models.UserResponse
import com.snofed.publicapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {
    private val acceptLanguage = "en-US"
    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    suspend fun registerUser(userRequest: UserRegRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.register(acceptLanguage, userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response =userAPI.signIn(acceptLanguage, userRequest)
        handleResponse(response)
    }

    suspend fun clubList(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response =userAPI.club(acceptLanguage)
        handleResponse(response)
    }

   /* suspend fun feedList(workoutRequest: Workout) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response =userAPI.feed(acceptLanguage, "token")
      //  handleResponse(response)
    }*/




    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))

        }
        else if(response.errorBody()!=null){
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
        else{
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
}