package com.duobang.jetpackmvvm.ui.fragment.workbench

import android.os.Bundle
import com.blankj.utilcode.util.BarUtils
import com.duobang.jetpackmvvm.R
import com.duobang.common.base.BaseFragment
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.jetpackmvvm.databinding.FragmentOrgBinding
import kotlinx.android.synthetic.main.fragment_work.*

/**     
  * @作者　: JayGengi
  * @时间　: 2020/12/13 17:22
  * @描述　: 工作台
 */

class WorkBenchFragment : BaseFragment<BaseViewModel, FragmentOrgBinding>() {


    override fun layoutId() = R.layout.fragment_work

    override fun initView(savedInstanceState: Bundle?) {
        work.setPadding(0, BarUtils.getStatusBarHeight(),0,0)
    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {

    }
}