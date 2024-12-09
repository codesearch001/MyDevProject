package com.snofed.publicapp.viewModel

import com.snofed.publicapp.models.realmModels.UserRealm
import com.snofed.publicapp.dto.UserDTO
import com.snofed.publicapp.dto.toDTO
import com.snofed.publicapp.dto.PublicUserSettingsDTO
import io.realm.Case
import io.realm.Realm
import io.realm.RealmList

class UserViewModel : GenericViewModel<UserRealm>(UserRealm::class.java)  {
    private val realm: Realm = Realm.getDefaultInstance()
    // All Generic methods are available here.
    // More specific methods can be add below here for UserRealm

    fun getPublicUserSettingValue(userId: String, key: String): String? {
        val user = fetchById(userId)
        return user?.publicUserSettings?.find { it.key == key }?.value
    }

    fun getFavouriteClients(userId: String): List<String> {
        val user = fetchById(userId)
        return user?.favouriteClients ?: emptyList()
    }

    // Method to update a specific publicUserSetting in UserRealm
    fun updatePublicUserSetting(userId: String, settingDTO: PublicUserSettingsDTO) {
        val user = fetchById(userId)

        user?.let {
            val existingSetting = it.publicUserSettings?.find { setting -> setting.key == settingDTO.key }

            if (existingSetting != null) {
                // Update the value of the existing setting
                existingSetting.value = settingDTO.value
            } else {
               // no need to add new settings
                // instead show msg , no such setting found
            }
            // Save the updated user back to Realm
            saveOrUpdate(it)
        }
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
            val user = fetchById(userId)
            user?.let {
                it.favouriteClients?.remove(clientId) // Remove the client ID
                transactionRealm.insertOrUpdate(it) // Save the updated object
            }
        }
    }

    fun fetchByIds(id: String): UserRealm? {
        val item = realm.where(UserRealm::class.java).equalTo("id", id, Case.INSENSITIVE).findFirst().let{
            realm.copyFromRealm(it)
        }
        return item
    }

    fun getUserDTOById(userId: String): UserDTO? {
        val user = fetchById(userId)
        user?.favouriteClients?.size
        return user?.toDTO()
    }

}