package com.snofed.publicapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snofed.publicapp.api.FeedBackUserAPI
import com.snofed.publicapp.api.MembershipApi
import com.snofed.publicapp.membership.model.BuyMembership
import com.snofed.publicapp.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Named

class MembershipRepository @Inject constructor(@Named("MembershipApi")private val membershipApi: MembershipApi) {
    private val acceptLanguage = "en-US"




    private val _membershipResponseLiveData = MutableLiveData<NetworkResult<BuyMembership>>()
    val membershipResponseLiveData: LiveData<NetworkResult<BuyMembership>>
        get() = _membershipResponseLiveData



    suspend fun getMembership(clientId: String) {
        _membershipResponseLiveData.postValue(NetworkResult.Loading())
        val response = membershipApi!!.getMembership(acceptLanguage, clientId)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _membershipResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _membershipResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _membershipResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


}
