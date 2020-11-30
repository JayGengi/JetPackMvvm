//package com.guoxun.xiaoyi.utils.picture
//
//import android.content.Context
//import cc.shinichi.library.ImagePreview
//import cc.shinichi.library.bean.ImageInfo
//
//
///**
// * @des 大图浏览器
// * *
// *
// *
// * * 开源库地址：https://github.com/SherlockGougou/BigImageViewPager
// * @auther JayGengi
// * @data 2019/01/11 09:29
// * @email JayGengi@163.com
// */
//object ImagePreviewUtils {
//
//    /**
//     * 大图查看
//     */
//    fun largerView(context: Context, position: Int, imageInfoList: List<String>) {
//        val imagesList: ArrayList<ImageInfo> = ArrayList()
//        for (i in imageInfoList.indices) {
//            val imageInfo = ImageInfo()
//            imageInfo.originUrl = imageInfoList[i]
//            imageInfo.thumbnailUrl = imageInfoList[i]
//            imagesList.add(imageInfo)
//        }
//        ImagePreview
//                .getInstance()
//                .setContext(context)
//                .setIndex(position)// 从第一张图片开始，索引从0开始哦
//                .setImageInfoList(imagesList)
//                .setShowDownButton(true)
//                .setLoadStrategy(ImagePreview.LoadStrategy.Default)
//                .setFolderName("vcang")
//                .setScaleLevel(1, 3, 8)
//                .setZoomTransitionDuration(500)
//                .setEnableDragClose(true)// 是否启用上拉/下拉关闭。默认不启用
////                .setShowIndicator(showIndicator)// 设置是否显示顶部的指示器（1/9）。默认显示
////                .setErrorPlaceHolder(R.drawable.load_failed)// 设置失败时的占位图，默认为R.drawable.load_failed，设置为0时不显示
//                .start()
//    }
//
//    /**
//     * 大图查看
//     */
//    fun largerView(context: Context, image: String) {
//        val imageInfo =  ImageInfo()
//        imageInfo.originUrl = image
//        imageInfo.thumbnailUrl = image
//        val imageInfoList: ArrayList<ImageInfo> = ArrayList()
//        imageInfoList.add(imageInfo)
//        ImagePreview
//                .getInstance()
//                .setContext(context)
//                .setIndex(0)// 从第一张图片开始，索引从0开始哦
//                .setImageInfoList(imageInfoList)
//                .setShowDownButton(true)
//                .setLoadStrategy(ImagePreview.LoadStrategy.Default)
//                .setFolderName("vcang")
//                .setScaleLevel(1, 3, 8)
//                .setZoomTransitionDuration(500)
//                .setEnableDragClose(true)// 是否启用上拉/下拉关闭。默认不启用
//                .setShowIndicator(false)// 设置是否显示顶部的指示器（1/9）。默认显示
////                .setErrorPlaceHolder(R.drawable.load_failed)// 设置失败时的占位图，默认为R.drawable.load_failed，设置为0时不显示
//                .start()
//    }
//}
