package com.snofed.publicapp.utils.enums


enum class PoisTypeEnum (val statusValue: Int) {
    HEART_STARTER(0),
    PARKING(1),
    INFO(2),
    WARNING(3),
    WATER(4),
    WINDBREAK(5),
    RESTAREA(6),
    FOOD(7),
    PETROL_STATION(8),
    PAYMENT_POINT(9),
    PUBLIC_TOILET(10),
    TERMINAL(11),
    TICKET_AUTOMAT(12),
    FIRST_AID(13),
    ACCOMMODATION(14),
    ROAD_CROSSING(15);

    fun getValue(): Int {
        return statusValue
    }
}