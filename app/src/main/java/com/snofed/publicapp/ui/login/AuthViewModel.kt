package com.snofed.publicapp.ui.login


import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snofed.publicapp.db.RoomDbRepo
import com.snofed.publicapp.models.Client
import com.snofed.publicapp.models.DataResponse
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
import com.snofed.publicapp.repository.UserRepository
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.MutableData
import com.snofed.publicapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository /*private val roomRepo: RoomDbRepo,*/) :
    ViewModel() {

    var mutableData = MutableData()

    /*  private val _clients = MutableLiveData<List<Client>>()
      val clients: LiveData<List<Client>> get() = _clients

      fun fetchClients() {
          viewModelScope.launch {
              try {
                  val clientList = roomRepo.getClients()
                  _clients.postValue(clientList)
              } catch (e: Exception) {
                  // Handle exceptions or show error state
                  _clients.postValue(emptyList())
              }
          }
          }*/


    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    val clubLiveData: LiveData<NetworkResult<NewClubData>>
        get() = userRepository.clubLiveData

    val subClubLiveData: LiveData<NetworkResult<BrowseSubClubResponse>>
        get() = userRepository.subClubLiveData

    val eventLiveData: LiveData<NetworkResult<EventResponse>>
        get() = userRepository.eventLiveData


    val eventDetailsLiveData: LiveData<NetworkResult<EventDetailsResponse>>
        get() = userRepository.eventDetailsLiveData

    val feedWorkoutLiveData: LiveData<NetworkResult<WorkoutActivites>>
        get() = userRepository.feedWorkoutLiveData

    val feedLiveData: LiveData<NetworkResult<FeedListResponse>>
        get() = userRepository.feedLiveData

    val trailsDetailsLiveData: LiveData<NetworkResult<TrailsDetilsResponse>>
        get() = userRepository.trailsDetailsLiveData

    val trailsDrawPolyLinesByIDLiveData: LiveData<NetworkResult<TrailPolyLinesResponse>>
        get() = userRepository.trailsDrawPolyLinesByIDLiveData

    val trailsDetailsGraphData: LiveData<NetworkResult<TrailGraphData>>
        get() = userRepository.eventDetailsGraphLiveData


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

    fun recoverPassword(userRequest: UserRecoverRequest) {
        viewModelScope.launch {
            userRepository.recoverPassword(userRequest)
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

    fun trailsDetailsRequestUser(trailID: String) {
        viewModelScope.launch {
            userRepository.getTrails(trailID)
        }
    }

    fun trailsDrawPolyLinesByIDRequestUser(trailID: String) {
        viewModelScope.launch {
            userRepository.getTrailsDrawPolyLinesByID(trailID)
        }
    }

    fun trailsDetailsGraph(trailID: String) {
        viewModelScope.launch {
            userRepository.getGraphRequest(trailID)
        }
    }

    fun eventDetailsRequestUser(eventId: String) {
        viewModelScope.launch {
            userRepository.getEventDetails(eventId)
        }
    }

    fun eventRequestUser() {
        viewModelScope.launch {
            userRepository.getEvent()
        }
    }

    fun feedRequestUser() {
        viewModelScope.launch {
            userRepository.getFeedClub()
        }
    }

    fun feedWorkoutRequestUser(workoutId: String) {
        viewModelScope.launch {
            userRepository.getFeedWorkout(workoutId)
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

    /* fun validateEmailForPasswordReset(emailAddress: String): Pair<Boolean, String> {
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


    // Function to validate email format
    private fun isValidEmail(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}"
        return email.matches(emailPattern.toRegex())
    }

}
*/
    fun validateEmailForPasswordReset(
        emailAddress: String
    ): Pair<Boolean, String> {

        var result = Pair(true, "")
        if (TextUtils.isEmpty(emailAddress)
        ) {
            result = Pair(false, "Please provide the credentials")
        } else if (!Helper.isValidEmail(emailAddress)) {
            result = Pair(false, "Email is invalid")
        }
        return result
    }
}