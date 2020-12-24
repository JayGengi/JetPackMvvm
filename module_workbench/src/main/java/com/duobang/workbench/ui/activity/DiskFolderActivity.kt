package com.duobang.workbench.ui.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.duobang.common.base.BaseActivity
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.DiskBean
import com.duobang.common.ext.setNbOnItemClickListener
import com.duobang.common.ext.showToast
import com.duobang.common.weight.recyclerview.DuobangLinearLayoutManager
import com.duobang.base.ext.parseState
import com.duobang.base.ext.view.clickNoRepeat
import com.duobang.workbench.R
import com.duobang.workbench.databinding.ActivityDiskFolderBinding
import com.duobang.workbench.ui.adapter.DiskFolderAdapter
import com.duobang.workbench.ui.adapter.DiskMenuAdapter
import com.duobang.workbench.viewmodel.request.RequestDiskFolderViewModel
import kotlinx.android.synthetic.main.activity_disk_folder.*
import java.util.*

/**
 * @Des: 云盘移动目录列表
 * @Author: JayGengi
 * @Date: 2020/10/21 14:37
 */
class DiskFolderActivity : BaseActivity<BaseViewModel, ActivityDiskFolderBinding>() {

    private val baseList: ArrayList<DiskBean> = ArrayList()
    private val menuList: ArrayList<DiskBean> = ArrayList()
    private val menuAdapter: DiskMenuAdapter by lazy { DiskMenuAdapter(menuList) }
    private val mAdapter: DiskFolderAdapter by lazy { DiskFolderAdapter(baseList) }
    var itemIds = ArrayList<String>()
    private var currentId = ""
    private var pid: String = ""

    //请求数据ViewModel
    private val requestDiskFolderViewModel: RequestDiskFolderViewModel by viewModels()

    override fun layoutId() = R.layout.activity_disk_folder

    override fun initView(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        if (null != bundle) {
            itemIds = bundle.getStringArrayList("item_ids")!!
            currentId = bundle.getString("current_id")!!
        } else {
            showToast("入参异常")
        }

        back_disk.clickNoRepeat { finish() }
        disk_menu_name.clickNoRepeat {
            //判斷是否二级目录
            if ("" != pid) {
                pid = ""
                menuAdapter.setList(null)
                requestDiskFolderViewModel.diskMoveList(currentId, pid)
            }
        }
        tv_save.clickNoRepeat {
            //完成
            if ("" != pid) {
                if (itemIds.contains(pid)) {
                    showToast("无法移动文件夹到该文件夹")
                } else {
                    val map: MutableMap<String, Any> = HashMap()
                    map["itemIds"] = itemIds
                    requestDiskFolderViewModel.diskFileMove(pid, map)
                }
            } else {
                showToast("请选择文件夹")
            }
        }

        recycler_menu.layoutManager =
            DuobangLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_menu.adapter = menuAdapter
        menuAdapter.setNbOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as DiskBean
            if (pid != item.id) {
                pid = item.id
                requestDiskFolderViewModel.diskBreadcrumbs(pid)
                requestDiskFolderViewModel.diskMoveList(currentId, pid)
            }
        }
        recycler.layoutManager = GridLayoutManager(this, 4)
        recycler.adapter = mAdapter
        mAdapter.setNbOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as DiskBean
            pid = item.id
            requestDiskFolderViewModel.diskBreadcrumbs(pid)
            requestDiskFolderViewModel.diskMoveList(currentId, pid)
        }
        requestDiskFolderViewModel.diskMoveList(currentId, pid)
    }

    override fun createObserver() {
        requestDiskFolderViewModel.run {
            loadDiskMoveListResult.observe(this@DiskFolderActivity, Observer { resultState ->
                parseState(resultState, {
                    baseList.clear()
                    baseList.addAll(it)
                    mAdapter.setList(baseList)
                }, {
                    showToast(it.errorMsg)
                })
            })
            loadDiskBreadcrumbsResult.observe(this@DiskFolderActivity, Observer { resultState ->
                parseState(resultState, {
                    menuAdapter.setList(it)
                }, {
                    showToast(it.errorMsg)
                })
            })
            loadDiskFileMoveResult.observe(this@DiskFolderActivity, Observer { resultState ->
                parseState(resultState, {
                    showToast("移动成功")
                    finish()
                }, {
                    showToast(it.errorMsg)
                })
            })
        }
    }
}
