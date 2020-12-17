package com.duobang.org.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseActivity
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.constant.IUserConstant
import com.duobang.common.ext.*
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.org.R
import com.duobang.org.databinding.ActivityOrgStructureBinding
import com.duobang.org.ui.adapter.UserAdapter
import com.duobang.org.viewmodel.request.RequestOrgGroupUserViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.activity_org_structure.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/30 14:25
 * @描述　: 组织部门人员列表
 */
class OrgGroupUserActivity : BaseActivity<BaseViewModel, ActivityOrgStructureBinding>() {
    private var groupId: String? = null
    private var groupName: String? = ""

    private val mUserAdapter: UserAdapter by lazy { UserAdapter(arrayListOf()) }

    //请求数据ViewModel
    private val requestOrgGroupUserViewModel: RequestOrgGroupUserViewModel by viewModels()

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    override fun layoutId() = R.layout.activity_org_structure

    override fun initView(savedInstanceState: Bundle?) {
        groupId = intent.getStringExtra(IUserConstant.GROUP_ID)
        groupName = intent.getStringExtra(IUserConstant.GROUP_NAME)
        toolbar.initClose(groupName!!) {
            finish()
        }
        //状态页配置
        loadsir = loadServiceInit(recyclerView) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestOrgGroupUserViewModel.getOrgGroupUserWrapper(groupId!!)
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(this), mUserAdapter).initFloatBtn(floatbtn)

        loadsir.showLoading()
        requestOrgGroupUserViewModel.getOrgGroupUserWrapper(groupId!!)

    }

    override fun createObserver() {
        requestOrgGroupUserViewModel.orgGroupUserResult.observe(
            this,
            Observer { resultState ->
                parseState(resultState, {
                    loadsir.showSuccess()
                    mUserAdapter.setList(it)
                }, {
                    ToastUtils.showShort(it.errorMsg)
                    loadsir.showError(it.errorMsg)
                })
            })
    }
}
