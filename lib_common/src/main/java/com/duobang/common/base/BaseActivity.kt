package com.duobang.common.base

import android.annotation.SuppressLint
import android.content.res.Resources
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.blankj.utilcode.util.BarUtils
import com.duobang.common.R
import com.duobang.common.base.activity.BaseVmDbActivity
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.ext.dismissLoadingExt
import com.duobang.common.ext.showLoadingExt
import com.duobang.jetpackmvvm.ext.getAppViewModel
import com.duobang.common.event.AppViewModel
import me.jessyan.autosize.AutoSizeCompat

/**
 * 时间　: 2019/12/21
 * 作者　: JayGengi
 * 描述　: 你项目中的Activity基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmActivity例如
 * abstract class BaseActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {

    //Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
    val appViewModel: AppViewModel by lazy { getAppViewModel<AppViewModel>() }

    abstract override fun layoutId(): Int

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        BarUtils.setStatusBarLightMode(this, true)
        AppManager.addActivity(this)
    }

    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 创建liveData观察者
     */
    override fun createObserver() {}

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        showLoadingExt(message)
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dismissLoadingExt()
    }

    /**
     * 在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法
     */
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }

    override fun onDestroy() {
        super.onDestroy()
        AppManager.removeActivity(this)
    }
}