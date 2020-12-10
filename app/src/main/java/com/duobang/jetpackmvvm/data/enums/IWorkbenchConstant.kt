package com.duobang.jetpackmvvm.data.enums

interface IWorkbenchConstant {
    /**
     * app类型
     */
    interface APP_TYPE {
        companion object {
            /* 系统级应用 */
            const val SYSTEM = 0

            /* 专业应用 */
            const val MAJOR = 1

            /* 通用应用 */
            const val CURRENCY = 2
        }
    }

    /**
     * 日事日毕
     */
    interface SCHEDULE {
        companion object {
            /* 事项ID */
            const val SCHEDULE_ID = "scheduleId"

            /* 操作类型/创建：修改 */
            const val OPERATE_TYPE = "operateType"

            /* 创建操作 */
            const val CREATE = 1

            /* 修改操作 */
            const val UPDATE = 2
        }
    }

    /**
     * 通用记录
     */
    interface NOTE {
        companion object {
            /* 重新加载 */
            const val RELOAD_TYPE = 0

            /* 继续加载 */
            const val ADDLOAD_TYPE = 1

            /* 列表改变 */
            const val CHANGE_NOTE = "changeNote"

            /* 创建人ID */
            const val CREATOR_ID = "creatorId"
        }
    }

    /**
     * 例会
     */
    interface MEETING {
        companion object {
            const val KEY_ID = "meetingId"
            const val AGENDA_ID = "agendaId"
        }
    }

    /**
     * 用户列表，单选/多选
     */
    interface USER {
        companion object {
            const val CHOOSE_USER = "chooseUser"
            const val IS_SINGLE = "isSingle"
            const val CHOOSE_LIST = "chooseList"
            const val REQUSET_CODE = "requestCode"
            const val CHOOSE_GROUP_USER_TYPE = "chooseGroupUserType"
            const val GROUP_ID = "groupId"
            const val GROUP_NAME = "groupName"
        }
    }

    interface APPROVAL {
        companion object {
            const val TYPE = "approvalType"

            /* 费用报销 */
            const val EXPENSE = "Expense"

            /* 物品领用 */
            const val ITEM = "Item"

            /* 请假 */
            const val LEAVE = "Leave"

            /* 出差 */
            const val TRAVEL = "Travel"

            /* 混凝土 */
            const val CONCRETE = "Concrete"
            const val APPROVER = 0
            const val CC = 1
            const val GROUP = 2
            const val APPROVALID = "approvalId"
        }
    }

    /**
     * 混凝土申请
     */
    interface APPROVAL_CONCRETE {
        companion object {
            /* 表单审批人 */
            const val FORM_APPROVERS = 1

            /* 生产发起人 */
            const val PRODUCTION_APPLICANT = 2

            /* 生产审批人 */
            const val PRODUCTION_APPROVERS = 3

            /* 发料执行人 */
            const val OPERATOR = 4
        }
    }

    /**
     * 审批状态
     */
    interface APPROVAL_STATE {
        companion object {
            /* 审批中 */
            const val GOING = 0

            /* 撤回 */
            const val REVOKE = 1

            /* 通过 */
            const val PASS = 2

            /* 审批未通过 */
            const val REFUSE = 3
        }
    }

    interface APPROVAL_SEND_CONCRETE_STATE {
        companion object {
            /* 运输中 */
            const val IN_TRANSIT = 0

            /* 已签收 */
            const val RECEIVE = 1

            /* 已退回 */
            const val BACK = 2
        }
    }

    /**
     * 审批节点状态
     */
    interface APPROVAL_NODE_STATE {
        companion object {
            /* 等待中 */
            const val WAIT = 0

            /* 通过 */
            const val PASS = 1

            /* 拒绝 */
            const val REFUSE = 2

            /* 回退 */
            const val RETURN = 3
        }
    }

    /**
     * 混凝土三个阶段状态 processState
     */
    interface APPROVAL_CONCRETE_STATE {
        companion object {
            /* 等待中(待处理) */
            const val PENDING = 0

            /* 审批中 */
            const val UNDER_APPROVAL = 1

            /* 同意 */
            const val AGREED = 2

            /* 拒绝 */
            const val REFUSED = 3

            /* 进行中(发料) */
            const val ONGOING = 4

            /* 完成(发料) */
            const val FINISHED = 5

            /* 回退 */
            const val RETURNED = 6
        }
    }

    /**
     * 节点类型
     */
    interface APPROVAL_NODE_TYPE {
        companion object {
            /* 发起人 */
            const val CRETOR = 0

            /* 审批人 */
            const val APPROVER = 1

            /* 抄送人 */
            const val CC = 2
        }
    }

    /**
     * 节点类型
     * @link Approval.processeOrder
     */
    interface APPROVAL_CONCRETE_NODE_ORDER {
        companion object {
            /* 申请表单 */
            const val FORM = 0

            /* 生产计划 */
            const val PRODUCTION_PLAN = 1

            /* 发料 */
            const val SEND_CONCRETE = 2
        }
    }

    /**
     * 审批操作
     */
    interface APPROVAL_OPERATE {
        companion object {
            /* 第几个节点 */
            const val POSITION = "position"

            /* 节点对象json */
            const val NODE = "node"

            /* 状态 */
            const val STATE = "state"
            const val OPERATE_TYPE = "type"

            /* 审批 */
            const val APPROVAL = 1

            /* 修改 */
            const val UPDATE = 2
            const val NODE_ID = "nodeId"
            const val OPINION = "opinion"
        }
    }

    interface APPROVAL_SEND_CONCRETE {
        companion object {
            const val TYPE = "type"
            const val CREATE = 1
            const val UPDATE = 2
            const val FORM = "form"
        }
    }

    interface NOTICE {
        companion object {
            const val KEY = "notice"

            /* 列表改变 */
            const val CHANGE_NOTICE = "changeNotice"
        }
    }

    interface TASK {
        companion object {
            const val ALLOW_STATE = "allowState"

            /* 公开 */
            const val PUBLIC = 0

            /* 私密 */
            const val PRIVARE = 1

            /* 部分可见 */
            const val PART = 2
        }
    }

    companion object {
        /**
         * 重新加载
         */
        const val RELOAD_TYPE = 0

        /**
         * 继续加载
         */
        const val ADDLOAD_TYPE = 1
    }
}