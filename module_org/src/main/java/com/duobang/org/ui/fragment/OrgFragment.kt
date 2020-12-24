package com.duobang.org.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseFragment
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.OrganizationInfo
import com.duobang.common.data.constant.IUserConstant
import com.duobang.common.data.constant.RouterConstant
import com.duobang.common.ext.init
import com.duobang.common.ext.initFloatBtn
import com.duobang.common.ext.setNbOnItemClickListener
import com.duobang.base.ext.parseState
import com.duobang.org.R
import com.duobang.org.databinding.FragmentOrgBinding
import com.duobang.org.ui.activity.OrgStructureActivity
import com.duobang.org.ui.adapter.OrgAdapter
import com.duobang.org.viewmodel.request.RequestOrgViewModel
import kotlinx.android.synthetic.main.fragment_org.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/13 17:21
 * @描述　: 联系人
 */
@Route(path = RouterConstant.FRAG.ORG)
class OrgFragment : BaseFragment<BaseViewModel, FragmentOrgBinding>() {

    private val mOrgAdapter: OrgAdapter by lazy { OrgAdapter(arrayListOf()) }

    //请求数据ViewModel
    private val requestOrgViewModel: RequestOrgViewModel by viewModels()

    private var isEdit = false

    private var listener: OnOrganizationSwitchListener? =null
    override fun layoutId() = R.layout.fragment_org

    override fun initView(savedInstanceState: Bundle?) {
        orgBar.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0)
        mDatabind.click = ProxyClick()
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), mOrgAdapter).initFloatBtn(floatbtn)

        mOrgAdapter.setNbOnItemClickListener { adapter, view, position ->
            val item: OrganizationInfo = adapter.getItem(position) as OrganizationInfo
            if (item.isEdit) {
                // 刷新并上传主组织，刷新主页
                requestOrgViewModel.switchHomeOrg(item.id!!)
            } else {
                openOrgStructureView(item)
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        listener = if (parent != null) {
            parent as OnOrganizationSwitchListener
        } else {
            context as OnOrganizationSwitchListener
        }
    }

    override fun lazyLoadData() {
        requestOrgViewModel.loadPersonOrg()
    }

    override fun createObserver() {
        requestOrgViewModel.run {
            resultPersonOrgData.observe(
                viewLifecycleOwner,
                Observer { resultState ->
                    parseState(resultState, {
                        //设置主组织
                        it.setHomeOrg()
                        mOrgAdapter.setList(it.orgList)
                    }, {
                        ToastUtils.showShort(it.errorMsg)
                    })
                })

            resultHomeOrgData.observe(
                viewLifecycleOwner,
                Observer { resultState ->
                    parseState(resultState, {
                        listener!!.onOrganizationSwitch()
                    }, {
                        ToastUtils.showShort(it.errorMsg)
                    })
                })
        }
    }

    /**
     * 跳转打开人员列表
     * @param item
     */
    private fun openOrgStructureView(item: OrganizationInfo) {
        val intent = Intent(activity, OrgStructureActivity::class.java)
        intent.putExtra(IUserConstant.ORG_ID, item.id)
        intent.putExtra(IUserConstant.ORG_NAME, item.name)
        startActivity(intent)
    }
    /**
     * 设置列表编辑状态
     *
     * @param isEdit
     */
    private fun handleListData(isEdit: Boolean) {
        if (mOrgAdapter.data.size == 0) {
            return
        }
        for (i in  mOrgAdapter.data.indices) {
            mOrgAdapter.data[i].isEdit = isEdit
        }
        mOrgAdapter.notifyDataSetChanged()
    }
    interface OnOrganizationSwitchListener {
        fun onOrganizationSwitch()
    }

    inner class ProxyClick {
        /** 切换组织*/
        fun switchOrg() {
            if (isEdit) {
                switchBtn.text = "切换组织"
                handleListData(false)
            } else {
                switchBtn.text = "关闭切换"
                handleListData(true)
            }
            isEdit = !isEdit
        }
    }
}