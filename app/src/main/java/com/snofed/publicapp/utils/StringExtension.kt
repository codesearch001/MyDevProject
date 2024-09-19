package com.snofed.publicapp.utils

fun String.isEmailValid():Boolean{
    val emailRegex = Regex("^\\S+@\\S+\\.\\S+\$")
    return emailRegex.matches(this)
}