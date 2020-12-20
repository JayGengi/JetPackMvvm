package com.duobang.workbench.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseActivity
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.DailyTask
import com.duobang.common.ext.init
import com.duobang.common.ext.setNbOnItemChildClickListener
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.workbench.R
import com.duobang.workbench.databinding.ActivityDailyTaskCreateBinding
import com.duobang.workbench.ui.adapter.DailyTaskCreateAdapter
import com.duobang.workbench.viewmodel.request.RequestDailyTaskCreateViewModel
import com.google.gson.Gson
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.activity_daily_task_create.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/20 9:48
 * @描述　: 日事日毕创建
 */
class DailyTaskCreateActivity : BaseActivity<BaseViewModel, ActivityDailyTaskCreateBinding>() {

    private val mAdapter: DailyTaskCreateAdapter by lazy { DailyTaskCreateAdapter(arrayListOf()) }

    //请求数据ViewModel
    private val requestDailyTaskCreateViewModel: RequestDailyTaskCreateViewModel by viewModels()

    private var orgId = ""

    override fun layoutId() = R.layout.activity_daily_task_create

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.click = ProxyClick()
        //初始化recyclerView
        list_create_daily_task.init(LinearLayoutManager(this), mAdapter)
        orgId = appViewModel.orginfo.value!!.homeOrgId!!

        mAdapter.addChildClickViewIds(
            R.id.content_create_daily_task_item,
            R.id.going_rg_create_daily_task_item,
            R.id.finish_rg_create_daily_task_item,
            R.id.nofinish_rg_create_daily_task_item
        )
        mAdapter.setNbOnItemChildClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as DailyTask
            when (view.id) {
                R.id.content_create_daily_task_item -> {
                    if (item.isEditable && !item.isEdit && !item.isDelete) {
                        itemChangeOrEdit(true, position, item)
                    } else {
                        if (item.isDelete) {
                            ToastUtils.showShort("请撤回删除，再进行修改！！")
                        } else if (!item.isEdit) {
                            ToastUtils.showShort("状态已修改，不可编辑！！")
                        }
                    }
                }
                R.id.going_rg_create_daily_task_item -> {
                    updateState(item, 0)
                }
                R.id.finish_rg_create_daily_task_item -> {
                    updateState(item, 1)
                }
                R.id.nofinish_rg_create_daily_task_item -> {
                    updateState(item, 2)
                }
            }
        }
        mAdapter.setOnItemDeleteClickListener(object : DailyTaskCreateAdapter.OnItemDeleteClickListener{
            override fun onItemDeleteClick(connectStr: String, position: Int, item: DailyTask) {
                if (item.isDelete) { //如果是删除状态，再次点击撤回
                    item.isDelete = false
                    item.isEdit = false
                    itemChangeOrEdit(false, position, item)
                } else {
                    if (item.isDeleteable) {
                        if (connectStr.isNotEmpty()) {
                            item.isDelete = true
                            item.content = connectStr
                            item.isEdit = false
                            itemChangeOrEdit(false, position, item)
                        } else {
                            handleEditData()
                            val dataList: MutableList<DailyTask> = mAdapter.data
                            if (position < dataList.size) {
                                item.isEdit = true
                                dataList[position] = item
                            }
                            mAdapter.setList(dataList)
                        }
                    } else {
                        ToastUtils.showShort("不可删除！")
                    }
                }
            }

        })
        requestDailyTaskCreateViewModel.loadPersonalDailyTasks(orgId)

    }

    /**
     * @param edit [true or false]
     * @param position 当前下标
     * @param item
     */
    private fun itemChangeOrEdit(edit: Boolean, position: Int, item: DailyTask) {
        handleEditData()
        val dataList: MutableList<DailyTask> = mAdapter.data
        if (position < dataList.size) {
            if (edit) {
                item.isEdit = true
            }
            dataList[position] = item
        }
        mAdapter.setList(dataList)
    }

    override fun createObserver() {
        requestDailyTaskCreateViewModel.run {
            loadPersonalDailyTasksResult.observe(
                this@DailyTaskCreateActivity,
                Observer { resultState ->
                    parseState(resultState, {
                        val tasks = it as ArrayList<DailyTask>
                        if (it.isEmpty()) {
                            val task = DailyTask()
                            task.isEdit = true
                            task.state = 0
                            tasks.add(task)
                        }
                        mAdapter.setList(tasks)
                    }, {
                        ToastUtils.showShort(it.errorMsg)
                    })
                })

            loadUploadDailyTaskResult.observe(
                this@DailyTaskCreateActivity,
                Observer { resultState ->
                    parseState(resultState, {
                        ToastUtils.showShort("更新成功")
                        finish()
                    }, {
                        ToastUtils.showShort(it.errorMsg)
                    })
                })
        }
    }

    /**
     * 将其他编辑状态的item数据保存并取消编辑状态
     */
    private fun handleEditData() {
        val dailyTaskList: List<DailyTask> = mAdapter.data
        for (i in dailyTaskList.indices) {
            val dailyTask = dailyTaskList[i]
            if (dailyTask.isEdit) {
                val child: View = list_create_daily_task.getChildAt(i)
                val input =
                    child.findViewById<EditText>(R.id.content_create_daily_task_item)
                val stateView =
                    child.findViewById<RadioGroup>(R.id.state_view_create_daily_task_item)
                dailyTask.content = input.text.toString()
                dailyTask.isEdit = false
                when (stateView.checkedRadioButtonId) {
                    R.id.going_rg_create_daily_task_item -> {
                        updateState(dailyTask, 0)
                    }
                    R.id.finish_rg_create_daily_task_item -> {
                        updateState(dailyTask, 1)
                    }
                    R.id.nofinish_rg_create_daily_task_item -> {
                        updateState(dailyTask, 2)
                    }
                }
            }
        }
    }

    /**
     * 移除删除状态的，以及空的task
     *
     * @param tasks
     * @return
     */
    private fun removeDeleteStateTask(tasks: List<DailyTask>): List<DailyTask>? {
        val list: MutableList<DailyTask> = java.util.ArrayList()
        for (i in tasks.indices) {
            if (!tasks[i].isDelete && tasks[i].content != null && tasks[i].content!!.isNotEmpty()
            ) {
                list.add(tasks[i])
            }
        }
        return list
    }

    private fun updateState(dailyTask: DailyTask, state: Int) {
        dailyTask.isSave = false
        dailyTask.state = state
        mAdapter.notifyDataSetChanged()
    }

    /**
     * 添加一条编辑状态的事项
     */
    private fun addNewEditItem() {
        val dataList: MutableList<DailyTask> = if (mAdapter.data.size > 0) {
            mAdapter.data
        } else {
            ArrayList()
        }
        val dailyTask = DailyTask()
        dailyTask.state = 0
        dailyTask.isEdit = true
        dailyTask.isSave = false
        dataList.add(dailyTask)
        mAdapter.setList(dataList)
    }

    inner class ProxyClick {
        fun cancel() {
            finish()
        }

        fun update() {
            handleEditData()
            requestDailyTaskCreateViewModel.uploadDailyTask(
                orgId,
                Gson().toJson(removeDeleteStateTask(mAdapter.data))
            )
        }

        fun addTask() {
            handleEditData()
            addNewEditItem()
        }
    }
}
