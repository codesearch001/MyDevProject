package com.snofed.publicapp.api

import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.UserRegRequest
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.models.UserResponse
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.models.workoutfeed.FeedListResponse
import com.snofed.publicapp.utils.Constants.GET_ALL_FEEDS
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface UserAPI {
    //Registration
    @POST("account/register-public-user")
    suspend fun register(@Header("Accept-Language") acceptLanguage: String, @Body userRequest: UserRegRequest) : Response<UserResponse>

    //Login
    @POST("account/public-app/sign-in")
    suspend fun signIn(@Header("Accept-Language") acceptLanguage: String, @Body userRequest: UserRequest) : Response<UserResponse>

    //Browse Club List
    @GET("public-app/sync/system-data?HasProTrails=false")
    suspend fun club(@Header("Accept-Language") acceptLanguage: String): Response<NewClubData>

    //curl -X GET "http://161.97.121.63:5000/api/public-app/sync/client?ClientId=7e4fc4aa-b5c3-48c0-8812-08d997a7706f&HasProTrails=false" -H  "accept: */*"
    @GET("public-app/sync/client?")
    suspend fun subClub(@Header("Accept-Language") acceptLanguage: String, @Query("ClientId") clientId: String, @Query("HasProTrails") hasProTrails: Boolean = false, ): Response<BrowseSubClubResponse>

    @GET(GET_ALL_FEEDS)
    suspend fun feed(@Header("Accept-Language") acceptLanguage: String, @Query("limit") limit: Int) : Response<FeedListResponse>
}