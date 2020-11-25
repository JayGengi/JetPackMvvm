package com.duobang.jetpackmvvm.ui.fragment.login

import android.os.Bundle
import android.widget.CompoundButton
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseFragment
import com.duobang.common.ext.hideSoftKeyboard
import com.duobang.common.ext.initClose
import com.duobang.common.util.SettingUtil
import com.duobang.jetpackmvvm.R
import com.duobang.jetpackmvvm.databinding.FragmentLoginBinding
import com.duobang.jetpackmvvm.ext.nav
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.jetpackmvvm.viewmodel.request.RequestLoginViewModel
import com.duobang.jetpackmvvm.viewmodel.state.LoginRegisterViewModel
import kotlinx.android.synthetic.main.include_toolbar.*
import okhttp3.Response

/**
 * 作者　: JayGengi
 * 时间　: 2019/12/23
 * 描述　: 登录页面
 */
class LoginFragment : BaseFragment<LoginRegisterViewModel, FragmentLoginBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_login

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewmodel = mViewModel

        mDatabind.click = ProxyClick()

        SettingUtil.setShapColor(toolbar, R.color.white)
        toolbar.initClose("") {
            nav().navigateUp()
        }
    }

    override fun createObserver() {
        requestLoginRegisterViewModel.loginResult.observe(
            viewLifecycleOwner,
            Observer { resultState ->
                parseState(resultState, {
                    it as Response
                    //登录成功 通知账户数据发生改变鸟
                    ToastUtils.showShort("登录成功")
//                    CacheUtil.setUser(it)
//                    CacheUtil.setIsLogin(true)
//                    appViewModel.userinfo.value = it
//                    nav().navigateUp()
                }, {
                    //登录失败
                    ToastUtils.showShort(it.errorMsg)
                })
            })
    }

    inner class ProxyClick {

        fun clear() {
            mViewModel.username.value = ""
        }

        fun login() {
            when {
                mViewModel.username.value.isEmpty() -> ToastUtils.showShort("请填写账号")
                mViewModel.password.get().isEmpty() -> ToastUtils.showShort("请填写密码")
                else -> {
                    val map = HashMap<String, Any>()
                    map["username"] = mViewModel.username.value
                    map["password"] = mViewModel.password.get()
                    requestLoginRegisterViewModel.loginReq(map)
                }
            }
        }

        fun goRegister() {
            hideSoftKeyboard(activity)
//            nav().navigateAction(R.id.action_loginFragment_to_registerFrgment)
        }

        var onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                mViewModel.isShowPwd.set(isChecked)
            }
    }
}