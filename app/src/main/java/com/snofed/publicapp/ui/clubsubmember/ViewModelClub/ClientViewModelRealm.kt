package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import androidx.lifecycle.ViewModel
import com.snofed.publicapp.models.realmModels.Client
import com.snofed.publicapp.repository.RealmRepository
import io.realm.Realm

open class ClientViewModelRealm : ViewModel() {

    private val realmRepository: RealmRepository = RealmRepository()
    private val realm: Realm = realmRepository.getRealmInstance()

    override fun onCleared() {
        super.onCleared()
        // Close Realm instance when ViewModel is cleared
        if (!realm.isClosed) {
            realm.close()
        }
    }
    // Retrieve a Client by its ID
    fun getClientById(clientId: String): Client? {
        return realmRepository.getById(Client::class.java, clientId)
    }

    fun getClientNameById(clientId: String): String? {
        val client = getClientById(clientId)
        return client?.publicName
    }

    // Retrieve all Clients
    fun getAllClients(): List<Client> {
        return realmRepository.getAll(Client::class.java)
    }

    // Add or update a single Client
    fun addOrUpdateClient(client: Client) {
        realmRepository.insertOrUpdate(client)
    }

    // Add or update multiple Clients
    fun addOrUpdateClients(clients: List<Client>) {
        realmRepository.insertOrUpdateAll(clients)
    }

    // Delete a Client by its ID
    fun deleteClientById(clientId: String) {
        realmRepository.deleteById(Client::class.java, clientId)
    }

    // Delete all Clients
    fun deleteAllClients() {
        realmRepository.deleteAll(Client::class.java)
    }

    // Check if a Client exists by ID
    fun doesClientExist(clientId: String): Boolean {
        return getClientById(clientId) != null
    }

    // Get Clients with a specific field value
    fun getClientsByField(fieldName: String, value: String): List<Client> {
        return realmRepository.query(Client::class.java) {
            equalTo(fieldName, value)
        }
    }

    // Count all Clients
    fun countAllClients(): Long {
        return realmRepository.count(Client::class.java)
    }

}