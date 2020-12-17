package com.duobang.jetpackmvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.OrgWrapper
import com.duobang.jetpackmvvm.ext.request
import com.duobang.common.data.bean.Organization
import com.duobang.common.network.apiService
import com.duobang.jetpackmvvm.state.ResultState

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/23 16:51
 * @描述　: 首页VM
 */
class RequestMainViewModel : BaseViewModel() {

    var resultPersonOrgData: MutableLiveData<ResultState<Organization>> = MutableLiveData()

    var orgGroupUserResult = MutableLiveData<ResultState<OrgWrapper>>()
    /**
     * 获取自己所在组织
     */
    fun loadPersonOrg() {
        request({ apiService.loadPersonOrg() }, resultPersonOrgData)
    }



    fun getOrgGroupUserWrapper(orgId :String) {
        request(
            { apiService.getOrgGroupUserWrapper(orgId,hasRole = false) }//请求体
            , orgGroupUserResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            false
        )
    }
}