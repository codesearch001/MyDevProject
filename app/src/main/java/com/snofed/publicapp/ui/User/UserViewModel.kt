package com.snofed.publicapp.ui.User

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.snofed.publicapp.models.User
import com.snofed.publicapp.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    // Retrieve a user by ID
//    fun getUserById(userId: String) = liveData {
//        emit(userRepository.getUserById(userId))
//    }

    // update a user
    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

//    // Retrieve all users
//    fun getAllUsers() = liveData {
//        emit(userRepository.getAllUsers())
//    }
//
//    // Delete a user by ID
//    fun deleteUserById(userId: String) {
//        viewModelScope.launch {
//            userRepository.deleteUserById(userId)
//        }
//    }
//
//    // Delete all users
//    fun deleteAllUsers() {
//        viewModelScope.launch {
//            userRepository.deleteAllUsers()
//        }
//    }
//
//    // Retrieve PublicUserSettings for a specific user
//    fun getPublicUserSettings(userId: String) = liveData {
//        emit(userRepository.getPublicUserSettings(userId))
//    }
//
//    // Save or update PublicUserSettings for a user
//    fun savePublicUserSettings(userId: String, settings: List<PublicUserSettings>) {
//        viewModelScope.launch {
//            userRepository.savePublicUserSettings(userId, settings)
//        }
//    }

}