package com.duobang.org.ui.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.User
import com.duobang.common.util.AppImageLoader
import com.duobang.common.weight.customview.AvatarView
import com.duobang.org.R

class UserAdapter(list: List<User>?) :
    BaseQuickAdapter<User, BaseViewHolder>(
        R.layout.item_user_list, list as MutableList<User>?
    ) {

    override fun convert(holder: BaseViewHolder, item: User) {
        val name: TextView = holder.getView(R.id.name_user_item)
        val avatar: AvatarView = holder.getView(R.id.avatar_user_item)
        name.text = item.nickname
        AppImageLoader.displayAvatar(item.avatar, item.nickname, avatar)
    }

}