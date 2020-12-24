package com.duobang.workbench.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duobang.common.common.SimpleArrayFactory.createDiskLabels
import com.duobang.common.ext.setNbOnItemClickListener
import com.duobang.workbench.R
import com.duobang.workbench.ui.adapter.DiskSampleLineAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DiskLabelDialogFragment : BottomSheetDialogFragment() {

    private var onLabelItemClickListener: OnLabelItemClickListener? = null
    private var userPermissions = 0
    private var pid: String = ""
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val parent = parentFragment
        onLabelItemClickListener = if (parent != null) {
            parent as OnLabelItemClickListener?
        } else {
            context as OnLabelItemClickListener
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notice_label_dialog_list_dialog, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        val recyclerView = view as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        val labels = createDiskLabels(pid, userPermissions)
        val dialogAdapter = DiskSampleLineAdapter(labels)
        recyclerView.adapter = dialogAdapter
        dialogAdapter.setNbOnItemClickListener { adapter, view, position ->
            val item = adapter.getItem(position) as String
            if (onLabelItemClickListener != null) {
                var index = -1
                if ("新建文件夹" == item) {
                    index = 0
                } else if ("添加文件" == item) {
                    index = 1
                }
                onLabelItemClickListener!!.onLabelItemClick(index, item)
                dismiss()
            }
        }
    }

    interface OnLabelItemClickListener {
        fun onLabelItemClick(index: Int, label: String?)
    }

    companion object {
        fun newInstance(pid: String, userPermissions: Int): DiskLabelDialogFragment {
            val fragment = DiskLabelDialogFragment()
            fragment.userPermissions = userPermissions
            fragment.pid = pid
            return fragment
        }
    }
}