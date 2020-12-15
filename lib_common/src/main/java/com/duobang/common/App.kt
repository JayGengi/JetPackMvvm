package com.duobang.common

import android.content.Context
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.duobang.common.base.BaseApp
import com.duobang.common.weight.loadCallBack.EmptyCallback
import com.duobang.common.weight.loadCallBack.ErrorCallback
import com.duobang.common.weight.loadCallBack.LoadingCallback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.tencent.mmkv.MMKV
import me.jessyan.autosize.utils.AutoSizeLog.isDebug

/**
 * 作者　: JayGengi
 * 时间　: 2019/12/23
 * 描述　:
 */

class App : BaseApp() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
        //界面加载管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())//加载
            .addCallback(ErrorCallback())//错误
            .addCallback(EmptyCallback())//空
            .setDefaultCallback(SuccessCallback::class.java)//设置默认加载状态页
            .commit()

        if (isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(instance) // 尽可能早，推荐在Application中初始化

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}
