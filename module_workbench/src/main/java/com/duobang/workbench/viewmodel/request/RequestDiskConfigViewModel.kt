package com.duobang.workbench.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.common.network.apiService
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.util.CacheUtil
import com.duobang.base.ext.request
import com.duobang.base.state.ResultState

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/15 17:46
 * @描述　: 云盘配置
 */
class RequestDiskConfigViewModel : BaseViewModel() {

    var loadDiskManagerResult = MutableLiveData<ResultState<Any>>()

    fun diskManager(map:Map<String,Any> ) {
        request(
            { apiService.diskManager(CacheUtil.getOrg()!!.homeOrgId!!,map) }//请求体
            , loadDiskManagerResult,//请求的返回结果
            true
        )
    }
}