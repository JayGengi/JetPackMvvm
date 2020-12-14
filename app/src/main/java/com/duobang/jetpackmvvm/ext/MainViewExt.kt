package com.duobang.jetpackmvvm.ext

import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.duobang.jetpackmvvm.base.appContext
import com.duobang.jetpackmvvm.ui.fragment.home.HomeFragment
import com.duobang.jetpackmvvm.ui.fragment.me.MeFragment
import com.duobang.jetpackmvvm.ui.fragment.org.OrgFragment
import com.duobang.jetpackmvvm.ui.fragment.project.ProjectFragment
import com.duobang.jetpackmvvm.ui.fragment.workbench.WorkBenchFragment
import com.duobang.jetpackmvvm.util.SettingUtil
import com.duobang.jetpackmvvm.weight.recyclerview.DefineLoadMoreView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx
import com.kingja.loadsir.core.LoadService
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * 作者　: JayGengi
 * 时间　: 2020/2/20
 * 描述　:项目中自定义类的拓展函数
 */


fun ViewPager2.initMain(activity: FragmentActivity): ViewPager2 {
    //是否可滑动
    this.isUserInputEnabled = false
    this.offscreenPageLimit = 5
    //设置适配器
    adapter = object : FragmentStateAdapter(activity) {
        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> {
                    return HomeFragment()
                }
                1 -> {
                    return ProjectFragment()
                }
                2 -> {
                    return OrgFragment()
                }
                3 -> {
                    return WorkBenchFragment()
                }
                4 -> {
                    return MeFragment()
                }
                else -> {
                    return HomeFragment()
                }
            }
        }

        override fun getItemCount() = 5
    }
    return this
}

fun BottomNavigationViewEx.init(navigationItemSelectedAction: (Int) -> Unit): BottomNavigationViewEx {
    enableAnimation(true)
    enableShiftingMode(false)
    enableItemShiftingMode(true)
    setTextSize(12F)
    setOnNavigationItemSelectedListener {
        navigationItemSelectedAction.invoke(it.itemId)
        true
    }
    return this
}


/**
 * 拦截BottomNavigation长按事件 防止长按时出现Toast ----
 * @receiver BottomNavigationViewEx
 * @param ids IntArray
 */
fun BottomNavigationViewEx.interceptLongClick(vararg ids: Int) {
    val bottomNavigationMenuView: ViewGroup = (this.getChildAt(0) as ViewGroup)
    for (index in ids.indices) {
        bottomNavigationMenuView.getChildAt(index).findViewById<View>(ids[index])
            .setOnLongClickListener {
                true
            }
    }
}
