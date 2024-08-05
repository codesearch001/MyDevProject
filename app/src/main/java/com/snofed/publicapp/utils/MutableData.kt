package com.snofed.publicapp.utils

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.snofed.publicapp.models.browseSubClub.Image
import com.snofed.publicapp.models.browseSubClub.PublicData


class MutableData {
    var publicUserId = ObservableField<String>()
    var clientId = ObservableField<String>()
    var description = ObservableField<String>()
    var galleryImage = MutableLiveData<PublicData>()
//    var galleryImage = MutableLiveData<List<Image>>()
//
//    // MutableLiveData to hold data
//    private val _data = MutableLiveData<List<Image>>()
//
//    // Expose LiveData to observe
//    val dataa: LiveData<List<Image>> get() = _data
//
//    // Method to update the data
//    fun updateData(newData: List<Image>) {
//        _data.postValue(newData)  // Use postValue() for background threads; setValue() for main thread
//    }
}