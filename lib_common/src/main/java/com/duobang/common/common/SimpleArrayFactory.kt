package com.duobang.common.common

import com.duobang.common.data.constant.IPmsConstant
import java.util.*

object SimpleArrayFactory {
    /**
     * 创建模型状态列表
     *
     * @return
     */
    fun createModelStateList(): List<String> {
        val list: MutableList<String> =
            ArrayList()
        list.add("建模中")
        list.add("进行中")
        list.add("已完成")
        return list
    }

    /**
     * 创建会议类型列表
     *
     * @return
     */
    fun createMeetingTypeList(): List<String> {
        val list: MutableList<String> =
            ArrayList()
        list.add("通用会议")
        list.add("周例会")
        list.add("月例会")
        return list
    }

    /**
     * 创建审批请假类型列表
     *
     * @return
     */
    fun createLeaveApprovals(): List<String> {
        val list: MutableList<String> =
            ArrayList()
        list.add("年假")
        list.add("事假")
        list.add("病假")
        list.add("调休")
        list.add("婚假")
        list.add("产假")
        list.add("陪产假")
        list.add("丧假")
        list.add("哺乳假")
        return list
    }

    fun createLeaveApprovalKeys(): List<String> {
        val list: MutableList<String> =
            ArrayList()
        list.add("Annual")
        list.add("Personal")
        list.add("Sick")
        list.add("Compensatory")
        list.add("Marriage")
        list.add("Maternity")
        list.add("Paternity")
        list.add("Bereavement")
        list.add("Breastfeeding")
        return list
    }

    /**
     * 创建审批报销类型列表
     *
     * @return
     */
    fun createExpenseApprovals(): List<String> {
        val list: MutableList<String> =
            ArrayList()
        list.add("差旅费")
        list.add("住宿费")
        list.add("交通费")
        list.add("招待费")
        list.add("团建费")
        list.add("通讯费")
        list.add("其他")
        return list
    }

    fun createExpenseApprovalKeys(): List<String> {
        val list: MutableList<String> =
            ArrayList()
        list.add("Travel")
        list.add("Lodging")
        list.add("Traffic")
        list.add("Entertain")
        list.add("League")
        list.add("Communication")
        list.add("Other")
        return list
    }

    fun createReportLabels(): List<String> {
        val list: MutableList<String> =
            ArrayList()
        list.add("日报")
        list.add("周报")
        list.add("月报")
        list.add("年报")
        return list
    }

    //根目录（云盘管理员【新建文件夹】）
    //子目录（云盘管理员、管理员【新建文件夹】【添加文件】、成员【添加文件】）
    fun createDiskLabels(
        pid: String,
        userPermissions: Int
    ): List<String> {
        val list: MutableList<String> =
            ArrayList()
        when (userPermissions) {
            IPmsConstant.DISK.ROOT_FOLDER_DIR -> {
                list.add("新建文件夹")
                if ("" != pid) {
                    list.add("添加文件")
                }
            }
            IPmsConstant.DISK.ADMIN_FOLDER_DIR -> {
                list.add("新建文件夹")
                list.add("添加文件")
            }
            IPmsConstant.DISK.MEMBER_FOLDER_DIR -> list.add("添加文件")
            else -> {
            }
        }
        return list
    }

    fun createDiskPer(): List<String> {
        val list: MutableList<String> =
            ArrayList()
        list.add("公开")
        list.add("私有")
        return list
    }
}