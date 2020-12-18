package com.duobang.org.ui.adapter

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.OrganizationInfo
import com.duobang.common.ext.setAdapterAnimation
import com.duobang.common.util.SettingUtil
import com.duobang.org.R

class OrgAdapter(list: List<OrganizationInfo>?) :
    BaseQuickAdapter<OrganizationInfo, BaseViewHolder>(
        R.layout.item_org_list, list as MutableList<OrganizationInfo>?
    ) {
    override fun convert(holder: BaseViewHolder, item: OrganizationInfo) {
        val name: TextView = holder.getView(R.id.name_org_list_item)
        val sign: ImageView = holder.getView(R.id.sign_org_list_item)
        name.text = item.name
        if (item.isCheck) {
            name.setTextColor(Color.parseColor("#3f3f3f"))
        } else {
            name.setTextColor(Color.parseColor("#8f8f8f"))
        }
        if (item.isEdit && item.isCheck) {
            sign.setImageResource(R.drawable.ic_ok_blue)
        } else if (!item.isEdit) {
            sign.setImageResource(R.drawable.ic_arrow_right)
        } else {
            sign.setImageDrawable(null)
        }
    }

}