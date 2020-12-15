package com.duobang.jetpackmvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.jetpackmvvm.ext.request
import com.duobang.common.network.apiService
import com.duobang.jetpackmvvm.state.ResultState
import okhttp3.RequestBody

/**
 * 作者　: JayGengi
 * 时间　: 2019/12/27
 * 描述　:
 */
class RequestMeViewModel : BaseViewModel() {

    var meData = MutableLiveData<ResultState<String>>()

    fun uploadAvatarFile(body: RequestBody) {
        request(
            { apiService.uploadAvatarFile(body) }//请求体
            , meData,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            false
        )
    }
}