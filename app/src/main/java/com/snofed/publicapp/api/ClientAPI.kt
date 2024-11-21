package com.snofed.publicapp.api

import com.snofed.publicapp.dto.SubscribeDTO
import com.snofed.publicapp.utils.ServiceUtil.SUBSCRIBE_TO_CLUB
import com.snofed.publicapp.utils.ServiceUtil.UNSUBSCRIBE_FROM_CLUB
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.Response


interface ClientAPI {

    @PUT(SUBSCRIBE_TO_CLUB)
    suspend fun subscribeToClub(@Header("Accept-Language") acceptLanguage: String?, @Body subscribe: SubscribeDTO): Call<Response<Object>>

    @PUT(UNSUBSCRIBE_FROM_CLUB)
    suspend fun unsubscribeFromClub(@Header("Accept-Language") acceptLanguage: String?, @Body subscribe: SubscribeDTO): Call<Response<Object>>

}