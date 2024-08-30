package com.snofed.publicapp.db

import com.snofed.publicapp.api.UserAPI
import com.snofed.publicapp.models.Client
import com.snofed.publicapp.models.NewClubData
import javax.inject.Inject

class RoomDbRepo @Inject constructor(private val clientDao: RoomDao) {
    suspend fun saveClients(clients: NewClubData) {
        clientDao.insertClients(clients)
    }

    suspend fun getClients(): List<Client> {
        return clientDao.getVisibleClients()
    }

    suspend fun updateWishlistStatus(clientId: String, status: Boolean) {
        clientDao.updateWishlistStatus(clientId, status)
    }
}