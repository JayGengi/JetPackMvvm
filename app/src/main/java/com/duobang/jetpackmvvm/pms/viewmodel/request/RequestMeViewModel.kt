package com.duobang.jetpackmvvm.pms.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.jetpackmvvm.base.viewmodel.BaseViewModel
import com.duobang.jetpackmvvm.ext.request
import com.duobang.jetpackmvvm.pms.app.network.apiService
import com.duobang.jetpackmvvm.pms.data.model.bean.IntegralResponse
import com.duobang.jetpackmvvm.state.ResultState

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/27
 * 描述　:
 */
class RequestMeViewModel : BaseViewModel() {

    var meData = MutableLiveData<ResultState<IntegralResponse>>()

    fun getIntegral() {
        request({ apiService.getIntegral() }, meData)
    }
}