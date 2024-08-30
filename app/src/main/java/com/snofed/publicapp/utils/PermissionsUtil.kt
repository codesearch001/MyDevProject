package com.snofed.publicapp.utils


import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment


class PermissionManager(
    private val caller: ActivityResultCaller,
    private val permissions: Array<String>,
    private val permissionGrantedCallback: () -> Unit,
    private val permissionDeniedCallback: () -> Unit
) {

    private val requestPermissionLauncher =
        caller.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            val allPermissionsGranted = result.all { it.value }
            if (allPermissionsGranted) {
                permissionGrantedCallback()
            } else {
                permissionDeniedCallback()
            }
        }

    fun checkPermissions() {
        val permissionsToRequest = permissions.filter {
            ContextCompat.checkSelfPermission((caller as Fragment).requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()

        if (permissionsToRequest.isNotEmpty()) {
            requestPermissionLauncher.launch(permissionsToRequest)
        } else {
            permissionGrantedCallback()
        }
    }
}
