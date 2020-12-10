package com.duobang.jetpackmvvm.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.jetpackmvvm.base.viewmodel.BaseViewModel
import com.duobang.jetpackmvvm.data.bean.StructureGroup
import com.duobang.jetpackmvvm.ext.request
import com.duobang.jetpackmvvm.network.apiService
import com.duobang.jetpackmvvm.state.ResultState
import com.duobang.jetpackmvvm.util.CacheUtil

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