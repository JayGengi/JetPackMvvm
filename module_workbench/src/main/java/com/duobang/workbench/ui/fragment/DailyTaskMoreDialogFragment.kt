package com.duobang.workbench.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duobang.common.data.bean.DailyTask
import com.duobang.common.data.bean.DailyTaskWrapper
import com.duobang.common.ext.init
import com.duobang.common.ext.setNbOnItemChildClickListener
import com.duobang.common.ext.showMessage
import com.duobang.common.ext.showToast
import com.duobang.common.room.repository.PmsRepository
import com.duobang.common.util.AppImageLoader
import com.duobang.common.weight.bottomDialog.BaseBottomDialogFragment
import com.duobang.common.weight.customview.AvatarView
import com.duobang.base.state.ResultState
import com.duobang.workbench.R
import com.duobang.workbench.ui.adapter.DailyTaskInnerAdapter
import com.duobang.workbench.viewmodel.request.RequestDailyTaskViewModel

/**     
  * @作者　: JayGengi
  * @时间　: 2020/12/18 14:10
  * @描述　: 日事日毕查看更多
 */
class DailyTaskMoreDialogFragment : BaseBottomDialogFragment() {
    private var avatar: AvatarView? = null
    private var name: TextView? = null
    private var date: TextView? = null
    private var mRecyclerView: RecyclerView? = null
    private var wrapper: DailyTaskWrapper? = null
    private val mAdapter: DailyTaskInnerAdapter by lazy { DailyTaskInnerAdapter(arrayListOf()) }
    //请求数据ViewModel
    private val requestDailyTaskViewModel: RequestDailyTaskViewModel by viewModels()
    companion object {
        fun newInstance(wrapper: DailyTaskWrapper?): DailyTaskMoreDialogFragment {
            val fragment = DailyTaskMoreDialogFragment()
            fragment.wrapper = wrapper
            return fragment
        }
    }
    fun getLayoutId() : Int = R.layout.fragment_dailytaskmore_list_dialog

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(getLayoutId(), container, false)

        avatar = rootView.findViewById(R.id.avatar_daily_task_dialog)
        name = rootView.findViewById<TextView>(R.id.name_daily_task_dialog)
        date = rootView.findViewById<TextView>(R.id.date_daily_task_dialog)
        mRecyclerView = rootView.findViewById(R.id.list_daily_task_dialog)
        initData()
        createObserver()
        return rootView
    }

    private fun initData() {
        val createUser = context?.let { PmsRepository(it).getUserById(wrapper!!.creatorId!!) }
        if (createUser != null) {
            AppImageLoader.displayAvatar(
                createUser.avatar,createUser.nickname, avatar)
            name?.text = createUser.nickname
            date?.text =wrapper!!.formatDate
        }
        setupRecyclerView(wrapper!!.dailyTasks!!)
    }

    private fun setupRecyclerView(dailyTaskList: List<DailyTask>) {
        //初始化recyclerView
        mRecyclerView?.init(LinearLayoutManager(context), mAdapter)
        mAdapter.setList(dailyTaskList)

        mAdapter.addChildClickViewIds(R.id.delay_daily_task_inner_item)
        mAdapter.setNbOnItemChildClickListener { adapter, view, position ->
            val info: DailyTask = adapter.getItem(position) as DailyTask
            //推迟日事日毕
            if (view.id == R.id.delay_daily_task_inner_item) {
                showMessage(
                    "您确定要推迟该条事项到今天么？推迟后该条将复制到您今天的日事日毕中",
                    "推迟日事日毕",
                    "确定",
                    {requestDailyTaskViewModel.loadDelayTask(info.id!!)},
                "取消"
                )
            }
        }
    }

    private fun createObserver() {
        requestDailyTaskViewModel.loadDelayTaskResult.observe(
            this,
            Observer { resultState ->
                when (resultState) {
                    is ResultState.Success -> {
                        showToast("推迟成功！请在今天列表查看！！")
                    }
                    is ResultState.Error -> {
                        showToast(resultState.error.errorMsg)
                    }
                }
            })
    }
}