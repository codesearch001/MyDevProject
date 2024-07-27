package com.snofed.publicapp.utils

interface DrawerController {
    fun openDrawer()
}

interface OnItemClickListener {
    fun onItemClick(itemId: Int)
}

interface DataPassListener {
    fun onDataPassed(data: String)
}
