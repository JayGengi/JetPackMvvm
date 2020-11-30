package com.duobang.jetpackmvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.common.data.bean.User
import com.duobang.common.network.apiService
import com.duobang.jetpackmvvm.base.viewmodel.BaseViewModel
import com.duobang.jetpackmvvm.ext.request
import com.duobang.jetpackmvvm.state.ResultState

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/30 17:46
 * @描述　: 修改用户昵称
 */
class RequestNickNameViewModel : BaseViewModel() {

    var userResult = MutableLiveData<ResultState<User>>()

    fun updateNickName(map: HashMap<String, Any>) {
        request(
            { apiService.updateNickName(map) }//请求体
            , userResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            false
        )
    }
}