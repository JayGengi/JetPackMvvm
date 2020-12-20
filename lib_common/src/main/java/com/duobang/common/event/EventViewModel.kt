package com.duobang.common.event

import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.DailyTaskBus
import com.duobang.jetpackmvvm.callback.livedata.event.EventLiveData

/**
 * 作者　: JayGengi
 * 时间　: 2019/5/2
 * 描述　:APP全局的ViewModel，可以在这里发送全局通知替代EventBus，LiveDataBus等
 */
class EventViewModel : BaseViewModel() {

    //日事日毕评论底部弹窗刷新父级
    val dailyCommentEvent = EventLiveData<DailyTaskBus>()

    //发送通知,目前仅有日事日毕有消息推送
    val dailyTaskEvent = EventLiveData<Boolean>()
}