package com.snofed.publicapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snofed.publicapp.api.UserAPI
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.TrailGraphData
import com.snofed.publicapp.models.TrailPolyLinesResponse
import com.snofed.publicapp.models.TrailsDetilsResponse
import com.snofed.publicapp.models.UserRecoverRequest
import com.snofed.publicapp.models.UserRegRequest
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.models.UserResponse
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.models.events.EventDetailsResponse
import com.snofed.publicapp.models.events.EventResponse
import com.snofed.publicapp.models.workoutfeed.FeedListResponse
import com.snofed.publicapp.models.workoutfeed.WorkoutActivites
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.TokenManager
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject


class UserRepository @Inject constructor(private val userAPI: UserAPI? /*, private val roomRepository: RoomDbRepo*/) {
    private val acceptLanguage = "en-US"


    /////////////////////////////////////////

    // private val _notesLiveData = MutableLiveData<NetworkResult<NewClubData>>()
    // val clubLiveData get() = _notesLiveData

    @Inject
    lateinit var tokenManager: TokenManager

    private val _clubLiveData = MutableLiveData<NetworkResult<NewClubData>>()
    val clubLiveData: LiveData<NetworkResult<NewClubData>>
        get() = _clubLiveData

    private val _subClubLiveData = MutableLiveData<NetworkResult<BrowseSubClubResponse>>()
    val subClubLiveData: LiveData<NetworkResult<BrowseSubClubResponse>>
        get() = _subClubLiveData

    private val _eventLiveData = MutableLiveData<NetworkResult<EventResponse>>()
    val eventLiveData: LiveData<NetworkResult<EventResponse>>
        get() = _eventLiveData

    private val _eventDetailsLiveData = MutableLiveData<NetworkResult<EventDetailsResponse>>()
    val eventDetailsLiveData: LiveData<NetworkResult<EventDetailsResponse>>
        get() = _eventDetailsLiveData

    private val _trailsDetailsGraphData = MutableLiveData<NetworkResult<TrailGraphData>>()
    val eventDetailsGraphLiveData: LiveData<NetworkResult<TrailGraphData>>
        get() = _trailsDetailsGraphData

    private val _feedWorkoutLiveData = MutableLiveData<NetworkResult<WorkoutActivites>>()
    val feedWorkoutLiveData: LiveData<NetworkResult<WorkoutActivites>>
        get() = _feedWorkoutLiveData


    private val _feedLiveData = MutableLiveData<NetworkResult<FeedListResponse>>()
    val feedLiveData: LiveData<NetworkResult<FeedListResponse>>
        get() = _feedLiveData

    private val _userResponseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = _userResponseLiveData

    private val _trailsDetailsLiveData = MutableLiveData<NetworkResult<TrailsDetilsResponse>>()
    val trailsDetailsLiveData: LiveData<NetworkResult<TrailsDetilsResponse>>
        get() = _trailsDetailsLiveData

    private val _trailsDrawPolyLinesByIDLiveData =
        MutableLiveData<NetworkResult<TrailPolyLinesResponse>>()
    val trailsDrawPolyLinesByIDLiveData: LiveData<NetworkResult<TrailPolyLinesResponse>>
        get() = _trailsDrawPolyLinesByIDLiveData


    suspend fun registerUser(userRequest: UserRegRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.register(acceptLanguage, userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.signIn(acceptLanguage, userRequest)
        handleResponse(response)
    }

    suspend fun recoverPassword(userRequest: UserRecoverRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.recoverPassword(acceptLanguage, userRequest)
        handleResponse2(response)
    }




    suspend fun getClub() {
        _clubLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.club(acceptLanguage)
        Log.e("response", "clubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            // Save to Room database
            //roomRepository.saveClients(response.body()!!)
            // clientDatabase.clientDao().insertClient(response.body()!!.data.clients)
            Log.e("jsonResponseData", "clubResponse " + response.body())
            _clubLiveData.postValue(NetworkResult.Success(response.body()!!))


        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _clubLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _clubLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getSubClub(clientId: String) {
        _subClubLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.subClub(acceptLanguage, clientId, false)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _subClubLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _subClubLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _subClubLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    suspend fun getEvent() {
        _eventLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.event(acceptLanguage)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _eventLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _eventLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _eventLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getEventDetails(eventId: String) {
        _eventDetailsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.eventDetails(acceptLanguage, eventId)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _eventDetailsLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _eventDetailsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _eventDetailsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getTrails(trailsId: String) {
        _eventDetailsLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.trailsDetails(acceptLanguage, trailsId)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _trailsDetailsLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _trailsDetailsLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _trailsDetailsLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getTrailsDrawPolyLinesByID(trailsId: String) {
        _trailsDrawPolyLinesByIDLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.trailsDrawPolyLinesByID(acceptLanguage, trailsId)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _trailsDrawPolyLinesByIDLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _trailsDrawPolyLinesByIDLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _trailsDrawPolyLinesByIDLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    suspend fun getGraphRequest(trailsId: String) {
        _trailsDetailsGraphData.postValue(NetworkResult.Loading())
        val response = userAPI!!.trailsGraphDetails(acceptLanguage, trailsId)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _trailsDetailsGraphData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _trailsDetailsGraphData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _trailsDetailsGraphData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getFeedWorkout(workoutId: String) {
        _feedWorkoutLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.workout(acceptLanguage, workoutId)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _feedWorkoutLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _feedWorkoutLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _feedWorkoutLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun getFeedClub() {
        _feedLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.feed(acceptLanguage, 150)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _feedLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _feedLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _feedLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
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
            Log.e("loginResponse", "loginResponse " + response.body())
            tokenManager.saveToken(response.body()!!.data.token)
            Log.e("token", "token " + response.body()!!.data.token)
            tokenManager.saveUser(response.body()!!.data.fullName)
            Log.e("firstName", "firstName " + response.body()!!.data.fullName)
            tokenManager.saveUserId(response.body()!!.data.id)
            Log.e("UserId", "UserId " + response.body()!!.data.id)

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private fun handleResponse2(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            Log.e("loginResponse", "loginResponse " + response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }

    }
}