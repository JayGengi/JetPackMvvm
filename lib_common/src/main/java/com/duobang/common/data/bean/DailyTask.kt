package com.duobang.common.data.bean

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duobang.common.util.CacheUtil
import com.duobang.common.util.DateUtil
import java.util.*

@Entity(tableName = "daily_task", ignoredColumns = ["imageList", "isEdit", "isDelete", "isSave"])
class DailyTask {
    @PrimaryKey(autoGenerate = true)
    var i = 0

    @ColumnInfo(name = "task_org_id")
    var orgId: String? = null

    @ColumnInfo(name = "task_content")
    var content: String? = null

    @ColumnInfo(name = "task_creator_id")
    var creatorId: String? = null
    var imageList: List<String>? = null

    @ColumnInfo(name = "task_state")
    var state = 0

    //    @Embedded
    //    @ColumnInfo(name = "create_task")
    @ColumnInfo(name = "task_create_at")
    var createAt: Date? = null

    @Embedded
    var operatorTime: Date? = null

    @ColumnInfo(name = "task_id")
    var id: String? = null
    var isEdit = false
    var isDelete = false
    var isSave = true

    /**
     * 是否提交
     *
     * @return
     */
    val isCommited: Boolean
        get() = id != null

    /**
     * 是否可编辑
     *
     *
     * 要求上：已经提交了，并且状态 != 0， 并且已经更新保存,不能修改
     *
     * @return
     */
    val isEditable: Boolean
        get() = !(isCommited && state != 0 && isSave)

    /**
     * 是否可删除
     *
     * @return
     */
    val isDeleteable: Boolean
        get() = isEditable

    /**
     * 当前用户是不是创建人
     *
     * @return
     */
    val isCreator: Boolean
        get() {
            val userId: String = CacheUtil.getUser()!!.id!!
            return if (userId == null || creatorId == null) {
                false
            } else userId == creatorId
        }

    /**
     * 创建时间是否为之前
     *
     * @return
     */
    val isDaysBefore: Boolean
        get() {
            val today: Long = DateUtil.parseDate(DateUtil.getCurrentDate()).getTime()
            val create: Long = DateUtil.parseDate(DateUtil.formatDate(createAt)).getTime()
            return today > create
        }
}