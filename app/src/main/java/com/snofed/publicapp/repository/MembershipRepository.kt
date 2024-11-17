package com.snofed.publicapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snofed.publicapp.api.MembershipApi
import com.snofed.publicapp.membership.model.ActiveMembership
import com.snofed.publicapp.membership.model.MembershipDetails
import com.snofed.publicapp.membership.model.BuyMembership
import com.snofed.publicapp.models.membership.Membership
import com.snofed.publicapp.purchasehistory.model.TicketPurchaseHistory
import com.snofed.publicapp.ui.order.model.TicketTypeData
import com.snofed.publicapp.ui.order.ticketing.OrderDTO
import com.snofed.publicapp.ui.order.ticketing.OrderResponseDTO
import com.snofed.publicapp.ui.order.ticketing.SwishResponseDTO
import com.snofed.publicapp.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Named

class MembershipRepository @Inject constructor(@Named("MembershipApi") private val membershipApi: MembershipApi) {
    private val acceptLanguage = "en-US"





    private val _membershipResponseLiveData = MutableLiveData<NetworkResult<BuyMembership>>()
    val membershipResponseLiveData: LiveData<NetworkResult<BuyMembership>>
        get() = _membershipResponseLiveData

    private val _activeMembershipResponseLiveData = MutableLiveData<NetworkResult<ActiveMembership>>()
    val activeMembershipResponseLiveData: LiveData<NetworkResult<ActiveMembership>>
        get() = _activeMembershipResponseLiveData

    private val _activeMembershipDetailsResponseLiveData = MutableLiveData<NetworkResult<MembershipDetails>>()
    val activeMembershipDetailsResponseLiveData: LiveData<NetworkResult<MembershipDetails>>
        get() = _activeMembershipDetailsResponseLiveData

    private val _purchaseOrderHistoryMembershipResponseLiveData = MutableLiveData<NetworkResult<TicketPurchaseHistory>>()
    val purchaseOrderHistoryMembershipResponseLiveData: LiveData<NetworkResult<TicketPurchaseHistory>>
        get() = _purchaseOrderHistoryMembershipResponseLiveData

   private val _getTicketTypeResponseLiveData = MutableLiveData<NetworkResult<TicketTypeData>>()
    val getTicketTypeResponseLiveData: LiveData<NetworkResult<TicketTypeData>>
        get() = _getTicketTypeResponseLiveData

   private val _getSendOrderDirectResponseLiveData = MutableLiveData<NetworkResult<OrderResponseDTO>>()
    val getSendOrderDirectResponseLiveData: LiveData<NetworkResult<OrderResponseDTO>>
        get() = _getSendOrderDirectResponseLiveData

   private val _getSendOrderSwishResponseLiveData = MutableLiveData<NetworkResult<SwishResponseDTO>>()
    val getSendOrderSwishResponseLiveData: LiveData<NetworkResult<SwishResponseDTO>>
        get() = _getSendOrderSwishResponseLiveData


    suspend fun getMembership(clientId: String) {
        _membershipResponseLiveData.postValue(NetworkResult.Loading())
        val response = membershipApi!!.getMembership(acceptLanguage, clientId)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _membershipResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _membershipResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
        } else {
            _membershipResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

   /* suspend fun getALLMembership() {
        _membershipResponseLiveData.postValue(NetworkResult.Loading())
        val response = membershipApi!!.getALLMembership(acceptLanguage)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _membershipResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _membershipResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _membershipResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }*/

    suspend fun getActiveMembership(userId: String) {
        _activeMembershipResponseLiveData.postValue(NetworkResult.Loading())
        val response = membershipApi!!.getActiveMembership(acceptLanguage, userId)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _activeMembershipResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _activeMembershipResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
        } else {
            _activeMembershipResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getBenfitsMembership(userId: String) {
        _activeMembershipDetailsResponseLiveData.postValue(NetworkResult.Loading())
        val response = membershipApi!!.getBenfitsMembership(acceptLanguage, userId)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _activeMembershipDetailsResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _activeMembershipDetailsResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
        } else {
            _activeMembershipDetailsResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    suspend fun getPurchaseOrderHistory(userId: String) {
        _purchaseOrderHistoryMembershipResponseLiveData.postValue(NetworkResult.Loading())
        val response = membershipApi!!.getPurchaseOrderHistory(acceptLanguage, userId)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _purchaseOrderHistoryMembershipResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _purchaseOrderHistoryMembershipResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
        } else {
            _purchaseOrderHistoryMembershipResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getTicketType(userId: String) {
        _getTicketTypeResponseLiveData.postValue(NetworkResult.Loading())
        val response = membershipApi!!.getTicketType(acceptLanguage, userId)
       // Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            //Log.e("jsonResponseData", "subClubResponse " + response.body())
            _getTicketTypeResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _getTicketTypeResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
        } else {
            _getTicketTypeResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

 suspend fun getSendOrderDirect(orderDTO: OrderDTO) {
        _getSendOrderDirectResponseLiveData.postValue(NetworkResult.Loading())
        val response = membershipApi!!.getSendOrderDirect(acceptLanguage, orderDTO)
       // Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _getSendOrderDirectResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _getSendOrderDirectResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
        } else {
            _getSendOrderDirectResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

 suspend fun getSendOrderSwish(orderDTO: OrderDTO) {
        _getSendOrderSwishResponseLiveData.postValue(NetworkResult.Loading())
        val response = membershipApi!!.getSendOrderSwish(acceptLanguage, orderDTO)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _getSendOrderSwishResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _getSendOrderSwishResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
        } else {
            _getSendOrderSwishResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

}
