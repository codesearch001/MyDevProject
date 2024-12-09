package com.snofed.publicapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.snofed.publicapp.R
import com.snofed.publicapp.api.ClientAPI
import com.snofed.publicapp.api.ResponseObject
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
import com.snofed.publicapp.models.realmModels.Club
import com.snofed.publicapp.models.realmModels.PublicUserSettingsRealm
import com.snofed.publicapp.models.realmModels.SystemDataHolder
import com.snofed.publicapp.models.realmModels.Trail
import com.snofed.publicapp.models.userData
import com.snofed.publicapp.models.workoutfeed.FeedListResponse
import com.snofed.publicapp.models.workoutfeed.WorkoutActivites
import com.snofed.publicapp.response.SubscribeResponse
import com.snofed.publicapp.ui.User.UserViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ActivityViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ClientViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ClubViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.HelpViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.IntervalViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.PoisTypeViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.TaskCategoryViewModelRealm
import com.snofed.publicapp.ui.clubsubmember.ViewModelClub.ZoneTypeViewModelRealm
import com.snofed.publicapp.ui.setting.UploadResponse
import com.snofed.publicapp.ui.setting.UploadWorkoutResponse
import com.snofed.publicapp.utils.AppPreference
import com.snofed.publicapp.utils.NetworkResult
import com.snofed.publicapp.utils.SharedPreferenceKeys
import com.snofed.publicapp.utils.TokenManager
import com.snofed.publicapp.viewModel.UserViewModel
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
                        @ApplicationContext private val context: Context
                        ) {
    private val acceptLanguage =""


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

    private val _clubLiveData = MutableLiveData<NetworkResult<ResponseObject<SystemDataHolder>>>()
    val clubLiveData: LiveData<NetworkResult<ResponseObject<SystemDataHolder>>>
        get() = _clubLiveData

    private val _userDashBoardLiveData = MutableLiveData<NetworkResult<FeedListResponse>>()
    val userDashBoardLiveData: LiveData<NetworkResult<FeedListResponse>>
        get() = _userDashBoardLiveData

    private val _subClubLiveData = MutableLiveData<NetworkResult<ResponseObject<Club>>>()
    val subClubLiveData: LiveData<NetworkResult<ResponseObject<Club>>>
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

    private val _userResponseLiveData = MutableLiveData<NetworkResult<ResponseObject<UserRealm>>>()
    val userResponseLiveData: LiveData<NetworkResult<ResponseObject<UserRealm>>>
        get() = _userResponseLiveData

    private val _userPasswordRecoveryLiveData = MutableLiveData<NetworkResult<ResponseObject<String>>>()
    val userPasswordRecoveryLiveData: LiveData<NetworkResult<ResponseObject<String>>>
        get() = _userPasswordRecoveryLiveData


    private val _userWorkoutRideLiveData = MutableLiveData<NetworkResult<RideApiResponse>>()
    val userWorkoutRideLiveData: LiveData<NetworkResult<RideApiResponse>>
        get() = _userWorkoutRideLiveData

    private val _trailsDetailsLiveData = MutableLiveData<NetworkResult<ResponseObject<Trail>>>()
    val trailsDetailsLiveData: LiveData<NetworkResult<ResponseObject<Trail>>>
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
        val response = userAPI!!.signIn(userRequest)
        handleResponse(response)
    }

    suspend fun updateUser(user: User) {
        _userResponseLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.settings(acceptLanguage, user)

        handleResponseSettings(response)
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


    suspend fun getAllClub() {
        _clubLiveData.postValue(NetworkResult.Loading())
        val response = userAPI!!.allClubs()
        Log.e("response", "clubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            // Save to Room database
            //roomRepository.saveClients(response.body()!!)
            // clientDatabase.clientDao().insertClient(response.body()!!.data.clients)
            Log.e("jsonResponseData", "clubResponse " + response.body())
            _clubLiveData.postValue(NetworkResult.Success(response.body()!!))
            //Save to realm
            val userResponseData = response.body()!!.data
            saveSytemDataToRealm(userResponseData)

        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _clubLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
        } else {
            _clubLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }
    fun saveSytemDataToRealm(systemDataRealm: SystemDataHolder) {
        val clients = systemDataRealm.clients
        val activities = systemDataRealm.systemData?.activities
        val poiTypes = systemDataRealm.systemData?.poiTypes
        val zoneTypes = systemDataRealm.systemData?.zoneTypes
        val helpArticles = systemDataRealm.systemData?.helpArticles
        val taskCategories = systemDataRealm.systemData?.taskCategories
        val intervals = systemDataRealm.systemData?.intervals


        val vmClient = ClientViewModelRealm()

        val vmActivity = ActivityViewModelRealm()
        val vmPoiType = PoisTypeViewModelRealm()
        val vmZoneType = ZoneTypeViewModelRealm()
        val vmHelpArticle = HelpViewModelRealm()
        val vmTaskCategory = TaskCategoryViewModelRealm()
        val vmInterval = IntervalViewModelRealm()

        // Update a list of class in a single transaction
        val filterClient = clients?.filter { it.visibility == 0 }
        filterClient?.let { vmClient.addOrUpdateClients(it) }
        activities?.let { vmActivity.addOrUpdateActivities(it) }
        poiTypes?.let { vmPoiType.addOrUpdatePoisTypes(it) }
        zoneTypes?.let { vmZoneType.addOrUpdateZoneTypes(it) }
        helpArticles?.let { vmHelpArticle.addOrUpdateHelpArticles(it) }
        taskCategories?.let { vmTaskCategory.addOrUpdateTaskCategories(it) }
        intervals?.let { vmInterval.addOrUpdateIntervals(it) }




    }
    suspend fun getClubDetails(clientId: String) {
        _subClubLiveData.postValue(NetworkResult.Loading())
//        val response = userAPI!!.subClub(acceptLanguage, clientId, false)
        val response = userAPI!!.getClub(clientId, false)
        Log.e("response", "subClubResponse " + response)
        if (response.isSuccessful && response.body() != null) {
            Log.e("jsonResponseData", "subClubResponse " + response.body())
            _subClubLiveData.postValue(NetworkResult.Success(response.body()!!))
            //_subClubLiveData_gallery_l.postValue(response.body())
            val clubDeatilsResponseData = response.body()!!.data
            saveClubToRealm(clubDeatilsResponseData)
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _subClubLiveData.postValue(NetworkResult.Error(errorObj.optString("title", "Unknown Error")))
        } else {
            _subClubLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    fun saveClubToRealm(clubResponseData: Club) {
        val realmRepository = RealmRepository()
        val clubViewModelRealm = ClubViewModelRealm()
        clubViewModelRealm.addOrUpdateClub(clubResponseData)
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
        _trailsDetailsLiveData.postValue(NetworkResult.Loading())
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


    private fun handleResponse(response: Response<ResponseObject<UserRealm>>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))

            val userRealm: UserRealm? = response.body()?.let { it.data }

            tokenManager.saveToken(userRealm?.token!!)
            tokenManager.saveUser(userRealm.fullName!!)
            tokenManager.saveUserId(userRealm.id)

            Log.e("loginResponse", "loginResponse " + response.body())
            Log.e("token", "token " + response.body()!!.data.token)
            Log.e("UserId", "UserId " + response.body()!!.data.id)
            Log.e("firstName", "firstName " + response.body()!!.data.fullName)

            val gson = Gson()
            val jsonString = gson.toJson(response.body()!!.data)
            val userResponse = Gson().fromJson(jsonString, userData::class.java)
            Log.e("USER_RESP", "LOGIN $jsonString")

            // Save UserRealm in Realm
            // Use the generic RealmRepository to save the UserRealm object
            val userViewModel = UserViewModel()
            userRealm.let {
                userViewModel.saveOrUpdate(userRealm)
            }

            AppPreference.savePreference(
                context,
                SharedPreferenceKeys.USER_TOKEN,
                response.body()!!.data.token
            )
            AppPreference.savePreference(
                context,
                SharedPreferenceKeys.USER_USER_ID,
                response.body()!!.data.id
            )
            AppPreference.savePreference(
                context,
                SharedPreferenceKeys.USER_FIRST_NAME,
                response.body()!!.data.firstName
            )
            AppPreference.savePreference(
                context,
                SharedPreferenceKeys.USER_LAST_NAME,
                response.body()!!.data.lastName
            )
            AppPreference.savePreference(
                context,
                SharedPreferenceKeys.USER_USER_AGE,
                response.body()!!.data.age.toString()
            )
            AppPreference.savePreference(
                context,
                SharedPreferenceKeys.USER_USER_WEIGHT,
                response.body()!!.data.weight.toString()
            )
            AppPreference.savePreference(
                context,
                SharedPreferenceKeys.USER_GENDER_TYPE,
                response.body()!!.data.gender.toString()
            )
            // AppPreference.savePreference(context, SharedPreferenceKeys.USER_GENDER_TYPE, response.body()!!.data.clientLogo.toString())
            AppPreference.savePreference(context, SharedPreferenceKeys.IS_USER_LOGGED_IN, "true")
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _userResponseLiveData.postValue(
                NetworkResult.Error(
                    errorObj.optString(
                        "message",
                        "One or more validation errors occurred."
                    )
                )
            )
            // _userResponseLiveData.postValue(NetworkResult.Error(response.body()?.message))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }


    private fun handleResponseSettings(response: Response<ResponseObject<UserRealm>>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseLiveData.postValue(NetworkResult.Success(response.body()!!))
            Log.e("SETTINGS", "settingResponse " + response.message())
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response?.errorBody()?.charStream()?.readText())
            _userResponseLiveData.postValue(NetworkResult.Error(errorObj.optString("message", "One or more validation errors occurred.")))

            // _userResponseLiveData.postValue(NetworkResult.Error(response.body()?.message))
        } else {
            _userResponseLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }
    }

    private fun handleResponse2(response: Response<ResponseObject<String>>) {
        if (response.isSuccessful && response.body() != null) {
            _userPasswordRecoveryLiveData.postValue(NetworkResult.Success(response.body()!!))

            var result : String = response.body()?.data!!

            Log.e("loginResponse", "loginResponse " + result)

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