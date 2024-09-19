package com.snofed.publicapp.utils.enums

enum class TaskStatus(val statusValue: Int) {
    InProgress(0),
    OnHold(1),
    NavigatorReport(2),
    Completed(3),
    PublicAppReport(4);

    companion object {
        // Static method to get enum from integer value
        fun fromValue(value: Int): TaskStatus {
            return values().find { it.statusValue == value }
                ?: throw IllegalArgumentException("Unknown status value: $value")
        }

        // Static method to get a human-readable description
        fun getDescription(status: TaskStatus): String {
            return when (status) {
                InProgress -> "Task is currently in progress"
                OnHold -> "Task is on hold"
                NavigatorReport -> "Task is awaiting navigator report"
                Completed -> "Task is completed"
                PublicAppReport -> "Task requires public app report"
            }
        }
    }
}


