package com.duobang.common.data.bean

class RecordField {
    var fieldName: String? = null
    var fieldType: String? = null
    private var fieldValue: Any? = null
    var isRequired = false
    fun <T> getFieldValue(): T? {
        return fieldValue as T?
    }

    fun <T> setFieldValue(fieldValue: T) {
        this.fieldValue = fieldValue
    }

}