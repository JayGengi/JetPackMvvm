package com.duobang.jetpackmvvm.ui.activity

import android.content.ServiceConnection
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseActivity
import com.duobang.common.data.constant.RouterConstant
import com.duobang.common.ext.showToast
import com.duobang.common.network.manager.NetState
import com.duobang.common.room.repository.PmsRepository
import com.duobang.common.socket.AppSocket
import com.duobang.common.socket.i.IConstants
import com.duobang.common.socket.i.IEventType
import com.duobang.common.socket.model.ObserverModel
import com.duobang.common.socket.model.SocketMessage
import com.duobang.common.socket.push.BaseTask
import com.duobang.common.socket.push.MainTask
import com.duobang.common.util.CacheUtil
import com.duobang.common.util.JsonUtil
import com.duobang.common.util.SettingUtil
import com.duobang.jetpackmvvm.R
import com.duobang.jetpackmvvm.data.repository.local.MainSocketDisposeManager
import com.duobang.jetpackmvvm.databinding.ActivityMainBinding
import com.duobang.jetpackmvvm.ext.init
import com.duobang.jetpackmvvm.ext.initMain
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.jetpackmvvm.viewmodel.request.RequestMainViewModel
import com.duobang.jetpackmvvm.viewmodel.state.MainViewModel
import com.duobang.org.ui.fragment.OrgFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

/**
 * 项目主页Activity
 */
@Route(path = RouterConstant.ACT.MAIN)
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>(),
    OrgFragment.OnOrganizationSwitchListener, java.util.Observer {
    private var conn: ServiceConnection? = null
    private var isConnService = false

    //请求数据ViewModel
    private val requestMainViewModel: RequestMainViewModel by viewModels()

    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {

        BarUtils.transparentStatusBar(this)

        requestMainViewModel.loadPersonOrg()

    }

    override fun createObserver() {
        requestMainViewModel.run {
            resultPersonOrgData.observe(
                this@MainActivity,
                Observer { resultState ->
                    parseState(resultState, {
                        //缓存组织信息（orgId）
                        CacheUtil.setOrg(it)
                        appViewModel.orginfo.value = it
                        requestMainViewModel.getOrgGroupUserWrapper(it.homeOrgId!!)
                        //初始化viewpager2
                        mainViewpager.initMain(this@MainActivity)
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
                        mainBottom.setTextTintList(
                            2,
                            SettingUtil.getColorStateList(R.color.bottom_selector2)
                        )
                        mainBottom.setIconTintList(
                            2,
                            SettingUtil.getColorStateList(R.color.bottom_selector2)
                        )

                        startTaskService()
                    }, {
                        ToastUtils.showShort(it.errorMsg)
                    })
                })
            //当前组织下所有用户
            orgGroupUserResult.observe(
                this@MainActivity,
                Observer { resultState ->
                    parseState(resultState, {
                        //设置部门头
                        PmsRepository(this@MainActivity).delAllUser()
                        PmsRepository(this@MainActivity).insertAllUser(it.userList!!)
                    }, {
                        ToastUtils.showShort(it.errorMsg)
                    })
                })
        }
    }

    private fun startTaskService() {
        MainTask.init()
//        startPushService()
        MainTask.getInstance().addObserver(this)
    }

//    private fun startPushService() {
//        conn = object : ServiceConnection {
//            override fun onServiceConnected(name: ComponentName, service: IBinder) {
//                isConnService = true
//            }
//
//            override fun onServiceDisconnected(name: ComponentName) {
//                isConnService = false
//            }
//        }
//        bindService(
//            Intent(this, PushService::class.java),
//            conn,
//            Context.BIND_AUTO_CREATE
//        )
//    }

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

    /**
     * 切换组织重新加载
     */
    override fun onOrganizationSwitch() {
        //切回首页重新加载数据
        mainBottom.currentItem = 0
        requestMainViewModel.loadPersonOrg()
    }

    override fun update(observable: Observable, o: Any) {
        if (observable is BaseTask) {
            val model: ObserverModel = o as ObserverModel
            val timeByOrgJson: String = CacheUtil.getSocketTimeStamp()
            val orgId: String = CacheUtil.getOrg()!!.homeOrgId!!
            val timeMap: MutableMap<String, Long> = JsonUtil.toMap(timeByOrgJson)
            when (model.eventType) {
                IEventType.CONNECT_MESSAGE ->
                    if (AppSocket.getInstance().isConnected) {
                        //socketIO连接中，客户端推送服务端 "Auth" 事件(用作用户认证)的body结构
                        val userId: String = CacheUtil.getUser()!!.id
                        val sessionId: String = CacheUtil.getToken()
                        val map: MutableMap<String, Any> = HashMap()
                        map["userId"] = userId
                        map["orgId"] = orgId
                        val time: Long = if (null != timeMap[orgId]) {
                            timeMap[orgId]!!
                        } else {
                            0
                        }
                        map["time"] = time
                        map["sessionId"] = sessionId
                        LogUtils.d("socketAuth", timeByOrgJson + "-----" + JsonUtil.toJson(map))
                        AppSocket.getInstance().socketConnectPath("Auth", JsonUtil.toJson(map))
                    }
                IEventType.DATA -> try {
                    val socketDisposeManager = MainSocketDisposeManager()
                    val socketMsg: List<SocketMessage> = model.socketMessages
                    if (socketMsg.isNotEmpty()) {
                        /**存储最后一条数据即最新的时间戳 */
                        val timeStamp: Long = socketMsg[socketMsg.size - 1].createAt
                        timeMap[orgId] = timeStamp
                        CacheUtil.setSocketTimeStamp(JsonUtil.toJson(timeMap))
                        LogUtils.d("socketTimeStamp", timeStamp.toString() + "")
                        LogUtils.d("socketConnect", JsonUtil.toJson(socketMsg).toString() + "")
                        var i = 0
                        while (i < socketMsg.size) {
                            when (socketMsg[i].namespace) {
                                IConstants.NAMESPACE.APP_DAILY_TASK ->
                                    socketDisposeManager.dailyTaskMsg(this, socketMsg[i])
                                else -> {
                                }
                            }
                            i++
                        }
                        //发送通知,目前仅有日事日毕有消息推送
                        eventViewModel.dailyTaskEvent.postValue(true)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                else -> {
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (MainTask.getInstance() != null) {
            MainTask.getInstance().deleteObserver(this)
        }
        if (AppSocket.getInstance() != null) {
            AppSocket.getInstance().disConnnect()
        }
    }


}
