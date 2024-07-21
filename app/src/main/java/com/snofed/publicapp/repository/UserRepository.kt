package com.snofed.publicapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snofed.publicapp.api.UserAPI
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.UserRegRequest
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.models.UserResponse
import com.snofed.publicapp.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


class UserRepository @Inject constructor(private val userAPI: UserAPI) {
    private val acceptLanguage = "en-US"
    private val _notesLiveData = MutableLiveData<NetworkResult<NewClubData>>()
    val clubLiveData get() = _notesLiveData


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





    suspend fun getClub()  {
        _notesLiveData.postValue(NetworkResult.Loading())
        val response = userAPI.club(acceptLanguage)
        Log.e("response","jjjj "+response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsondata","jjjj "+response.body())
            _notesLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
             _notesLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _notesLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }




//    private fun handleClubListResponse(response: Response<ClubListResponse>) {
//        if (response.isSuccessful && response.body() != null) {
//            _clubListResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
//
//        }
//        else if(response.errorBody()!=null){
//            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
//            _clubListResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
//        }
//        else{
//            _clubListResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
//        }
//    }



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