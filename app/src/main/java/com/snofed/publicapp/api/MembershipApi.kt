package com.snofed.publicapp.api


import com.snofed.publicapp.membership.model.BuyMembership
import com.snofed.publicapp.ui.feedback.model.FeedBackCategories
import com.snofed.publicapp.utils.ServiceUtil.BUY_MEMBERSHIP
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MembershipApi {
    //BUY_MEMBERSHIP
    @GET(BUY_MEMBERSHIP)
    suspend fun getMembership(@Header("Accept-Language") acceptLanguage: String,
                              @Query("ClientId") clientId: String): Response<BuyMembership>




}