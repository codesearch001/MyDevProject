package com.snofed.publicapp.utils.enums

enum class MembershipTypeEnum(val statusValue: Int) {
    ADMIN(1),
    CLIENT(2);

    fun getValue(): Int {
        return statusValue
    }
}