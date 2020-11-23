package com.duobang.jetpackmvvm.pms.api

import com.duobang.jetpackmvvm.pms.data.model.bean.*
import retrofit2.http.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/20 10:15
 * @描述　: 网络API
 */
interface ApiService {

    companion object {
        const val SERVER_URL = "http://pms.duobangbox.cn/"
    }

    /**
     * 账号登录
     */
    @POST("api/account/v1/sign/signin/username?from=Android")
    suspend fun accountLogin(@Body map: Map<String, @JvmSuppressWildcards Any>): ApiResponse<Account>

    /**
     * 获取自己信息
     */
    @GET("api/account/v1/user/my/info")
    suspend fun loadPersonInfo(): ApiResponse<User>

    /**
     * 获取自己所在组织
     *
     * @return DuobangResponse<Organization>
    </Organization> */
    @GET("api/account/v1/org/my/org/list")
    suspend fun loadPersonOrg(): ApiResponse<Organization>
    /**
     * 获取总览页面指标
     *
     * @param orgId
     * @return
     */
    @GET("api/record/v1/agg/org/{orgId}/quantity-price/metrix/time")
    suspend fun getDashboardQuota(@Path("orgId") orgId: String?): ApiResponse<Dashboard>


    /**
     * 获取组织下的记录，分页
     *
     * @param orgId
     * @param page
     * @param count
     * @return
     */
    @GET("api/record/v1/record/org/{orgId}")
    suspend fun getOrgRecordList(
        @Path("orgId") orgId: String?,
        @Query("pageNumber") page: Int,
        @Query("pageSize") count: Int
    ): ApiResponse<ApiPagerResponse<ArrayList<RecordWrapper>>>
}