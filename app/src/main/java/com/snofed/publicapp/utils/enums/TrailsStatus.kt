package com.snofed.publicapp.utils.enums

enum class TrailsStatusEnum(val statusValue: Int) {
    CLOSE(0),
    OPEN(1);

    companion object {
        fun fromValue(value: Int): TrailsStatusEnum {
            return values().find { it.statusValue == value }
                ?: throw IllegalArgumentException("No enum constant with value: $value")
        }
    }
}

enum class TicketOrderTypeEnum {
    SINGLE_TICKET, MULTIPLE_TICKET, MEMBERSHIP
}