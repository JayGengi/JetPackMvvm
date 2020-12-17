package com.duobang.org.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseActivity
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.OrgGroup
import com.duobang.common.data.constant.IUserConstant
import com.duobang.common.ext.*
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.org.R
import com.duobang.org.databinding.ActivityOrgStructureBinding
import com.duobang.org.ui.adapter.OrgGroupAdapter
import com.duobang.org.ui.adapter.UserAdapter
import com.duobang.org.viewmodel.request.RequestOrgGroupViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.activity_org_structure.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/30 14:25
 * @描述　: 组织人员列表
 */
class OrgStructureActivity : BaseActivity<BaseViewModel, ActivityOrgStructureBinding>() {
    private var orgId: String? = null
    private var orgName: String? = ""

    private val mUserAdapter: UserAdapter by lazy { UserAdapter(arrayListOf()) }

    private val mOrgGroupAdapter: OrgGroupAdapter by lazy { OrgGroupAdapter(arrayListOf()) }
    //请求数据ViewModel
    private val requestOrgGroupViewModel: RequestOrgGroupViewModel by viewModels()
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    override fun layoutId() = R.layout.activity_org_structure

    override fun initView(savedInstanceState: Bundle?) {
        orgId = intent.getStringExtra(IUserConstant.ORG_ID)
        orgName = intent.getStringExtra(IUserConstant.ORG_NAME)
        toolbar.initClose(orgName!!) {
            finish()
        }
        //状态页配置
        loadsir = loadServiceInit(recyclerView) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestOrgGroupViewModel.getOrgGroupUserWrapper(orgId!!)
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(this), mUserAdapter).initFloatBtn(floatbtn)

        loadsir.showLoading()
        requestOrgGroupViewModel.getOrgGroupUserWrapper(orgId!!)

    }

    override fun createObserver() {
        requestOrgGroupViewModel.orgGroupUserResult.observe(
            this,
            Observer { resultState ->
                parseState(resultState, {
                    loadsir.showSuccess()
                    //设置部门头
                    mUserAdapter.addHeaderView(orgStructureHeader(it.groupList!!))
                    mUserAdapter.setList(it.userList)
                }, {
                    ToastUtils.showShort(it.errorMsg)
                    loadsir.showError(it.errorMsg)
                })
            })
    }
    /**
     * @param 组织部门
     * */
    private fun orgStructureHeader(orgGroup: List<OrgGroup>): View {
        val view: View = layoutInflater.inflate(R.layout.header_org_group, recyclerView, false)
        val orgGroupRecycler : RecyclerView = view.findViewById(R.id.org_group_recycler)
        //初始化recyclerView
        orgGroupRecycler.init(LinearLayoutManager(this), mOrgGroupAdapter)
        mOrgGroupAdapter.setList(orgGroup)
        mOrgGroupAdapter.setNbOnItemClickListener { adapter, view, position ->
            val item: OrgGroup = adapter.getItem(position) as OrgGroup
            openOrgGroupUserView(item)
        }
        return view
    }
    /**
     * 跳转打开部门人员列表
     * @param item
     */
    private fun openOrgGroupUserView(item: OrgGroup) {
        val intent = Intent(this, OrgGroupUserActivity::class.java)
        intent.putExtra(IUserConstant.GROUP_ID, item.id)
        intent.putExtra(IUserConstant.GROUP_NAME, item.name)
        startActivity(intent)
    }
}
