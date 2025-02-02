package com.duobang.common.data.constant

import com.blankj.utilcode.util.SDCardUtils

interface IConstant {
    interface APP {
        companion object {
            /**
             * 文件共享
             */
            const val FILE_PROVIDER = "com.duobang.dbdemo.fileprovider"

            val SAVE_REAL_PATH = SDCardUtils.getSDCardPathByEnvironment() + "/pms"
        }
    }

    interface PHOTO {
        companion object {
            /* 展示 */
            const val SHOW = 0

            /* 编辑 */
            const val EDIT = 1
        }
    }

    interface FILE {
        companion object {
            /* 展示 */
            const val SHOW = 0

            /* 编辑 */
            const val EDIT = 1

            /* 文档 */
            const val WORD = 1

            /* pdf */
            const val PDF = 2

            /* 表格 */
            const val EXCEL = 3

            /* 文本 */
            const val TXT = 4

            /* 其他 */
            const val OTHER = 5
        }
    }

    companion object {
        /**
         * 评论
         */
        const val COMMENT = "commentJson"
    }
}