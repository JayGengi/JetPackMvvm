package com.duobang.common.data.enums

interface IUserConstant {
    /**
     * 验证码页面验证类型
     */
    interface INVITATION_TYPE {
        companion object {
            /* 激活账号 */
            const val ACCOUNT_ACTIVATE = 1

            /* 手机号验证 */
            const val PHONE_VERIFY = 2
        }
    }

    /**
     * 用户状态
     */
    interface USER_STATE {
        companion object {
            /* 未激活 */
            const val NOT_ACTIVATION = 0

            /* 可用 */
            const val AVAILABLE = 1

            /* 禁用 */
            const val DISABLE = 2

            /* 已删除 */
            const val DELETED = 3
        }
    }

    companion object {
        const val KEY = "user"

        /**
         * 隐私声明网页地址
         */
        const val CONTRACT_URL = "url"

        /**
         * 用户名
         */
        const val USER_NAME = "username"

        /**
         * 部门ID
         */
        const val GROUP_ID = "groupId"
        const val GROUP_NAME = "groupName"

        /**
         * 组织ID
         */
        const val ORG_ID = "orgId"

        /**
         * 组织名称
         */
        const val ORG_NAME = "orgName"

        /**
         * 按组选择用户类型
         * [IWorkbenchConstant.APPROVAL_CONCRETE]
         */
        const val CHOOSE_GROUP_USER_TYPE = "chooseGroupUserType"
    }
}