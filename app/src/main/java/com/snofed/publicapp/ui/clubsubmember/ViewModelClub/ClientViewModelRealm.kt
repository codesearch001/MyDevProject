package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import RealmRepository
import com.snofed.publicapp.models.realmModels.Client

open class ClientViewModelRealm(private val realmRepository: RealmRepository) {

    // Retrieve a Client by its ID
    fun getClientById(clientId: String): Client? {
        return realmRepository.getById(Client::class.java, clientId)
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