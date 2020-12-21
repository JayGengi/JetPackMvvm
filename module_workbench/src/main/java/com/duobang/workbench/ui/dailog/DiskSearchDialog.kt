package com.duobang.workbench.ui.dailog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import com.duobang.workbench.R

/**
 * @Des: 云盘文件搜索框
 * @Author: JayGengi
 * @Date: 2020/10/14 18:02
 */
class DiskSearchDialog(context: Context) :
    Dialog(context, R.style.view_dialog) {
    private var mSearchView: SearchView? = null
    private var onSearchListener: OnSearchListener? = null
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_disk_search)
        mSearchView = findViewById(R.id.search_view)
        val window = window!!
        val params = window.attributes
        params.gravity = Gravity.TOP
        initView()
    }

    private fun initView() {
        //搜索图标是否显示在搜索框内
        mSearchView!!.setIconifiedByDefault(false)

        //设置搜索框展开时是否显示提交按钮，可不显示
        mSearchView!!.isSubmitButtonEnabled = false

        //让键盘的回车键设置成搜索
        mSearchView!!.imeOptions = EditorInfo.IME_ACTION_SEARCH
        //搜索框是否展开，false表示展开
        mSearchView!!.isIconified = false
        //获取焦点
        mSearchView!!.isFocusable = true
        mSearchView!!.requestFocusFromTouch()

        // 设置搜索文本监听
        mSearchView!!.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            // 当点击搜索按钮时触发该方法
            override fun onQueryTextSubmit(query: String): Boolean {
                //清除焦点，收软键盘
                mSearchView!!.clearFocus()
                onSearchListener!!.OnSearchClick(query)
                dismiss()
                return false
            }

            // 当搜索内容改变时触发该方法
            override fun onQueryTextChange(newText: String): Boolean {
                //do something
                //当没有输入任何内容的时候清除结果，看实际需求
                if (TextUtils.isEmpty(newText));
                return false
            }
        })
    }

    fun setOnSearchListener(onSearchListener: OnSearchListener?) {
        this.onSearchListener = onSearchListener
    }

    interface OnSearchListener {
        fun OnSearchClick(query: String?)
    }
}