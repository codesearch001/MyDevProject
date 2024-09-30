package com.snofed.publicapp.utils.enums


enum class PaymentOrderStatus(val status: Int) {
    Canceled(0),
    Approved(1),
    Invoiced(2),
    Failed(3),
    Settled(4),
    Pending(5);

    companion object {
        fun fromStatus(status: Int): String {
            return values().find { it.status == status }?.name ?: "Unknown"
        }
    }
}

/*
enum class PaymentOrderStatus(val status: Long) {
    Canceled(0),
    Approved(1),
    Invoiced(2),
    Failed(3),
    Settled(4),
    Pending(5);

    companion object {
        fun fromStatus(status: Long): String {
            return when (status) {
                0L -> "Order Canceled"
                1L -> "Order Approved"
                2L -> "Order Invoiced"
                3L -> "Payment Failed"
                4L -> "Payment Settled"
                5L -> "Order Pending"
                else -> "Unknown Status"
            }
        }
    }
}*/
