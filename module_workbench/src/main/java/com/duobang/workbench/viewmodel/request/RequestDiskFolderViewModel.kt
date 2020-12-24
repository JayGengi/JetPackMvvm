package com.duobang.workbench.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.common.network.apiService
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.*
import com.duobang.common.util.CacheUtil
import com.duobang.base.ext.request
import com.duobang.base.state.ResultState

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/16 12:40
 * @描述　: 获取工作项列表
 */
class RequestDiskFolderViewModel : BaseViewModel() {

    private val orgId = CacheUtil.getOrg()!!.homeOrgId!!

    var loadDiskMoveListResult = MutableLiveData<ResultState<List<DiskBean>>>()

    var loadDiskBreadcrumbsResult = MutableLiveData<ResultState<List<DiskBean>>>()

    var loadDiskFileMoveResult = MutableLiveData<ResultState<Any>>()


    //获取某个文件夹下的子文件夹列表(供文件移动选择目录使用)
    fun diskMoveList(currentId:String,pid: String) {
        request(
            { apiService.diskMoveList(orgId,currentId,pid) }//请求体
            , loadDiskMoveListResult//请求的返回结果
        )
    }

    //获取当前文件夹的面包屑导航路径
    fun diskBreadcrumbs(dirId :String) {
        request(
            { apiService.diskBreadcrumbs(dirId) }//请求体
            , loadDiskBreadcrumbsResult//请求的返回结果
        )
    }

    //文件或文件夹移动
    fun diskFileMove(dirId: String,map :Map<String,Any>) {
        request(
            { apiService.diskFileMove(orgId,dirId,map) }//请求体
            , loadDiskFileMoveResult,//请求的返回结果
            false
        )
    }
}