package com.duobang.common.data.bean


class Record {
    var id: String? = null
    var orgId: String? = null
    var structureId: String? = null
    var structureName: String? = null
    var modelId: String? = null
    var modelName: String? = null
    private var branchInfo: List<BranchInfo>? = null
    var userId: String? = null
    var createTime // long
            : String? = null
    var state = 0
    var type = 0
    var items: List<RecordField>? = null
    var templateName: String? = null

    fun getBranchInfo(): List<BranchInfo>? {
        return branchInfo
    }

    fun setBranchInfo(branchInfo: List<BranchInfo>?) {
        this.branchInfo = branchInfo
    }

    /**
     * 模型完整的名称（带结构）
     *
     * @return
     */
    val completeName: String?
        get() {
            val name = StringBuilder()
            name.append(structureName)
            name.append(" ")
            if (branchInfo != null) {
                for (i in branchInfo!!.indices) {
                    name.append(branchInfo!![i].name)
                    name.append(" ")
                }
                return name.toString()
            }
            return modelName
        }

}