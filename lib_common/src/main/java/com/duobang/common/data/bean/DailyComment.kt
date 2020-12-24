package com.duobang.common.data.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.duobang.common.util.CacheUtil
import java.util.*

@Entity(tableName = "daily_comment", ignoredColumns = ["imageList", "replyUserId"])
class DailyComment {
    /**
     * comment : 评论一下
     * imageList : null
     * createAt : 2020-07-14T08:39:12.420Z
     * id : 82102153-1c56-42eb-a0c5-b45dd807afcb
     * creator : fb583d9a-a88f-46c2-b15d-9d13ed2a103a
     * replyUserId : null
     */
    @PrimaryKey(autoGenerate = true)
    var i = 0

    @ColumnInfo(name = "comment")
    var comment: String? = null
    var imageList: List<String>? = null

    @ColumnInfo(name = "comment_create_at")
    var createAt: Date? = null

    @ColumnInfo(name = "comment_id")
    var id: String? = null

    @ColumnInfo(name = "comment_creator")
    var creator: String? = null
    var replyUserId: String? = null

    /**
     * 返回创建人信息
     *
     * @return
     */
//    val creatorUser: User?
//        get() {
//            val members: List<User> =
//                PreferenceManager.getInstance().getMemberPreferences().getMembers(
//                    User::class.java
//                )
//            if (members != null) {
//                for (i in members.indices) {
//                    if (members[i].id == creator) {
//                        return members[i]
//                    }
//                }
//            }
//            return null
//        }
//
//    /**
//     * 返回被回复人信息
//     *
//     * @return
//     */
//    val replyor: User?
//        get() {
//            val members: List<User> =
//                PreferenceManager.getInstance().getMemberPreferences().getMembers(
//                    User::class.java
//                )
//            if (members != null) {
//                for (i in members.indices) {
//                    if (members[i].id == replyUserId) {
//                        return members[i]
//                    }
//                }
//            }
//            return null
//        }
//
    val isPersonal: Boolean
        get() {
            val userId: String = CacheUtil.getUser()!!.id
            return creator == userId
        }
}