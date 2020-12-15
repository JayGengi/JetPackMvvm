package com.duobang.common.data.constant

interface RouterConstant {
    //activity
    interface ACT {
        companion object {
            /**
             * 首页
             */
            private const val MODULE_HOME = "/module_home"
            const val MAIN = "$MODULE_HOME/Main"

            /**
             * 工程
             */
            private const val MODULE_PROJECT = "/module_project"

            /**
             * 工作台
             */
            private const val MODULE_WORKBENCH = "/module_workbench"

            /**
             * 登录
             */
            private const val MODULE_LOGIN = "/module_login"
            const val LOGIN = "$MODULE_LOGIN/Login"

        }
    }

    // Fragment
    interface FRAG {
        companion object {
            /**
             * 首页
             */
            private const val MODULE_HOME = "/module_home"
            const val HOME = "$MODULE_HOME/Home"

            /**
             * 工程
             */
            private const val MODULE_PROJECT = "/module_project"

            /**
             * 工作台
             */
            private const val MODULE_WORKBENCH = "/module_workbench"

        }
    }


}