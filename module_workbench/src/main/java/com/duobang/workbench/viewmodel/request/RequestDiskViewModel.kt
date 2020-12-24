package com.duobang.workbench.viewmodel.request

import androidx.lifecycle.MutableLiveData
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.DiskBean
import com.duobang.common.network.apiService
import com.duobang.common.util.CacheUtil
import com.duobang.base.ext.request
import com.duobang.base.state.ResultState
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
  * @作者　: JayGengi
  * @时间　: 2020/12/21 9:34
  * @描述　: 云盘
 */
class RequestDiskViewModel : BaseViewModel() {

    private val orgId = CacheUtil.getOrg()!!.homeOrgId!!

    var loadDiskPermissionResult = MutableLiveData<ResultState<Boolean>>()

    var loadDiskDirResult = MutableLiveData<ResultState<Any>>()

    var loadDiskListResult = MutableLiveData<ResultState<List<DiskBean>>>()

    var loadReNameResult = MutableLiveData<ResultState<Any>>()

    var loadDiskFileDelResult = MutableLiveData<ResultState<Any>>()

    var loadDiskkBreadcrumbsResult = MutableLiveData<ResultState<List<DiskBean>>>()

    var loadDiskFileUpResult = MutableLiveData<ResultState<Any>>()

    var loadDiskFileUrlResult = MutableLiveData<ResultState<String>>()

    //检查云盘管理员权限
    fun diskPermission() {
        request(
            { apiService.diskPermission(orgId) }//请求体
            , loadDiskPermissionResult//请求的返回结果
        )
    }

    //创建文件夹
    fun diskDir(map :Map<String, Any> ) {
        request(
            { apiService.diskDir(orgId,map) }//请求体
            , loadDiskDirResult//请求的返回结果
        )
    }

    //获取某个文件夹下的文件(文件夹列表)
    fun diskList(pid :String) {
        request(
            { apiService.diskList(orgId,pid) }//请求体
            , loadDiskListResult//请求的返回结果
        )
    }
    //目录重命名
    fun diskFileReName(pid :String,map :Map<String, Any> ) {
        request(
            { apiService.diskFileReName(pid,map) }//请求体
            , loadReNameResult//请求的返回结果
        )
    }

    //删除多个文件或者文件夹，包括服务器端和oss端
    fun diskFileDel(map :Map<String, Any>) {
        request(
            { apiService.diskFileDel(orgId,map) }//请求体
            , loadDiskFileDelResult//请求的返回结果
        )
    }
    //获取当前文件夹的面包屑导航路径
    fun diskBreadcrumbs(dirId :String) {
        request(
            { apiService.diskBreadcrumbs(dirId) }//请求体
            , loadDiskkBreadcrumbsResult//请求的返回结果
        )
    }
    //云盘文件上传，必须有文件夹id
    fun diskFileUp(dirId :String,filePaths: String) {

        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val file = File(filePaths)
        builder.addFormDataPart(
            "file",
            file.name,
            RequestBody.create(MediaType.parse("multipart/form-data"), file)
        )
        val body = builder.build()
        request(
            { apiService.diskFileUp(dirId,body) }//请求体
            , loadDiskFileUpResult//请求的返回结果
        )
    }

    //获取文件在oss上面的url，用于web前端下载或预览文件
    fun diskFileUrl(fileId :String) {
        request(
            { apiService.diskFileUrl(fileId) }//请求体
            , loadDiskFileUrlResult//请求的返回结果
        )
    }

}