package com.duobang.workbench.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.DiskBean
import com.duobang.workbench.R

class DiskMenuAdapter(list: List<DiskBean>) :
    BaseQuickAdapter<DiskBean, BaseViewHolder>(R.layout.item_disk_menu,
        list as MutableList<DiskBean>
    ) {
    override fun convert(holder: BaseViewHolder, item: DiskBean) {
        holder.setText(R.id.disk_menu_name, item.name)
    }
}