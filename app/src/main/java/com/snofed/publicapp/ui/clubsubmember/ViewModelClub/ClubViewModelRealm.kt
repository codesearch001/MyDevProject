package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import androidx.lifecycle.ViewModel
import com.snofed.publicapp.models.realmModels.Club
import com.snofed.publicapp.repository.RealmRepository
import io.realm.Realm

open class ClubViewModelRealm : ViewModel() {

    private val realmRepository: RealmRepository = RealmRepository()
    private val realm: Realm = realmRepository.getRealmInstance()

    override fun onCleared() {
        super.onCleared()
        // Close Realm instance when ViewModel is cleared
        if (!realm.isClosed) {
            realm.close()
        }
    }
    // Retrieve a Club by its ID
    fun getClubById(clubId: String): Club? {
        return realmRepository.getById(Club::class.java, clubId)
    }

    // Retrieve all Clubs
    fun getAllClubs(): List<Club> {
        return realmRepository.getAll(Club::class.java)
    }

    // Add or update a single Club
    fun addOrUpdateClub(club: Club) {
        realmRepository.insertOrUpdate(club)
    }

    // Add or update multiple Clubs
    fun addOrUpdateClubs(clubs: List<Club>) {
        realmRepository.insertOrUpdateAll(clubs)
    }

    // Delete a Club by its ID
    fun deleteClubById(clubId: String) {
        realmRepository.deleteById(Club::class.java, clubId)
    }

    // Delete all Clubs
    fun deleteAllClubs() {
        realmRepository.deleteAll(Club::class.java)
    }

    fun hasParentOrganisation(clientId: String): Boolean {
        return getClubById(clientId)?.parentOrganisation != null
    }

    fun hasSubOrganisations(clientId: String): Boolean {
        return getClubById(clientId)?.subOrganisations?.isNotEmpty() ?: false
    }
    // Check if a Club exists by ID
    fun doesClubExist(clubId: String): Boolean {
        return getClubById(clubId) != null
    }

    // Get Clubs with a specific field value
    fun getClubsByField(fieldName: String, value: String): List<Club> {
        return realmRepository.query(Club::class.java) {
            equalTo(fieldName, value)
        }
    }

    // Count all Clubs
    fun countAllClubs(): Long {
        return realmRepository.count(Club::class.java)
    }



}