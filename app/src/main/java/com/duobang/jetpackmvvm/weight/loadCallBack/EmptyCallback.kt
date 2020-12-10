package com.duobang.jetpackmvvm.weight.loadCallBack


import com.kingja.loadsir.callback.Callback
import com.duobang.jetpackmvvm.R


class EmptyCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }

}