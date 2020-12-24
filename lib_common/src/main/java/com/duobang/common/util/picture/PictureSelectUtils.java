package com.duobang.common.util.picture;

import android.app.Activity;

import com.duobang.common.data.constant.IConstant;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;


/**
 * @des 图片选择封装
 * * <p>
 * * 开源库地址：https://github.com/zhihu/Matisse
 * @auther JayGengi
 * @data 2018/12/18 09:29
 * @email JayGengi@163.com
 */
public class PictureSelectUtils {

    /**
     * 图片选择
     */
    public static void SelectSystemPhoto(Activity activity, int requestCode, int imgCount) {
        Matisse.from(activity)
                .choose(MimeType.ofImage())
                // 图片计数（第几张选中的）
                .countable(false)
                // 开启拍照
                .capture(true)
                .captureStrategy(
                        new CaptureStrategy(true, IConstant.APP.FILE_PROVIDER))
                .maxSelectable(imgCount)
                // 原图按钮
                .originalEnable(true)
                .imageEngine(new MyGlideEngine())
                .forResult(requestCode);
    }
}
