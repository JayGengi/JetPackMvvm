package com.duobang.common.data.bean

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import java.util.*

class StructureGroup : BaseExpandNode() {
    /**
     * groupId : edb300ae-746f-427f-9116-7bf8b6d283d1
     * groupName : 桥梁
     * groupType : 1
     * structureList : [{"createAt":"2020-05-12T09:24:41.112Z","id":"84597cc2-0469-4437-b1e4-6b60345d7f14","name":"K7+720人行天桥","type":1,"description":"这个是啥为也不清楚","orgId":"391a6c13-63e8-4052-91bb-dd7254e9d1ed","state":0,"groupId":"edb300ae-746f-427f-9116-7bf8b6d283d1","bimUrl":"","extraInfo":null}]
     */
    var createAt: Date? = null
    var id: String? = null
    var name: String? = null
    var groupType = 0
    var type = 0
    var scopeType = 0
    var orgId: String? = null
    var modelHierarchys: Any? = null
    var modelTypes: Any? = null
    var structures: MutableList<Structure>? = null
    override var childNode: MutableList<BaseNode>? =
        null

}