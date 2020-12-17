package com.duobang.workbench.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.DailyTask
import com.duobang.common.data.bean.DailyTaskWrapper
import com.duobang.common.data.bean.User
import com.duobang.common.ext.init
import com.duobang.common.ext.setAdapterAnimation
import com.duobang.common.ext.setNbOnItemChildClickListener
import com.duobang.common.room.PmsDataBase
import com.duobang.common.room.repository.PmsRepository
import com.duobang.common.util.AppImageLoader
import com.duobang.common.util.SettingUtil
import com.duobang.jetpackmvvm.ext.view.visibleOrGone
import com.duobang.workbench.R

class DailyTaskAdapter(list: List<DailyTaskWrapper>?) :
    BaseQuickAdapter<DailyTaskWrapper, BaseViewHolder>(
        R.layout.item_daily_task_list, list as MutableList<DailyTaskWrapper>?
    ) {

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }

    private var onInnerItemDelayClickListener: OnInnerItemDelayClickListener? = null
    override fun convert(holder: BaseViewHolder, item: DailyTaskWrapper) {
        val creator: User = PmsRepository(context).getUserById(item.creatorId!!)
        AppImageLoader.displayAvatar(
            creator.avatar,
            creator.nickname,
            holder.getView(R.id.avatar_daily_task_item)
        )
        holder.setText(R.id.name_daily_task_item, creator.nickname)
            .setText(R.id.date_daily_task_item, String.format("提交于 %s", item.formatDate))

        val showMore: TextView = holder.getView(R.id.show_more_daily_task_item)
        val top: ImageView = holder.getView(R.id.topping_daily_task_item)
        val taskView: RecyclerView = holder.getView(R.id.list_daily_task_item)
        val comment: TextView = holder.getView(R.id.comment_daily_task_item)
        val dailyTaskList: List<DailyTask> = item.dailyTasks!!
        showMore.visibleOrGone(dailyTaskList.size > 3)
        if (item.isTop) {
            top.imageAlpha = 255
        } else {
            top.imageAlpha = 40
        }
        val tasks = handleTaskList(dailyTaskList)
        setupTaskView(taskView, tasks!!)
        if (item.comments == null || item.comments!!.isEmpty()) {
            comment.text = "评论"
        } else {
            comment.text = item.comments!!.size.toString()
        }
    }

    private fun setupTaskView(taskView: RecyclerView, tasks: List<DailyTask>) {
        val innerAdapter = DailyTaskInnerAdapter(tasks)
        taskView.init(LinearLayoutManager(context), innerAdapter)

        innerAdapter.addChildClickViewIds(R.id.delay_daily_task_inner_item)
        innerAdapter.setNbOnItemChildClickListener { adapter, view, position ->
            val info: DailyTask = adapter.getItem(position) as DailyTask
            //推迟日事日毕
            if (view.id == R.id.delay_daily_task_inner_item) {
                onInnerItemDelayClickListener!!.OnItemDelayClick(view, info)
            }
        }
    }

    private fun handleTaskList(dailyTaskList: List<DailyTask>?): List<DailyTask>? {
        return if (dailyTaskList != null && dailyTaskList.size > 3) {
            dailyTaskList.subList(0, 3)
        } else dailyTaskList
    }

    fun setOnInnerItemDelayClickListener(onInnerItemDelayClickListener: OnInnerItemDelayClickListener) {
        this.onInnerItemDelayClickListener = onInnerItemDelayClickListener
    }

    interface OnInnerItemDelayClickListener {
        fun OnItemDelayClick(v: View?, task: DailyTask?)
    }

}