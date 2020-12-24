package com.duobang.workbench.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.common.network.apiService
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.OrgWrapper
import com.duobang.base.ext.request
import com.duobang.base.state.ResultState

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/15 17:46
 * @描述　: 获取组织下全部人员，包括部门
 */
class RequestOrgGroupViewModel : BaseViewModel() {

    var orgGroupUserResult = MutableLiveData<ResultState<OrgWrapper>>()

    fun getOrgGroupUserWrapper(orgId :String,hasRole : Boolean = false) {
        request(
            { apiService.getOrgGroupUserWrapper(orgId,hasRole) }//请求体
            , orgGroupUserResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            false
        )
    }
}