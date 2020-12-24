package com.duobang.workbench.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.duobang.common.base.BaseActivity
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.common.SimpleArrayFactory
import com.duobang.common.data.bean.DiskBean
import com.duobang.common.data.bean.User
import com.duobang.common.data.constant.IPmsConstant
import com.duobang.common.data.constant.IWorkbenchConstant
import com.duobang.common.data.constant.REQUEST_CODE
import com.duobang.common.ext.setNbOnItemClickListener
import com.duobang.common.ext.showToast
import com.duobang.common.room.repository.PmsRepository
import com.duobang.common.util.AppImageLoader
import com.duobang.common.util.AppRoute
import com.duobang.common.util.JsonUtil
import com.duobang.common.weight.recyclerview.DuobangLinearLayoutManager
import com.duobang.base.ext.parseState
import com.duobang.workbench.R
import com.duobang.workbench.databinding.ActivityDiskConfigBinding
import com.duobang.workbench.ui.adapter.AllowUserAdapter
import com.duobang.workbench.ui.fragment.WorkLabelDialogFragment
import com.duobang.workbench.viewmodel.request.RequestDiskConfigViewModel
import kotlinx.android.synthetic.main.activity_disk_config.*
import java.util.*

/**
 * @Des: 云盘配置
 * @Author: JayGengi
 * @Date: 2020/10/20 16:05
 */
class DiskConfigActivity : BaseActivity<BaseViewModel, ActivityDiskConfigBinding>(),
    WorkLabelDialogFragment.OnLabelItemClickListener {
    private var allowUsers: ArrayList<User> = ArrayList()
    private val allowUserAdapter: AllowUserAdapter by lazy { AllowUserAdapter(allowUsers) }

    //私密性, 0 不公开 1 公开
    private var allowState = 1
    private var diskInfo: DiskBean? = null
    private val dirIds: ArrayList<String> = ArrayList()
    private var createOperator: User? = null
    private var operator: User? = null
    //用户权限  -- 云盘管理员 管理员 成员
    private var userPermissions = 0

    //请求数据ViewModel
    private val requestDiskConfigViewModel: RequestDiskConfigViewModel by viewModels()

    override fun layoutId() = R.layout.activity_disk_config

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.click = ProxyClick()
        val bundle = intent.extras
        if (null != bundle) {
            val createTimeStr = bundle.getString("create_time")
            userPermissions = bundle.getInt("user_permissions")
            diskInfo = bundle.getParcelable("disk_info")
            create_time.text = createTimeStr
        }
        //成员静态查看
        if (IPmsConstant.DISK.MEMBER_FOLDER_DIR == userPermissions) {
            back_create_task.text = "返回"
            commit_create_task.visibility = View.INVISIBLE
        }

        // 是否单选和有管理员和成员进行修改
//        if (1 == diskList.size()) {
        dirIds.add(diskInfo!!.id)
        allowState = diskInfo!!.privacy
        setStateStr()
        //创建者
        createOperator = PmsRepository(this).getUserById(diskInfo!!.userId)
        //管理员
        if(null != diskInfo!!.manager) {
            operator = PmsRepository(this).getUserById(diskInfo!!.manager!!)
        }
        //成员
        if (null != diskInfo!!.members && diskInfo!!.members.size > 0) {
            for (j in 0 until diskInfo!!.members.size) {
                val user = PmsRepository(this).getUserById(diskInfo!!.members[j])
                allowUsers.add(user)
            }
        }
        if (createOperator == null) {
            disk_create_per.visibility = View.GONE
        } else {
            disk_create_per.visibility = View.VISIBLE
            AppImageLoader.displayAvatar(
                createOperator!!.avatar,
                createOperator!!.nickname,
                disk_create_per
            )
        }

        allow_create_task.layoutManager =
            DuobangLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        allow_create_task.adapter = allowUserAdapter;
        allowUserAdapter.setNbOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as User
            var json  = ""
            if(allowUsers.size>0){
                json = JsonUtil.toJson(allowUsers)
            }
            openAllowView(json)
        }

        setupOperatorView(operator)
        setupAllowView()


    }

    private fun setupOperatorView(operator: User?) {
        this.operator = operator
        if (operator == null) {
            operator_create_task.visibility = View.GONE
            return
        }
        operator_create_task.visibility = View.VISIBLE
        AppImageLoader.displayAvatar(operator.avatar, operator.nickname, operator_create_task)
    }
    private fun setupAllowView() {
    allowUserAdapter.setList(allowUsers);
    }

    /**
     * 打开选择执行人
     */
    private fun openOperatorView(json: String) {
        AppRoute.openChooseUserView(this, REQUEST_CODE.CHOOSE_USER, true, json)
    }

    private fun openAllowView(json: String) {
        AppRoute.openChooseUserView(this, REQUEST_CODE.CHOOSE_APPROVER, false, json)
    }
    override fun createObserver() {
        requestDiskConfigViewModel.loadDiskManagerResult.observe(this, Observer { resultState ->
            parseState(resultState, {
                showToast("配置成功")
                finish()
            }, {
                showToast(it.errorMsg)
            })

        })
    }
    private fun canCommit(): Boolean {
        if (operator == null) {
            showToast("执行人不可为空！！")
        }
        return operator != null
    }
    inner class ProxyClick {
        fun cancel() {
            finish()
        }

        fun commit() {
            if (canCommit()) {
                val map: MutableMap<String, Any> = HashMap()
                map["dirIds"] = dirIds
                map["privacy"] = if (1 == allowState) 1 else 0
                map["manager"] = operator!!.id
                //成员
                if (allowUsers.size > 0) {
                    val userIds: MutableList<String> = ArrayList()
                    for (i in allowUsers.indices) {
                        userIds.add(allowUsers[i].id)
                    }
                    map["members"] = userIds
                }
                requestDiskConfigViewModel.diskManager(map)
            }
        }

        fun operatorView() {
            if (IPmsConstant.DISK.ROOT_FOLDER_DIR == userPermissions) {
                var json = ""
                if(null != operator){
                    json = JsonUtil.toJson(operator)
                }
                openOperatorView(json)
            }
        }

        fun allowView() {
            if (IPmsConstant.DISK.MEMBER_FOLDER_DIR != userPermissions) {
                WorkLabelDialogFragment.newInstance(SimpleArrayFactory.createDiskPer())
                    .show(supportFragmentManager, "dialog")
            }
        }

        fun member() {
            if (IPmsConstant.DISK.MEMBER_FOLDER_DIR != userPermissions) {
                var json  = ""
                if(allowUsers.size>0){
                    json = JsonUtil.toJson(allowUsers)
                }
                openAllowView(json)
            }
        }
    }


    override fun onLabelItemClick(label: String) {
        if ("公开" == label) {
            allowState = 1
        } else if ("私有" == label) {
            allowState = 0
        }
        setStateStr()
    }

    private fun setStateStr() {
        if (1 == allowState) {
            allow_tip_create_task.text = "公开"
        } else {
            allow_tip_create_task.text = "私有"
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE.CHOOSE_USER -> {
                if (resultCode != REQUEST_CODE.CHOOSE_USER || data == null) {
                    return
                }
                setupOperatorView(null)
                val isSingle =
                    data.getBooleanExtra(IWorkbenchConstant.USER.IS_SINGLE, false)
                if (isSingle) {
                    val json =
                        data.getStringExtra(IWorkbenchConstant.USER.CHOOSE_USER)
                    val users: List<User>
                    try {
                        users = JsonUtil.toList(json, User::class.java)
                        operator = users[0]
                        setupOperatorView(operator)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            REQUEST_CODE.CHOOSE_APPROVER -> {
                if (resultCode != REQUEST_CODE.CHOOSE_APPROVER || data == null) {
                    return
                }
                val json = data.getStringExtra(IWorkbenchConstant.USER.CHOOSE_USER)
                if (allowUsers == null) {
                    allowUsers = ArrayList()
                }
                allowUsers.clear()
                try {
                    val userList: List<User> =
                        JsonUtil.toList(json, User::class.java)
                    allowUsers.addAll(userList)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                setupAllowView()
            }
            else -> {
            }
        }
    }

}
