package com.duobang.jetpackmvvm.viewmodel.state

import com.blankj.utilcode.util.BarUtils
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.jetpackmvvm.callback.databind.IntObservableField
import com.duobang.jetpackmvvm.callback.databind.StringObservableField


/**
 * 作者　: JayGengi
 * 时间　: 2019/12/27
 * 描述　: 专门存放 MeFragment 界面数据的ViewModel
 */
class MeViewModel : BaseViewModel() {

    var name = StringObservableField("请先登录~")

    var integral = IntObservableField(0)

    var info = StringObservableField("id：--　排名：-")

    var barHeight = IntObservableField(BarUtils.getStatusBarHeight())
}