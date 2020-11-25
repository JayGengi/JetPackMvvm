package com.duobang.common

import android.app.Application.getProcessName
import android.os.Process
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import com.duobang.common.weight.loadCallBack.EmptyCallback
import com.duobang.common.weight.loadCallBack.ErrorCallback
import com.duobang.common.weight.loadCallBack.LoadingCallback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.tencent.bugly.Bugly
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.mmkv.MMKV
import com.duobang.jetpackmvvm.base.BaseApp
import com.duobang.jetpackmvvm.ext.util.jetpackMvvmLog

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
        MultiDex.install(this)
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")
        //界面加载管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())//加载
            .addCallback(ErrorCallback())//错误
            .addCallback(EmptyCallback())//空
            .setDefaultCallback(SuccessCallback::class.java)//设置默认加载状态页
            .commit()

    }

}
