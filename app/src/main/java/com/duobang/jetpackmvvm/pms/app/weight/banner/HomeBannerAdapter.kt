package com.duobang.jetpackmvvm.pms.app.weight.banner

/**
 * 作者　: JayGengi
 * 时间　: 2020/2/20
 * 描述　:
 */

import android.view.View
import com.zhpan.bannerview.BaseBannerAdapter
import com.duobang.jetpackmvvm.pms.R
import com.duobang.jetpackmvvm.pms.data.model.bean.BannerResponse

class HomeBannerAdapter : BaseBannerAdapter<BannerResponse, HomeBannerViewHolder>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_itemhome
    }

    override fun createViewHolder(itemView: View, viewType: Int): HomeBannerViewHolder {
        return HomeBannerViewHolder(itemView);
    }

    override fun onBind(
        holder: HomeBannerViewHolder?,
        data: BannerResponse?,
        position: Int,
        pageSize: Int
    ) {
        holder?.bindData(data, position, pageSize);
    }


}
