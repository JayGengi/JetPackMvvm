package com.duobang.login.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.Account
import com.duobang.common.data.bean.User
import com.duobang.common.network.apiService
import com.duobang.base.ext.request
import com.duobang.base.state.ResultState

/**
 * 作者　: JayGengi
 * 时间　: 2019/12/23
 * 描述　:登录注册的请求ViewModel
 */
class RequestLoginViewModel : BaseViewModel() {

    //方式1  自动脱壳过滤处理请求结果，判断结果是否成功
    var loginResult = MutableLiveData<ResultState<Account>>()

    var userInfoResult = MutableLiveData<ResultState<User>>()

    fun loginReq(map: HashMap<String, Any>) {
        request(
            { apiService.accountLogin(map) }//请求体
            , loginResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            false,//是否显示等待框，，默认false不显示 可以默认不传
            "正在登录中..."//等待框内容，可以默认不填请求网络中...
        )
    }

    fun loadPersonInfo() {
        request({ apiService.loadPersonInfo() }, userInfoResult,true)
    }
}