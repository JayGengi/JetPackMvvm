package com.duobang.jetpackmvvm.pms.ui.fragment.me

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.duobang.jetpackmvvm.pms.R
import com.duobang.jetpackmvvm.pms.base.BaseFragment
import com.duobang.jetpackmvvm.pms.databinding.FragmentMeBinding
import com.duobang.jetpackmvvm.pms.util.AppImageLoader
import com.duobang.jetpackmvvm.pms.viewmodel.request.RequestMeViewModel
import com.duobang.jetpackmvvm.pms.viewmodel.state.MeViewModel
import kotlinx.android.synthetic.main.fragment_me.*

/**
 * 作者　: JayGengi
 * 时间　: 2019/12/23
 * 描述　: 我的
 */

class MeFragment : BaseFragment<MeViewModel, FragmentMeBinding>() {


    private val requestMeViewModel: RequestMeViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_me

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        appViewModel.userinfo.value!!.apply {
            mViewModel.name.set(nickname)
            AppImageLoader.displayAvatar(avatar, nickname, user_icon_person)
        }

    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {

    }

    inner class ProxyClick {
        /** 个人信息*/
        fun userInfo(){

        }
        /** 设置*/
        fun setting() {
//            nav().navigateAction(R.id.action_mainfragment_to_settingFragment)
        }


    }
}