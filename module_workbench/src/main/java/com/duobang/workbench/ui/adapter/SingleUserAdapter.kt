package com.duobang.workbench.ui.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.Role
import com.duobang.common.data.bean.User
import com.duobang.common.util.AppImageLoader
import com.duobang.workbench.R

class SingleUserAdapter(list: List<User>) :
    BaseQuickAdapter<User, BaseViewHolder>(
        R.layout.item_single_user_list,
        list as MutableList<User>
    ) {
    override fun convert(holder: BaseViewHolder, item: User) {
        val sign : ImageView = holder.getView(R.id.sign_single_user)
        if (item.isSelected) {
            sign.setImageResource(R.drawable.ic_selected)
        } else {
            sign.setImageResource(R.drawable.ic_select_def)
        }

        holder.setText(R.id.name_single_user,item.nickname)
            .setText(R.id.role_single_user,getRole(item.roles))
        AppImageLoader.displayAvatar(
            item.avatar,
            item.nickname,
            holder.getView(R.id.avatar_single_user)
        )
    }
    private fun getRole(roles: List<Role>?): String? {
        val role = StringBuilder()
        if (roles != null) {
            for (i in roles.indices) {
                role.append(roles[i].roleName).append("、")
            }
        }
        return if (role.isNotEmpty()) role.substring(0, role.length - 1) else null
    }
}