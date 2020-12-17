package com.duobang.org.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.common.data.bean.User
import com.duobang.common.network.apiService
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.OrgWrapper
import com.duobang.common.data.bean.StructureGroup
import com.duobang.jetpackmvvm.ext.request
import com.duobang.jetpackmvvm.state.ResultState

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/15 17:46
 * @描述　: 获取组织部门下全部人员，不包括部门
 */
class RequestOrgGroupUserViewModel : BaseViewModel() {

    var orgGroupUserResult = MutableLiveData<ResultState<List<User>>>()

    fun getOrgGroupUserWrapper(groupId :String) {
        request(
            { apiService.getOrgGroupUsers(groupId) }//请求体
            , orgGroupUserResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果
            false
        )
    }
}