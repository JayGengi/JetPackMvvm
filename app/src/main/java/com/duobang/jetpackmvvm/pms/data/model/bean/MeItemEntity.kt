package com.duobang.jetpackmvvm.pms.data.model.bean

import com.duobang.jetpackmvvm.pms.data.model.enums.MeItemType

/**
 * @author : hgj
 * @date   : 2020/6/11
 *
 */
data class MeItemEntity(
    var itemType: MeItemType,
    var itemPosition: Int,
    var data: Any
)