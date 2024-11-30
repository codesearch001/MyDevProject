package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import RealmRepository
import com.snofed.publicapp.models.realmModels.Club

open class ClubViewModelRealm(private val realmRepository: RealmRepository) {

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