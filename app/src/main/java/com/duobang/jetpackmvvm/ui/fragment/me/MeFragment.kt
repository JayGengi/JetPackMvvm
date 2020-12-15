package com.duobang.jetpackmvvm.ui.fragment.me

import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginTop
import com.blankj.utilcode.util.BarUtils
import com.duobang.common.base.BaseFragment
import com.duobang.common.util.ActivityMessenger
import com.duobang.common.util.AppImageLoader
import com.duobang.jetpackmvvm.R
import com.duobang.jetpackmvvm.databinding.FragmentMeBinding
import com.duobang.jetpackmvvm.ui.activity.me.PersonalActivity
import com.duobang.jetpackmvvm.viewmodel.state.MeViewModel
import kotlinx.android.synthetic.main.fragment_me.*


/**
 * 作者　: JayGengi
 * 时间　: 2019/12/23
 * 描述　: 我的
 */

class MeFragment : BaseFragment<MeViewModel, FragmentMeBinding>() {


    override fun layoutId() = R.layout.fragment_me

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()

        baseLay.setPadding(40,BarUtils.getStatusBarHeight()+40,40,80)

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
            ActivityMessenger.startActivity(this@MeFragment, PersonalActivity::class)
        }
        /** 设置*/
        fun setting() {
//            nav().navigateAction(R.id.action_mainfragment_to_settingFragment)
        }


    }
}