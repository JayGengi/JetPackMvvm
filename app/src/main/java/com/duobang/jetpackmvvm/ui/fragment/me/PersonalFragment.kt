package com.duobang.jetpackmvvm.ui.fragment.me

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.duobang.common.base.BaseFragment
import com.duobang.common.data.bean.Organization
import com.duobang.common.ext.initClose
import com.duobang.common.util.AppImageLoader
import com.duobang.common.util.StatusBarUtil
import com.duobang.jetpackmvvm.R
import com.duobang.jetpackmvvm.databinding.FragmentPersonalBinding
import com.duobang.jetpackmvvm.ext.nav
import com.duobang.jetpackmvvm.ext.navigateAction
import com.duobang.jetpackmvvm.ext.util.notNull
import com.duobang.jetpackmvvm.viewmodel.request.RequestMeViewModel
import com.duobang.jetpackmvvm.viewmodel.state.PersonalViewModel
import kotlinx.android.synthetic.main.fragment_personal.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/30 14:25
 * @描述　: 我的信息
 */

class PersonalFragment : BaseFragment<PersonalViewModel, FragmentPersonalBinding>() {


    private val requestMeViewModel: RequestMeViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_personal

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()

        toolbar.initClose("个人信息") {
            nav().navigateUp()
        }

        appViewModel.userinfo.value!!.apply {
            AppImageLoader.displayAvatar(avatar, nickname, user_avatar_per)
            mViewModel.name.set(nickname)
            mViewModel.account.set(username)
            mViewModel.phone.set(phone)
            mViewModel.orgName.set(getHomeOrgName(appViewModel.orginfo.value!!))
            if (groupList != null && groupList!!.isNotEmpty()) {
                mViewModel.department.set(groupList!![0].groupName)
            }
            if (roleList != null && roleList!!.isNotEmpty()) {
                mViewModel.orgRole.set(roleList!![0].roleName)
            }
        }

    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {
        appViewModel.userinfo.observe(viewLifecycleOwner, Observer { it ->
            it.notNull({
                mViewModel.name.set(it.nickname)
                AppImageLoader.displayAvatar(it.avatar, it.nickname, user_avatar_per)
            }, {})
        })
    }

    /**
     * @作者　: JayGengi
     * @时间　: 2020/11/30 14:58
     * @描述　: 当前所属部门组织
     */
    private fun getHomeOrgName(org: Organization): String? {
        for (info in org.orgList!!) {
            if (info.id.equals(org.homeOrgId)) {
                return info.name
            }
        }
        return null
    }

    inner class ProxyClick {
        /** 用户昵称*/
        fun nickName() {
            nav().navigateAction(R.id.action_to_NickNameFragment)
        }
//        /** 设置*/
//        fun setting() {
////            nav().navigateAction(R.id.action_mainfragment_to_settingFragment)
//        }


    }
}