package com.snofed.publicapp.utils

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.snofed.publicapp.api.ResponseObject
import com.snofed.publicapp.models.NewClubData
import com.snofed.publicapp.models.TrailsDetilsResponse
import com.snofed.publicapp.models.browseSubClub.BrowseSubClubResponse
import com.snofed.publicapp.models.membership.Membership
import com.snofed.publicapp.models.realmModels.Club
import com.snofed.publicapp.models.realmModels.SystemDataHolder
import com.snofed.publicapp.models.workoutfeed.FeedListResponse
import com.snofed.publicapp.models.workoutfeed.WorkoutActivites

class SharedViewModel : ViewModel() {
    //val browseSubClubResponse = MutableLiveData<Data>()
    val browseSubClubResponse = MutableLiveData<ResponseObject<Club>>()
    val browseClubResponse = MutableLiveData<ResponseObject<SystemDataHolder>>()
    val WorkoutActivites = MutableLiveData<WorkoutActivites>()
    val TrailsDetilsResponse = MutableLiveData<TrailsDetilsResponse>()
    val eventResponse = MutableLiveData<TrailsDetilsResponse>()
    val feedListResponse = MutableLiveData<FeedListResponse>()
    val membershipResponse = MutableLiveData<Membership>()


}
