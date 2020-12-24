package com.duobang.common.util.permissions

sealed class PermissionResult {

    object Grant : PermissionResult()
    class Deny(val permissions: List<PermissionBean>) : PermissionResult()
}