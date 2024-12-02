package com.snofed.publicapp.utils

import androidx.lifecycle.LiveData
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


    //Area ID
    private val _selectedAreaId = MutableLiveData<String>()
    val selectedAreaId: LiveData<String> get() = _selectedAreaId

    fun updateSelectedAreaIds(id: String) {
        _selectedAreaId.value = id
    }

    //POIS ID
    private val _selectedPoiIds = MutableLiveData<List<String>>()
    val selectedPoisIds: LiveData<List<String>> get() = _selectedPoiIds

    fun updateSelectedIds(ids: List<String>) {
        _selectedPoiIds.value = ids
    }
    // Use this method to get the selected IDs from SharedPreferences or other sources
    fun getSelectedIds(): List<String> {
        return _selectedPoiIds.value ?: emptyList()
    }

    //Trails ID
    private val _selectedTrailId = MutableLiveData<List<String>>()
    val selectedTrailId: LiveData<List<String>> get() = _selectedTrailId

    fun updateSelectedTrailsIds(ids: List<String>) {
        _selectedTrailId.value = ids
    }


    //ZoneType ID
    private val _selectedZoneId = MutableLiveData<List<String>>()
    val selectedZoneTypeId: LiveData<List<String>> get() = _selectedZoneId

    fun updateSelectedZoneIds(ids: List<String>) {
        _selectedZoneId.value = ids
    }

    // Use this method to get the selected IDs from SharedPreferences or other sources
    fun getSelectedZoneIds(): List<String> {
        return _selectedZoneId.value ?: emptyList()
    }
}
