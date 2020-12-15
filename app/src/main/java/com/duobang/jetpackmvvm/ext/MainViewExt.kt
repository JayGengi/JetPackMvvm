package com.duobang.jetpackmvvm.ext

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.duobang.jetpackmvvm.ui.fragment.home.HomeFragment
import com.duobang.jetpackmvvm.ui.fragment.me.MeFragment
import com.duobang.jetpackmvvm.ui.fragment.org.OrgFragment
import com.duobang.jetpackmvvm.ui.fragment.project.ProjectFragment
import com.duobang.jetpackmvvm.ui.fragment.workbench.WorkBenchFragment
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx

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
                    return WorkBenchFragment()
                }
                3 -> {
                    return OrgFragment()
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
