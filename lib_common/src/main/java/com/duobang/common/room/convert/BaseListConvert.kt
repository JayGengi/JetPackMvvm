package com.duobang.common.room.convert

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/26 14:23
 * @描述　: 转list的Convert基类
 */
open class BaseListConvert<T> {
    @TypeConverter
    fun revert(jsonString: String?): List<T>? {
        // 使用Gson方法把json格式的string转成List
        try {
            val type =
                object : TypeToken<ArrayList<T>?>() {}.type
            return Gson().fromJson(jsonString, type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @TypeConverter
    fun converter(list: List<T>?): String {
        // 使用Gson方法把List转成json格式的string，便于我们用的解析
        return Gson().toJson(list)
    }
}