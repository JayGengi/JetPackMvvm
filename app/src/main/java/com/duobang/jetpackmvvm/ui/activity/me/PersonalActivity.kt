package com.duobang.jetpackmvvm.ui.activity.me

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseActivity
import com.duobang.common.data.bean.Organization
import com.duobang.common.data.constant.REQUEST_CODE
import com.duobang.common.ext.initClose
import com.duobang.common.ext.requestPermission
import com.duobang.common.util.ActivityMessenger
import com.duobang.common.util.AppImageLoader
import com.duobang.common.util.permissions.PermissionResult
import com.duobang.common.util.picture.PictureCropUtils
import com.duobang.common.util.picture.PictureSelectUtils
import com.duobang.jetpackmvvm.R
import com.duobang.jetpackmvvm.databinding.ActivityPersonalBinding
import com.duobang.jetpackmvvm.ext.parseState
import com.duobang.jetpackmvvm.ext.util.logd
import com.duobang.jetpackmvvm.ext.util.notNull
import com.duobang.jetpackmvvm.viewmodel.request.RequestMeViewModel
import com.duobang.jetpackmvvm.viewmodel.state.PersonalViewModel
import com.yalantis.ucrop.UCrop
import com.zhihu.matisse.Matisse
import kotlinx.android.synthetic.main.activity_personal.*
import kotlinx.android.synthetic.main.include_toolbar.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * @作者　: JayGengi
 * @时间　: 2020/11/30 14:25
 * @描述　: 昵称
 */
class PersonalActivity : BaseActivity<PersonalViewModel, ActivityPersonalBinding>() {

    //请求数据ViewModel
    private val requestMeViewModel: RequestMeViewModel by viewModels()

    private lateinit var pathResult: MutableList<Uri>
    override fun layoutId() = R.layout.activity_personal

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        toolbar.initClose("个人信息") { finish() }

        appViewModel.userinfo.value!!.apply {
            AppImageLoader.displayAvatar(avatar, nickname, user_avatar_per)
            mViewModel.name.set(nickname)
            mViewModel.account.set(username)
            mViewModel.phone.set(phone)
            mViewModel.orgName.set(getHomeOrgName(appViewModel.orginfo.value!!))
            if (groupList != null && groupList!!.isNotEmpty()) {
                mViewModel.department.set(groupList!![0].groupName)
            }
            if (roleList != null && roleList!!.isNotEmpty()) {
                mViewModel.orgRole.set(roleList!![0].roleName)
            }
        }

    }

    override fun createObserver() {
        appViewModel.userinfo.observe(this, Observer { it ->
            it.notNull({
                mViewModel.name.set(it.nickname)
                AppImageLoader.displayAvatar(it.avatar, it.nickname, user_avatar_per)
            }, {})
        })

        requestMeViewModel.meData.observe(this, Observer { resultState ->
            parseState(resultState, {
                ToastUtils.showShort("修改成功")
                appViewModel.userinfo.value!!.avatar = it
            }, {
                ToastUtils.showShort(it.errorMsg)
            })
        })
    }

    /**
     * @作者　: JayGengi
     * @时间　: 2020/11/30 14:58
     * @描述　: 当前所属部门组织
     */
    private fun getHomeOrgName(org: Organization): String? {
        for (info in org.orgList!!) {
            if (info.id.equals(org.homeOrgId)) {
                return info.name
            }
        }
        return null
    }

    /**
     * @作者　: JayGengi
     * @时间　: 2020/12/6 11:38
     * @描述　: 上传头像
     */
    private fun updateHeadImg(photoPath: String) {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val file = File(photoPath)
        builder.addFormDataPart(
            "file",
            file.name,
            RequestBody.create(MediaType.parse("image/*"), file)
        )
        val body = builder.build()
        requestMeViewModel.uploadAvatarFile(body)
    }

    /**
     * @des    回调方法
     * @auther JayGengi
     * @data 2018/12/18 11:50
     * @email JayGengi@163.com
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE.SELECT_PHOTO_CODE ->
                    try {
                        if (null != Matisse.obtainPathResult(data)) {
                            pathResult = Matisse.obtainResult(data)
                            //图片裁剪
                            PictureCropUtils.cropCirclePicture(this, pathResult[0])
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                UCrop.REQUEST_CROP -> {
                    try {
                        if (data != null) {
                            updateHeadImg(UCrop.getOutput(data)!!.path!!)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    inner class ProxyClick {
        /** 用户昵称*/
        fun nickName() {
            ActivityMessenger.startActivity(this@PersonalActivity, NickNameActivity::class)
        }

        /** 用户头像*/
        fun updateAvatar() {

            requestPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ).observe(this@PersonalActivity, Observer {
                when (it) {
                    is PermissionResult.Grant -> {
                        "申请权限成功".logd("permission")
                        PictureSelectUtils.SelectSystemPhoto(
                            this@PersonalActivity,
                            REQUEST_CODE.SELECT_PHOTO_CODE,
                            1
                        )
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
    }
}