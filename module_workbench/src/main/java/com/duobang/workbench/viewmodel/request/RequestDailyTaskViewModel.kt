package com.duobang.workbench.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.common.network.apiService
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.*
import com.duobang.jetpackmvvm.ext.request
import com.duobang.jetpackmvvm.state.ResultState

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/16 12:40
 * @描述　: 获取工作项列表
 */
class RequestDailyTaskViewModel : BaseViewModel() {

    var loadDailyTaskResult = MutableLiveData<ResultState<List<DailyTaskWrapper>>>()

    fun loadDailyTaskList(ordId :String,date:String) {
        request(
            { apiService.getDailyTaskList(ordId,date) }//请求体
            , loadDailyTaskResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果
            false
        )
    }
}