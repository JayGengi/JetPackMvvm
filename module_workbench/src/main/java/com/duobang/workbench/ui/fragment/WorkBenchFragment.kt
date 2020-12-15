package com.duobang.workbench.ui.fragment

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.duobang.common.base.BaseFragment
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.constant.RouterConstant
import com.duobang.workbench.R
import com.duobang.workbench.databinding.FragmentWorkbenchBinding
import kotlinx.android.synthetic.main.fragment_workbench.*

/**     
  * @作者　: JayGengi
  * @时间　: 2020/12/13 17:22
  * @描述　: 工作台
 */
@Route(path = RouterConstant.FRAG.WORKBENCH)
class WorkBenchFragment : BaseFragment<BaseViewModel, FragmentWorkbenchBinding>() {


    override fun layoutId() = R.layout.fragment_workbench

    override fun initView(savedInstanceState: Bundle?) {
        work.setPadding(0, BarUtils.getStatusBarHeight(),0,0)
    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {

    }
}