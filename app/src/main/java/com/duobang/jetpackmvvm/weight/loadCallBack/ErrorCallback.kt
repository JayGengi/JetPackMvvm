package com.duobang.jetpackmvvm.weight.loadCallBack

import com.kingja.loadsir.callback.Callback
import com.duobang.jetpackmvvm.R


class ErrorCallback : Callback() {

    override fun onCreateView(): Int {
        return R.layout.layout_error
    }

}