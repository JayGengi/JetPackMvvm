package com.duobang.project.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.blankj.utilcode.util.BarUtils
import com.chad.library.adapter.base.entity.node.BaseNode
import com.duobang.common.base.BaseFragment
import com.duobang.common.data.bean.Structure
import com.duobang.common.data.bean.StructureGroup
import com.duobang.common.data.constant.RouterConstant
import com.duobang.common.ext.init
import com.duobang.common.ext.loadServiceInit
import com.duobang.common.ext.setErrorText
import com.duobang.common.weight.loadCallBack.ErrorCallback
import com.duobang.base.ext.parseState
import com.duobang.project.R
import com.duobang.project.databinding.FragmentProjectBinding
import com.duobang.project.ui.adapter.ProjectNodeAdapter
import com.duobang.project.viewmodel.request.RequestProjectViewModel
import com.duobang.project.viewmodel.state.ProjectViewModel
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.fragment_project.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/6 17:21
 * @描述　: 工程
 */
@Route(path = RouterConstant.FRAG.PROJECT)
class ProjectFragment : BaseFragment<ProjectViewModel, FragmentProjectBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //适配器
    private val baseAdapter: ProjectNodeAdapter by lazy { ProjectNodeAdapter() }

    private val requestProViewModel: RequestProjectViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_project

    override fun initView(savedInstanceState: Bundle?) {

        val layoutParams = proBar.layoutParams as LinearLayout.LayoutParams
        layoutParams.height = BarUtils.getStatusBarHeight() //这里拿到状态栏的高度
        proBar.layoutParams = layoutParams//设置view高度
        toolbar.init("工程")
        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            lazyLoadData()
        }

        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), baseAdapter)

        // 顶部header
        val view: View = layoutInflater.inflate(R.layout.project_header, recyclerView, false)
        baseAdapter.addHeaderView(view)

        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestProViewModel.getProjectData()
        }
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        requestProViewModel.getProjectData()
    }

    override fun createObserver() {
        requestProViewModel.projectDataState.observe(viewLifecycleOwner, Observer { data ->
            parseState(data, {
                loadsir.showSuccess()
                swipeRefresh.isRefreshing = false

                val list: ArrayList<BaseNode> = ArrayList()
                for (i in it.indices) {
                    val secondList: ArrayList<BaseNode> = ArrayList()
                    for (y in it[i].structures!!.indices) {
                        val secondNode: Structure = it[i].structures!![y]
                        secondList.add(secondNode)
                    }
                    val firstNode: StructureGroup = it[i]
                    firstNode.isExpanded = firstNode.structures!!.size > 0
                    firstNode.childNode =secondList
                    list.add(firstNode)
                }
                baseAdapter.setList(list)

            }, {
                swipeRefresh.isRefreshing = false
                loadsir.showCallback(ErrorCallback::class.java)
                loadsir.setErrorText(it.errorMsg)
            })
        })

    }

}