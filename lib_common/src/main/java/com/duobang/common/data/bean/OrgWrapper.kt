package com.duobang.common.data.bean

class OrgWrapper {
    var org: OrganizationInfo? = null
    var groupList: List<OrgGroup>? = null

    /**
     * 用户缓存，取出后强转
     */
    var userList: List<User>? = null

}