package com.duobang.dbdemo.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.dbdemo.R
import com.duobang.common.data.bean.RecordLaborTeam

class SimpleTeamsAdapter(list: List<RecordLaborTeam>?) :
    BaseQuickAdapter<RecordLaborTeam, BaseViewHolder>(
        R.layout.simple_teams_list_adapter, list as MutableList<RecordLaborTeam>?
    ) {

    override fun convert(holder: BaseViewHolder, item: RecordLaborTeam) {
        holder.setText(R.id.item_simple_teams, item.teamName)
    }

}