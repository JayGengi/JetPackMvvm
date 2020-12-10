package com.duobang.jetpackmvvm.data.bean

class Material {
    var modelQuantityId: String? = null
    var materialId: String? = null
    var materialType: String? = null
    var materialName: String? = null
    var materialUnit: String? = null
    var factor = 0f
    var value = 0f
    var designValue = 0f

    /**
     * 拼接标题
     *
     * @return
     */
    val title: String
        get() = "$materialName $materialType $materialUnit"
}