package com.duobang.jetpackmvvm.data.repository.local

import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.duobang.common.data.bean.DailyTaskWrapper
import com.duobang.common.room.repository.PmsRepository
import com.duobang.common.socket.i.IConstants
import com.duobang.common.socket.model.SocketMessage
import com.duobang.common.util.JsonUtil

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/2 11:14
 * @描述　: 推送消息處理中心
 */
class MainSocketDisposeManager {
    /**
     * @作者　: JayGengi
     * @时间　: 2020/12/2 15:23
     * @描述　: 日事日毕数据處理层
     */
    @Throws(Exception::class)
    fun dailyTaskMsg(context: Context?, socketMessage: SocketMessage) {
        val json = JsonUtil.toJson(socketMessage.content)
        val wrappers =
            JsonUtil.toObj(json, DailyTaskWrapper::class.java)
        LogUtils.d("socketMsg", JsonUtil.toJson(socketMessage))
        if (null != wrappers) {
            when (socketMessage.action) {
                IConstants.ACTION.CREATE,
                IConstants.ACTION.UPDATE ->
                    PmsRepository(context!!).insertOneDailyTaskWrapper(wrappers)
                IConstants.ACTION.DELETE ->
                    PmsRepository(context!!).delDailyTaskWrapperById(wrappers.id)
                else -> {
                }
            }
        }
    }
}