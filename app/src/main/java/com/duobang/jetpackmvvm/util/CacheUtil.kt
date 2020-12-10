package com.duobang.jetpackmvvm.util

import android.text.TextUtils
import com.duobang.jetpackmvvm.data.bean.Organization
import com.duobang.jetpackmvvm.data.bean.User
import com.google.gson.Gson
import com.tencent.mmkv.MMKV

object CacheUtil {
    /**
     * 获取保存的账户信息
     */
    fun getUser(): User? {
        val kv = MMKV.mmkvWithID("app")
        val userStr = kv.decodeString("user")
        return if (TextUtils.isEmpty(userStr)) {
            null
        } else {
            Gson().fromJson(userStr, User::class.java)
        }
    }

    /**
     * 设置账户信息
     */
    fun setUser(userResponse: User?) {
        val kv = MMKV.mmkvWithID("app")
        if (userResponse == null) {
            kv.encode("user", "")
        } else {
            kv.encode("user", Gson().toJson(userResponse))
        }

    }

    /**
     * 获取组织信息
     */
    fun getOrg(): Organization? {
        val kv = MMKV.mmkvWithID("app")
        val orgStr = kv.decodeString("org")
        return if (TextUtils.isEmpty(orgStr)) {
            null
        } else {
            Gson().fromJson(orgStr, Organization::class.java)
        }
    }

    /**
     * 设置组织信息
     */
    fun setOrg(orgResponse: Organization?) {
        val kv = MMKV.mmkvWithID("app")
        if (orgResponse == null) {
            kv.encode("org", "")
        } else {
            kv.encode("org", Gson().toJson(orgResponse))
        }

    }

    /**
     * token
     */
    fun getToken(): String {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeString("token", "")
    }

    /**
     * token
     */
    fun setToken(token: String): String {
        val kv = MMKV.mmkvWithID("app")
        return kv.encode("token", token).toString()
    }

    /**
     * 是否是第一次登陆
     */
    fun isFirst(): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeBool("first", true)
    }

    /**
     * 是否是第一次登陆
     */
    fun setFirst(first: Boolean): Boolean {
        val kv = MMKV.mmkvWithID("app")
        return kv.encode("first", first)
    }
}