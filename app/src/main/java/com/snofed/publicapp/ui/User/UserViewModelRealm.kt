package com.snofed.publicapp.ui.User

import androidx.lifecycle.ViewModel
import com.snofed.publicapp.dto.PublicUserSettingsDTO
import com.snofed.publicapp.dto.UserDTO
import com.snofed.publicapp.models.realmModels.PublicUserSettingsRealm
import com.snofed.publicapp.models.realmModels.UserRealm
import com.snofed.publicapp.repository.RealmRepository
import io.realm.Realm
import io.realm.RealmList

open class UserViewModelRealm : ViewModel() {

    private val realmRepository: RealmRepository = RealmRepository()
    private val realm: Realm = realmRepository.getRealmInstance()

    override fun onCleared() {
        super.onCleared()
        // Close Realm instance when ViewModel is cleared
        if (!realm.isClosed) {
            realm.close()
        }
    }
    // Retrieve a User by ID
    fun getUserById(userId: String): UserRealm? {
        return realmRepository.getById(UserRealm::class.java, userId)
    }

    // Retrieve a UserDTO by ID
    fun getUserDTOById(userId: String): UserDTO? {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        return user?.toUserDTO()
    }

    // Add or update a single User
    fun addOrUpdateUser(user: UserRealm) {
        realmRepository.insertOrUpdate(user)
    }

    // Retrieve all Users and return as a list of UserDTOs
    fun getAllUsers(): List<UserDTO> {
        val users = realmRepository.getAll(UserRealm::class.java)
        return users.map { it.toUserDTO() }
    }

    // Delete a User by ID
    fun deleteUserById(userId: String) {
        realmRepository.deleteById(UserRealm::class.java, userId)
    }

    // Delete all Users
    fun deleteAllUsers() {
        realmRepository.deleteAll(UserRealm::class.java)
    }

    // Get favorite Clients for a User
    fun getFavClients(userId: String): List<String> {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        return user?.favouriteClients.orEmpty()
    }

    // Add a client ID to the Favourite Clients list
    fun addFavouriteClient(userId: String, clientId: String) {
        realm.executeTransaction { transactionRealm ->
            val user = transactionRealm.where(UserRealm::class.java).equalTo("id", userId).findFirst()
            user?.let {
                if (it.favouriteClients == null) {
                    it.favouriteClients = RealmList() // Initialize if null
                }
                if (!it.favouriteClients.orEmpty().contains(clientId)) {
                    it.favouriteClients?.add(clientId) // Add if not already present
                    transactionRealm.insertOrUpdate(it) // Save the updated object
                }
            }
        }
    }

    // Remove a client ID from the Favourite Clients list
    fun removeFavouriteClient(userId: String, clientId: String) {
        realm.executeTransaction { transactionRealm ->
            val user = getUserById(userId)
            user?.let {
                it.favouriteClients?.remove(clientId) // Remove the client ID
                transactionRealm.insertOrUpdate(it) // Save the updated object
            }
        }
    }

    // Retrieve PublicUserSettings for a specific User
    fun getPublicUserSettings(userId: String): List<PublicUserSettingsDTO> {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        return user?.publicUserSettings?.map {
            PublicUserSettingsDTO(key = it.key, value = it.value)
        } ?: emptyList()
    }

    // Get a specific PublicUserSetting value by key
    fun getPublicUserSettingValue(userId: String, key: String): String? {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        return user?.publicUserSettings?.find { it.key == key }?.value
    }

    // Add or update PublicUserSettings for a User
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

    // Update User's subscription status
    fun updateUserSubscriptionStatus(userId: String, isSubscribed: Boolean) {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        user?.let {
            realmRepository.insertOrUpdate(it.apply {
                this.isSubscribed = isSubscribed
            })
        }
    }

    // Update PublicUserSetting for a User
    fun updatePublicUserSetting(userId: String, setting: PublicUserSettingsDTO) {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        if (user != null) {
            realmRepository.getRealmInstance().executeTransaction { transactionRealm ->
                val managedUser = transactionRealm.where(UserRealm::class.java).equalTo("id", userId).findFirst()
                managedUser?.let {
                    val existingSetting = it.publicUserSettings?.find { it.key == setting.key }
                    existingSetting?.let {
                        it.value = setting.value
                    }
                    transactionRealm.insertOrUpdate(it)
                } ?: println("User with ID $userId not found in Realm.")
            }
        } else {
            println("User with ID $userId not found.")
        }
    }

    // Update User with data from UserDTO
    fun updateUser(userId: String, userDTO: UserDTO) {
        val user = realmRepository.getById(UserRealm::class.java, userId)
        user?.let {
            realmRepository.getRealmInstance().executeTransaction { transactionRealm ->
                it.firstName = userDTO.firstName
                it.lastName = userDTO.lastName
                it.fullName = "${userDTO.firstName} ${userDTO.lastName}"
                it.gender = userDTO.gender
                it.weight = userDTO.weight
                it.age = userDTO.age
            }
        }
    }

    // Helper to convert a List to RealmList
    private fun <T> List<T>.toRealmList(): RealmList<T> {
        return RealmList<T>().apply { addAll(this@toRealmList) }
    }

    // Convert UserRealm to UserDTO
    private fun UserRealm.toUserDTO(): UserDTO {
        return UserDTO(
            id = this.id,
            email = this.email,
            firstName = this.firstName,
            lastName = this.lastName,
            fullName = this.fullName!!,
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
