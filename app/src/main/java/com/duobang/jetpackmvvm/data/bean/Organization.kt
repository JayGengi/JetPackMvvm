package com.duobang.jetpackmvvm.data.bean

class Organization {
    /**
     * {
     * "homeOrgId": "9ca08e73-7833-4b81-bcf6-320c7fbf1bf8",
     * "orgList": [
     * {
     * "id": "9ca08e73-7833-4b81-bcf6-320c7fbf1bf8",
     * "name": "济南周一",
     * "discription": "文化东路88号",
     * "type": 0,
     * "ownerId": "2a765ed2-b219-44bd-8069-1cc33053758a",
     * "parentId": "",
     * "state": 0
     * }
     * ]
     * }
     */
    var homeOrgId: String? = null
    var orgList: List<OrganizationInfo>? = null

    /**
     * 获取主组织实体
     *
     * @return OrganizationInfo
     */
    val homeOrgInfo: OrganizationInfo?
        get() {
            if (orgList != null) {
                for (i in orgList!!.indices) {
                    if (orgList!![i].id == homeOrgId) return orgList!![i]
                }
            }
            return null
        }

    /**
     * 设置主组织
     */
    fun setHomeOrg() {
        if (orgList != null) {
            for (i in orgList!!.indices) {
                if (orgList!![i].id == homeOrgId) {
                    orgList!![i].isCheck = true
                }
            }
        }
    }
}