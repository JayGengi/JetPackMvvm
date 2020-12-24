package com.duobang.project.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.StructureGroup
import com.duobang.base.ext.request
import com.duobang.common.network.apiService
import com.duobang.base.state.ResultState
import com.duobang.common.util.CacheUtil

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/28
 * 描述　:
 */
class RequestProjectViewModel : BaseViewModel() {

    var projectDataState: MutableLiveData<ResultState<List<StructureGroup>>> = MutableLiveData()

    fun getProjectData(orgId: String? = CacheUtil.getOrg()!!.homeOrgId) {
        request({ apiService.getStructureList(orgId) }, projectDataState)
    }
}