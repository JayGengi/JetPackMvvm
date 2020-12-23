package com.duobang.workbench.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseFragment
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.DailyHeader
import com.duobang.common.data.bean.DailyTask
import com.duobang.common.data.bean.DailyTaskWrapper
import com.duobang.common.ext.*
import com.duobang.common.room.repository.PmsRepository
import com.duobang.common.util.ActivityMessenger
import com.duobang.common.util.DateUtil
import com.duobang.common.util.JsonUtil
import com.duobang.common.weight.recyclerview.DuobangLinearLayoutManager
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.workbench.R
import com.duobang.workbench.databinding.FragmentDailyManageBinding
import com.duobang.workbench.ui.activity.DailyTaskCreateActivity
import com.duobang.workbench.ui.activity.DiskActivity
import com.duobang.workbench.ui.adapter.DailyHeaderAdapter
import com.duobang.workbench.ui.adapter.DailyTaskAdapter
import com.duobang.workbench.viewmodel.request.RequestDailyTaskViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.header_daily_task.*
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

    private var orgId = ""
    override fun layoutId() = R.layout.fragment_daily_manage

    override fun initView(savedInstanceState: Bundle?) {

        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            if (emptyList) {
                ActivityMessenger.startActivity(this, DailyTaskCreateActivity::class)
            } else {
                lazyLoadData()
            }
        }
        orgId = appViewModel.orginfo.value!!.homeOrgId!!
        dailyTaskHeader()
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), mAdapter).initFloatBtn(floatbtn)
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestDailyTaskViewModel.loadDailyTaskList(orgId, date)
        }
//        recyclerView.addHeaderView(dailyTaskHeader())
        mAdapter.addChildClickViewIds(
            R.id.topping_daily_task_item,
            R.id.show_more_daily_task_item,
            R.id.comment_daily_task_item
        )
        mAdapter.setNbOnItemChildClickListener { adapter, view, position ->
            val info = adapter.getItem(position) as DailyTaskWrapper
            when (view.id) {
                R.id.topping_daily_task_item -> {
                    //置顶
                }
                R.id.show_more_daily_task_item -> {
                    //查看更多
                    DailyTaskMoreDialogFragment.newInstance(info)
                        .show(activity?.supportFragmentManager!!, "dialog")
                }
                R.id.comment_daily_task_item -> {
                    //评论
                    DailyTaskCommentDialogFragment.newInstance(info, position)
                        .show(activity?.supportFragmentManager!!, "dialog")
                }
            }
        }
        mAdapter.setOnInnerItemDelayClickListener(object :
            DailyTaskAdapter.OnInnerItemDelayClickListener {
            override fun OnItemDelayClick(v: View?, task: DailyTask) {
                //推迟弹窗
                showMessage(
                    "您确定要推迟该条事项到今天么？推迟后该条将复制到您今天的日事日毕中",
                    "推迟日事日毕",
                    "确定",
                    { requestDailyTaskViewModel.loadDelayTask(task.id!!) },
                    "取消"
                )
            }
        })

    }

    /**
     * 日事日毕头布局
     * */
    private fun dailyTaskHeader() {
        val list: ArrayList<DailyHeader> = ArrayList()
        list.add(DailyHeader("公告", R.drawable.ic_notice))
        list.add(DailyHeader("随笔记录", R.drawable.ic_note_fill))
        list.add(DailyHeader("任务", R.drawable.ic_task))
        list.add(DailyHeader("审批", R.drawable.ic_approval))
        list.add(DailyHeader("混凝土", R.drawable.ic_concrete))
        list.add(DailyHeader("汇报", R.drawable.ic_report))
        list.add(DailyHeader("云盘", R.drawable.ic_disk))
        list.add(DailyHeader("例会", R.drawable.ic_meeting))
//        val view: View = layoutInflater.inflate(R.layout.header_daily_task, recyclerView, false)
//        val dailyHeaderRecycler: RecyclerView = view.findViewById(R.id.daily_header_recycler)
        //初始化recyclerView
        daily_header_recycler.layoutManager =
            DuobangLinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val dailyHeaderAdapter = DailyHeaderAdapter(list)
        daily_header_recycler.adapter = dailyHeaderAdapter
        dailyHeaderAdapter.setNbOnItemClickListener { adapter, view, position ->
            when (position) {
                0 -> {}
                1 -> {}
                2 -> {}
                3 -> {}
                4 -> {}
                5 -> {}
                6 -> ActivityMessenger.startActivity(this,DiskActivity::class)
                7 -> {}
            }
        }
//        return view
    }

    override fun lazyLoadData() {
        loadDailyTaskList()
    }

    /**
     * 当天数据走socket推送，历史数据走api服务
     * */
    private fun loadDailyTaskList() {
        loadsir.showLoading()
        //每天的数据是socket推送，判断是否今天数据拉取，历史数据根据时间戳是否拉取数据
        if (date == DateUtil.getCurrentDate()) {
            swipeRefresh.isEnabled = false
            loadRoomDailyTask(date)
        } else {

            swipeRefresh.isEnabled = true
            requestDailyTaskViewModel.loadDailyTaskList(orgId, date)
        }
    }

    /**
     * @作者　: JayGengi
     * @时间　: 2020/12/7 9:11
     * @描述　: 获取今日本地日事日毕数据
     */
    private fun loadRoomDailyTask(date: String) {
        val ymdStr: Array<String> = DateUtil.getYMD(DateUtil.parseDate(date))
        val year = ymdStr[0].toInt() //获取年
        val month = ymdStr[1].toInt() //获取月
        val day = ymdStr[2].toInt() //获取日
        val list: List<DailyTaskWrapper?>? =
            PmsRepository(requireContext()).dailyTaskWrapperDao!!.getDailyTaskFromData(
                year,
                month,
                day,
                orgId
            )
        val dailyTaskJson: String = JsonUtil.toJson(list)
        val dailyTaskWrappers: List<DailyTaskWrapper> = JsonUtil.toList(
            dailyTaskJson,
            DailyTaskWrapper::class.java
        )
        if (dailyTaskWrappers.isNotNull()) {
            emptyList = false
            loadsir.showSuccess()
            mAdapter.setList(dailyTaskWrappers)
        } else {
            emptyList = true
            loadsir.showEmpty("快来创建一条吧～")
        }
    }

    override fun createObserver() {
        requestDailyTaskViewModel.run {
            loadDailyTaskResult.observe(
                this@DailyTaskFragment,
                Observer { resultState ->
                    parseState(resultState, {
                        if (it.isNotNull()) {
                            emptyList = false
                            loadsir.showSuccess()
                            swipeRefresh.isRefreshing = false
                            mAdapter.setList(it)
                        } else {
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
        //评论或删除评论，通过全局消息刷新
        eventViewModel.run {
            dailyCommentEvent.observe(this@DailyTaskFragment, Observer {
                mAdapter.data[it.position].comments = it.comments
                mAdapter.notifyItemChanged(it.position)
            })
            dailyTaskEvent.observe(this@DailyTaskFragment, Observer {
                if (it) {
                    //首页收到socket通知，刷新数据
                    loadDailyTaskList()
                }
            })
        }
    }

    fun dateChange(date: String?) {
        this.date = date!!
        if (isVisible) {
            //当天禁用下拉刷新
            loadDailyTaskList()
        }
    }

}