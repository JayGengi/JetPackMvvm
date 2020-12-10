package com.duobang.jetpackmvvm.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.jetpackmvvm.R
import com.duobang.jetpackmvvm.data.bean.Material

class RecordShowMaterialAdapter( list: List<Material>?) : BaseQuickAdapter<Material, BaseViewHolder>(
    R.layout.record_material_show_list_item, list as MutableList<Material>?
) {

    override fun convert(holder: BaseViewHolder, item: Material) {
        holder.setText(R.id.title_material_item,item.title)
            .setText(R.id.design_material_item,item.designValue.toString())
            .setText(R.id.current_material_item,item.value.toString())
    }

}