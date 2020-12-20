package com.duobang.workbench.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.DailySubmission
import com.duobang.common.ext.setAdapterAnimation
import com.duobang.common.util.AppImageLoader
import com.duobang.common.util.DateUtil
import com.duobang.common.util.SettingUtil
import com.duobang.workbench.R
import com.google.android.material.button.MaterialButton

class DailyManageAdapter(list: List<DailySubmission>?) :
    BaseQuickAdapter<DailySubmission, BaseViewHolder>(
        R.layout.item_daily_submission_list, list as MutableList<DailySubmission>?
    ) {
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }
    private val date = DateUtil.getCurrentDate()

    override fun convert(holder: BaseViewHolder, item: DailySubmission) {
        holder.setText(R.id.name_daily_submission_item,item.nickname)
        AppImageLoader.displayAvatar(item.avatar, item.nickname, holder.getView(R.id.avatar_daily_submission_item))

        val state: MaterialButton = holder.getView(R.id.state_daily_submission_item)
        if (item.isSubmission) {
            state.setIconTintResource(R.color.green)
            state.setIconResource(R.drawable.ic_ok)
        } else {
            val today: Long = DateUtil.parseDate(DateUtil.getCurrentDate()).time
            if (DateUtil.parseDate(date).time < today) {
                state.setIconTintResource(R.color.red)
                state.setIconResource(R.drawable.ic_err)
            } else {
                state.setIconTintResource(R.color.orange)
                state.setIconResource(R.drawable.ic_going)
            }
        }
    }

}