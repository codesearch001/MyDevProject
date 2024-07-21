package com.snofed.publicapp.api

import com.snofed.publicapp.models.ClubListRequest
import com.snofed.publicapp.models.ClubListResponse
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.NoteResponse
import com.snofed.publicapp.models.UserRegRequest
import com.snofed.publicapp.models.UserRequest
import com.snofed.publicapp.models.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST


interface UserAPI {
    @POST("account/register-public-user")
    suspend fun register(@Header("Accept-Language") acceptLanguage: String, @Body userRequest: UserRegRequest) : Response<UserResponse>

    @POST("account/public-app/sign-in")
    suspend fun signIn(@Header("Accept-Language") acceptLanguage: String, @Body userRequest: UserRequest) : Response<UserResponse>


        //public-app/sync/system-data?HasProTrails=false
//    @GET("public-app/sync/system-data?HasProTrails=false")
//   // suspend fun club(@Header("Accept-Language") acceptLanguage: String) : Response<ClubListResponse>
//    suspend fun club(@Header("Accept-Language") acceptLanguage: String) : Response<List<NewClubData>>


    @GET("public-app/sync/system-data?HasProTrails=false")
    suspend fun club(@Header("Accept-Language") acceptLanguage: String): Response<NewClubData>

   /* @GET("account/workouts/feed")
    suspend fun feed(@Header("Accept-Language") acceptLanguage: String, @Header("Authorization") token: String) : Response<Workout>*/
}