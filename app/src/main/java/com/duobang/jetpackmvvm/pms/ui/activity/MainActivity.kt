package com.duobang.jetpackmvvm.pms.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.jetpackmvvm.network.manager.NetState
import com.duobang.jetpackmvvm.pms.R
import com.duobang.jetpackmvvm.pms.base.BaseActivity
import com.duobang.jetpackmvvm.pms.databinding.ActivityMainBinding
import com.duobang.jetpackmvvm.pms.util.CacheUtil
import com.duobang.jetpackmvvm.pms.viewmodel.request.RequestHomeViewModel
import com.duobang.jetpackmvvm.pms.viewmodel.request.RequestMainViewModel
import com.duobang.jetpackmvvm.pms.viewmodel.state.MainViewModel
import com.tencent.bugly.beta.Beta

/**
 * 项目主页Activity
 */
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    var exitTime = 0L

    //请求数据ViewModel
    private val requestMainViewModel: RequestMainViewModel by viewModels()

    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav = Navigation.findNavController(this@MainActivity, R.id.host_fragment)
                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mainfragment) {
                    //如果当前界面不是主页，那么直接调用返回即可
                    nav.navigateUp()
                } else {
                    //是主页
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        ToastUtils.showShort("再按一次退出程序")
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }
        })

        requestMainViewModel.loadDashboardQuota()
    }

    override fun createObserver() {
        requestMainViewModel.resultPersonOrgData.observe(
            this,
            Observer { resultState ->
                parseState(resultState, {
                    //缓存组织信息（orgId）
                    CacheUtil.setOrg(it)
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
            Toast.makeText(applicationContext, "网络已连接", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "网络中断", Toast.LENGTH_SHORT).show()
        }
    }

}
