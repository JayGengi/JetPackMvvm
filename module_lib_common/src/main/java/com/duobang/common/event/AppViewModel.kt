package com.duobang.common.event

import com.duobang.common.data.bean.User
import com.duobang.jetpackmvvm.base.viewmodel.BaseViewModel
import com.duobang.jetpackmvvm.callback.livedata.UnPeekLiveData
import com.duobang.common.util.CacheUtil
import com.duobang.common.util.SettingUtil

/**
 * 作者　: JayGengi
 * 时间　: 2019/12/23
 * 描述　:APP全局的ViewModel，可以存放公共数据，当他数据改变时，所有监听他的地方都会收到回调,也可以做发送消息
 * 比如 全局可使用的 地理位置信息，账户信息,App的基本配置等等，
 */
class AppViewModel : BaseViewModel() {

    //App的账户信息
    var userinfo = UnPeekLiveData<User>()

    //App 列表动画
    var appAnimation = UnPeekLiveData<Int>()

    init {
        //默认值保存的账户信息，没有登陆过则为null
        userinfo.value = CacheUtil.getUser()
        //初始化列表动画
        appAnimation.value = SettingUtil.getListMode()
    }
}