package com.duobang.org.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.Organization
import com.duobang.common.data.bean.OrganizationInfo
import com.duobang.common.network.apiService
import com.duobang.base.ext.request
import com.duobang.base.state.ResultState

/**
  * @作者　: JayGengi
  * @时间　: 2020/12/15 17:43
  * @描述　: 组织首页
 */
class RequestOrgViewModel : BaseViewModel() {

    //获取自己所在组织
    var resultPersonOrgData: MutableLiveData<ResultState<Organization>> = MutableLiveData()

    //切换主组织
    var resultHomeOrgData: MutableLiveData<ResultState<OrganizationInfo>> = MutableLiveData()

    /**
     * 获取自己所在组织
     */
    fun loadPersonOrg() {
        request({ apiService.loadPersonOrg() }, resultPersonOrgData)
    }
    /**
     * 切换主组织
     */
    fun switchHomeOrg(id : String) {
        request({ apiService.updateHomeOrg(id) }, resultHomeOrgData)
    }
}