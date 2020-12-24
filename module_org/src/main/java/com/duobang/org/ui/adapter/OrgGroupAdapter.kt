package com.duobang.org.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.OrgGroup
import com.duobang.org.R

class OrgGroupAdapter(list: List<OrgGroup>?) :
    BaseQuickAdapter<OrgGroup, BaseViewHolder>(
        R.layout.item_org_group_list, list as MutableList<OrgGroup>?
    ) {

    override fun convert(holder: BaseViewHolder, item: OrgGroup) {
        holder.setText(R.id.name_org_group_item,item.name)
    }

}