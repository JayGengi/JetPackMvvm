package com.duobang.common.api

import com.duobang.common.data.bean.*
import okhttp3.RequestBody
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
     * 切换主组织
     *
     * @param orgId
     * @return
     */
    @PUT("/api/account/v1/org/my/home/org/{orgId}")
    suspend fun updateHomeOrg(@Path("orgId") orgId: String?): ApiResponse<OrganizationInfo>
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
    ): ApiResponse<RecordWrapper>

    /**
     * 更新昵称
     */
    @PUT("api/account/v1/user/nickname")
    suspend fun updateNickName(@Body map: Map<String, @JvmSuppressWildcards Any>): ApiResponse<User>

    /**
     * 上传头像文件到OSS服务器，获取返回的URL
     * @param body
     * @return
     */
    @POST("api/file/image")
    suspend fun uploadAvatarFile(@Body body: RequestBody): ApiResponse<String>

    /**
     * 获取项目的单位工程分组列表
     *
     * @return DuobangResponse<StructureGroup>
    </StructureGroup> */
    @GET("api/model/v1/structure/org/{orgId}/group/list")
    suspend fun getStructureList(@Path("orgId") orgId: String?): ApiResponse<List<StructureGroup>>

    /**
     * 获取组织下全部人员，包括部门
     *
     * @param orgId 组织ID
     * @param hasRole 是否需要角色
     * @return DuobangResponse<OrgWrapper>
    </OrgWrapper> */
    @GET("api/account/v1/org/{orgId}/user-group")
    suspend fun getOrgGroupUserWrapper(
        @Path("orgId") orgId: String?,
        @Query("hasRole") hasRole: Boolean
    ): ApiResponse<OrgWrapper>

    /**
     * 获取部门下人员
     *
     * @param groupId
     * @return
     */
    @GET("api/account/v1/group/{groupId}")
    suspend fun getOrgGroupUsers(@Path("groupId") groupId: String?): ApiResponse<List<User>>
}