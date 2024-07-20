package com.snofed.publicapp.ui.help

data class Category(
    val title: String,
    val items: List<String>,
    var isExpanded: Boolean = false
)
