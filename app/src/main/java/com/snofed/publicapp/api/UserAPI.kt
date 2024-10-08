package com.snofed.publicapp.api


import com.snofed.publicapp.db.WorkoutResponse
import com.snofed.publicapp.membership.model.BuyMembership
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.RideApiResponse
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
import com.snofed.publicapp.utils.ServiceUtil.BROWSE_CLUB_DETAILS
import com.snofed.publicapp.utils.ServiceUtil.BROWSE_CLUB_LIST
import com.snofed.publicapp.utils.ServiceUtil.CLUB_EVENT
import com.snofed.publicapp.utils.ServiceUtil.CLUB_EVENT_DETAILS
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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface UserAPI {

    //REGISTER
    @POST(REGISTER)
    suspend fun register(@Header("Accept-Language") acceptLanguage: String, @Body userRequest: UserRegRequest) : Response<UserResponse>

    //LOGIN
    @POST(LOGIN)
    suspend fun signIn(@Header("Accept-Language") acceptLanguage: String, @Body userRequest: UserRequest) : Response<UserResponse>

    //RECOVER_PASSWORD
    @POST(RECOVER_PASSWORD)
    suspend fun recoverPassword(@Header("Accept-Language") acceptLanguage: String, @Body userRequest: UserRecoverRequest) : Response<UserResponse>


    //CLUB_WORKOUT_RIDE
    @POST(CLUB_WORKOUT_RIDE)
    suspend fun workoutRide(@Header("Accept-Language") acceptLanguage: String,
                            @Body workouts: List<WorkoutResponse>) : Response<RideApiResponse>


    //GET_USER_WORKOUTS
    @GET(GET_USER_WORKOUTS)
    suspend fun userDashBoard(@Header("Accept-Language")
                                  acceptLanguage: String, @Path("id") id: String): Response<FeedListResponse>


    //BROWSE_CLUB
    @GET(BROWSE_CLUB_LIST)
    suspend fun club(@Header("Accept-Language") acceptLanguage: String): Response<NewClubData>

    //BROWSE_CLUB_DETAILS
    @GET(BROWSE_CLUB_DETAILS)
    suspend fun subClub(@Header("Accept-Language") acceptLanguage: String,
                        @Query("ClientId") clientId: String,
                        @Query("HasProTrails") hasProTrails: Boolean = false): Response<BrowseSubClubResponse>



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
                              @Path("id") id: String) : Response<TrailsDetilsResponse>

    //CLUB_TRAILS_DRAW_POLYLINES
    @GET(CLUB_TRAILS_DRAW_POLYLINES)
    suspend fun trailsDrawPolyLinesByID(@Header("Accept-Language") acceptLanguage: String,
                                        @Path("id") id: String) : Response<TrailPolyLinesResponse>

    //CLUB_TRAILS_CHART
    @GET(CLUB_TRAILS_CHART)
    suspend fun trailsGraphDetails(@Header("Accept-Language") acceptLanguage: String,
                                   @Path("id") id: String) : Response<TrailGraphData>





}