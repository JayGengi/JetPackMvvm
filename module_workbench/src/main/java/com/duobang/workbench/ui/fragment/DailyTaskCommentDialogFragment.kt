package com.duobang.workbench.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duobang.common.data.bean.DailyComment
import com.duobang.common.data.bean.DailyTaskWrapper
import com.duobang.common.ext.init
import com.duobang.common.ext.setNbOnItemChildClickListener
import com.duobang.common.ext.showToast
import com.duobang.common.room.repository.PmsRepository
import com.duobang.common.util.AppImageLoader
import com.duobang.common.weight.bottomDialog.BaseBottomDialogFragment
import com.duobang.common.weight.customview.AvatarView
import com.duobang.jetpackmvvm.state.ResultState
import com.duobang.workbench.R
import com.duobang.workbench.ui.adapter.DailyCommentAdapter
import com.duobang.workbench.viewmodel.request.RequestDailyTaskViewModel
import com.google.android.material.button.MaterialButton

/**     
  * @作者　: JayGengi
  * @时间　: 2020/12/18 14:10
  * @描述　: 日事日毕评论列表BottomSheet
 */
class DailyTaskCommentDialogFragment : BaseBottomDialogFragment() {
    private var wrapper: DailyTaskWrapper? = null
    private var position = 0
    private var avatar: AvatarView? = null
    private var name: TextView? = null
    private var date: TextView? = null
    private var mRecyclerView: RecyclerView? = null
    private var comments: List<DailyComment>? = null
    private lateinit var input: EditText
    private var delPos = 0
    private var onCommentChangedListener: OnCommentChangedListener? = null
    private val mAdapter: DailyCommentAdapter by lazy { DailyCommentAdapter(arrayListOf()) }
    //请求数据ViewModel
    private val requestDailyTaskViewModel: RequestDailyTaskViewModel by viewModels()
    companion object {
        fun newInstance(wrapper: DailyTaskWrapper,position: Int): DailyTaskCommentDialogFragment {
            val fragment = DailyTaskCommentDialogFragment()
            fragment.wrapper = wrapper
            fragment.position = position
            fragment.comments = wrapper.comments
            return fragment
        }
    }
    fun getLayoutId() : Int = R.layout.fragment_daily_task_comment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(getLayoutId(), container, false)

        avatar = rootView.findViewById(R.id.avatar_daily_task_comment)
        name = rootView.findViewById(R.id.name_daily_task_comment)
        date = rootView.findViewById(R.id.date_daily_task_comment)
        mRecyclerView = rootView.findViewById(R.id.list_daily_task_comment)
        input = rootView.findViewById(R.id.input_daily_task_comment)
        val send: MaterialButton = rootView.findViewById(R.id.send_daily_task_comment)
        send.isEnabled = false
        send.setOnClickListener {
            commitComment(input.text.toString())
            input.setText("")
        }
        input.addTextChangedListener(CommentTextWatcher(send))
        initData()
        createObserver()
        return rootView
    }
    /**
     * 底部弹窗的高度
     * 子类可以自己实现，定义高度
     */
    override fun getDialogHeight(context: Context) = (context.resources.displayMetrics.heightPixels * 0.6).toInt()

    private fun initData() {
        val createUser = context?.let { PmsRepository(it).getUserById(wrapper!!.creatorId!!) }
        if (createUser != null) {
            AppImageLoader.displayAvatar(
                createUser.avatar,createUser.nickname, avatar)
            name?.text = createUser.nickname
            date?.text =wrapper!!.formatDate
        }
        setupRecyclerView(wrapper!!.comments!!)
    }

    private fun setupRecyclerView(dailyCommentList: List<DailyComment>) {
        //初始化recyclerView
        mRecyclerView?.init(LinearLayoutManager(context), mAdapter)
        mAdapter.setList(dailyCommentList)

        mAdapter.addChildClickViewIds(R.id.delete_daily_comment_item)
        mAdapter.setNbOnItemChildClickListener { adapter, view, position ->
            val info: DailyComment = adapter.getItem(position) as DailyComment
            //删除自己的
            if (view.id == R.id.delete_daily_comment_item) {
                delPos = position
                requestDailyTaskViewModel.deleteDailyComment(info.id!!)
            }
        }
    }

    /**
     * 发表评论
     *
     * @param comment
     */
    private fun commitComment(comment: String) {
        val map = HashMap<String,Any>()
        map["comment"] = comment
        requestDailyTaskViewModel.uploadDailyComment(wrapper!!.id,map)
    }


    private fun createObserver() {
        requestDailyTaskViewModel.run {
            //删除一条
            loadDeleteDailyComment.observe(
                this@DailyTaskCommentDialogFragment,
                Observer { resultState ->
                    when (resultState) {
                        is ResultState.Success -> {
                            mAdapter.removeAt(delPos)
                            onCommentChangedListener?.onCommentChanged(position, mAdapter.data)
                            showToast("删除成功")
                        }
                        is ResultState.Error -> {
                            showToast(resultState.error.errorMsg)
                        }
                    }
                })
            //评论一条
            loadUploadDailyComment.observe(
                this@DailyTaskCommentDialogFragment,
                Observer { resultState ->
                    when (resultState) {
                        is ResultState.Success -> {
                            val info = resultState.data
                            wrapper = info
                            comments = info.comments
                            mAdapter.setList(comments)
                            mRecyclerView!!.scrollToPosition(mAdapter.itemCount -1)
                            if (onCommentChangedListener != null) {
                                comments?.let { onCommentChangedListener?.onCommentChanged(position, it) }
                            }
                        }
                        is ResultState.Error -> {
                            showToast(resultState.error.errorMsg)
                        }
                    }
                })
        }
    }
    override fun onDetach() {
        onCommentChangedListener = null
        super.onDetach()
    }
    private class CommentTextWatcher(var button: MaterialButton) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            button.isEnabled = s.isNotEmpty()
        }

        override fun afterTextChanged(s: Editable) {}

    }

    interface OnCommentChangedListener {
        fun onCommentChanged(
            position: Int,
            dailyCommentList: List<DailyComment>
        )
    }
}