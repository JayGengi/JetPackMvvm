package com.duobang.jetpackmvvm.ui.fragment.org

import android.os.Bundle
import com.blankj.utilcode.util.BarUtils
import com.duobang.common.base.BaseFragment
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.jetpackmvvm.R
import com.duobang.jetpackmvvm.databinding.FragmentOrgBinding
import kotlinx.android.synthetic.main.fragment_org.*

/**
  * @作者　: JayGengi
  * @时间　: 2020/12/13 17:21
  * @描述　: 联系人
 */

class OrgFragment : BaseFragment<BaseViewModel, FragmentOrgBinding>() {



    override fun layoutId() = R.layout.fragment_org

    override fun initView(savedInstanceState: Bundle?) {
        text.setPadding(0, BarUtils.getStatusBarHeight(),0,0)
    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {

    }
}