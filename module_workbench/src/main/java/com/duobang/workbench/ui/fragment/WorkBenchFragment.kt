package com.duobang.workbench.ui.fragment

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.duobang.common.base.BaseFragment
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.constant.RouterConstant
import com.duobang.common.util.ActivityMessenger
import com.duobang.common.util.AppDatePicker
import com.duobang.common.util.DateUtil
import com.duobang.workbench.R
import com.duobang.workbench.databinding.FragmentWorkbenchBinding
import com.duobang.workbench.ui.activity.DailyTaskCreateActivity
import kotlinx.android.synthetic.main.fragment_workbench.*
import java.util.*

/**     
  * @作者　: JayGengi
  * @时间　: 2020/12/13 17:22
  * @描述　: 工作台
 */
@Route(path = RouterConstant.FRAG.WORKBENCH)
class WorkBenchFragment : BaseFragment<BaseViewModel, FragmentWorkbenchBinding>() {
    private lateinit var currentDate: String
    private var isMain = true
    private var dailyTaskFragment: DailyTaskFragment? = null
    private var dailyManageFragment: DailyManageFragment? = null
    override fun layoutId() = R.layout.fragment_workbench

    override fun initView(savedInstanceState: Bundle?) {
        workBenchBar.setPadding(0, BarUtils.getStatusBarHeight(),0,0)
        mDatabind.click = ProxyClick()

        dailyTaskFragment = DailyTaskFragment()
        dailyManageFragment = DailyManageFragment()
        activity?.supportFragmentManager?.beginTransaction()!!
            .add(R.id.container_daily_task, dailyTaskFragment!!).commitAllowingStateLoss()
    }
    override fun onResume() {
        super.onResume()
        currentDate = DateUtil.getCurrentDate()
        date_daily_task.text = currentDate
        if (dailyTaskFragment != null) {
            dailyTaskFragment!!.dateChange(currentDate)
        }
        if (dailyManageFragment != null) {
            dailyManageFragment!!.dateChange(currentDate)
        }
    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {

    }
    inner class ProxyClick {
        fun dailySwitch() {
            switchFragment()
        }
        fun lastMonth() {
            upDate(false)
        }
        fun daysChoose() {
            showDatePicker()
        }
        fun nextMonth() {
            upDate(true)
        }
        fun createTask() {
            ActivityMessenger.startActivity(this@WorkBenchFragment, DailyTaskCreateActivity::class)
        }
    }

    private fun showDatePicker() {
        AppDatePicker.showDatePicker(date_daily_task) { date ->
            if (dailyTaskFragment != null) {
                dailyTaskFragment!!.dateChange(date)
            }
            if (dailyManageFragment != null) {
                dailyManageFragment!!.dateChange(date)
            }
        }
    }
    /**
     * 切换fragment
     */
    private fun switchFragment() {
        val fragmentManager = activity?.supportFragmentManager
        val transaction = fragmentManager?.beginTransaction()
        if (isMain) {
            transaction?.setCustomAnimations(R.anim.top_in, R.anim.bottom_out)
            if (!dailyManageFragment!!.isAdded) {
                transaction?.add(R.id.container_daily_task, dailyManageFragment!!)?.hide(dailyTaskFragment!!)
            } else {
                transaction?.show(dailyManageFragment!!)?.hide(dailyTaskFragment!!)
            }
        } else {
            transaction?.setCustomAnimations(R.anim.bottom_in, R.anim.top_out)
            if (!dailyTaskFragment!!.isAdded) {
                transaction?.add(R.id.container_daily_task, dailyTaskFragment!!)?.hide(dailyManageFragment!!)
            } else {
                transaction?.show(dailyTaskFragment!!)?.hide(dailyManageFragment!!)
            }
        }
        isMain = !isMain
        updateState()
        transaction?.commitAllowingStateLoss()
    }
    private fun updateState() {
        if (isMain) {
            manage_daily_task.setIconResource(R.drawable.ic_manage)
            commit_daily_task.visibility = View.VISIBLE
        } else {
            manage_daily_task.setIconResource(R.drawable.ic_error)
            commit_daily_task.visibility = View.GONE
        }
    }
    private fun upDate(isUp: Boolean) {
        currentDate = date_daily_task.text.toString()
        val beforeDay: Long = if (isUp) {
            DateUtil.parseDate(currentDate).time + 24 * 60 * 60 * 1000
        } else {
            DateUtil.parseDate(currentDate).time - 24 * 60 * 60 * 1000
        }
        currentDate = DateUtil.formatDate(Date(beforeDay))
        date_daily_task.text = currentDate
        if (dailyTaskFragment != null) {
            dailyTaskFragment!!.dateChange(currentDate)
        }
        if (dailyManageFragment != null) {
            dailyManageFragment!!.dateChange(currentDate)
        }
    }
}