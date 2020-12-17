package com.duobang.common.room.convert

import androidx.room.TypeConverter
import java.util.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/30 9:13
 * @描述　: Room 时间转换器
 */
class DateConvert {
    @TypeConverter
    fun fromTimestamp(value: Long): Date? {
        return if (value > 0) {
            Date(value)
        } else {
            null
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }
}