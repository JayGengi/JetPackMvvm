package com.duobang.common.data.constant

interface RouterConstant {
    //activity
    interface ACT {
        companion object {
            /**
             * 首页
             */
            private const val MODULE_HOME = "/act_home"
            const val MAIN = "$MODULE_HOME/Main"

            /**
             * 工程
             */
            private const val MODULE_PROJECT = "/act_project"

            /**
             * 工作台
             */
            private const val MODULE_WORKBENCH = "/act_workbench"

            /**
             * 登录
             */
            private const val MODULE_LOGIN = "/act_login"
            const val LOGIN = "$MODULE_LOGIN/Login"

        }
    }

    // Fragment
    interface FRAG {
        companion object {
            /**
             * 首页
             */
            private const val MODULE_HOME = "/frag_home"
            const val HOME = "$MODULE_HOME/Home"

            /**
             * 工程
             */
            private const val MODULE_PROJECT = "/frag_project"
            const val PROJECT = "$MODULE_PROJECT/Project"

            /**
             * 工作台
             */
            private const val MODULE_WORKBENCH = "/frag_workbench"
            const val WORKBENCH = "$MODULE_WORKBENCH/WorkBench"

            /**
             * 组织
             */
            private const val MODULE_ORG = "/frag_org"
            const val ORG = "$MODULE_ORG/Home"

        }
    }


}