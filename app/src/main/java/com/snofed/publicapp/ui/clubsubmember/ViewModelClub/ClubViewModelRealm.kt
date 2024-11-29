package com.snofed.publicapp.ui.clubsubmember.ViewModelClub

import RealmRepository
import com.snofed.publicapp.models.realmModels.Club

open class ClubViewModelRealm(private val realmRepository: RealmRepository) {

    fun getClubById(clubId: String): Club? {
        val user = realmRepository.getById(Club::class.java, clubId)
        return user
    }
    // Add or update a Club
    fun addClub(user: Club) {
        realmRepository.insertOrUpdate(user)
    }
}