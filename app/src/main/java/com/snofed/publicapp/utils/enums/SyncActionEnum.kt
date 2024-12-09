package com.snofed.publicapp.utils.enums

enum class SyncActionEnum(val statusValue: Int) {
    NEW(1),
    MODIFIED(2),
    DELETED(3);

    fun getValue(): Int {
        return statusValue
    }
}
