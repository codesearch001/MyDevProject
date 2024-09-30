package com.snofed.publicapp.ui.login


import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snofed.publicapp.db.WorkoutResponse
import com.snofed.publicapp.membership.model.ActiveMembership
import com.snofed.publicapp.membership.model.MembershipDetails
import com.snofed.publicapp.membership.model.BuyMembership
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.RideApiResponse
import com.snofed.publicapp.models.TrailGraphData
import com.snofed.publicapp.models.TrailPolyLinesResponse
import com.snofed.publicapp.models.TrailsDetilsResponse
import com.snofed.publicapp.models.UserRecoverRequest
import com.snofed.publicapp.models.UserRegRequest
import com.snofed.publicapp.models.UserReport
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.models.UserResponse
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.models.events.EventDetailsResponse
import com.snofed.publicapp.models.events.EventResponse
import com.snofed.publicapp.models.membership.Membership
import com.snofed.publicapp.models.workoutfeed.FeedListResponse
import com.snofed.publicapp.models.workoutfeed.WorkoutActivites
import com.snofed.publicapp.purchasehistory.model.TicketPurchaseHistory
import com.snofed.publicapp.repository.MembershipRepository
import com.snofed.publicapp.repository.UserFeedBackRepository
import com.snofed.publicapp.repository.UserRepository
import com.snofed.publicapp.ui.feedback.FeedApiResponse
import com.snofed.publicapp.ui.feedback.adapter.FeedBackDetails
import com.snofed.publicapp.ui.feedback.model.FeedBackCategories
import com.snofed.publicapp.ui.feedback.model.FeedBackTaskCategories
import com.snofed.publicapp.ui.order.model.TicketTypeData
import com.snofed.publicapp.ui.order.ticketing.OrderDTO
import com.snofed.publicapp.ui.order.ticketing.OrderResponseDTO
import com.snofed.publicapp.ui.order.ticketing.SwishResponseDTO
import com.snofed.publicapp.utils.Helper
import com.snofed.publicapp.utils.MutableData
import com.snofed.publicapp.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: UserRepository,
                                        private val feedBackRepository: UserFeedBackRepository,
                                        private val membershipRepository: MembershipRepository

) :
    ViewModel() {

    var mutableData = MutableData()


    val userResponseLiveData: LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponseLiveData

    val userWorkoutRideLiveData: LiveData<NetworkResult<RideApiResponse>>
        get() = userRepository.userWorkoutRideLiveData

   val userFeedBackResponseLiveData: LiveData<NetworkResult<FeedApiResponse>>
        get() = feedBackRepository.userFeedBackResponseLiveData

 val feedBackTaskCategoriesLiveData: LiveData<NetworkResult<FeedBackTaskCategories>>
        get() = feedBackRepository.feedBackTaskCategoriesLiveData

    val membershipResponseLiveData: LiveData<NetworkResult<BuyMembership>>
        get() = membershipRepository.membershipResponseLiveData


    val activeMembershipResponseLiveData: LiveData<NetworkResult<ActiveMembership>>
        get() = membershipRepository.activeMembershipResponseLiveData

    val activeMembershipDetailsResponseLiveData: LiveData<NetworkResult<MembershipDetails>>
        get() = membershipRepository.activeMembershipDetailsResponseLiveData

   val purchaseOrderHistoryMembershipResponseLiveData: LiveData<NetworkResult<TicketPurchaseHistory>>
        get() = membershipRepository.purchaseOrderHistoryMembershipResponseLiveData

   val getTicketTypeResponseLiveData: LiveData<NetworkResult<TicketTypeData>>
        get() = membershipRepository.getTicketTypeResponseLiveData

   val getSendOrderDirectResponseLiveData: LiveData<NetworkResult<OrderResponseDTO>>
        get() = membershipRepository.getSendOrderDirectResponseLiveData

   val getSendOrderSwishResponseLiveData: LiveData<NetworkResult<SwishResponseDTO>>
        get() = membershipRepository.getSendOrderSwishResponseLiveData

    val feedBackTaskByCategoriesIDLiveData: LiveData<NetworkResult<FeedBackCategories>>
        get() = feedBackRepository.feedBackTaskByCategoriesIDLiveData

    val feedBackTaskDetailsLiveData: LiveData<NetworkResult<FeedBackDetails>>
        get() = feedBackRepository.feedBackTaskDetailsLiveData

    val clubLiveData: LiveData<NetworkResult<NewClubData>>
        get() = userRepository.clubLiveData

    val userDashBoardLiveData: LiveData<NetworkResult<FeedListResponse>>
        get() = userRepository.userDashBoardLiveData

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

    //RECOVER_PASSWORD
    fun recoverPassword(userRequest: UserRecoverRequest) {
        viewModelScope.launch {
            userRepository.recoverPassword(userRequest)
        }
    }

 fun userReportRequest(userRequest: List<UserReport>) {
        viewModelScope.launch {
            feedBackRepository.userReportRequest(userRequest)
        }
    }

    fun getFeedBackTaskCategories() {
        viewModelScope.launch {
            feedBackRepository.getFeedBackTaskCategories()
        }
    }

    fun getTaskByCategoriesID(categoriesID: String) {
        viewModelScope.launch {
            feedBackRepository.getTaskByCategoriesID(categoriesID)
        }
    }

    fun getTaskDetails(feedBackID: String) {
        viewModelScope.launch {
            feedBackRepository.getTaskDetails(feedBackID)
        }
    }


    fun getMembership(clientId: String) {
        viewModelScope.launch {
            membershipRepository.getMembership(clientId)
        }
    }

    fun getActiveMembership(userId: String) {
        viewModelScope.launch {
            membershipRepository.getActiveMembership(userId)
        }
    }

    fun getBenfitsMembership(userId: String) {
        viewModelScope.launch {
            membershipRepository.getBenfitsMembership(userId)
        }
    }
    fun getPurchaseOrderHistory(userId: String) {
        viewModelScope.launch {
            membershipRepository.getPurchaseOrderHistory(userId)
        }
    }
    fun getTicketType(userId: String) {
        viewModelScope.launch {
            membershipRepository.getTicketType(userId)
        }
    }

    fun getSendOrderDirect(orderDTO: OrderDTO) {
        viewModelScope.launch {
            membershipRepository.getSendOrderDirect(orderDTO)
        }
    }

    fun getSendOrderSwish(orderDTO: OrderDTO) {
        viewModelScope.launch {
            membershipRepository.getSendOrderSwish(orderDTO)
        }
    }

 /*   fun getALLMembership() {
        viewModelScope.launch {
            membershipRepository.getALLMembership()
        }
    }
*/

    //Start Ride Request
    fun workOutRideRequest(workoutRequest: List<WorkoutResponse>) {
        viewModelScope.launch {
            userRepository.workOutRideRequest(workoutRequest)
        }
    }

    fun clubRequestUser() {
        viewModelScope.launch {
            userRepository.getClub()
        }
    }

    fun userDashBoardRequestUser(userid: String) {
        viewModelScope.launch {
            userRepository.getUserDasBoard(userid)
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