package com.duobang.jetpackmvvm.ui.fragment.project

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.entity.node.BaseNode
import com.duobang.jetpackmvvm.R
import com.duobang.jetpackmvvm.base.BaseFragment
import com.duobang.jetpackmvvm.base.viewmodel.BaseViewModel
import com.duobang.jetpackmvvm.data.bean.Structure
import com.duobang.jetpackmvvm.data.bean.StructureGroup
import com.duobang.jetpackmvvm.databinding.FragmentProjectBinding
import com.duobang.jetpackmvvm.ext.*
import com.duobang.jetpackmvvm.ui.adapter.project.ProjectNodeAdapter
import com.duobang.jetpackmvvm.viewmodel.request.RequestProjectViewModel
import com.duobang.jetpackmvvm.weight.loadCallBack.ErrorCallback
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*

/**
 * @作者　: JayGengi
 * @时间　: 2020/12/6 17:21
 * @描述　: 工程
 */
class ProjectFragment : BaseFragment<BaseViewModel, FragmentProjectBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //适配器
    private val baseAdapter: ProjectNodeAdapter by lazy { ProjectNodeAdapter() }

    private val requestProViewModel: RequestProjectViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_project

    override fun initView(savedInstanceState: Bundle?) {

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
        loadsir.showLoading()
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
                    for (y in it[i].structures.indices) {
                        val secondNode: Structure = it[i].structures[y]
                        secondList.add(secondNode)
                    }
                    val firstNode: StructureGroup = it[i]
                    firstNode.isExpanded = firstNode.structures!!.size > 0
                    firstNode.setChildNode(secondList)
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