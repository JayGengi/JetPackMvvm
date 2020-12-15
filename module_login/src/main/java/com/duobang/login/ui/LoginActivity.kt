package com.duobang.login.ui

import android.os.Bundle
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseActivity
import com.duobang.common.data.constant.RouterConstant
import com.duobang.common.ext.hideSoftKeyboard
import com.duobang.common.ext.initClose
import com.duobang.common.ext.routerJump
import com.duobang.common.ext.showToast
import com.duobang.common.util.CacheUtil
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.login.R
import com.duobang.login.databinding.ActivityLoginBinding
import com.duobang.login.viewmodel.request.RequestLoginViewModel
import com.duobang.login.viewmodel.state.LoginRegisterViewModel
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/23 9:53
 * @描述　: 登录页
 */
@Route(path = RouterConstant.ACT.LOGIN)
class LoginActivity : BaseActivity<LoginRegisterViewModel, ActivityLoginBinding>() {

    private val requestLoginRegisterViewModel: RequestLoginViewModel by viewModels()

    override fun layoutId() = R.layout.activity_login


    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()
        toolbar.initClose("", R.drawable.ic_delete) {
            finish()
        }
        toolbar.background = null
    }

    override fun createObserver() {
        requestLoginRegisterViewModel.run {
            loginResult.observe(
                this@LoginActivity,
                Observer { resultState ->
                    parseState(resultState, {
                        if (!it.isActivate) {
                            //已有组织用户获取用户信息登录
                            requestLoginRegisterViewModel.loadPersonInfo()
                        } else {
                            //组织邀请码页面填写
                        }
                    }, {
                        //登录失败
                        showToast(it.errorMsg)
                    })
                })
            userInfoResult.observe(
                this@LoginActivity,
                Observer { resultState ->
                    parseState(resultState, {
                        //登录成功 通知账户数据发生改变鸟
                        CacheUtil.setUser(it)
                        appViewModel.userinfo.value = it
                        routerJump(RouterConstant.ACT.MAIN)
                        finish()
                    }, {
                        showToast(it.errorMsg)
                    })
                })
        }
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
            hideSoftKeyboard(activity = this@LoginActivity)
//            nav().navigateAction(R.id.action_loginFragment_to_registerFrgment)
        }

        var onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                mViewModel.isShowPwd.set(isChecked)
            }
    }
}
