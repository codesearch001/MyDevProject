package com.snofed.publicapp.ui.User

import RealmRepository
import com.snofed.publicapp.dto.PublicUserSettingsDTO
import com.snofed.publicapp.dto.UserDTO
import com.snofed.publicapp.models.realmModels.PublicUserSettingsRealm
import com.snofed.publicapp.models.realmModels.UserRealm
import io.realm.RealmList

class UserViewModelRealm(private val realmRepository: RealmRepository) {

    // Retrieve a user by ID
    fun getUserById(userId: String): UserDTO? {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        return user?.toUserDTO()
    }

    // Add or update a user
    fun addUser(user: UserRealm) {
        realmRepository.insertOrUpdate(user)
    }

    // Retrieve all users and return as a list of UserDTOs
    fun getAllUsers(): List<UserDTO> {
        val users = realmRepository.getAll(UserRealm::class.java)
        return users.map { it.toUserDTO() }
    }

    // Delete a user by ID
    fun deleteUserById(userId: String) {
        realmRepository.deleteById(UserRealm::class.java, userId)
    }

    // Delete all users
    fun deleteAllUsers() {
        realmRepository.deleteAll(UserRealm::class.java)
    }

    fun getFavClients(userId: String): List<String> {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        return user?.favouriteClients ?: emptyList()
    }

    // Retrieve PublicUserSettings for a specific user as a list of DTOs
    fun getPublicUserSettings(userId: String): List<PublicUserSettingsDTO> {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        return user?.publicUserSettings?.map {
            PublicUserSettingsDTO(key = it.key, value = it.value)
        } ?: emptyList()
    }

    // Add or update PublicUserSettings for a user
    fun addOrUpdatePublicUserSettings(userId: String, settings: List<PublicUserSettingsDTO>) {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        if (user != null) {
            realmRepository.insertOrUpdate(user.apply {
                publicUserSettings = settings.map {
                    PublicUserSettingsRealm().apply {
                        key = it.key
                        value = it.value
                    }
                }.toRealmList()
            })
        }
    }

    // Helper to convert a list to RealmList
    private fun <T> List<T>.toRealmList(): RealmList<T> {
        val realmList = RealmList<T>()
        realmList.addAll(this)
        return realmList
    }

    // Close Realm when done
    fun closeRealm() {
        realmRepository.close()
    }


    private fun UserRealm.toUserDTO(): UserDTO {
        return UserDTO(
            id = this.id,
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName,
            fullName = this.fullName,
            username = this.username,
            phone = this.phone,
            cellphone = this.cellphone,
            isConfirmed = this.isConfirmed,
            isDeleted = this.isDeleted,
            password = this.password,
            roleName = this.roleName,
            clientName = this.clientName,
            clientId = this.clientId,
            token = this.token,
            userGroupId = this.userGroupId,
            gender = this.gender,
            weight = this.weight,
            age = this.age,
            isSubscribed = this.isSubscribed,
            favouriteClients = this.favouriteClients?.toList() ?: emptyList(),
            publicUserSettings = this.publicUserSettings?.map { setting ->
                PublicUserSettingsDTO(key = setting.key, value = setting.value)
            } ?: emptyList()
        )
    }
}
