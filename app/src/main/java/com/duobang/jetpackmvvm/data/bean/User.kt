package com.duobang.jetpackmvvm.data.bean

class User {
    var id: String? = null
    var username: String? = null
    var nickname: String? = null
    var phone: String? = null
    var state = 0
    var avatar: String? = null
    var roleList: List<Role>? = null
    var roles: List<Role>? = null
    var groupList: List<Group>? =
        null
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
    var groupId: String? = null
    var groupName: String? = null

}