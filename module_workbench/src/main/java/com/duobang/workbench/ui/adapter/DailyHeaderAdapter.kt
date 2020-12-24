package com.duobang.workbench.ui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.DailyHeader
import com.duobang.common.data.bean.OrgGroup
import com.duobang.workbench.R

class DailyHeaderAdapter(list: List<DailyHeader>?) :
    BaseQuickAdapter<DailyHeader, BaseViewHolder>(
        R.layout.item_daily_header, list as MutableList<DailyHeader>?
    ) {

    override fun convert(holder: BaseViewHolder, item: DailyHeader) {
        holder.setText(R.id.title,item.title)
            .setImageResource(R.id.daily_header_iv,item.res)
        if(holder.adapterPosition in 1..3){
            holder.getView<ImageView>(R.id.daily_header_iv).setPadding(1,1,1,1)
        }
    }
}