package com.duobang.jetpackmvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.jetpackmvvm.base.viewmodel.BaseViewModel
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

    //获取总览页面指标
    var resultPersonOrgData: MutableLiveData<ResultState<Organization>> = MutableLiveData()

    /**
     * 获取总览页面指标
     */
    fun loadDashboardQuota() {
        request({ apiService.loadPersonOrg() }, resultPersonOrgData)
    }
}