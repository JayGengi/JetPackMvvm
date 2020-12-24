package com.duobang.workbench.ui.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.User
import com.duobang.common.util.AppImageLoader
import com.duobang.workbench.R

class AllowUserAdapter(list: List<User>) :
    BaseQuickAdapter<User, BaseViewHolder>(
        R.layout.item_allow_user_list,
        list as MutableList<User>
    ) {
    override fun convert(holder: BaseViewHolder, item: User) {

        AppImageLoader.displayAvatar(
            item.avatar,
            item.nickname,
            holder.getView(R.id.avatar_allow_user_item)
        )
    }
}