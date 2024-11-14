package com.snofed.publicapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.snofed.publicapp.R
import com.snofed.publicapp.api.UserAPI
import com.snofed.publicapp.db.WorkoutResponse
import com.snofed.publicapp.membership.model.BuyMembership
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.RideApiResponse
import com.snofed.publicapp.models.TrailGraphData
import com.snofed.publicapp.models.TrailPolyLinesResponse
import com.snofed.publicapp.models.TrailsDetilsResponse
import com.snofed.publicapp.models.User
import com.snofed.publicapp.models.UserRecoverRequest
import com.snofed.publicapp.models.UserRegRequest
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.models.UserResponse
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.models.events.EventDetailsResponse
import com.snofed.publicapp.models.events.EventResponse
import com.snofed.publicapp.models.userData
import com.snofed.publicapp.models.workoutfeed.FeedListResponse
import com.snofed.publicapp.models.workoutfeed.WorkoutActivites
import com.snofed.publicapp.ui.setting.UploadResponse
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedPreferenceKeys
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Named


class UserRepository @Inject constructor(@Named("UserAPI") private val userAPI: UserAPI?,@ApplicationContext private val context: Context) {
    private val acceptLanguage = R.string.backend_localization.toString()


    /////////////////////////////////////////

    // private val _notesLiveData = MutableLiveData<NetworkResult<NewClubData>>()
    // val clubLiveData get() = _notesLiveData

    @Inject
    lateinit var tokenManager: TokenManager

    private val _uploadResult = MutableLiveData<NetworkResult<UploadResponse>>()
    val uploadResult: LiveData<NetworkResult<UploadResponse>>
        get() = _uploadResult

    private val _clubLiveData = MutableLiveData<NetworkResult<NewClubData>>()
    val clubLiveData: LiveData<NetworkResult<NewClubData>>
        get() = _clubLiveData

    private val _userDashBoardLiveData = MutableLiveData<NetworkResult<FeedListResponse>>()
    val userDashBoardLiveData: LiveData<NetworkResult<FeedListResponse>>
        get() = _userDashBoardLiveData

    private val _subClubLiveData = MutableLiveData<NetworkResult<BrowseSubClubResponse>>()
    val subClubLiveData: LiveData<NetworkResult<BrowseSubClubResponse>>
        get() = _subClubLiveData

    private val _membershipLiveData = MutableLiveData<NetworkResult<BuyMembership>>()
    val membershipLiveData: LiveData<NetworkResult<BuyMembership>>
        get() = _membershipLiveData

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


    private val _userWorkoutRideLiveData = MutableLiveData<NetworkResult<RideApiResponse>>()
    val userWorkoutRideLiveData: LiveData<NetworkResult<RideApiResponse>>
        get() = _userWorkoutRideLiveData

    private val _trailsDetailsLiveData = MutableLiveData<NetworkResult<TrailsDetilsResponse>>()
    val trailsDetailsLiveData: LiveData<NetworkResult<TrailsDetilsResponse>>
        get() = _trailsDetailsLiveData

    private val _trailsDrawPolyLinesByIDLiveData =
        MutableLiveData<NetworkResult<TrailPolyLinesResponse>>()
    val trailsDrawPolyLinesByIDLiveData: LiveData<NetworkResult<TrailPolyLinesResponse>>
        get() = _trailsDrawPolyLinesByIDLiveData


    //REGISTER
    suspend fun registerUser(userRequest: UserRegRequest) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.register(acceptLanguage, userRequest)
        handleResponse(response)
    }

    //LOGIN
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


    //Start Ride Request
  suspend fun workOutRideRequest(workoutRequest: List<WorkoutResponse>) {
        _userWorkoutRideLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.workoutRide(acceptLanguage, workoutRequest)
        handleResponse3(response)
    }

 /*   private fun handleResponse3(response: Response<UserRideResponse>) {
            if (response.isSuccessful && response.body() != null) {
                _userWorkoutRideLiveData.postValue(NetworkResult.Success(response.body()!!))
                Log.e("loginResponsedddd", "loginResponseddd " + response.body())

            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
//                _userWorkoutRideLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
            } else {
                _userWorkoutRideLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
            }
    }*/


    private fun handleResponse3(response: Response<RideApiResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userWorkoutRideLiveData.postValue(NetworkResult.Success(response.body()!!))
            Log.e("StartRide", "StartRideResponse " + response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            try {

                _userWorkoutRideLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))

            } catch (e: JSONException) {
                _userWorkoutRideLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))

            }
        } else {
            _userWorkoutRideLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    suspend fun getUserDasBoard(userId: String) {
        _userDashBoardLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.userDashBoard(acceptLanguage,userId)
        Log.e("response", "clubResponse " + response)
        if (response.isSuccessful && response.body() != null) {

            // Save to Room database
            //roomRepository.saveClients(response.body()!!)
            // clientDatabase.clientDao().insertClient(response.body()!!.data.clients)
            Log.e("jsonResponseData", "clubResponse " + response.body())
            _userDashBoardLiveData.postValue(NetworkResult.Success(response.body()!!))

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())

            _userDashBoardLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))

        } else {

           _userDashBoardLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    suspend fun uploadProfileImage(userId: String, file: File): Result<UploadResponse> {
        _uploadResult.postValue(NetworkResult.Loading())
        return try {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

            val response = userAPI?.uploadProfileImage(userId, body)


            if (response?.isSuccessful== true && response?.body() != null) {
                _uploadResult.postValue(NetworkResult.Success(response.body()!!))
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error: ${response?.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
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
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _clubLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
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
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _subClubLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
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
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _eventLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
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
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _eventDetailsLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
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
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _trailsDetailsLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
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
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _trailsDrawPolyLinesByIDLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
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
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _trailsDetailsGraphData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
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
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _feedWorkoutLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
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
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())

            _feedLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
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
//            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
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


           /* val gson = Gson()
            val jsonString = gson.toJson(response.body()!!.data)
            val user = Gson().fromJson(jsonString, userData::class.java)


            Log.e("Settings", "userResponse1 " + jsonString)
            Log.e("Settings", "userResponse2 " + user)
*/



            AppPreference.savePreference(context, SharedPreferenceKeys.USER_TOKEN, response.body()!!.data.token)
            AppPreference.savePreference(context, SharedPreferenceKeys.USER_USER_ID, response.body()!!.data.id)
            AppPreference.savePreference(context, SharedPreferenceKeys.USER_FIRST_NAME, response.body()!!.data.firstName)
            AppPreference.savePreference(context, SharedPreferenceKeys.USER_LAST_NAME, response.body()!!.data.lastName)
            AppPreference.savePreference(context, SharedPreferenceKeys.USER_USER_AGE, response.body()!!.data.age.toString())
            AppPreference.savePreference(context, SharedPreferenceKeys.USER_USER_WEIGHT, response.body()!!.data.weight.toString())
            AppPreference.savePreference(context, SharedPreferenceKeys.USER_GENDER_TYPE, response.body()!!.data.gender.toString())
           // AppPreference.savePreference(context, SharedPreferenceKeys.USER_GENDER_TYPE, response.body()!!.data.clientLogo.toString())
            AppPreference.savePreference(context, SharedPreferenceKeys.IS_USER_LOGGED_IN, "true")
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private fun handleResponse2(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            Log.e("loginResponse", "loginResponse " + response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }

    }
}