package com.duobang.workbench.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.DiskBean
import com.duobang.workbench.R

class DiskSampleLineAdapter(list: List<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_simple_line_list,
        list as MutableList<String>
    ) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.text_simple_dialog_item, item)
    }
}