package com.duobang.common.room.repository

import android.content.Context
import com.duobang.common.data.bean.User
import com.duobang.common.room.PmsDataBase

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/17 16:59
 * @描述　: 本地db数据库存储器
 */
class PmsRepository(context: Context) {

    private val userDao = PmsDataBase.getInstance(context).userDao()
    private val dailyTaskWrapperDao = PmsDataBase.getInstance(context).dailyTaskWrapperDao()

    fun getUserById(userId: String): User {
        return userDao!!.getUserById(userId)
    }

    fun insertAllUser(userList: List<User>) {
        userDao!!.insertAllUser(userList)
    }

    fun delAllUser() {
        userDao!!.delAllUser()
    }

}