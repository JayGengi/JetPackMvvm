package com.duobang.workbench.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.DiskBean
import com.duobang.workbench.R

class DiskFolderAdapter(list: List<DiskBean>) :
    BaseQuickAdapter<DiskBean, BaseViewHolder>(R.layout.item_disk_img,
        list as MutableList<DiskBean>
    ) {
    override fun convert(holder: BaseViewHolder, item: DiskBean) {
        holder.setText(R.id.disk_name, item.name)
            .setImageResource(R.id.img_photo_item, R.drawable.ic_folder)
    }
}