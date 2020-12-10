package com.duobang.jetpackmvvm.util.permissions

sealed class PermissionResult {

    object Grant : PermissionResult()
    class Deny(val permissions: List<PermissionBean>) : PermissionResult()
}