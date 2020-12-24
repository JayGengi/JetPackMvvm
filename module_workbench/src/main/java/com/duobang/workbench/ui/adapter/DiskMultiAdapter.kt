package com.duobang.workbench.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.common.data.bean.DiskBean
import com.duobang.common.data.constant.IConstant
import com.duobang.common.network.apiService
import com.duobang.common.util.AppImageLoader
import com.duobang.common.util.CacheUtil
import com.duobang.common.util.DateUtil
import com.duobang.common.util.FileFormUtils
import com.duobang.common.weight.roundImage.RoundedImageView
import com.duobang.workbench.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiskMultiAdapter() :
    BaseDelegateMultiAdapter<DiskBean, BaseViewHolder>() {
    private var showEdit = false

    //    //用于二级目录 操作 使用（owner操作属于自己 筛离）
    //    private boolean childFolder = false;
    //
    //    public void setChildFolder(boolean childFolder) {
    //        this.childFolder = childFolder;
    //    }
    private val userId: String = CacheUtil.getUser()!!.id


    private var imgMap : MutableMap<String,String> = HashMap()


    public fun getImgMap(): MutableMap<String, String> {
        return imgMap
    }
    @SuppressLint("SetTextI18n")
    override fun convert(holder: BaseViewHolder, item: DiskBean) {
        val checkView = holder.getView<ImageView>(R.id.check_view)
        holder.setText(R.id.disk_name, item.name)
        if (0 == holder.adapterPosition) {
            showEdit = item.isShowEdit
        }
        //编辑状态
        if (showEdit) {
            //若是子文件夹显示 需要判断是否有操作权限
//            if (childFolder) {
//                //----子文件夹是否有操作权限-----
//                if (userId.equals(item.getManager())) {
//                    checkView.setVisibility(View.VISIBLE);
//                    if (item.isEdit()) {
//                        checkView.setImageResource(R.drawable.ic_selected);
//                    } else {
//                        checkView.setImageResource(R.drawable.ic_select_normal);
//                    }
//                } else {
//                    checkView.setVisibility(View.GONE);
//                }
//                //----子文件夹是否有操作权限-----
//            } else {
            checkView.visibility = View.VISIBLE
            if (item.isEdit) {
                checkView.setImageResource(R.drawable.ic_selected)
            } else {
                checkView.setImageResource(R.drawable.ic_select_normal)
            }
            //            }
        } else {
            checkView.visibility = View.GONE
        }
        when (holder.itemViewType) {
            DiskBean.FOLDER, DiskBean.IMG -> {
                val img: RoundedImageView = holder.getView(R.id.img_photo_item)
                if (item.subType == DiskBean.FOLDER) {
                    holder.setImageResource(R.id.img_photo_item, R.drawable.ic_folder)
                } else {
                    try {
                        //缓存图片集合 map静态变量跟着生命周期销毁
                        val cacheImage: String? =
                            imgMap[DateUtil.getNowHour().toString() + item.id]
                        if (null != cacheImage && "" != cacheImage) {
                            AppImageLoader.showImageView(context, R.color.glass, cacheImage, img)
                        } else {
                            getRedirectUrl(img, item.id)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            DiskBean.FILE -> {
                val cardView = holder.getView<CardView>(R.id.card_view)
                val ivFileItem =
                    holder.getView<ImageView>(R.id.iv_file_item)
                when (FileFormUtils.getFileType(item.name)) {
                    IConstant.FILE.WORD -> {
                        ivFileItem.setImageResource(R.drawable.ic_word)
                        cardView.setCardBackgroundColor(Color.parseColor("#202C97FF"))
                    }
                    IConstant.FILE.PDF -> {
                        ivFileItem.setImageResource(R.drawable.ic_pdf)
                        cardView.setCardBackgroundColor(Color.parseColor("#20F55670"))
                    }
                    IConstant.FILE.EXCEL -> {
                        ivFileItem.setImageResource(R.drawable.ic_excel)
                        cardView.setCardBackgroundColor(Color.parseColor("#202CBE6E"))
                    }
                    IConstant.FILE.TXT -> {
                        ivFileItem.setImageResource(R.drawable.ic_txt)
                        cardView.setCardBackgroundColor(Color.parseColor("#2047D9EF"))
                    }
                    IConstant.FILE.OTHER -> {
                        ivFileItem.setImageResource(R.drawable.ic_other_file)
                        cardView.setCardBackgroundColor(Color.parseColor("#208095FF"))
                    }
                }
            }
            else -> {
            }
        }
    }

    internal class MyMultiTypeDelegate : BaseMultiTypeDelegate<DiskBean>() {
        override fun getItemType(data: List<DiskBean>, position: Int): Int {
            when (data[position].subType) {
                0 -> return DiskBean.FOLDER
                1 -> return DiskBean.FILE
                2 -> return DiskBean.IMG
                else -> {
                }
            }
            return 1
        }

        init {
            addItemType(DiskBean.FOLDER, R.layout.item_disk_img)
            addItemType(DiskBean.IMG, R.layout.item_disk_img)
            addItemType(DiskBean.FILE, R.layout.item_disk_file)
        }

    }

    /**
     * 获取文件在oss上面的url
     * 在adapter粗暴使用协程同步response
     * @param fileId
     * @return
     * @throws Exception
     */
    @Throws(Exception::class)
    private fun getRedirectUrl(iv: RoundedImageView, fileId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val responseJson = withContext(Dispatchers.IO){
                    val fileRes = apiService.diskFileUrlAdapter(fileId)
                    fileRes
                }
                val files = responseJson.data
                imgMap[DateUtil.getNowHour().toString() + fileId] = files
                //缓存路径
                AppImageLoader.showImageView(context, R.color.glass, files, iv)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally { }
        }
    }

    init {
        // 实现自己的代理类：
        setMultiTypeDelegate(MyMultiTypeDelegate())
    }
}