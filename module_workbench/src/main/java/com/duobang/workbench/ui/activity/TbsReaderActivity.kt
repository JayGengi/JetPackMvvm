package com.duobang.workbench.ui.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.util.Log
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.alibaba.android.arouter.facade.annotation.Route
import com.duobang.common.base.BaseActivity
import com.duobang.common.base.viewmodel.BaseViewModel
import com.duobang.common.data.constant.RouterConstant
import com.duobang.common.ext.initClose
import com.duobang.common.ext.loadServiceInit
import com.duobang.common.ext.showError
import com.duobang.common.ext.showToast
import com.duobang.common.util.DownloadUtil
import com.duobang.common.util.DownloadUtil.OnDownloadListener
import com.duobang.common.util.FileFormUtils
import com.duobang.workbench.R
import com.duobang.workbench.databinding.ActivityTbsReaderBinding
import com.duobang.workbench.utils.FileUtils
import com.kingja.loadsir.core.LoadService
import com.tencent.smtt.sdk.QbSdk
import com.tencent.smtt.sdk.TbsReaderView
import kotlinx.android.synthetic.main.activity_tbs_reader.*
import kotlinx.android.synthetic.main.include_workbench_toolbar.*
import java.io.File

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/21 13:59
 * @描述　: tbs内核浏览
 */
@Route(path = RouterConstant.ACT.TBS_READER)
class TbsReaderActivity : BaseActivity<BaseViewModel, ActivityTbsReaderBinding>(), TbsReaderView.ReaderCallback {
    var downloadUtil: DownloadUtil? = null
    var tbsReaderView: TbsReaderView? = null
    private var officeUrl = ""
    private var officeType = ""
    private var officeSaveName = ""
    private val filePath = FileFormUtils.getPathDir()
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    private var openFile: File? = null
    private var otherApp = false
    override fun layoutId(): Int {
        return R.layout.activity_tbs_reader
    }

    override fun initView(savedInstanceState: Bundle?) {
        val bundle = intent.extras
        if(null != bundle){
            officeUrl = bundle.getString("url")!!
            officeType = bundle.getString("type")!!
            officeSaveName = bundle.getString("file_name")!!
        }else{
            showToast("参数参数")
            finish()
        }

        toolbar.initClose(officeSaveName){finish()}
        //状态页配置
        loadsir = loadServiceInit(li_root) {
            //点击重试时触发的操作
            if (otherApp) {
                otherApp = false
                openFile(openFile)
            }
        }
        //X5的WebView用动态创建，当内核准备好时，在创建，可能是异步也可能是同步操作
        createWebView()
    }

    private fun createWebView() {
        tbsReaderView = TbsReaderView(this, this)
        li_root!!.addView(
            tbsReaderView,
            RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        downloadUtil = DownloadUtil.get()
        downLoadFile()
    }


    /**
     * 下载文件
     */
    private fun downLoadFile() {
        DownloadUtil.get().download(
            officeUrl,
            filePath,
            officeSaveName,
            object : OnDownloadListener {
                override fun onDownloadSuccess(file: File) {
                    loadsir.showSuccess()
                    runOnUiThread { showOffice(file) }
                }

                override fun onDownloading(progress: Int) {}
                override fun onDownloadFailed(e: Exception) {
                    //下载异常进行相关提示操作
                    e.printStackTrace()
                    loadsir.showError("下载附件失败")
                }
            })
    }

    /**
     * 加载文件
     */
    private fun showOffice(file: File) {
        val bsReaderTempFile = File(filePath)
        if (!bsReaderTempFile.exists()) {
            val mkdir = bsReaderTempFile.mkdir()
            if (!mkdir) {
                Log.d("print", "创建/pms失败！！！！！")
            }
        }
        //加载文件
        val localBundle = Bundle()
        localBundle.putString("filePath", file.toString())
        localBundle.putString(
            "tempPath",
            filePath
        )
        if (tbsReaderView == null) {
            tbsReaderView = tbsView
        }
        val result =
            tbsReaderView!!.preOpen(DownloadUtil.getFileType(file.toString()), false)
        //        boolean result = tbsReaderView.preOpen(officeType, false);
        if (result) {
            tbsReaderView!!.openFile(localBundle)
        } else {
            //错误页面
            openFile = file
            otherApp = true
            loadsir.showError("文件不识别！其他应用打开")
            QbSdk.clearAllWebViewCache(this, true)
        }
    }

    private fun openFile(file: File?) {
        try {
            val intent = Intent()
            // 这是比较流氓的方法，绕过7.0的文件权限检查
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val builder = VmPolicy.Builder()
                StrictMode.setVmPolicy(builder.build())
            }

            //        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//设置标记
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.action = Intent.ACTION_VIEW //动作，查看
            intent.setDataAndType(
                Uri.fromFile(file),
                FileUtils.getMIMEType(file.toString())
            ) //设置类型
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            loadsir.showError("文件不识别！其他应用打开")
        }
    }

    private fun grantUriPermission(fileUri: Uri, intent: Intent) {
        val resInfoList =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        for (resolveInfo in resInfoList) {
            val packageName = resolveInfo.activityInfo.packageName
            grantUriPermission(
                packageName,
                fileUri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

    private val tbsView: TbsReaderView
        private get() = TbsReaderView(this, this)

    override fun onCallBackAction(
        integer: Int,
        o: Any,
        o1: Any
    ) {
    }

    override fun onDestroy() {
        super.onDestroy()
        tbsReaderView!!.onStop()
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1006 -> finish()
            else -> {
            }
        }
    }
}