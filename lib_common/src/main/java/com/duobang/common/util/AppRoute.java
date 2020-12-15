package com.duobang.common.util;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.launcher.ARouter;

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

}
