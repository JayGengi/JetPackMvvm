package com.duobang.workbench.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.DailyTask
import com.duobang.common.network.apiService
import com.duobang.base.ext.request
import com.duobang.base.state.ResultState
import okhttp3.MediaType
import okhttp3.RequestBody

/**
  * @作者　: JayGengi
  * @时间　: 2020/12/20 9:57
  * @描述　: 日事日毕创建更新
 */
class RequestDailyTaskCreateViewModel : BaseViewModel() {

    var loadPersonalDailyTasksResult = MutableLiveData<ResultState<List<DailyTask>>>()

    var loadUploadDailyTaskResult = MutableLiveData<ResultState<List<DailyTask>>>()
    //获取个人当天日事日毕列表
    fun loadPersonalDailyTasks(ordId :String) {
        request(
            { apiService.getPersonalDailyTasks(ordId) }//请求体
            , loadPersonalDailyTasksResult,//请求的返回结果
            false
        )
    }

    //创建新的日事日毕事项
    fun uploadDailyTask(ordId :String,body :String) {
        request(
            { apiService.uploadDailyTask(ordId,
                RequestBody.create(MediaType.parse("application/json; charset=utf-8"), body)) }//请求体
            , loadUploadDailyTaskResult,//请求的返回结果
            true
        )
    }

}