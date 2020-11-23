package com.duobang.jetpackmvvm.pms.ui.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_main.*
import com.duobang.jetpackmvvm.pms.R
import com.duobang.jetpackmvvm.pms.base.BaseFragment
import com.duobang.jetpackmvvm.pms.ext.init
import com.duobang.jetpackmvvm.pms.ext.initMain
import com.duobang.jetpackmvvm.pms.ext.interceptLongClick
import com.duobang.jetpackmvvm.pms.ext.setUiTheme
import com.duobang.jetpackmvvm.pms.databinding.FragmentMainBinding
import com.duobang.jetpackmvvm.pms.viewmodel.state.MainViewModel

/**
 * 时间　: 2019/12/27
 * 作者　: JayGengi
 * 描述　:项目主页Fragment
 */
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    override fun layoutId() = R.layout.fragment_main

    override fun initView(savedInstanceState: Bundle?) {
        //初始化viewpager2
        mainViewpager.initMain(this)
        //初始化 bottomBar
        mainBottom.init{
            when (it) {
                R.id.menu_main -> mainViewpager.setCurrentItem(0, false)
//                R.id.menu_project -> mainViewpager.setCurrentItem(1, false)
//                R.id.menu_system -> mainViewpager.setCurrentItem(2, false)
//                R.id.menu_public -> mainViewpager.setCurrentItem(3, false)
                R.id.menu_me -> mainViewpager.setCurrentItem(1, false)
            }
        }
        mainBottom.interceptLongClick(R.id.menu_main,R.id.menu_me)
    }

    override fun createObserver() {
    }
}