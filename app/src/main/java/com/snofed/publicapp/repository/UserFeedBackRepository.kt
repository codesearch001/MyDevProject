package com.snofed.publicapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snofed.publicapp.api.FeedBackUserAPI
import com.snofed.publicapp.models.UserReport
import com.snofed.publicapp.models.UserResponse
import com.snofed.publicapp.models.workoutfeed.FeedListResponse
import com.snofed.publicapp.ui.feedback.FeedApiResponse
import com.snofed.publicapp.ui.feedback.adapter.FeedBackDetails
import com.snofed.publicapp.ui.feedback.model.FeedBackByCategoriesIdResponse
import com.snofed.publicapp.ui.feedback.model.FeedBackCategories
import com.snofed.publicapp.ui.feedback.model.FeedBackTaskCategories
import com.snofed.publicapp.ui.feedback.model.FeedBackTaskCategoriesResponse
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named


class UserFeedBackRepository @Inject constructor(@Named("FeedBackUserAPI")private val feedBackUserAPI: FeedBackUserAPI?) {
    private val acceptLanguage = "en-US"


    @Inject
    lateinit var tokenManager: TokenManager
    private val _userFeedBackResponseLiveData = MutableLiveData<NetworkResult<FeedApiResponse>>()
    val userFeedBackResponseLiveData: LiveData<NetworkResult<FeedApiResponse>>
        get() = _userFeedBackResponseLiveData


    private val _feedBackTaskCategoriesLiveData = MutableLiveData<NetworkResult<FeedBackTaskCategories>>()
    val feedBackTaskCategoriesLiveData: LiveData<NetworkResult<FeedBackTaskCategories>>
        get() = _feedBackTaskCategoriesLiveData


   private val _feedBackTaskByCategoriesIDLiveData = MutableLiveData<NetworkResult<FeedBackCategories>>()
    val feedBackTaskByCategoriesIDLiveData: LiveData<NetworkResult<FeedBackCategories>>
        get() = _feedBackTaskByCategoriesIDLiveData


   private val _feedBackTaskDetailsLiveData = MutableLiveData<NetworkResult<FeedBackDetails>>()
    val feedBackTaskDetailsLiveData: LiveData<NetworkResult<FeedBackDetails>>
        get() = _feedBackTaskDetailsLiveData



    suspend fun userReportRequest(userReportRequest: List<UserReport>) {
        _userFeedBackResponseLiveData.postValue(NetworkResult.Loading())
         val response = feedBackUserAPI!!.sendFeedBack(acceptLanguage, userReportRequest)
         handleResponse(response)
    }


    suspend fun getFeedBackTaskCategories() {
        _feedBackTaskCategoriesLiveData.postValue(NetworkResult.Loading())
        try {
            val response = feedBackUserAPI!!.getTaskCategories(acceptLanguage)
            Log.e("response", "subClubResponse " + response)
            if (response.isSuccessful && response.body() != null) {
                Log.e("jsonResponseData", "subClubResponse " + response.body())
                _feedBackTaskCategoriesLiveData.postValue(NetworkResult.Success(response.body()!!))


            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
                _feedBackTaskCategoriesLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
            } else {
                _feedBackTaskCategoriesLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        }
        catch (e:Exception){
            _feedBackTaskCategoriesLiveData.postValue(NetworkResult.Error(e.localizedMessage ?: "Unknown error"))
        }
    }


    suspend fun getTaskByCategoriesID(categoriesID: String) {
        _feedBackTaskByCategoriesIDLiveData.postValue(NetworkResult.Loading())
        try {
            val response = feedBackUserAPI!!.getTaskByCategoriesID(acceptLanguage,categoriesID)
            Log.e("response", "subClubResponse " + response)
            if (response.isSuccessful && response.body() != null) {
                Log.e("jsonResponseData", "subClubResponse " + response.body())
                _feedBackTaskByCategoriesIDLiveData.postValue(NetworkResult.Success(response.body()!!))

            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
                _feedBackTaskByCategoriesIDLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
            } else {
                _feedBackTaskByCategoriesIDLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
        }
        catch (e:Exception){
            _feedBackTaskByCategoriesIDLiveData.postValue(NetworkResult.Error(e.localizedMessage ?: "Unknown error"))
        }
    }


 suspend fun getTaskDetails(feedBackID: String) {
        _feedBackTaskDetailsLiveData.postValue(NetworkResult.Loading())
         try {
             val response = feedBackUserAPI!!.getTaskDetails(acceptLanguage,feedBackID)
             Log.e("response", "subClubResponse " + response)
             if (response.isSuccessful && response.body() != null) {
                 Log.e("jsonResponseData", "subClubResponse " + response.body())
                 _feedBackTaskDetailsLiveData.postValue(NetworkResult.Success(response.body()!!))


             } else if (response.errorBody() != null) {
                 val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
                 _feedBackTaskDetailsLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
             } else {
                 _feedBackTaskDetailsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
             }
         }
         catch (e:Exception){
             _feedBackTaskDetailsLiveData.postValue(NetworkResult.Error(e.localizedMessage ?: "Unknown error"))
         }
    }



        private fun handleResponse(response: Response<FeedApiResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userFeedBackResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            Log.e("FeedResponse", "FeedResponse " + response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            try {

                _userFeedBackResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))

            } catch (e: JSONException) {
                _userFeedBackResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))

            }
        } else {
            _userFeedBackResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
}