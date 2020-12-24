package com.duobang.workbench.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.common.network.apiService
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.DailySubmission
import com.duobang.base.ext.request
import com.duobang.base.state.ResultState

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/15 17:46
 * @描述　: 获取项目下某天日事日毕用户的提交情况
 */
class RequestDailyManageViewModel : BaseViewModel() {

    var loadSubmissionResult = MutableLiveData<ResultState<List<DailySubmission>>>()

    fun loadSubmission(ordId :String,date:String) {
        request(
            { apiService.loadSubmission(ordId,date) }//请求体
            , loadSubmissionResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果
            false
        )
    }
}