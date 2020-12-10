package com.duobang.jetpackmvvm.util.permissions
/**
 * @param permissionName 权限名称
 * @param isRetryEnable 被拒权限是否可以再次申请
 */
data class PermissionBean(var permissionName: String, var isRetryEnable: Boolean)
