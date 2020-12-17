package com.duobang.jetpackmvvm.ui.fragment.home

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.duobang.common.base.BaseFragment
import com.duobang.common.ext.*
import com.duobang.common.weight.recyclerview.DefineLoadMoreView
import com.duobang.common.weight.recyclerview.SpaceItemDecoration
import com.duobang.jetpackmvvm.R
import com.duobang.jetpackmvvm.databinding.FragmentHomeBinding
import com.duobang.jetpackmvvm.ui.adapter.RecordAdapter
import com.duobang.jetpackmvvm.viewmodel.request.RequestHomeViewModel
import com.duobang.jetpackmvvm.viewmodel.state.HomeViewModel
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*

/**
 * 作者　: JayGengi
 * 时间　: 2020/11/11
 * 描述　: 首頁（总览）
 */
class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    //适配器
    private val mRecordAdapter : RecordAdapter by lazy {
        RecordAdapter(
            arrayListOf()
        )
    }

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //recyclerview的底部加载view 因为在首页要动态改变他的颜色，所以加了他这个字段
    private lateinit var footView: DefineLoadMoreView

    //请求数据ViewModel
    private val requestHomeViewModel: RequestHomeViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_home

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestHomeViewModel.loadDashboardQuota()
            requestHomeViewModel.loadRecordList(true)
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), mRecordAdapter).let {
            //因为首页要添加轮播图，所以我设置了firstNeedTop字段为false,即第一条数据不需要设置间距
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                requestHomeViewModel.loadRecordList(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestHomeViewModel.loadRecordList(true)
        }
    }

    /**
     * 懒加载
     */
    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        //请求轮播图数据
        requestHomeViewModel.loadDashboardQuota()
        //请求文章列表数据
        requestHomeViewModel.loadRecordList(true)
    }

    override fun createObserver() {
        requestHomeViewModel.run {
            //监听首页文章列表请求的数据变化
            recordListData.observe(viewLifecycleOwner, Observer {
                //设值 新写了个拓展函数，搞死了这个恶心的重复代码
                loadListData(
                    it,
                    mRecordAdapter,
                    loadsir,
                    recyclerView,
                    swipeRefresh
                )
            })
//            //监听轮播图请求的数据变化
//            bannerData.observe(viewLifecycleOwner, Observer { resultState ->
//                parseState(resultState, { data ->
//                    //请求轮播图数据成功，添加轮播图到headview ，如果等于0说明没有添加过头部，添加一个
//                    if (recyclerView.headerCount == 0) {
//                        val headview = LayoutInflater.from(context).inflate(R.layout.include_banner, null).apply {
//                                    findViewById<BannerViewPager<BannerResponse, HomeBannerViewHolder>>(R.id.banner_view).apply {
//                                        adapter = HomeBannerAdapter()
//                                        setLifecycleRegistry(lifecycle)
//                                        setOnPageClickListener {
//                                            nav().navigateAction(R.id.action_to_webFragment, Bundle().apply {putParcelable("bannerdata", data[it])})
//                                        }
//                                        create(data)
//                                    }
//                                }
//                        recyclerView.addHeaderView(headview)
//                    }
//                })
//            })
        }
    }
}