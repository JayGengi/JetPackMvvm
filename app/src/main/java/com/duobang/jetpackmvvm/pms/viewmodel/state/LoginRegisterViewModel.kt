package com.duobang.jetpackmvvm.pms.viewmodel.state

import com.duobang.jetpackmvvm.base.viewmodel.BaseViewModel
import com.duobang.jetpackmvvm.callback.databind.BooleanObservableField
import com.duobang.jetpackmvvm.callback.databind.StringObservableField
import com.duobang.jetpackmvvm.callback.livedata.StringLiveData

/**
 * 作者　: JayGengi
 * 时间　: 2019/12/23
 * 描述　:登录注册的ViewModel
 */
class LoginRegisterViewModel : BaseViewModel() {

    //用户名
    var username = StringLiveData()

    //密码(登录注册界面)
    var password = StringObservableField()

    var password2 = StringObservableField()

    //是否显示明文密码（登录注册界面）
    var isShowPwd = BooleanObservableField()

    var isShowPwd2 = BooleanObservableField()

}