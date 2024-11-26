package com.snofed.publicapp.repository

import RealmRepository
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.snofed.publicapp.R
import com.snofed.publicapp.api.ClientAPI
import com.snofed.publicapp.api.UserAPI
import com.snofed.publicapp.db.WorkoutResponse
import com.snofed.publicapp.dto.PublicUserSettingsDTO
import com.snofed.publicapp.dto.SubscribeDTO
import com.snofed.publicapp.dto.UserDTO
import com.snofed.publicapp.membership.model.BuyMembership
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.RideApiResponse
import com.snofed.publicapp.models.TrailGraphData
import com.snofed.publicapp.models.TrailPolyLinesResponse
import com.snofed.publicapp.models.TrailsDetilsResponse
import com.snofed.publicapp.models.User
import com.snofed.publicapp.models.realmModels.UserRealm
import com.snofed.publicapp.models.UserRecoverRequest
import com.snofed.publicapp.models.UserRegRequest
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.models.UserResponse
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.models.events.EventDetailsResponse
import com.snofed.publicapp.models.events.EventResponse
import com.snofed.publicapp.models.realmModels.PublicUserSettingsRealm
import com.snofed.publicapp.models.toRealm
import com.snofed.publicapp.models.userData
import com.snofed.publicapp.models.workoutfeed.FeedListResponse
import com.snofed.publicapp.models.workoutfeed.WorkoutActivites
import com.snofed.publicapp.response.SubscribeResponse
import com.snofed.publicapp.ui.User.UserViewModelRealm
import com.snofed.publicapp.ui.setting.UploadResponse
import com.snofed.publicapp.ui.setting.UploadWorkoutResponse
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedPreferenceKeys
import com.snofed.publicapp.utils.TokenManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.realm.Realm
import io.realm.RealmList
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Named


class UserRepository @Inject constructor(
                        @Named("UserAPI") private val userAPI: UserAPI?,
                        @Named("ClientAPI") private val clientAPI: ClientAPI?,
                        @ApplicationContext private val context: Context) {
    private val acceptLanguage = R.string.backend_localization.toString()


    /////////////////////////////////////////

    // private val _notesLiveData = MutableLiveData<NetworkResult<NewClubData>>()
    // val clubLiveData get() = _notesLiveData

    @Inject
    lateinit var tokenManager: TokenManager

    //Fav club
    private val _clubFavLiveData = MutableLiveData<NetworkResult<BrowseSubClubResponse>>()
    val clubFavLiveData: LiveData<NetworkResult<BrowseSubClubResponse>>
        get() = _clubFavLiveData

    private val _uploadResult = MutableLiveData<NetworkResult<UploadResponse>>()
    val uploadResult: LiveData<NetworkResult<UploadResponse>>
        get() = _uploadResult


    private val _uploadWorkoutResult = MutableLiveData<NetworkResult<UploadWorkoutResponse>>()
    val uploadWorkoutResult: LiveData<NetworkResult<UploadWorkoutResponse>>
        get() = _uploadWorkoutResult

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


    private val _subscribeResponseLiveData = MutableLiveData<NetworkResult<SubscribeResponse>>()
    val subscribeResponseLiveData: LiveData<NetworkResult<SubscribeResponse>>
        get() = _subscribeResponseLiveData

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

    suspend fun updateUser(user: User) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        var gson = Gson()
        var json = gson.toJson(user)
        Log.e("TAG_UserRepository", "updateUser: $json")
        val response = userAPI!!.settings(acceptLanguage, user)
        //val jsonResponse = gson.toJson(response)

// Log the JSON response
        //Log.d("API Response", jsonResponse)
        handleResponseTemp(response)
    }
    suspend fun getUserById(userId: String) {
        TODO()
    }
    suspend fun saveUser(user: User) {
       TODO()
    }
    suspend fun savePublicUserSettings(userId: String, settings: List<PublicUserSettingsDTO>) {
        TODO()
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

    suspend fun subscribeClub(subscribeDTO: SubscribeDTO) {
        _subscribeResponseLiveData.postValue(NetworkResult.Loading())
        val response = clientAPI!!.subscribeToClub(acceptLanguage, subscribeDTO)
    }
    suspend fun unsubscribeClub(subscribeDTO: SubscribeDTO) {
        val response = clientAPI!!.unsubscribeFromClub(acceptLanguage, subscribeDTO)
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

    suspend fun uploadWorkOutIdImage(workoutId: String, files: List<File>): Result<UploadWorkoutResponse> {
        _uploadWorkoutResult.postValue(NetworkResult.Loading())
        return try {
            // Prepare files for multipart
            val fileParts = files.map { file ->
                val fileReqBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                //val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("WorkoutImages", file.name, fileReqBody)
            }
            val workoutIdRequestBody: RequestBody = workoutId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            //val workoutIdRequestBody = workOutId.toRequestBody("text/plain".toMediaTypeOrNull())


            Log.e("Praveen_Request","fileParts.... "+ fileParts.size)
            // Assuming userAPI supports multiple files
            val response = userAPI?.uploadWorkoutImage(workoutIdRequestBody, fileParts)

            if (response?.isSuccessful == true && response.body() != null) {
                _uploadWorkoutResult.postValue(NetworkResult.Success(response.body()!!))
                Result.success(response.body()!!)
            } else {
                _uploadWorkoutResult.postValue(NetworkResult.Error(response?.body()?.message ?: "Unknown error"))
                Result.failure(Exception("Error: ${response?.message()}"))
            }
        } catch (e: Exception) {
            _uploadWorkoutResult.postValue(NetworkResult.Error(e.localizedMessage ?: "Unknown error"))
            Result.failure(e)
        }
    }

// suspend fun uploadWorkOutIdImage(workOutId: String, file: List<File>): Result<UploadResponse> {
//        _uploadResult.postValue(NetworkResult.Loading())
//        return try {
//            val requestFile = file.asRequestBody("WorkoutImages/*".toMediaTypeOrNull())
//            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
//
//            val response = userAPI?.uploadWorkoutImage(workOutId, body)
//
//
//            if (response?.isSuccessful== true && response?.body() != null) {
//                _uploadResult.postValue(NetworkResult.Success(response.body()!!))
//                Result.success(response.body()!!)
//            } else {
//                Result.failure(Exception("Error: ${response?.message()}"))
//            }
//        } catch (e: Exception) {
//            Result.failure(e)
//        }
//    }


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


           val gson = Gson()
            val jsonString = gson.toJson(response.body()!!.data)
            val userResponse = Gson().fromJson(jsonString, userData::class.java)
            Log.e("JAGGU_RESP", "JAGGU_START " + response.body()!!.data)
            // Convert response data to UserRealm
            val userResponseData = response.body()!!.data
            val userRealm = UserRealm().apply {
                id = userResponseData.id
                email = userResponseData.email
                firstName = userResponseData.firstName
                lastName = userResponseData.lastName
                fullName = userResponseData.fullName
                username = userResponseData.username
                phone = userResponseData.phone.toString()
                cellphone = userResponseData.cellphone.toString()
                isConfirmed = userResponseData.isConfirmed
                isDeleted = userResponseData.isDeleted
                password = userResponseData.password.toString()
                roleName = userResponseData.roleName.toString()
                clientName = userResponseData.clientName.toString()
                clientId = userResponseData.clientId.toString()
                token = userResponseData.token
                userGroupId = userResponseData.userGroupId
                gender = userResponseData.gender
                weight = userResponseData.weight
                age = userResponseData.age
                isSubscribed = userResponseData.isSubscribed
                favouriteClients = RealmList(*userResponseData.favouriteClients?.toTypedArray() ?: arrayOf())
                publicUserSettings = RealmList(*userResponseData.publicUserSettings?.map { setting ->
                    PublicUserSettingsRealm().apply {
                        key = setting.key
                        value = setting.value
                    }
                }?.toTypedArray() ?: arrayOf())
            }

            // Save UserRealm in Realm
            // Use the generic RealmRepository to save the UserRealm object
            val realmRepository = RealmRepository()
            val userViewModelRealm = UserViewModelRealm(realmRepository)
            realmRepository.insertOrUpdate(userRealm)
            //userViewModelRealm.addUser(userRealm)
            // Print saved data
            //val savedUser = realmRepository.getById(UserRealm::class.java, userRealm.id)
            //val getUser = userViewModelRealm.getUserById(userRealm.id)

            //val userDTO = userViewModelRealm.getUserById(userRealm.id)
            //val jsonUser = Gson().toJson(userDTO)

            //Log.e("SavedUserNew", "Saved User Data: $jsonUser")

            // Log the publicUserSettings if available
//            getUser?.publicUserSettings?.forEach {
//                Log.e("PublicUserSettings", "Key: ${it.key}, Value: ${it.value}")
//            }


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
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("message", "One or more validation errors occurred.")))

           // _userResponseLiveData.postValue(NetworkResult.Error(response.body()?.message))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    private fun handleResponseTemp(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))

            Log.e("loginResponse", "loginResponse " + response.body())
            //tokenManager.saveToken(response.body()!!.data.token)
            //Log.e("token", "token " + response.body()!!.data.token)
            //tokenManager.saveUser(response.body()!!.data.fullName)
            //Log.e("firstName", "firstName " + response.body()!!.data.fullName)
            //tokenManager.saveUserId(response.body()!!.data.id)
            //Log.e("UserId", "UserId " + response.body()!!.data.id)


            val gson = Gson()
            val jsonString = gson.toJson(response.body()!!.data)
            val userResponse = Gson().fromJson(jsonString, userData::class.java)

            Log.e("JAGGU_RESP", "JAGGU_RESP " + response.body()!!.data)

            // Convert response data to UserRealm
            val userResponseData = response.body()!!.data
            val userRealm = UserRealm().apply {
                id = userResponseData.id
                email = userResponseData.email
                firstName = userResponseData.firstName
                lastName = userResponseData.lastName
                fullName = userResponseData.fullName
                username = userResponseData.username
                phone = userResponseData.phone.toString()
                cellphone = userResponseData.cellphone.toString()
                isConfirmed = userResponseData.isConfirmed
                isDeleted = userResponseData.isDeleted
                password = userResponseData.password.toString()
                roleName = userResponseData.roleName.toString()
                clientName = userResponseData.clientName.toString()
                clientId = userResponseData.clientId.toString()
                token = userResponseData.token
                userGroupId = userResponseData.userGroupId
                gender = userResponseData.gender
                weight = userResponseData.weight
                age = userResponseData.age
                isSubscribed = userResponseData.isSubscribed
                favouriteClients = RealmList(*userResponseData.favouriteClients?.toTypedArray() ?: arrayOf())
                publicUserSettings = RealmList(*userResponseData.publicUserSettings?.map { setting ->
                    PublicUserSettingsRealm().apply {
                        key = setting.key
                        value = setting.value
                    }
                }?.toTypedArray() ?: arrayOf())
            }

            // Save UserRealm in Realm
            // Use the generic RealmRepository to save the UserRealm object
            val realmRepository = RealmRepository()
            val userViewModelRealm = UserViewModelRealm(realmRepository)
            //realmRepository.insertOrUpdate(userRealm)
            userViewModelRealm.addUser(userRealm)
            // Print saved data
            //val savedUser = realmRepository.getById(UserRealm::class.java, userRealm.id)
            //val getUser = userViewModelRealm.getUserById(userRealm.id)

            //val userDTO = userViewModelRealm.getUserById(userRealm.id)
            //val jsonUser = Gson().toJson(userDTO)

            //Log.e("SavedUserNew", "Saved User Data: $jsonUser")

            // Log the publicUserSettings if available
//            getUser?.publicUserSettings?.forEach {
//                Log.e("PublicUserSettings", "Key: ${it.key}, Value: ${it.value}")
//            }


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
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("message", "One or more validation errors occurred.")))

            // _userResponseLiveData.postValue(NetworkResult.Error(response.body()?.message))
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
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("message", "One or more validation errors occurred.")))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }

    }



    suspend fun getClubById(favClientId: String) {
        _clubFavLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.requestFavClubById(acceptLanguage, favClientId)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _clubFavLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _clubFavLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
        } else {
            _clubFavLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }

    }
}