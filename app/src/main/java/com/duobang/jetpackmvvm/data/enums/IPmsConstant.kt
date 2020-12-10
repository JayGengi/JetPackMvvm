package com.duobang.jetpackmvvm.data.enums

interface IPmsConstant {
    /**
     * 组织状态
     */
    interface ORG_STATE {
        companion object {
            /* 可用 */
            const val AVAILABLE = 0

            /* 归档 */
            const val FINISHED = 1

            /* 禁用 */
            const val DISABLE = 2

            /* 已删除 */
            const val DELETED = 3
        }
    }

    /**
     * 组织类型
     */
    interface ORG_TYPE {
        companion object {
            /* 公司 —> 项目 */
            const val COMPANY = 0

            /* 项目 -> 工程 */
            const val PROJECT = 1
        }
    }

    /**
     * 查询页类型
     */
    interface SEARCH_TYPE {
        companion object {
            const val KEY = "searchType"

            /* 工程 */
            const val STRUCTURE = 0

            /* 联系人 */
            const val CONTACTS = 1
        }
    }

    /**
     * 单位工程
     */
    interface STRUCTURE {
        companion object {
            const val ID = "structureId"
            const val NAME = "structureName"
        }
    }

    /**
     * 模型
     */
    interface MODEL {
        companion object {
            const val ID = "modelId"
            const val NAME = "modelName"

            /* 劳务队伍 */
            const val LABOR_TEAMS = "teams"
        }
    }

    /**
     * 模型状态
     */
    interface MODEL_STATE {
        companion object {
            /* 初始状态 */
            const val INIT = 0

            /* 进行中 */
            const val PROCESSING = 1

            /* 已完成 */
            const val FINISH = 2
        }
    }

    /**
     * 记录
     */
    interface RECORD {
        companion object {
            /* 记录模版ID */
            const val TEMPLATE_ID = "templateId"

            /* 记录模版名称 */
            const val TEMPLATE_NAME = "templateName"

            /* 模型名称 */
            const val MODEL_NAME = "modelName"
        }
    }

    /**
     * 记录组件配置类型
     */
    interface FIELD_TYPE {
        companion object {
            /* 长文本 */
            const val TEXT = "text"

            /* 短文本 */
            const val STRING = "string"

            /* 图片 */
            const val IMAGE = "image"

            /* 文件 */
            const val FILE = "file"

            /* 日期 */
            const val DATE = "date"

            /* 时间 */
            const val TIME = "time"

            /* 日期+时间 */
            const val DATETIME = "datatime"

            /* 数字 */
            const val NUMBER = "number"

            /* 进度（工程量） */
            const val METAQ = "metaQ"

            /* 材料 */
            const val MATERIAL = "material"

            /* 工序 */
            const val PROCEDURE = "procedure"

            /* 多劳务队伍 */
            const val LABORS = "laborTeams"

            /* 劳务队伍 */
            const val LABOR = "laborTeam"
        }
    }

    /**
     * 统计
     */
    interface STAT_DIM {
        companion object {
            const val KEY = "key"

            /* 工程量 */
            const val QUANTITY = "Quantity"

            /* 产值 */
            const val OUTPUT = "Output"

            /* 材料 */
            const val MATERIAL = "Material"

            /* 劳务 */
            const val LABOR = "Labor"

            /* 全部 */
            const val ALL = "All"
        }
    }

    /**
     * 总览
     */
    interface DASHBOARD {
        companion object {
            /* 重新加载 */
            const val RELOAD_TYPE = 0

            /* 继续加载 */
            const val ADDLOAD_TYPE = 1
        }
    }

    /**
     * 云盘
     */
    interface DISK {
        companion object {
            /* 根目录云盘管理员 */
            const val ROOT_FOLDER_DIR = 1

            /* 子目录管理员(包括云盘管理员) */
            const val ADMIN_FOLDER_DIR = 2

            /* 成员 */
            const val MEMBER_FOLDER_DIR = 3
        }
    }
}