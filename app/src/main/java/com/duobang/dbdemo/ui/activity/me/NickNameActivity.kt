package com.duobang.dbdemo.ui.activity.me

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseActivity
import com.duobang.common.ext.initClose
import com.duobang.common.util.CacheUtil
import com.duobang.dbdemo.R
import com.duobang.dbdemo.databinding.ActivityNickNameBinding
import com.duobang.base.ext.parseState
import com.duobang.dbdemo.viewmodel.request.RequestNickNameViewModel
import com.duobang.dbdemo.viewmodel.state.NickNameViewModel
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/30 14:25
 * @描述　: 昵称
 */
class NickNameActivity : BaseActivity<NickNameViewModel, ActivityNickNameBinding>() {

    //请求数据ViewModel
    private val requestNickNameViewModel: RequestNickNameViewModel by viewModels()

    override fun layoutId() = R.layout.activity_nick_name

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        toolbar.run {
            initClose("昵称修改") {
                finish()
            }
            inflateMenu(R.menu.menu_commit)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_commit -> {
                        //提交'
                        when {
                            mViewModel.nickname.get().isEmpty() -> ToastUtils.showShort("请填写昵称")
                            else -> {
                                val map = HashMap<String, Any>()
                                map["userId"] = mViewModel.userId.get()
                                map["nickname"] = mViewModel.nickname.get()
                                requestNickNameViewModel.updateNickName(map)
                            }
                        }
                    }
                }
                true
            }
        }
        mViewModel.userId.set(appViewModel.userinfo.value!!.id)
        mViewModel.nickname.set(appViewModel.userinfo.value!!.nickname)


    }

    override fun createObserver() {
        requestNickNameViewModel.userResult.observe(this, Observer { resultState ->
            parseState(resultState, {
                //修改成功 通知账户数据发生改变鸟
                CacheUtil.setUser(it)
                appViewModel.userinfo.value = it
                finish()
            }, {
                ToastUtils.showShort(it.errorMsg)
            })

        })
    }

    inner class ProxyClick {
        fun clear() {
            mViewModel.nickname.set("")
        }
    }


}
