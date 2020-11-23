package com.duobang.jetpackmvvm.pms.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.jetpackmvvm.base.viewmodel.BaseViewModel
import com.duobang.jetpackmvvm.ext.request
import com.duobang.jetpackmvvm.pms.data.model.bean.Dashboard
import com.duobang.jetpackmvvm.pms.data.model.bean.RecordWrapper
import com.duobang.jetpackmvvm.pms.network.apiService
import com.duobang.jetpackmvvm.pms.network.stateCallback.ListDataUiState
import com.duobang.jetpackmvvm.pms.util.CacheUtil
import com.duobang.jetpackmvvm.state.ResultState

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/23 16:51
 * @描述　: 首页VM
 */
class RequestHomeViewModel : BaseViewModel() {

    //页码 首页数据页码从0开始
    var pageNo = 0

    //获取总览页面指标
    var dashboardQuotaData: MutableLiveData<ResultState<Dashboard>> = MutableLiveData()

    //获取组织下的记录，分页
    var recordListData: MutableLiveData<ListDataUiState<RecordWrapper>> = MutableLiveData()

    val orgId = CacheUtil.getOrg()?.homeOrgInfo?.id

    /**
     * 获取组织下的记录，分页
     * @param isRefresh 是否是刷新，即第一页
     */
    fun loadRecordList(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        request({ apiService.getOrgRecordList(orgId, pageNo, 10) }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            recordListData.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<RecordWrapper>()
                )
            recordListData.value = listDataUiState
        })
    }

    /**
     * 获取总览页面指标
     */
    fun loadDashboardQuota() {
        request({ apiService.getDashboardQuota(orgId) }, dashboardQuotaData)
    }
}