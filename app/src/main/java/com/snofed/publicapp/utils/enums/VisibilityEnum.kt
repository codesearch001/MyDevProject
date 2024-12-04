package com.snofed.publicapp.utils.enums

enum class VisibilityEnum(val statusValue: Int) {

    PRIVATE(0),
    PUBLIC(1);

    fun getValue(): Int {
        return statusValue
    }
}

