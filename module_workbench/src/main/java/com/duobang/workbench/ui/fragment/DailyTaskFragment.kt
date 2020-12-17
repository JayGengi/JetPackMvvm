package com.duobang.workbench.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseFragment
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.DailyTask
import com.duobang.common.ext.*
import com.duobang.common.util.DateUtil
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.workbench.R
import com.duobang.workbench.databinding.FragmentDailyManageBinding
import com.duobang.workbench.ui.adapter.DailyTaskAdapter
import com.duobang.workbench.viewmodel.request.RequestDailyTaskViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.fragment_workbench.*
import kotlinx.android.synthetic.main.include_workbench_list.*
import kotlinx.android.synthetic.main.include_workbench_recyclerview.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/26 9:41
 * @描述　: 日事日毕
 */
class DailyTaskFragment : BaseFragment<BaseViewModel, FragmentDailyManageBinding>() {

    //请求数据ViewModel
    private val requestDailyTaskViewModel: RequestDailyTaskViewModel by viewModels()

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private val mAdapter: DailyTaskAdapter by lazy { DailyTaskAdapter(arrayListOf()) }
    private var emptyList = false
    private var date: String = DateUtil.getCurrentDate()
    override fun layoutId() = R.layout.fragment_daily_manage

    override fun initView(savedInstanceState: Bundle?) {

        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            if(emptyList){
                showToast("添加go go go")
            }else {
                lazyLoadData()
            }
        }

        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), mAdapter).initFloatBtn(floatbtn)
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestDailyTaskViewModel.loadDailyTaskList(appViewModel.orginfo.value!!.homeOrgId!!,date)
        }


        mAdapter.addChildClickViewIds(R.id.topping_daily_task_item,R.id.show_more_daily_task_item,R.id.comment_daily_task_item)
        mAdapter.setNbOnItemChildClickListener { adapter, view, position ->
            val info = adapter.getItem(position) as DailyTask
            when(view.id){
                R.id.topping_daily_task_item ->{
                    //置顶
                }
                R.id.show_more_daily_task_item ->{
                    //查看更多
                }
                R.id.comment_daily_task_item ->{
                    //评论
                }
            }
        }
        mAdapter.setOnInnerItemDelayClickListener(object : DailyTaskAdapter.OnInnerItemDelayClickListener {
             override fun OnItemDelayClick(v: View?, task: DailyTask?) {
                 //推迟弹窗
            }
        })


    }

    override fun lazyLoadData() {
        loadsir.showLoading()
        requestDailyTaskViewModel.loadDailyTaskList(appViewModel.orginfo.value!!.homeOrgId!!,date)
    }

    override fun createObserver() {
        requestDailyTaskViewModel.loadDailyTaskResult.observe(
            this,
            Observer { resultState ->
                parseState(resultState, {
                    if(it.isNotNull()) {
                        emptyList = false
                        loadsir.showSuccess()
                        swipeRefresh.isRefreshing = false
                        mAdapter.setList(it)
                    }else{
                        emptyList = true
                        loadsir.showEmpty("快来创建一条吧～")
                    }
                }, {
                    emptyList = false
                    swipeRefresh.isRefreshing = false
                    ToastUtils.showShort(it.errorMsg)
                    loadsir.showError(it.errorMsg)
                })
            })
    }
    fun dateChange(date: String?) {
        this.date = date!!
        if (isVisible) {
            requestDailyTaskViewModel.loadDailyTaskList(
                appViewModel.orginfo.value!!.homeOrgId!!,
                date
            )
        }
    }

}