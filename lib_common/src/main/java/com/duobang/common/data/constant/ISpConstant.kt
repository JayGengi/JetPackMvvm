package com.duobang.common.data.constant

interface ISpConstant {
    /**
     * 用户配置 UserPreferences
     */
    interface USER {
        companion object {
            const val USER_ID = "userId"
            const val USER_NAME = "username"
            const val USER_NICK_NAME = "nickname"
            const val USER_PHONE = "userPhone"
            const val USER_AVATAR = "userAvatar"

            /**
             * 0未激活
             * 1可用
             * 2禁用
             * 3已删除
             *
             *
             * [IUserConstant.USER_STATE]
             */
            const val USER_STATE = "state"
            const val USER_ORG_ID = "userOrgId"
            const val USER_ORG_NAME = "userOrgName"

            /**
             * 0: 公司 -> 项目
             * 1: 项目 -> 工程
             */
            const val USER_ORG_TYPE = "userOrgType"

            /**
             * 0可用
             * 1已归档
             * 2禁用
             * 3已删除
             *
             *
             * [IPmsConstant.ORG_STATE]
             */
            const val USER_ORG_STATE = "userOrgState"
        }
    }

    /**
     * 成员缓存
     */
    interface MEMBER {
        companion object {
            const val LIST = "members"
        }
    }

    interface APP {
        companion object {
            /**
             * 任务 - 执行人 保存ID
             */
            const val TASK_OPERATOR = "taskOerator"
        }
    }

    /**
     * 汇报对象
     */
    interface REPORT {
        companion object {
            const val LIST = "reportUsers"
        }
    }
}