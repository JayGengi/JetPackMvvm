package com.duobang.dbdemo.viewmodel.state

import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.base.callback.databind.StringObservableField


/**
 * 作者　: JayGengi
 * 时间　: 2019/12/27
 * 描述　: 专门存放 MeFragment 界面数据的ViewModel
 */
class PersonalViewModel : BaseViewModel() {

    var name = StringObservableField("")

    var account = StringObservableField("")

    var phone = StringObservableField("")

    var department = StringObservableField("")

    var orgName  = StringObservableField("")

    var orgRole = StringObservableField("")

}