package com.duobang.workbench.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.common.network.apiService
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.*
import com.duobang.base.ext.request
import com.duobang.base.state.ResultState

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/16 12:40
 * @描述　: 获取工作项列表
 */
class RequestDailyTaskViewModel : BaseViewModel() {

    var loadDailyTaskResult = MutableLiveData<ResultState<List<DailyTaskWrapper>>>()

    //获取工作项列表
    fun loadDailyTaskList(ordId :String,date:String) {
        request(
            { apiService.getDailyTaskList(ordId,date) }//请求体
            , loadDailyTaskResult,//请求的返回结果
            false
        )
    }

    var loadDelayTaskResult = MutableLiveData<ResultState<DailyTask>>()

    var loadUploadDailyComment = MutableLiveData<ResultState<DailyTaskWrapper>>()

    var loadDeleteDailyComment = MutableLiveData<ResultState<Any>>()


    //推迟之前未完成的事项到今天
    fun loadDelayTask(dailyTaskId :String) {
        request(
            { apiService.delayTask(dailyTaskId) }//请求体
            , loadDelayTaskResult,//请求的返回结果
            false
        )
    }

    //发表评论
    fun uploadDailyComment(dailyGroupId: String,map :Map<String,Any>) {
        request(
            { apiService.uploadDailyComment(dailyGroupId,map) }//请求体
            , loadUploadDailyComment,//请求的返回结果
            false
        )
    }

    //删除评论
    fun deleteDailyComment(commentId :String) {
        request(
            { apiService.deleteDailyComment(commentId) }//请求体
            , loadDeleteDailyComment,//请求的返回结果
            false
        )
    }
}