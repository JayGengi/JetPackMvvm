package com.duobang.workbench.ui.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.DailyComment
import com.duobang.common.room.repository.PmsRepository
import com.duobang.common.util.AppImageLoader
import com.duobang.common.weight.customview.AvatarView
import com.duobang.jetpackmvvm.ext.view.visibleOrGone
import com.duobang.workbench.R
import com.google.android.material.button.MaterialButton
import java.text.SimpleDateFormat
import java.util.*

class DailyCommentAdapter(list: List<DailyComment>?) :
    BaseQuickAdapter<DailyComment, BaseViewHolder>(R.layout.item_daily_comment_list,
        list as MutableList<DailyComment>?
    ) {
    override fun convert(holder: BaseViewHolder, item: DailyComment) {
        val createUser = PmsRepository(context).getUserById(item.creator!!)
        holder.setText(R.id.name_daily_comment_item, createUser.nickname)
            .setText(R.id.content_daily_comment_item, item.comment)
            .setText(R.id.time_daily_comment_item, getTimeFormatText(item.createAt))
        val delBt = holder.getView<MaterialButton>(R.id.delete_daily_comment_item)
        delBt.visibleOrGone(item.isPersonal)
        AppImageLoader.displayAvatar(
            createUser.avatar,
            createUser.nickname,
            holder.getView<View>(R.id.avatar_daily_comment_item) as AvatarView
        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun getTimeFormatText(date: Date?): String? {
        if (date == null) {
            return null
        }
        val diff = Date().time - date.time
        var r: Long = 0
        if (diff > year) {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            return formatter.format(date)
        }
        if (diff in (day + 1) until year) {
            r = diff / day
            val formatter = SimpleDateFormat("MM-dd HH:mm")
            return formatter.format(date)
        }
        if (diff in (hour + 1) until day) {
            r = diff / hour
            return r.toString() + "个小时前"
        }
        if (diff in (minute + 1) until hour) {
            r = diff / minute
            return r.toString() + "分钟前"
        }
        return "刚刚"
    }

    companion object {
        private const val minute = 60 * 1000 // 1分钟
            .toLong()
        private const val hour = 60 * minute // 1小时
        private const val day = 24 * hour // 1天
        private const val month = 31 * day // 月
        private const val year = 12 * month // 年
    }
}