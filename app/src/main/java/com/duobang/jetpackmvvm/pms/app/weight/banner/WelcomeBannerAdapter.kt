package com.duobang.jetpackmvvm.pms.app.weight.banner

/**
 * 作者　: JayGengi
 * 时间　: 2020/2/20
 * 描述　:
 */

import android.view.View
import com.zhpan.bannerview.BaseBannerAdapter
import com.duobang.jetpackmvvm.pms.R

class WelcomeBannerAdapter : BaseBannerAdapter<String, WelcomeBannerViewHolder>() {

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_itemwelcome
    }

    override fun createViewHolder(itemView: View, viewType: Int): WelcomeBannerViewHolder {
        return WelcomeBannerViewHolder(itemView);
    }

    override fun onBind(
        holder: WelcomeBannerViewHolder?,
        data: String?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize);
    }
}
