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

    /**
     * 获取项目下某天日事日毕用户的提交情况
     *
     * @param orgId
     * @param date
     * @return
     */
    @GET("api/daily-task/v1/daily-task/org/{orgId}/submission")
    suspend fun loadSubmission(
        @Path("orgId") orgId: String?, @Query("date") date: String?
    ): ApiResponse<List<DailySubmission>>

    /**
     * 获取工作项列表
     *
     * @return DuobangResponse<ModelTree>
    </ModelTree> */
    @GET("api/daily-task/v1/daily-task/org/{orgId}")
    suspend fun getDailyTaskList(
        @Path("orgId") orgId: String?,
        @Query("date") date: String?
    ): ApiResponse<List<DailyTaskWrapper>>

    /**
     * 推迟之前未完成的事项到今天
     *
     * @param dailyTaskId
     * @return
     */
    @PUT("api/daily-task/v1/daily-task/{dailyTaskId}/delay")
    suspend fun delayTask(@Path("dailyTaskId") dailyTaskId: String): ApiResponse<DailyTask>

    /**
     * 发表评论
     *
     * @param dailyGroupId
     * @param body
     * @return
     */
    @POST("api/daily-task/v1/daily-task/comment/{dailyGroupId}")
    suspend fun uploadDailyComment(
        @Path("dailyGroupId") dailyGroupId: String?,
        @Body map: Map<String, @JvmSuppressWildcards Any>
    ): ApiResponse<DailyTaskWrapper>

    /**
     * 删除评论
     *
     * @param commentId
     * @return
     */
    @DELETE("api/daily-task/v1/daily-task/comment/{commentId}")
    suspend fun deleteDailyComment(@Path("commentId") commentId: String?): ApiResponse<Any>

    /**
     * 获取个人当天日事日毕列表
     *
     * @param orgId
     * @return
     */
    @GET("api/daily-task/v1/daily-task/org/{orgId}/mine/today")
    suspend fun getPersonalDailyTasks(@Path("orgId") orgId: String): ApiResponse<List<DailyTask>>

    /**
     * 创建新的日事日毕事项
     *
     * @param orgId
     * @param body
     * @return
     */
    @POST("api/daily-task/v1/daily-task/org/{orgId}/")
    suspend fun uploadDailyTask(@Path("orgId") orgId: String,  @Body body: RequestBody
    ): ApiResponse<List<DailyTask>>


    /**----------------------------云盘begin----------------------------*/
    /**
     * 检查云盘管理员权限
     */
    @GET("/api/file/disk/org/{orgId}/admin/permission")
    suspend fun diskPermission(@Path("orgId") orgId: String): ApiResponse<Boolean>

    /**
     * 创建文件夹,没有pid属性(或者不赋值)则为顶级目录，只能由云盘管理权限创建，子文件夹必须要pid
     */
    @POST("/api/file/disk/org/{orgId}/dir")
    suspend fun diskDir(@Path("orgId") orgId: String, @Body map: Map<String, @JvmSuppressWildcards Any>): ApiResponse<Any>

    /**
     * 设置顶级的文件夹的属性, 私密性, 管理员, 成员
     */
    @PUT("/api/file/disk/org/{orgId}/dirs/options")
    suspend fun diskManager(@Path("orgId") orgId: String, @Body map: Map<String, @JvmSuppressWildcards Any>): ApiResponse<Any>

    /**
     * 云盘文件上传，必须有文件夹id
     */
    @POST("/api/file/disk/dir/{id}/file")
    suspend fun diskFileUp(@Path("id") id: String, @Body body: RequestBody): ApiResponse<Any>

    /**
     * 获取某个文件夹下的文件(文件夹列表)
     */
    @GET("/api/file/disk/org/{orgId}/list")
    suspend fun diskList(@Path("orgId") orgId: String, @Query("pid") pid: String): ApiResponse<List<DiskBean>>

    /**
     * 获取文件在oss上面的url，用于web前端下载或预览文件
     */
    @GET("/api/file/disk/{fileId}/url")
    suspend fun diskFileUrlAdapter(@Path("fileId") fileId: String): ApiResponse<String>

    /**
     * 获取文件在oss上面的url，用于web前端下载或预览文件
     */
    @GET("/api/file/disk/{fileId}/url")
    suspend fun diskFileUrl(@Path("fileId") fileId: String): ApiResponse<String>
    /**
     * 删除多个文件或者文件夹，包括服务器端和oss端
     */
    @HTTP(method = "DELETE", path = "/api/file/disk/org/{orgId}", hasBody = true)
    suspend fun diskFileDel(@Path("orgId") orgId: String?, @Body map: Map<String, @JvmSuppressWildcards Any>
    ): ApiResponse<Any>

    /**
     * 获取某个文件夹下的子文件夹列表(供文件移动选择目录使用)
     */
    @GET("/api/file/disk/org/{orgId}/current/{currentId}/dir/list")
    suspend fun diskMoveList(
        @Path("orgId") orgId: String,
        @Path("currentId") currentId: String,
        @Query("pid") pid: String
    ): ApiResponse<List<DiskBean>>

    /**
     * 文件或文件夹移动
     */
    @PUT("/api/file/disk/org/{orgId}/target/dir/{dirId}")
    suspend fun diskFileMove(
        @Path("orgId") orgId: String,
        @Path("dirId") dirId: String,
        @Body map: Map<String, @JvmSuppressWildcards Any>
    ): ApiResponse<Any>

    /**
     * 目录重命名
     */
    @PUT("/api/file/disk/item/{id}/rename")
    suspend fun diskFileReName(@Path("id") id: String, @Body map: Map<String, @JvmSuppressWildcards Any>): ApiResponse<Any>

    /**
     * 获取当前文件夹的面包屑导航路径
     */
    @GET("/api/file/disk/dir/{dirId}/path/breadcrumbs")
    suspend fun diskBreadcrumbs(@Path("dirId") dirId: String): ApiResponse<List<DiskBean>>

    /**----------------------------云盘end----------------------------*/
}