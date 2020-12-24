package com.duobang.workbench.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.ToastUtils
import com.duobang.common.base.BaseActivity
import com.duobang.base.base.viewmodel.BaseViewModel
import com.duobang.common.data.bean.User
import com.duobang.common.data.constant.IWorkbenchConstant
import com.duobang.common.data.constant.REQUEST_CODE
import com.duobang.common.data.constant.RouterConstant
import com.duobang.common.ext.*
import com.duobang.common.util.JsonUtil
import com.duobang.base.ext.parseState
import com.duobang.base.ext.view.afterTextChange
import com.duobang.base.ext.view.clickNoRepeat
import com.duobang.workbench.R
import com.duobang.workbench.databinding.ActivityUserChooseBinding
import com.duobang.workbench.ui.adapter.SingleUserAdapter
import com.duobang.workbench.viewmodel.request.RequestOrgGroupViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.activity_user_choose.*
import kotlinx.android.synthetic.main.include_workbench_list_no_refresh.*
import kotlinx.android.synthetic.main.include_workbench_recyclerview_no_refresh.*
import org.json.JSONException
import java.util.*

/**     
  * @作者　: JayGengi
  * @时间　: 2020/12/21 16:20
  * @描述　: 选择当前组织下的用户
 */
@Route(path = RouterConstant.ACT.CHOOSE_USER)
class ChooseUserActivity : BaseActivity<BaseViewModel, ActivityUserChooseBinding>() {

    private var users: ArrayList<User> = ArrayList()
    private var rootData: ArrayList<User> = ArrayList()
    private var isSingle = false
    private var singleUser: User = User()
    private var multipleList: ArrayList<User> = ArrayList()
    private var selected: ArrayList<User> = ArrayList()
    private var requestCode = 0
    private val adapter: SingleUserAdapter by lazy { SingleUserAdapter(arrayListOf()) }
    //请求数据ViewModel
    private val requestOrgGroupViewModel: RequestOrgGroupViewModel by viewModels()
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    private var orgId = ""
    override fun layoutId() = R.layout.activity_user_choose

    override fun initView(savedInstanceState: Bundle?) {
        orgId = appViewModel.orginfo.value!!.homeOrgId!!

        val bundle = intent.extras
        if(null != bundle){
            isSingle = bundle.getBoolean(IWorkbenchConstant.USER.IS_SINGLE, false)
            val json = bundle.getString(IWorkbenchConstant.USER.CHOOSE_LIST)
            requestCode = bundle.getInt(IWorkbenchConstant.USER.REQUSET_CODE, -1)
            if (json != null && "" != json) {
                if (isSingle) {
                    singleUser = JsonUtil.toObj(json, User::class.java)
                    selected.add(singleUser)
                } else {
                    try {
                        multipleList = JsonUtil.toList(json, User::class.java)
                        selected.addAll(multipleList)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }
        //状态页配置
        loadsir = loadServiceInit(recyclerView) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestOrgGroupViewModel.getOrgGroupUserWrapper(orgId,true)
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(this), adapter).initFloatBtn(floatbtn)
        adapter.setNbOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as User
            val isSelected: Boolean = item.isSelected
            mathSelected(item)
            syncRootData(item)
            if (isSelected) {
                users[position].isSelected = false
            } else if (isSingle) {
                handleSingle(position, users)
            } else {
                handleMultiple(position, users)
            }
            adapter.notifyDataSetChanged()
        }

        back_user_choose.clickNoRepeat { finish() }
        commit_user_choose.clickNoRepeat {
            forBack()
        }
        search_user_choose.afterTextChange {
            val searchInfo: String = search_user_choose.text.toString()
            val searchList: MutableList<User> = ArrayList()
            for (i in rootData.indices) {
                if (rootData[i].nickname!!.contains(searchInfo)) {
                    searchList.add(rootData[i])
                }
            }
            if (searchInfo.isNotEmpty()) {
                updateListInfo(searchList)
            } else {
                updateListInfo(rootData)
            }
        }
        loadsir.showLoading()
        requestOrgGroupViewModel.getOrgGroupUserWrapper(orgId,true)
    }


    override fun createObserver() {
        requestOrgGroupViewModel.orgGroupUserResult.observe(
            this,
            Observer { resultState ->
                parseState(resultState, {
                    loadsir.showSuccess()
                    //设置部门头
                    users.clear()
                    users.addAll(it.userList!!)
                    rootData.clear()
                    rootData.addAll(it.userList!!)
                    mathSelected()
                    adapter.setList(it.userList!!)
                }, {
                    ToastUtils.showShort(it.errorMsg)
                    loadsir.showError(it.errorMsg)
                })
            })
    }

    private fun mathSelected() {
        if (isSingle) {
            if (singleUser != null) {
                for (i in users.indices) {
                    if (users[i].id == singleUser.id) {
                        users[i].isSelected =true
                    }
                }
            }
        } else {
            if (multipleList != null) {
                for (i in multipleList.indices) {
                    for (j in users.indices) {
                        if (multipleList[i].id == users[j].id) {
                            users[j].isSelected =true
                        }
                    }
                }
            }
        }
    }
    private fun mathSelected(user: User) {
        if (user.isSelected) {
            for (i in selected.indices) {
                if (selected[i].id == user.id) {
                    selected.removeAt(i)
                    return
                }
            }
        } else {
            if (isSingle) {
                selected.clear()
            }
            selected.add(user)
        }
    }
    private fun syncRootData(user: User) {
        for (i in rootData.indices) {
            if (isSingle) {
                rootData[i].isSelected = false
            }
            if (rootData[i].id == user.id) {
                rootData[i].isSelected = !user.isSelected
            }
        }
    }
    private fun handleMultiple(position: Int, data: List<User>) {
        data[position].isSelected = true
    }

    private fun handleSingle(position: Int, data: List<User>) {
        for (i in data.indices) {
            data[i].isSelected = i == position
        }
    }


    private fun updateListInfo(list: List<User>) {
        users.clear()
        users.addAll(list)
        adapter.notifyDataSetChanged()
    }
    private fun forBack() {
        val intent = Intent()
        intent.putExtra(IWorkbenchConstant.USER.IS_SINGLE, isSingle)
        if (selected.size > 0) {
            intent.putExtra(IWorkbenchConstant.USER.CHOOSE_USER, JsonUtil.toJson(selected))
        }
        when (requestCode) {
            REQUEST_CODE.CHOOSE_USER -> setResult(REQUEST_CODE.CHOOSE_USER, intent)
            REQUEST_CODE.CHOOSE_APPROVER -> setResult(REQUEST_CODE.CHOOSE_APPROVER, intent)
            REQUEST_CODE.CHOOSE_CC -> setResult(REQUEST_CODE.CHOOSE_CC, intent)
            REQUEST_CODE.CHOOSE_APPLICANT -> setResult(REQUEST_CODE.CHOOSE_APPLICANT, intent)
            REQUEST_CODE.CHOOSE_OPERATOR -> setResult(REQUEST_CODE.CHOOSE_OPERATOR, intent)
        }
        finish()
    }


}
