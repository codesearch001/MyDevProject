package com.snofed.publicapp.utils

import android.net.Uri

interface DrawerController {
    fun openDrawer()
}

interface OnItemClickListener {
    fun onItemClick(itemId: Int)
}

interface DataPassListener {
    fun onDataPassed(data: String)
}

interface ImageUriCallback {
    fun onImageUriReceived(uri: Uri)
}