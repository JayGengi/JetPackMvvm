package com.duobang.common.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;
import com.duobang.common.data.constant.IWorkbenchConstant;
import com.duobang.common.data.constant.RouterConstant;

/**
 * 路由
 * 跳转统一管理类（主要用户模块间跳转，以及同模块内通用型页面跳转）
 */
public class AppRoute {



    /**
     * 初始化
     */
    public static void init(@NonNull Application applicationContext, boolean debug) {
        if (debug) {
            debug();
        }
        ARouter.init(applicationContext);
    }

    private static void debug() {
        ARouter.openDebug();
        ARouter.openLog();
        ARouter.printStackTrace();
    }

    public static void route(String path) {
        ARouter.getInstance().build(path).navigation();
    }

    public static void route(Context context, String path) {
        ARouter.getInstance().build(path).navigation(context);
    }

    public static void openX5WebView(String url,String fileName,String type){
        ARouter.getInstance().build(RouterConstant.ACT.TBS_READER)
                .withString("url", url)
                .withString("file_name", fileName)
                .withString("type", type)
                .navigation();
    }

    /**
     * 打开单选用户页
     */
    public static void openChooseUserView(Activity activity, int requestCode, boolean isSingle, String json) {
        ARouter.getInstance().build(RouterConstant.ACT.CHOOSE_USER)
                .withBoolean(IWorkbenchConstant.USER.IS_SINGLE, isSingle)
                .withString(IWorkbenchConstant.USER.CHOOSE_LIST, json)
                .withInt(IWorkbenchConstant.USER.REQUSET_CODE, requestCode)
                .navigation(activity, requestCode);
    }


}
