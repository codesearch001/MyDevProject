package com.snofed.publicapp.models

data class Data(
    val serviceAuthenticationToken: String,
    val systemData: SystemData,
    val user: Any,
    val clients: List<Client>
)