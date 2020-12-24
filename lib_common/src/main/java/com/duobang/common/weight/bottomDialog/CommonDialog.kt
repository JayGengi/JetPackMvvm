package com.duobang.common.weight.bottomDialog

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup

class CommonDialog(
    context: Context?,
    width: Int,
    height: Int,
    layout: View?,
    style: Int
) : Dialog(context!!, style) {
    constructor(context: Context?, layout: View?, style: Int) : this(
        context,
        default_width,
        default_height,
        layout,
        style
    ) {
    }

    companion object {
        private const val default_width = ViewGroup.LayoutParams.WRAP_CONTENT //默认宽度
        private const val default_height = ViewGroup.LayoutParams.WRAP_CONTENT //默认高度
    }

    init {
        setContentView(layout!!)
        val window = window!!
        val params = window.attributes
        params.width = width
        params.height = height
        params.gravity = Gravity.CENTER
        window.attributes = params
    }
}