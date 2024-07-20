package com.snofed.publicapp.api

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
    // suspend fun signup(@Body userRequest: UserRequest) : Response<UserResponse>
    suspend fun register(@Header("Accept-Language") acceptLanguage: String, @Body userRequest: UserRegRequest) : Response<UserResponse>

    @POST("account/public-app/sign-in")
    // suspend fun signin(@Body userRequest: UserRequest) : Response<UserResponse>
    suspend fun signIn(@Header("Accept-Language") acceptLanguage: String, @Body userRequest: UserRequest) : Response<UserResponse>


    @GET("public-app/system-data")
    suspend fun club(@Header("Accept-Language") acceptLanguage: String ) : Response<UserResponse>

   /* @GET("account/workouts/feed")
    suspend fun feed(@Header("Accept-Language") acceptLanguage: String, @Header("Authorization") token: String) : Response<Workout>*/
}