package com.snofed.publicapp.api


import com.snofed.publicapp.db.WorkoutResponse
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
import com.snofed.publicapp.models.realmModels.Club
import com.snofed.publicapp.models.realmModels.SystemDataHolder
import com.snofed.publicapp.models.realmModels.Trail
import com.snofed.publicapp.models.realmModels.UserRealm
import com.snofed.publicapp.models.workoutfeed.FeedListResponse
import com.snofed.publicapp.models.workoutfeed.WorkoutActivites
import com.snofed.publicapp.ui.setting.UploadResponse
import com.snofed.publicapp.ui.setting.UploadWorkoutResponse
import com.snofed.publicapp.utils.ServiceUtil.BROWSE_CLUB_LIST
import com.snofed.publicapp.utils.ServiceUtil.CLUB_DETAILS
import com.snofed.publicapp.utils.ServiceUtil.CLUB_EVENT
import com.snofed.publicapp.utils.ServiceUtil.CLUB_EVENT_DETAILS
import com.snofed.publicapp.utils.ServiceUtil.CLUB_FAV_ITEMS
import com.snofed.publicapp.utils.ServiceUtil.CLUB_TRAILS_CHART
import com.snofed.publicapp.utils.ServiceUtil.CLUB_TRAILS_DETAILS
import com.snofed.publicapp.utils.ServiceUtil.CLUB_TRAILS_DRAW_POLYLINES
import com.snofed.publicapp.utils.ServiceUtil.CLUB_WORKOUT_RIDE
import com.snofed.publicapp.utils.ServiceUtil.GET_ALL_FEEDS
import com.snofed.publicapp.utils.ServiceUtil.GET_ALL_FEEDS_DETAILS
import com.snofed.publicapp.utils.ServiceUtil.GET_USER_WORKOUTS
import com.snofed.publicapp.utils.ServiceUtil.LOGIN
import com.snofed.publicapp.utils.ServiceUtil.RECOVER_PASSWORD
import com.snofed.publicapp.utils.ServiceUtil.REGISTER
import com.snofed.publicapp.utils.ServiceUtil.UPLOAD_WORKOUT_IMAGES
import com.snofed.publicapp.utils.ServiceUtil.USER_PROFILE_IMAGE
import com.snofed.publicapp.utils.ServiceUtil.USER_SETTINGS
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query


interface UserAPI {

    //REGISTER
    @POST(REGISTER)
    suspend fun register(@Header("Accept-Language") acceptLanguage: String, @Body userRequest: UserRegRequest) : Response<ResponseObject<UserRealm>>

    //LOGIN
    @POST(LOGIN)
    suspend fun signIn( @Body userRequest: UserRequest) : Response<ResponseObject<UserRealm>>

    //SETTINGS
    @POST(USER_SETTINGS)
    suspend fun settings(@Header("Accept-Language") acceptLanguage: String?, @Body user: User): Response<ResponseObject<UserRealm>>

    //RECOVER_PASSWORD
    @POST(RECOVER_PASSWORD)
    suspend fun recoverPassword(@Header("Accept-Language") acceptLanguage: String, @Body userRequest: UserRecoverRequest) : Response<ResponseObject<String>>


    //CLUB_WORKOUT_RIDE
    @POST(CLUB_WORKOUT_RIDE)
    suspend fun workoutRide(@Header("Accept-Language") acceptLanguage: String, @Body workouts: List<WorkoutResponse>) : Response<RideApiResponse>

    //GET_USER_WORKOUTS
    @GET(GET_USER_WORKOUTS)
    suspend fun userDashBoard(@Header("Accept-Language") acceptLanguage: String, @Path("id") id: String): Response<FeedListResponse>

    @Multipart
    @POST(USER_PROFILE_IMAGE)
    suspend fun uploadProfileImage(@Path("userId") userId: String,
                                   @Part file: MultipartBody.Part): Response<UploadResponse>  // Define `UploadResponse` based on your API response

    @Multipart
    @POST(UPLOAD_WORKOUT_IMAGES)
    suspend fun uploadWorkoutImage(@Part("WorkoutId") workoutId: RequestBody,
                                   @Part WorkoutImages: List<MultipartBody.Part>): Response<UploadWorkoutResponse>  // Define `UploadResponse` based on your API response

/*    //BROWSE_CLUB
    @GET(BROWSE_CLUB_LIST)
    suspend fun club(@Header("Accept-Language") acceptLanguage: String): Response<NewClubData>*/

    /*//BROWSE_CLUB_DETAILS
    @GET(BROWSE_CLUB_DETAILS)
    suspend fun subClub(@Header("Accept-Language") acceptLanguage: String,
                        @Query("ClientId") clientId: String,
                        @Query("HasProTrails") hasProTrails: Boolean = false): Response<BrowseSubClubResponse>*/

    //BROWSE_CLUB
    @GET(BROWSE_CLUB_LIST)
    suspend fun allClubs(): Response<ResponseObject<SystemDataHolder>>

    //BROWSE_CLUB_DETAILS
    @GET(CLUB_DETAILS)
    suspend fun getClub(@Query("ClientId") clientId: String,
                        @Query("HasProTrails") hasProTrails: Boolean = false): Response<ResponseObject<Club>>



    //GET_ALL_FEEDS
    @GET(GET_ALL_FEEDS)
    suspend fun feed(@Header("Accept-Language") acceptLanguage: String,
                     @Query("limit") limit: Int) : Response<FeedListResponse>

    //CLUB_EVENT
    @GET(CLUB_EVENT)
    suspend fun event(@Header("Accept-Language") acceptLanguage: String) : Response<EventResponse>

    //CLUB_EVENT_DETAILS
    @GET(CLUB_EVENT_DETAILS)
    suspend fun eventDetails(@Header("Accept-Language") acceptLanguage: String,
                             @Path("id") id: String) : Response<EventDetailsResponse>

    //GET_ALL_FEEDS_DETAILS
    @GET(GET_ALL_FEEDS_DETAILS)
    suspend fun workout(@Header("Accept-Language") acceptLanguage: String,
                        @Path("id") id: String) : Response<WorkoutActivites>

    //CLUB_TRAILS_DETAILS
    @GET(CLUB_TRAILS_DETAILS)
    suspend fun trailsDetails(@Header("Accept-Language") acceptLanguage: String,
                              @Path("id") id: String) : Response<ResponseObject<Trail>>

    //CLUB_TRAILS_DRAW_POLYLINES
    @GET(CLUB_TRAILS_DRAW_POLYLINES)
    suspend fun trailsDrawPolyLinesByID(@Header("Accept-Language") acceptLanguage: String,
                                        @Path("id") id: String) : Response<TrailPolyLinesResponse>

    //CLUB_TRAILS_CHART
    @GET(CLUB_TRAILS_CHART)
    suspend fun trailsGraphDetails(@Header("Accept-Language") acceptLanguage: String,
                                   @Path("id") id: String) : Response<TrailGraphData>


    //CLUB_TRAILS_CHART
    @GET(CLUB_FAV_ITEMS)
    suspend fun requestFavClubById(@Header("Accept-Language") acceptLanguage: String,
                                   @Query("ClientId") clientId: String,
                                   @Query("HasProTrails") hasProTrails: Boolean = false): Response<BrowseSubClubResponse>
    // public static final String USER_PROFILE_IMAGE = "public-app/upload/profile-image/{userId}";
}