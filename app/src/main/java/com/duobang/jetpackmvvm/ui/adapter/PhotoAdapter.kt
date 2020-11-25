package com.duobang.jetpackmvvm.ui.adapter

import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.util.AppImageLoader
import com.duobang.jetpackmvvm.ext.view.visibleOrGone
import com.duobang.common.data.enums.IConstant
import com.duobang.jetpackmvvm.R

class PhotoAdapter(
    list: MutableList<String>?,
    var showType: Int,
    var sizeType: SizeType? = SizeType.SMALL
) : BaseQuickAdapter<String, BaseViewHolder>(
    R.layout.photo_item, list
) {

    // 选择图片的尺寸风格
    //
    // - 一行单张图片，建议 LARGE
    // - 一行两张图片，建议 MEDIUM
    // - 一行多张图片，建议 DEFAULT
    enum class SizeType {
        SMALL, MEDIUM, LARGE
    }

    override fun convert(holder: BaseViewHolder, item: String) {
        val img = holder.getView<ImageView>(R.id.img_photo_item)
        AppImageLoader.displayWithoutPlaceHolder(item, img)
        val delete: ImageView = holder.getView(R.id.delete_photo_item)
        delete.visibleOrGone(showType != IConstant.PHOTO.SHOW)

        val ivParams: ViewGroup.LayoutParams = img.layoutParams
        when (sizeType) {
            SizeType.MEDIUM -> ivParams.height *= 1.5.toInt()
            SizeType.LARGE -> ivParams.height *= 2
            else -> {
            }
        }
    }

}