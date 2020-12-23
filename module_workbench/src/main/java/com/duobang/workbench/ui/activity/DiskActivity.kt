package com.duobang.workbench.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cc.shinichi.library.ImagePreview
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseActivity
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.DiskBean
import com.duobang.common.data.constant.IPmsConstant
import com.duobang.common.data.constant.REQUEST_CODE
import com.duobang.common.ext.requestPermission
import com.duobang.common.ext.showMessage
import com.duobang.common.ext.showToast
import com.duobang.common.util.*
import com.duobang.common.util.permissions.PermissionResult
import com.duobang.common.weight.bottomDialog.CommonDialog
import com.duobang.common.weight.recyclerview.DuobangLinearLayoutManager
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.jetpackmvvm.ext.util.logd
import com.duobang.workbench.R
import com.duobang.workbench.databinding.ActivityDiskBinding
import com.duobang.workbench.ui.adapter.DiskMenuAdapter
import com.duobang.workbench.ui.adapter.DiskMultiAdapter
import com.duobang.workbench.ui.dailog.DiskSearchDialog
import com.duobang.workbench.ui.fragment.DiskLabelDialogFragment
import com.duobang.workbench.utils.FileUtils
import com.duobang.workbench.viewmodel.request.RequestDiskViewModel
import kotlinx.android.synthetic.main.activity_disk.*
import java.io.File
import java.util.*
import kotlin.collections.HashMap

/**
 * 1.云盘管理员 -->文件夹管理管 -->成员<br/>
 * 1.1.云盘管理员  根目录的新建、编辑->配置（管理员、成员）、重命名、删除<br/>
 * 1.2.管理员     子文件夹的目录新建、文件上传、编辑->配置（管理员、成员）、重命名、删除、移动、下载<br/>
 * 1.3.成员       区别于管理员的（只对自己新建的文件夹管理）<br/>
 *
 * @Des: 云盘
 * @Author: JayGengi
 * @Date: 2020/10/13 14:09
 * @see DiskBean 类枚举说明
 */

class DiskActivity : BaseActivity<BaseViewModel, ActivityDiskBinding>(),
    DiskLabelDialogFragment.OnLabelItemClickListener {

    private var edit = false
    private var pid: String = "" //子文件夹目录，当有value时代表在子级目录

    private var chooseNum = 0

    //用户权限  -- 云盘管理员 管理员 成员
    private var userPermissions = 0
    private var userId = CacheUtil.getUser()!!.id

    private var diskFileUrlItem: DiskBean? = null
    private var diskFileUrlDown = false

    //用于下载拦截文件夹操作
    private var canDown = false
    private var canMoveOrDel = false
    private var canReName = false
    private var isResume = false
    private var dialog: DiskSearchDialog? = null

    //请求数据ViewModel
    private val requestDiskViewModel: RequestDiskViewModel by viewModels()

    private val baseList: ArrayList<DiskBean> = ArrayList()
    private val chooseList: ArrayList<DiskBean> = ArrayList()
    private val menuList: List<DiskBean> = ArrayList()
    private val menuAdapter: DiskMenuAdapter by lazy { DiskMenuAdapter(menuList) }
    private val mAdapter: DiskMultiAdapter by lazy { DiskMultiAdapter() }
    override fun layoutId() = R.layout.activity_disk

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.click = ProxyClick()

        recycler_menu.layoutManager =
            DuobangLinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler_menu.adapter = menuAdapter
        menuAdapter.setOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as DiskBean
            if (pid != item.id) {
                pid = item.id
                edit = false
                edit_disk.setIconResource(R.drawable.ic_edit)
                bottom_lay.visibility = View.GONE
                requestDiskViewModel.diskBreadcrumbs(pid)
                requestDiskViewModel.diskList(pid)
            }
        }


        recycler.layoutManager = GridLayoutManager(this, 4)
        recycler.adapter = mAdapter

        mAdapter.setOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as DiskBean
            if (edit) {
                //                    //不是云盘管理员只能操作自己的
                //                    if (null == pid && IPmsConstant.DISK.ROOT_FOLDER_DIR != userPermissions) {
                //                        if (userId.equals(item.getManager())) {
                //                            bottomUpdate(item, position);
                //                        }
                //                    } else {
                bottomUpdate(item, position)
                //                    }
            } else {
                when (item.subType) {
                    DiskBean.FOLDER -> {
                        pid = item.id
                        //判断自身userId与DiskBean中管理员managerId用户id是否一直（文件夹管理员比成员多一个新建文件夹操作）
                        //排除云盘管理员
                        if (userPermissions != IPmsConstant.DISK.ROOT_FOLDER_DIR) {
                            add_disk.visibility = View.VISIBLE
                            if (null != item.manager && "" != item.manager && userId == item.manager) {
                                userPermissions = IPmsConstant.DISK.ADMIN_FOLDER_DIR
                            } else {
                                userPermissions = IPmsConstant.DISK.MEMBER_FOLDER_DIR
                                //当非成员隐藏
                                if (!item.members.contains(userId)) {
                                    add_disk.visibility = View.GONE
                                }
                            }
                        }
                        requestDiskViewModel.diskBreadcrumbs(pid)
                        requestDiskViewModel.diskList(pid)
                    }
                    DiskBean.IMG -> {
                        val cacheImage: String = mAdapter.getImgMap()[DateUtil.getNowHour() + item.id]!!
                        if ("" != cacheImage) {
                            ImagePreview.getInstance().setContext(this@DiskActivity)
                                .setImage(cacheImage)
                                // 是否启用下拉关闭。默认不启用
                                .setEnableDragClose(true)
                                .start()
                        } else {
                            diskFileUrlItem = item
                            diskFileUrlDown = false
                            requestDiskViewModel.diskFileUrl(diskFileUrlItem!!.id!!)
                        }
                    }
                    DiskBean.FILE -> {
                        requestPermission(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ).observe(this@DiskActivity, Observer {
                            when (it) {
                                is PermissionResult.Grant -> {
                                    "申请权限成功".logd("permission")
                                    diskFileUrlItem = item
                                    diskFileUrlDown = false
                                    requestDiskViewModel.diskFileUrl(diskFileUrlItem!!.id!!)
                                }
                                is PermissionResult.Deny -> {
                                    it.permissions.forEach { per ->
                                        "拒绝的权限:${per.permissionName} 是否可以重复申请：${per.isRetryEnable}".logd("permission")
                                    }
                                    ToastUtils.showShort("权限拒绝")
                                }
                            }
                        })

                    }
                    else -> {
                    }
                }
            }
        }

        requestDiskViewModel.diskPermission()
    }

    override fun createObserver() {
        requestDiskViewModel.run {
            //检查云盘管理员权限
            loadDiskPermissionResult.observe(this@DiskActivity, Observer { resultState ->
                parseState(resultState, {
                    if (it) {
                        userPermissions = IPmsConstant.DISK.ROOT_FOLDER_DIR
                        add_disk.visibility = View.VISIBLE
                    } else {
                        add_disk.visibility = View.GONE
                    }
                    diskList(pid)
                }, {
                    showToast(it.errorMsg)
                })
            })
            //创建文件夹
            loadDiskDirResult.observe(this@DiskActivity, Observer { resultState ->
                parseState(resultState, {
                    showToast("创建成功")
                    diskListRefresh()
                }, {
                    showToast(it.errorMsg)
                })
            })
            //获取某个文件夹下的文件(文件夹列表)
            loadDiskListResult.observe(this@DiskActivity, Observer { resultState ->
                parseState(resultState, {
                    baseList.clear()
                    baseList.addAll(it)
                    mAdapter.setList(baseList)
                }, {
                    showToast(it.errorMsg)
                })
            })
            //目录重命名
            loadReNameResult.observe(this@DiskActivity, Observer { resultState ->
                parseState(resultState, {
                    showToast("修改成功")
                    diskListRefresh()
                }, {
                    showToast(it.errorMsg)
                })
            })
            //删除多个文件或者文件夹，包括服务器端和oss端
            loadDiskFileDelResult.observe(this@DiskActivity, Observer { resultState ->
                parseState(resultState, {
                    showToast("删除成功")
                    diskListRefresh()
                }, {
                    showToast(it.errorMsg)
                })
            })
            //获取当前文件夹的面包屑导航路径
            loadDiskkBreadcrumbsResult.observe(this@DiskActivity, Observer { resultState ->
                parseState(resultState, {
                    menuAdapter.setList(it)
                }, {
                    showToast(it.errorMsg)
                })
            })
            //云盘文件上传，必须有文件夹id
            loadDiskFileUpResult.observe(this@DiskActivity, Observer { resultState ->
                parseState(resultState, {
                    showToast("上传成功")
                    requestDiskViewModel.diskList(pid)
                }, {
                    showToast(it.errorMsg)
                })
            })
            //获取文件在oss上面的url，用于web前端下载或预览文件
            loadDiskFileUrlResult.observe(this@DiskActivity, Observer { resultState ->
                parseState(resultState, {
                    this@DiskActivity.diskFileUrl(it)
                }, {
                    showToast(it.errorMsg)
                })
            })
        }
    }

    private fun diskFileUrl(filePath: String) {
        if (diskFileUrlDown) {
            downLoadFile(filePath, diskFileUrlItem!!.name)
        } else {
            when (diskFileUrlItem!!.subType) {
                DiskBean.IMG -> {
                    ImagePreview.getInstance().setContext(this@DiskActivity)
                        .setImage(filePath)
                        // 是否启用下拉关闭。默认不启用
                        .setEnableDragClose(true)
                        .start()
                }
                DiskBean.FILE ->{
                    ActivityMessenger.startActivity(this,TbsReaderActivity::class,
                        "url" to filePath,
                        "file_name" to diskFileUrlItem!!.name,
                        "type" to DownloadUtil.getFileType(diskFileUrlItem!!.name))
                }

                else -> {
                }
            }
        }
    }

    /**
     * 下载文件
     *
     * @param url      下载路径
     * @param fileName 保存名字
     */
    private fun downLoadFile(url: String, fileName: String) {
        DownloadUtil.get().download(
            url,
            FileFormUtils.getPathDir(),
            fileName,
            object : DownloadUtil.OnDownloadListener {
                override fun onDownloadSuccess(file: File?) {
                    runOnUiThread {
                        showToast("下载成功,文件夹：" + FileFormUtils.getPathDir())
                    }
                }

                override fun onDownloading(progress: Int) {}
                override fun onDownloadFailed(e: java.lang.Exception) {
                    //下载异常进行相关提示操作
                    e.printStackTrace()
                    runOnUiThread { showToast("下载失败") }
                }
            })
    }

    /**
     * @作者　: JayGengi
     * @时间　: 2020/12/21 11:20
     * @描述　: 创建修改接口刷新列表
     */
    private fun diskListRefresh() {
        edit = false
        edit_disk.setIconResource(R.drawable.ic_edit)
        bottom_lay.visibility = View.GONE
        requestDiskViewModel.diskList(pid)
    }

    /**
     * @Des: 编辑操作状态
     * @Author: JayGengi
     * @Date: 2020/10/14 15:06
     */
    private fun diskEditStatus() {
        if (baseList.isNotEmpty()) {
            baseList[0].isShowEdit = edit
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun bottomUpdate(item: DiskBean, position: Int) {
        item.isEdit = !item.isEdit
        mAdapter.notifyItemChanged(position)
        bottomEditManager()
    }

    /**
     * @Des: 底部操作按钮状态管理
     * @Author: JayGengi
     * @Date: 2020/10/20 10:19
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private fun bottomEditManager() {
        chooseNum = 0
        chooseList.clear()
        if (baseList.isNotEmpty()) {
            for (i in baseList.indices) {
                if (baseList[i].isEdit) {
                    chooseNum++
                    chooseList.add(baseList[i])
                }
            }
        }
        //根目录  配置、重命名、删除(root管理员)
        //子目录  重命名、移动、下载、删除
        if ("" == pid) {
            tv_config.visibility = View.VISIBLE
            tv_down.visibility = View.GONE
            tv_move.visibility = View.GONE
            if (userPermissions == IPmsConstant.DISK.ROOT_FOLDER_DIR) {
                tv_del.visibility = View.VISIBLE
            } else {
                tv_del.visibility = View.GONE
            }
        } else {
            tv_config.visibility = View.GONE
            tv_down.visibility = View.VISIBLE
            tv_move.visibility = View.VISIBLE
            tv_del.visibility = View.VISIBLE
        }
        //云盘管理员
        //不然则动态判断文件夹是否有配置权限
        if (1 <= chooseNum) {
            if (1 == chooseNum) {
                tv_config.setTextColor(resources.getColor(R.color.black))
                if ("" != pid) {
                    canReName = false
                    canDown = true
                    if (DiskBean.FOLDER == chooseList[0].subType) {
                        //如果有目录不支持下载
                        canDown = false
                    }
                    canReName = !(IPmsConstant.DISK.MEMBER_FOLDER_DIR == userPermissions
                            && userId != chooseList[0].userId)
                    if (canDown) {
                        tv_down.setTextColor(resources.getColor(R.color.black))
                    } else {
                        tv_down.setTextColor(resources.getColor(R.color.darkgrey))
                    }
                    if (canReName) {
                        tv_re_name.setTextColor(resources.getColor(R.color.black))
                    } else {
                        tv_re_name.setTextColor(resources.getColor(R.color.darkgrey))
                    }
                } else {
                    var per = userPermissions
                    if (per != IPmsConstant.DISK.ROOT_FOLDER_DIR) {
                        per = if (userId == chooseList[0].manager) {
                            IPmsConstant.DISK.ADMIN_FOLDER_DIR
                        } else {
                            IPmsConstant.DISK.MEMBER_FOLDER_DIR
                        }
                    }
                    if (IPmsConstant.DISK.MEMBER_FOLDER_DIR == per) {
                        //不能改名 是成员(根目录)
                        canReName = false
                        tv_re_name.setTextColor(resources.getColor(R.color.darkgrey))
                    } else {
                        //不能改名 是成员 且不是自己userId
                        canReName = true
                        tv_re_name.setTextColor(resources.getColor(R.color.black))
                    }
                }
            } else {
                canReName = false
                canDown = false
                tv_config.setTextColor(resources.getColor(R.color.darkgrey))
                tv_down.setTextColor(resources.getColor(R.color.darkgrey))
                tv_re_name.setTextColor(resources.getColor(R.color.darkgrey))
            }
            canMoveOrDel = true
            for (i in chooseList.indices) {
                if (IPmsConstant.DISK.MEMBER_FOLDER_DIR == userPermissions
                    && userId != chooseList[i].userId
                ) {
                    //不能改名 是成员 且不是自己userId
                    canMoveOrDel = false
                    break
                }
            }
            if (canMoveOrDel) {
                tv_move.setTextColor(resources.getColor(R.color.black))
                tv_del.setTextColor(resources.getColor(R.color.black))
            } else {
                tv_move.setTextColor(resources.getColor(R.color.darkgrey))
                tv_del.setTextColor(resources.getColor(R.color.darkgrey))
            }
        } else {
            tv_config.setTextColor(resources.getColor(R.color.darkgrey))
            tv_re_name.setTextColor(resources.getColor(R.color.darkgrey))
            tv_move.setTextColor(resources.getColor(R.color.darkgrey))
            tv_down.setTextColor(resources.getColor(R.color.darkgrey))
            tv_del.setTextColor(resources.getColor(R.color.darkgrey))
        }
    }

    /**
     * 顶部搜索框
     */
    private fun showDiskSearchDialog() {
        if (null == dialog) {
            dialog = DiskSearchDialog(this@DiskActivity)
        }
        dialog!!.show()
    }

    private var rightType = ""

    /**
     * 展示创建云盘目录或文件的dialog
     *
     * @param index 0 新建文件夹  1 重命名
     */
    private fun showCreateMeetingDialog(index: Int, disk: DiskBean?) {
        val view =
            View.inflate(this@DiskActivity, R.layout.dialog_disk_file, null)
        val title = view.findViewById<TextView>(R.id.title)
        val cancel = view.findViewById<TextView>(R.id.cancel_dialog)
        val ok = view.findViewById<TextView>(R.id.ok_dialog)
        val name = view.findViewById<EditText>(R.id.input_name_disk_file_dialog)
        name.addTextChangedListener(CommentTextWatcher(ok))
        if (1 == index) {
            title.text = "重命名"
            var leftName = ""
            val diskName = disk?.name
            if (DiskBean.FOLDER != disk?.subType) {
                try {
                    // 截取后面的字符串：
                    leftName = diskName!!.substring(0, diskName.lastIndexOf("."))
                    // 截取前面的字符串：
                    rightType = diskName.substring(diskName.lastIndexOf("."))
                } catch (e: Exception) {
                    leftName = diskName!!
                    rightType = ""
                }
            } else {
                rightType = ""
                leftName = diskName!!
            }
            name.setText(leftName)
            name.setSelection(leftName.length)
        }
        val dialog = CommonDialog(this, view, R.style.view_dialog)
        dialog.show()
        cancel.setOnClickListener { dialog.cancel() }
        ok.setOnClickListener {
            val names = name.text.toString().trim { it <= ' ' }
            if (names.isNotEmpty()) {
                val map: MutableMap<String, Any> =
                    HashMap()
                if (0 == index) {
                    map["dirname"] = names
                    map["pid"] = pid
                    requestDiskViewModel.diskDir(map)
                } else if (1 == index) {
                    map["name"] = names + rightType
                    requestDiskViewModel.diskFileReName(disk!!.id, map)
                }
            } else {
                showToast("名称为必填项！！")
            }
            dialog.cancel()
        }
    }

    private class CommentTextWatcher(var button: TextView) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(
            s: CharSequence, start: Int, before: Int, count: Int
        ) {
            if (s.isNotEmpty()) {
                button.setTextColor(Color.parseColor("#298eff"))
            } else {
                button.setTextColor(Color.parseColor("#999999"))
            }
        }

        override fun afterTextChanged(s: Editable) {}

    }

    private fun showDeleteDialog(itemIds: List<String>) {
        val map: MutableMap<String, Any> = HashMap()
        map["itemIds"] = itemIds
        showMessage(
            "您确定要删除所选中？删除后将无法找回",
            "删除",
            "确定",
            { requestDiskViewModel.diskFileDel(map) },
            "取消"
        )
    }

    /**
     * @param index 0 新建文件夹  1 创建文件
     */
    override fun onLabelItemClick(index: Int, label: String?) {
        if (0 == index) {
            showCreateMeetingDialog(index, null)
        } else if (1 == index) {
            //创建文件
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            startActivityForResult(intent, REQUEST_CODE.CHOOSE_FILE)
        }
    }

    inner class ProxyClick {
        fun backDisk() {
            finish()
        }

        fun searchDisk() {
            showDiskSearchDialog()
        }

        fun editDisk() {
            if (edit) {
                //完成 status
                edit = false
                edit_disk.setIconResource(R.drawable.ic_edit)
                bottom_lay.visibility = View.GONE
            } else {
                //编辑 status
                edit = true
                edit_disk.setIconResource(R.drawable.ic_error)
                bottom_lay.visibility = View.VISIBLE
            }
            diskEditStatus()
            bottomEditManager()
        }

        fun addDisk() {
            //根目录（云盘管理员【新建文件夹】）
            //子目录（云盘管理员、管理员【新建文件夹】【添加文件】、成员【添加文件】）
            DiskLabelDialogFragment.newInstance(pid, userPermissions)
                .show(supportFragmentManager, "dialog")
        }

        fun menuDisk() {
            //判斷是否二级目录
            if ("" != pid) {
                edit = false
                edit_disk.setIconResource(R.drawable.ic_edit)
                bottom_lay.visibility = View.GONE
                pid = ""
                menuAdapter.setList(null)
                requestDiskViewModel.diskPermission()
            }
        }

        fun configDisk() {
            //1.根目录只有文件夹情况
            //2.带有文件排版需要判断批量操作是否有文件，则不能进行批量配置
            if (1 == chooseNum) {
                isResume = true
                val bundle = Bundle()
                //判断选中去配置页的用户角色
                var per = userPermissions
                if (per != IPmsConstant.DISK.ROOT_FOLDER_DIR) {
                    per = if (userId == chooseList[0].manager) {
                        IPmsConstant.DISK.ADMIN_FOLDER_DIR
                    } else {
                        IPmsConstant.DISK.MEMBER_FOLDER_DIR
                    }
                }
                bundle.putInt("user_permissions", per)
                bundle.putString(
                    "create_time",
                    DateUtil.formatDate(chooseList[0].createAt)
                )
                bundle.putParcelable("disk_info", chooseList[0])
                startActivity(
                    Intent(this@DiskActivity, DiskConfigActivity::class.java).putExtras(
                        bundle
                    )
                )
            }
        }

        fun renameDisk() {
            //重命名支持单个操作
            if (1 == chooseNum) {
                if ("" != pid && !canReName) {
                    return
                }
                showCreateMeetingDialog(1, chooseList[0])
            }
        }

        fun moveDisk() {
            //跟删除的权限是一样的owner方可操作
            if (0 < chooseNum) {
                if ("" != pid && !canMoveOrDel) {
                    return
                }
                val itemIds =
                    ArrayList<String>()
                for (i in chooseList.indices) {
                    itemIds.add(chooseList[i].id)
                }
                isResume = true
                val bundle = Bundle()
                bundle.putString("current_id", pid)
                bundle.putStringArrayList("item_ids", itemIds)
                startActivity(
                    Intent(this@DiskActivity, DiskFolderActivity::class.java).putExtras(
                        bundle
                    )
                )
            }
        }

        fun downDisk() {
            //看到皆可down(非文件夹)
            if (0 < chooseNum) {
                if (canDown) {
                    diskFileUrlItem = chooseList[0]
                    diskFileUrlDown = true
                    requestDiskViewModel.diskFileUrl(diskFileUrlItem!!.id!!)
                }
            }
        }

        fun delDisk() {
            //删除需要是文件的owner方可操作
            if (1 <= chooseNum) {
                if ("" != pid && !canMoveOrDel) {
                    return
                }
                val itemIds: MutableList<String> =
                    ArrayList()
                for (i in chooseList.indices) {
                    itemIds.add(chooseList[i].id)
                }
                showDeleteDialog(itemIds)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE.CHOOSE_FILE -> {
                if (resultCode != Activity.RESULT_OK || data == null) {
                    return
                }
                val uri = data.data
                val filePath: String = UriToFileUtils.getFileAbsolutePath(this, uri)
                val file = File(filePath)
                //大于10m拦截不给予上传
                try {
                    if (FileUtils.formetFileSize(FileUtils.getFileSize(file))) {
                        LogUtils.d("filePath", filePath)
                        if (filePath != null) {
                            requestDiskViewModel.diskFileUp(pid, filePath)
                        }
                    } else {
                        showToast("文件太大不支持上传")
                    }
                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                }
            }
            else -> {
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isResume) {
            isResume = false
            edit = false
            edit_disk.setIconResource(R.drawable.ic_edit)
            bottom_lay.visibility = View.GONE
            requestDiskViewModel.diskList(pid)
        }
    }
}
