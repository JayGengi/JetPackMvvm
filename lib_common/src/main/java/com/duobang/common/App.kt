package com.duobang.common

import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.duobang.base.base.BaseApp
import com.duobang.common.weight.loadCallBack.EmptyCallback
import com.duobang.common.weight.loadCallBack.ErrorCallback
import com.duobang.common.weight.loadCallBack.LoadingCallback
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.tencent.mmkv.MMKV
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.QbSdk.PreInitCallback
import me.jessyan.autosize.utils.AutoSizeLog.isDebug
import java.util.*

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

        initX5WebView()
    }

    private fun initX5WebView() {
        //设置非wifi条件下允许下载X5内核
        QbSdk.setDownloadWithoutWifi(true)
        val map: HashMap<String, Any> = HashMap()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        val cb: PreInitCallback = object : PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("x5", " onViewInitFinished is $arg0")
            }
            override fun onCoreInitFinished() {
                Log.d("x5", " onCoreInitFinished   @@@@@@@@@@")
            }
        }
        //x5内核初始化接口
        QbSdk.initX5Environment(applicationContext, cb)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }



}
