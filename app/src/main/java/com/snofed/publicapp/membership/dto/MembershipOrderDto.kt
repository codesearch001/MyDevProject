package com.snofed.publicapp.membership.dto

import com.snofed.publicapp.models.membership.Membership

data class MembershipOrderDto(
    var membership: Membership? = null,
    var email: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var localisation: String = "",
    var userId: String = ""
)
