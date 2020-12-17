package com.duobang.common.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duobang.common.data.bean.User

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/17 11:11
 * @描述　: 当前组织下用户群Dao接口
 */
@Dao
interface UserDao {
    @get:Query("SELECT * FROM user")
    val all: List<User?>?

    @Query("SELECT * FROM user where id =(:id)")
    fun getUserById(id: String?): User

    @Query("DELETE FROM user")
    fun delAllUser()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllUser(userList: List<User?>?)
}