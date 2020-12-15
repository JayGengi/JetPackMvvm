package com.duobang.jetpackmvvm.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.duobang.jetpackmvvm.R
import com.duobang.common.base.BaseActivity
import com.duobang.common.data.constant.RouterConstant
import com.duobang.jetpackmvvm.databinding.ActivityMainBinding
import com.duobang.jetpackmvvm.ext.initMain
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.common.ext.showToast
import com.duobang.common.network.manager.NetState
import com.duobang.common.util.CacheUtil
import com.duobang.jetpackmvvm.ext.init
import com.duobang.jetpackmvvm.viewmodel.request.RequestMainViewModel
import com.duobang.jetpackmvvm.viewmodel.state.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 项目主页Activity
 */
@Route(path = RouterConstant.ACT.MAIN)
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    //请求数据ViewModel
    private val requestMainViewModel: RequestMainViewModel by viewModels()

    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        BarUtils.transparentStatusBar(this)
        BarUtils.setStatusBarLightMode(this, true)
        requestMainViewModel.loadDashboardQuota()
    }

    override fun createObserver() {
        requestMainViewModel.resultPersonOrgData.observe(
            this,
            Observer { resultState ->
                parseState(resultState, {
                    //缓存组织信息（orgId）
                    CacheUtil.setOrg(it)
                    appViewModel.orginfo.value = it

                    //初始化viewpager2
                    mainViewpager.initMain(this)
                    //初始化 bottomBar
                    mainBottom.init {
                        when (it) {
                            R.id.menu_main -> mainViewpager.setCurrentItem(0, false)
                            R.id.menu_project -> mainViewpager.setCurrentItem(1, false)
                            R.id.menu_work -> mainViewpager.setCurrentItem(2, false)
                            R.id.menu_org -> mainViewpager.setCurrentItem(3, false)
                            R.id.menu_me -> mainViewpager.setCurrentItem(4, false)
                        }
                    }
                }, {
                    ToastUtils.showShort(it.errorMsg)
                })
            })
    }

    /**
     * 示例，在Activity/Fragment中如果想监听网络变化，可重写onNetworkStateChanged该方法
     */
    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
            showToast("网络已连接")
        } else {
            showToast("网络中断")
        }
    }

    private var mExitTime: Long = 0
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis().minus(mExitTime) <= 2000) {
                finish()
            } else {
                mExitTime = System.currentTimeMillis()
                showToast("再按一次退出程序")
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


}
