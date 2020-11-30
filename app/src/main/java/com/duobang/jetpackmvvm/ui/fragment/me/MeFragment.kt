package com.duobang.jetpackmvvm.ui.fragment.me

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.duobang.common.base.BaseFragment
import com.duobang.common.util.AppImageLoader
import com.duobang.jetpackmvvm.R
import com.duobang.jetpackmvvm.databinding.FragmentMeBinding
import com.duobang.jetpackmvvm.ext.nav
import com.duobang.jetpackmvvm.ext.navigateAction
import com.duobang.jetpackmvvm.viewmodel.request.RequestMeViewModel
import com.duobang.jetpackmvvm.viewmodel.state.MeViewModel
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
            nav().navigateAction(R.id.action_to_PersonalFragment)
        }
        /** 设置*/
        fun setting() {
//            nav().navigateAction(R.id.action_mainfragment_to_settingFragment)
        }


    }
}