package com.duobang.jetpackmvvm.pms.weight.loadCallBack


import com.kingja.loadsir.callback.Callback
import com.duobang.jetpackmvvm.pms.R


class EmptyCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }

}