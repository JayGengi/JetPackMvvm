package com.duobang.jetpackmvvm.ui.fragment.workbench

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.duobang.jetpackmvvm.R
import com.duobang.jetpackmvvm.base.BaseFragment
import com.duobang.jetpackmvvm.base.viewmodel.BaseViewModel
import com.duobang.jetpackmvvm.databinding.FragmentOrgBinding

/**     
  * @作者　: JayGengi
  * @时间　: 2020/12/13 17:22
  * @描述　: 工作台
 */

class WorkBenchFragment : BaseFragment<BaseViewModel, FragmentOrgBinding>() {


    override fun layoutId() = R.layout.fragment_org

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {
    }

    override fun createObserver() {

    }
}