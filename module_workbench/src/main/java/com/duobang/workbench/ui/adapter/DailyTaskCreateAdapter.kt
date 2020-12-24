package com.duobang.workbench.ui.adapter

import android.graphics.Color
import android.graphics.Paint
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.DailyTask
import com.duobang.base.ext.view.clickNoRepeat
import com.duobang.workbench.R

class DailyTaskCreateAdapter(list: List<DailyTask>?) :
    BaseQuickAdapter<DailyTask, BaseViewHolder>(
        R.layout.item_create_daily_task_list, list as MutableList<DailyTask>?
    ) {

    private var onItemDeleteClickListener: OnItemDeleteClickListener? = null
    override fun convert(holder: BaseViewHolder, item: DailyTask) {

        holder.setText(R.id.content_create_daily_task_item,item.content)
        setupState(holder,item)
        setupEditState(holder,item)
        setupContent(holder,item)
        setupDelete(holder,item)
    }

    private fun setupContent(holder: BaseViewHolder, item: DailyTask) {
        val content : EditText = holder.getView(R.id.content_create_daily_task_item)
        if (!item.isEditable) { //已提交，并且状态已完成，并且已经更新保存
            // 不可编辑，字体变灰
            content.isFocusable = false
            content.isFocusableInTouchMode = false
            content.setTextColor(Color.parseColor("#8f8f8f"))
            content.paintFlags = content.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        } else if (item.isDelete) { // 删除状态
            // 不可编辑，字体变灰，带删除线
            content.isFocusable = false
            content.isFocusableInTouchMode = false
            content.setTextColor(Color.parseColor("#8f8f8f"))
            content.paintFlags = content.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            // 可编辑，字体黑色
            content.setTextColor(Color.parseColor("#000000"))
            content.paintFlags = content.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            if (item.isEdit) {
                content.isFocusable = true
                content.isFocusableInTouchMode = true
                content.requestFocus()
                content.setSelection(content.text.toString().length)
            } else {
                content.isFocusable = false
                content.isFocusableInTouchMode = false
            }
        }
    }

    private fun setupEditState(holder: BaseViewHolder, item: DailyTask) {
        val stateView : RadioGroup = holder.getView(R.id.state_view_create_daily_task_item)
        val goingButton : RadioButton = holder.getView(R.id.going_rg_create_daily_task_item)
        val finishButton : RadioButton = holder.getView(R.id.finish_rg_create_daily_task_item)
        val noFinishBtn : RadioButton = holder.getView(R.id.nofinish_rg_create_daily_task_item)
        if (item.isEdit) {
            stateView.visibility = View.VISIBLE
            when (item.state) {
                1 -> finishButton.isChecked = true
                2 -> noFinishBtn.isChecked = true
                else -> goingButton.isChecked = true
            }
        } else {
            stateView.visibility = View.GONE
        }
    }

    private fun setupState(holder: BaseViewHolder,item: DailyTask) {
        val stateSign :ImageView = holder.getView(R.id.state_create_daily_task_item)
        if (item.isEdit) {
            stateSign.visibility = View.INVISIBLE
        } else {
            stateSign.visibility = View.VISIBLE
            when (item.state) {
                1 ->stateSign.setImageResource(R.drawable.ic_ok_orange)
                2 -> stateSign.setImageResource(R.drawable.ic_error)
                else -> stateSign.setImageResource(R.drawable.ic_going)
            }
        }
    }
    private fun setupDelete(holder: BaseViewHolder, item: DailyTask) {
        val deleteSign :ImageView = holder.getView(R.id.delete_create_daily_task_item)
        val content : EditText = holder.getView(R.id.content_create_daily_task_item)
        deleteSign.clickNoRepeat {
            onItemDeleteClickListener?.onItemDeleteClick(content.text.toString(),holder.adapterPosition,item)
        }
        when {
            item.isDelete -> deleteSign.setImageResource(R.drawable.ic_revoke_daily_task)
            item.isEditable -> deleteSign.setImageResource(R.drawable.ic_delete_photo)
            else -> deleteSign.setImageResource(R.drawable.ic_delete_grey)
        }
    }

    fun setOnItemDeleteClickListener(onItemDeleteClickListener: OnItemDeleteClickListener) {
        this.onItemDeleteClickListener = onItemDeleteClickListener
    }


    interface OnItemDeleteClickListener {
        fun onItemDeleteClick(connectStr: String, i: Int,item: DailyTask)
    }
}