package com.duobang.jetpackmvvm.pms.app.weight.banner

/**
 * 作者　: JayGengi
 * 时间　: 2020/2/20
 * 描述　:
 */

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.zhpan.bannerview.BaseViewHolder
import com.duobang.jetpackmvvm.base.appContext
import com.duobang.jetpackmvvm.pms.R
import com.duobang.jetpackmvvm.pms.data.model.bean.BannerResponse

class HomeBannerViewHolder(view: View) : BaseViewHolder<BannerResponse>(view) {
    override fun bindData(data: BannerResponse?, position: Int, pageSize: Int) {
        val img = itemView.findViewById<ImageView>(R.id.bannerhome_img)
        data?.let {
            Glide.with(appContext)
                .load(it.imagePath)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(img)
        }
    }

}
