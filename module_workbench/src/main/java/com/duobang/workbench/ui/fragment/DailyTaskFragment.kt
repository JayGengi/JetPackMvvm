package com.duobang.workbench.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseFragment
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.DailyComment
import com.duobang.common.data.bean.DailyTask
import com.duobang.common.data.bean.DailyTaskWrapper
import com.duobang.common.ext.*
import com.duobang.common.util.DateUtil
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.workbench.R
import com.duobang.workbench.databinding.FragmentDailyManageBinding
import com.duobang.workbench.ui.adapter.DailyTaskAdapter
import com.duobang.workbench.viewmodel.request.RequestDailyTaskViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_workbench_list.*
import kotlinx.android.synthetic.main.include_workbench_recyclerview.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/26 9:41
 * @描述　: 日事日毕
 */
class DailyTaskFragment : BaseFragment<BaseViewModel, FragmentDailyManageBinding>(),
    DailyTaskCommentDialogFragment.OnCommentChangedListener {

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
            val info = adapter.getItem(position) as DailyTaskWrapper
            when(view.id){
                R.id.topping_daily_task_item ->{
                    //置顶
                }
                R.id.show_more_daily_task_item ->{
                    //查看更多
                    DailyTaskMoreDialogFragment.newInstance(info).show(activity?.supportFragmentManager!!, "dialog")
                }
                R.id.comment_daily_task_item ->{
                    //评论
                    DailyTaskCommentDialogFragment.newInstance(info,position).show(activity?.supportFragmentManager!!, "dialog")
                }
            }
        }
        mAdapter.setOnInnerItemDelayClickListener(object : DailyTaskAdapter.OnInnerItemDelayClickListener {
             override fun OnItemDelayClick(v: View?, task: DailyTask) {
                 //推迟弹窗
                 showMessage(
                     "您确定要推迟该条事项到今天么？推迟后该条将复制到您今天的日事日毕中",
                     "推迟日事日毕",
                     "确定",
                     {requestDailyTaskViewModel.loadDelayTask(task.id!!)},
                     "取消"
                 )
            }
        })

    }

    override fun lazyLoadData() {
        loadsir.showLoading()
        requestDailyTaskViewModel.loadDailyTaskList(appViewModel.orginfo.value!!.homeOrgId!!,date)
    }

    override fun createObserver() {
        requestDailyTaskViewModel.run {
            loadDailyTaskResult.observe(
                this@DailyTaskFragment,
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

            loadDelayTaskResult.observe(
                this@DailyTaskFragment,
                Observer { resultState ->
                    parseState(resultState, {
                        showToast("推迟成功！请在今天列表查看！！")
                    }, {
                        showToast(it.errorMsg)
                    })
                })
        }
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

    override fun onCommentChanged(position: Int, dailyCommentList: List<DailyComment>) {
        mAdapter.data[position].comments = dailyCommentList
        mAdapter.notifyItemChanged(position)
    }

}