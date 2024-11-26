package com.snofed.publicapp.ui.User

import RealmRepository
import com.snofed.publicapp.dto.PublicUserSettingsDTO
import com.snofed.publicapp.dto.UserDTO
import com.snofed.publicapp.dto.toRealm
import com.snofed.publicapp.models.realmModels.PublicUserSettingsRealm
import com.snofed.publicapp.models.realmModels.UserRealm
import io.realm.RealmList

class UserViewModelRealm(private val realmRepository: RealmRepository) {

    // Retrieve a user by ID
    fun getUserById(userId: String): UserRealm? {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        return user
    }

    fun getUserDTOById(userId: String): UserDTO? {
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

    fun getPublicUserSettingValue(userId: String, key: String): String? {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        return user?.publicUserSettings?.find { it.key == key }?.value
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

    fun updateUserSubscriptionStatus(userId: String, isSubscribed: Boolean) {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        user?.let {
            realmRepository.insertOrUpdate(it.apply {
                this.isSubscribed = isSubscribed
            })
        }
    }

    fun updatePublicUserSetting(userId: String, setting: PublicUserSettingsDTO) {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        if (user != null) {
            realmRepository.getRealmInstance().executeTransaction { transactionRealm ->
                val managedUser = transactionRealm.where(UserRealm::class.java).equalTo("id", userId).findFirst()
                if (managedUser != null) {
                    // Find the setting in the managed list
                    val existingSetting = managedUser.publicUserSettings?.find { it.key == setting.key }

                    if (existingSetting != null) {
                        // Update the value of the existing setting
                        existingSetting.value = setting.value
                    }
                    // Insert or update the user object in Realm
                    transactionRealm.insertOrUpdate(managedUser)
                } else {
                    println("User with ID $userId not found in Realm.")
                }
            }
        } else {
            println("User with ID $userId not found.")
        }
    }

    fun updateUser(userId: String, userDTO: UserDTO) {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        if (user != null) {
            realmRepository.getRealmInstance().executeTransaction { transactionRealm ->
                // Update all fields in the user object
                user.firstName = userDTO.firstName
                user.lastName = userDTO.lastName
                user.fullName = userDTO.firstName + " " + userDTO.lastName
                user.gender = userDTO.gender
                user.weight = userDTO.weight
                user.age = userDTO.age

                // Update `favouriteClients`
//                user.favouriteClients = RealmList<String>().apply {
//                    userDTO.favouriteClients?.let { addAll(it) }
//                }


            }
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
