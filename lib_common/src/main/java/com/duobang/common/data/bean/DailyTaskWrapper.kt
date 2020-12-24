package com.duobang.common.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duobang.common.util.DateUtil
import java.util.*

@Entity(tableName = "daily_task_wrapper")
class DailyTaskWrapper {
    //@PrimaryKey 声明id这个字段为主键。autoGenerate自增
    //    @PrimaryKey(autoGenerate = true)
    var i = 0

    @PrimaryKey
    var id: String = ""

    @ColumnInfo(name = "creator_id")
    var creatorId: String? = null

    @ColumnInfo(name = "operator_time")
    var operatorTime: Long = 0

    @ColumnInfo(name = "is_delayed")
    var isDelayed = false

    @ColumnInfo(name = "top_time")
    var topTime: Long = 0
    var dailyTasks: List<DailyTask>? = null
    var comments: List<DailyComment>? = null

    @ColumnInfo(name = "org_id")
    var orgId: String? = null

    @ColumnInfo(name = "year")
    var year = 0

    @ColumnInfo(name = "month")
    var month = 0

    @ColumnInfo(name = "day")
    var day = 0

    //    @Embedded
    var createAt: Date? = null

    /**
     * 是否置顶
     *
     * @return
     */
    val isTop: Boolean
        get() = topTime != 0L

    /**
     * 返回格式化的日期 HH:mm
     *
     * @return
     */
    val formatDate: String
        get() = DateUtil.formatMinuteTime(createAt)
}