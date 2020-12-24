package com.duobang.workbench.ui.fragment

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseFragment
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.ext.*
import com.duobang.common.util.DateUtil
import com.duobang.base.ext.parseState
import com.duobang.workbench.R
import com.duobang.workbench.databinding.FragmentDailyManageBinding
import com.duobang.workbench.ui.adapter.DailyManageAdapter
import com.duobang.workbench.viewmodel.request.RequestDailyManageViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_workbench_list.*
import kotlinx.android.synthetic.main.include_workbench_recyclerview.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/17 9:31
 * @描述　: 日事日毕组织人员提交状态
 */
class DailyManageFragment : BaseFragment<BaseViewModel, FragmentDailyManageBinding>() {

    private var date: String = DateUtil.getCurrentDate()

    //请求数据ViewModel
    private val requestDailyManageViewModel: RequestDailyManageViewModel by viewModels()

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter: DailyManageAdapter by lazy { DailyManageAdapter(arrayListOf()) }

    override fun layoutId() = R.layout.fragment_daily_manage

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            lazyLoadData()
        }

        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), mAdapter).initFloatBtn(floatbtn)
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestDailyManageViewModel.loadSubmission(
                appViewModel.orginfo.value!!.homeOrgId!!,
                date
            )
        }
    }

    override fun lazyLoadData() {
        loadsir.showLoading()
        requestDailyManageViewModel.loadSubmission(appViewModel.orginfo.value!!.homeOrgId!!, date)
    }

    override fun createObserver() {
        requestDailyManageViewModel.loadSubmissionResult.observe(
            this,
            Observer { resultState ->
                parseState(resultState, {
                    loadsir.showSuccess()
                    swipeRefresh.isRefreshing = false
                    mAdapter.setList(it)
                }, {
                    swipeRefresh.isRefreshing = false
                    ToastUtils.showShort(it.errorMsg)
                    loadsir.showError(it.errorMsg)
                })
            })
    }
    fun dateChange(date: String?) {
        this.date = date!!
        if (isVisible) {
            requestDailyManageViewModel.loadSubmission(
                appViewModel.orginfo.value!!.homeOrgId!!,
                date
            )
        }
    }
}