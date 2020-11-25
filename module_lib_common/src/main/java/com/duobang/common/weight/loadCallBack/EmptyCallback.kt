package com.duobang.common.weight.loadCallBack


import com.kingja.loadsir.callback.Callback
import com.duobang.common.R


class EmptyCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }

}