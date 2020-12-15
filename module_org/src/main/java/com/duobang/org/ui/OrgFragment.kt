package com.duobang.org.ui

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseFragment
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.OrganizationInfo
import com.duobang.common.data.constant.RouterConstant
import com.duobang.common.ext.init
import com.duobang.common.ext.initFloatBtn
import com.duobang.common.ext.setNbOnItemClickListener
import com.duobang.common.ext.showToast
import com.duobang.common.util.CacheUtil
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.org.R
import com.duobang.org.databinding.FragmentOrgBinding
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
    override fun layoutId() = R.layout.fragment_org

    override fun initView(savedInstanceState: Bundle?) {
        orgBar.setPadding(0, BarUtils.getStatusBarHeight(), 0, 0)
        mDatabind.click = ProxyClick()
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), mOrgAdapter).initFloatBtn(floatbtn)

        requestOrgViewModel.loadPersonOrg()

        mOrgAdapter.setNbOnItemClickListener { adapter, view, position ->
            val item: OrganizationInfo = adapter.getItem(position) as OrganizationInfo
            if (item.isEdit) {
                // 刷新并上传主组织，刷新主页
                requestOrgViewModel.switchHomeOrg(item.id!!)
            } else {
//                openOrgStructureView(item)
            }
        }
    }

    override fun lazyLoadData() {
        requestOrgViewModel.run {
            resultPersonOrgData.observe(
                viewLifecycleOwner,
                Observer { resultState ->
                    parseState(resultState, {
                        mOrgAdapter.setList(it.orgList)
                    }, {
                        ToastUtils.showShort(it.errorMsg)
                    })
                })

            resultHomeOrgData.observe(
                viewLifecycleOwner,
                Observer { resultState ->
                    parseState(resultState, {
                        //TODO 全局刷新 ，方案待定
                        showToast("方案待定")
                    }, {
                        ToastUtils.showShort(it.errorMsg)
                    })
                })
        }
    }

    override fun createObserver() {

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