package com.snofed.publicapp.api

import com.snofed.publicapp.models.UserReport
import com.snofed.publicapp.ui.feedback.FeedApiResponse
import com.snofed.publicapp.ui.feedback.adapter.FeedBackDetails
import com.snofed.publicapp.ui.feedback.model.FeedBackCategories
import com.snofed.publicapp.ui.feedback.model.FeedBackTaskCategories
import com.snofed.publicapp.utils.ServiceUtil.SYNC_PUBLIC_TASK_CATEGORIES
import com.snofed.publicapp.utils.ServiceUtil.SYNC_TASKS
import com.snofed.publicapp.utils.ServiceUtil.SYNC_TASK_DETAILS
import com.snofed.publicapp.utils.ServiceUtil.SYNC_TASK_SUB_CATEGORIES
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface FeedBackUserAPI {


    //SYNC_TASK_CATEGORIES
    @GET(SYNC_PUBLIC_TASK_CATEGORIES)
    suspend fun getTaskCategories(@Header("Accept-Language") acceptLanguage: String) : Response<FeedBackTaskCategories>

    //SYNC_TASKS
    @POST(SYNC_TASKS)
    suspend fun sendFeedBack(@Header("Accept-Language") acceptLanguage: String, @Body userReportRequest: List<UserReport>) : Response<FeedApiResponse>

    //SYNC_TASK_SUB_CATEGORIES
    @GET(SYNC_TASK_SUB_CATEGORIES)
    suspend fun getTaskByCategoriesID(@Header("Accept-Language") acceptLanguage: String,  @Path("id") id: String) : Response<FeedBackCategories>


   //SYNC_TASK_DETAILS
    @GET(SYNC_TASK_DETAILS)
    suspend fun getTaskDetails(@Header("Accept-Language") acceptLanguage: String,  @Path("id") id: String) : Response<FeedBackDetails>
}