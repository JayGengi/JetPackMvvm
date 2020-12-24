package com.duobang.workbench.ui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.DailyTask
import com.duobang.base.ext.view.visibleOrGone
import com.duobang.workbench.R
import com.google.android.material.button.MaterialButton

class DailyTaskInnerAdapter(list: List<DailyTask>?) :
    BaseQuickAdapter<DailyTask, BaseViewHolder>(
        R.layout.item_daily_task_inner_list, list as MutableList<DailyTask>?
    ) {

    override fun convert(holder: BaseViewHolder, item: DailyTask) {

        val sign: ImageView = holder.getView(R.id.sign_daily_task_inner_item)
        val delay: MaterialButton = holder.getView(R.id.delay_daily_task_inner_item)
        when (item.state) {
            0 -> sign.setImageResource(R.drawable.ic_going)
            1 -> sign.setImageResource(R.drawable.ic_ok_orange)
            2 -> sign.setImageResource(R.drawable.ic_error)
        }
        holder.setText(R.id.content_daily_task_inner_item,item.content)

        delay.visibleOrGone(item.isDaysBefore && item.state == 0 && item.isCreator)
    }

}