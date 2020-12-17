package com.duobang.jetpackmvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.common.base.viewmodel.BaseViewModel
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

    /**
     * 获取自己所在组织
     */
    fun loadPersonOrg() {
        request({ apiService.loadPersonOrg() }, resultPersonOrgData)
    }
}