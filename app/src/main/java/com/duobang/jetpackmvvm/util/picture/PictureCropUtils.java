package com.duobang.jetpackmvvm.util.picture;//package com.duobang.jetpackmvvm.util.picture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;

import com.blankj.utilcode.util.FileUtils;
import com.duobang.jetpackmvvm.R;
import com.duobang.jetpackmvvm.data.enums.IConstant;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;

/**
 * 描述：图片剪裁封装
 * <p>
 * 开源库地址：https://github.com/Yalantis/uCrop
 *
 * @author jayGengi
 * @date 2018/7/19
 */
public class PictureCropUtils {
    /**
     * 通用配置
     *
     * @return
     */
    private static UCrop.Options commonlyOptions(Activity activity) {
        // 修改配置参数（部分配置）
        UCrop.Options options = new UCrop.Options();
        // 修改标题栏颜色
        options.setToolbarColor(activity.getResources().getColor(R.color.colorPrimary));
        // 修改状态栏颜色
        options.setStatusBarColor(activity.getResources().getColor(R.color.colorPrimary));
        // 隐藏底部工具
        options.setHideBottomControls(false);
        // 图片格式
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        //设置裁剪图片可操作的手势
        options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);

        return options;
    }

    /**
     * 圆形剪裁
     */
    public static void cropCirclePicture(Activity activity, Uri uri) {

        UCrop.Options options = commonlyOptions(activity);

        // 设置图片压缩质量
        options.setCompressionQuality(100);

        // 是否让用户调整范围(默认false)，如果开启，可能会造成剪切的图片的长宽比不是设定的
        // 如果不开启，用户不能拖动选框，只能缩放图片
        options.setFreeStyleCropEnabled(false);
        // 圆形蒙版
        options.setCircleDimmedLayer(true);

        FileUtils.createOrExistsDir(IConstant.APP.Companion.getSAVE_REAL_PATH());

        // 设置源uri及目标uri
        UCrop.of(uri, Uri.fromFile(new File(IConstant.APP.Companion.getSAVE_REAL_PATH(), System.currentTimeMillis() + ".jpg")))
                // 长宽比
                .withAspectRatio(1, 1)
                // 图片大小
                .withMaxResultSize(200, 200)
                // 配置参数
                .withOptions(options)
                .start(activity);
    }

    /**
     * 通用剪裁
     */
    public static void cropGeneralPicture(Activity activity, Uri uri) {

        UCrop.Options options = commonlyOptions(activity);

        // 设置图片压缩质量
        options.setCompressionQuality(100);
        //是否能调整裁剪框
        options.setFreeStyleCropEnabled(true);
        // 是否让用户调整范围(默认false)，如果开启，可能会造成剪切的图片的长宽比不是设定的
        // 如果不开启，用户不能拖动选框，只能缩放图片
        options.setFreeStyleCropEnabled(true);
        FileUtils.createOrExistsDir(IConstant.APP.Companion.getSAVE_REAL_PATH());

        // 设置源uri及目标uri
        UCrop.of(uri, Uri.fromFile(new File(IConstant.APP.Companion.getSAVE_REAL_PATH(), System.currentTimeMillis() + ".jpg")))
                // 长宽比
                .withAspectRatio(4, 3)
                // 图片大小
                .withMaxResultSize(700, 300)
                // 配置参数
                .withOptions(options)
                .start(activity);
    }
}
