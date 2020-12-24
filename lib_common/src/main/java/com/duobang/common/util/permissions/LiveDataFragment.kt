package com.duobang.common.util.permissions

import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData

class LiveDataFragment : Fragment() {

    val liveData by lazy {
        MutableLiveData<PermissionResult>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @TargetApi(Build.VERSION_CODES.M)
    fun requestPermission(permissions: Array<out String>) {
        requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
            val denyPermissions = mutableListOf<PermissionBean>()
            grantResults.forEachIndexed { index, result ->
                if (result == PackageManager.PERMISSION_DENIED) {
                    val permission = permissions[index]
                    val isRetryEnable = shouldShowRequestPermissionRationale(permission)
                    denyPermissions.add(PermissionBean(permission, isRetryEnable))
                }
            }
            if (denyPermissions.isEmpty()) {
                liveData.value = PermissionResult.Grant
            } else {
                liveData.value = PermissionResult.Deny(denyPermissions)
            }
        }
    }

    companion object {
        const val PERMISSIONS_REQUEST_CODE = 100
    }
}
