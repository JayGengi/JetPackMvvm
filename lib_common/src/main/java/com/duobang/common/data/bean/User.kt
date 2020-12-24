package com.duobang.common.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user",
    ignoredColumns = ["roleList", "roles", "groupList", "isRead", "isIgnore", "isSelected", "isPermission", "isShowArrow"])
class User {
    @PrimaryKey
    var id: String = ""
    @ColumnInfo(name = "user_name")
    var username: String? = null
    @ColumnInfo(name = "user_nick_name")
    var nickname: String? = null
    @ColumnInfo(name = "user_phone")
    var phone: String? = null
    @ColumnInfo(name = "user_state")
    var state = 0
    @ColumnInfo(name = "user_avatar")
    var avatar: String? = null
    @ColumnInfo(name = "user_group_id")
    var groupId: String? = null
    @ColumnInfo(name = "user_group_name")
    var groupName: String? = null
    var roleList: List<Role>? = null
    var roles: List<Role>? = null
    var groupList: List<Group>? =null
    var isRead = false

    /**
     * 用于日事日毕人员列表中标记忽略的人员
     */
    var isIgnore = false

    /**
     * 选择列表
     */
    var isSelected = false

    /**
     * 有权限--公告
     */
    var isPermission = false

    /**
     * 混凝土多节点审批
     * 组最后一个用户isShowArrow = true
     */
    var isShowArrow = false


}