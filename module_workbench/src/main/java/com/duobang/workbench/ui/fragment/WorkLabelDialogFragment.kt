package com.duobang.workbench.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.duobang.common.ext.setNbOnItemClickListener
import com.duobang.workbench.R
import com.duobang.workbench.ui.adapter.DiskSampleLineAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*

class WorkLabelDialogFragment : BottomSheetDialogFragment() {
    private var onLabelItemClickListener: OnLabelItemClickListener? =
        null
    private var labels: List<String> =
        ArrayList()

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
        val dialogAdapter = DiskSampleLineAdapter(labels)
        recyclerView.adapter = dialogAdapter
        dialogAdapter.setNbOnItemClickListener { adapter, _, position ->
            val item = adapter.getItem(position) as String
            if (onLabelItemClickListener != null) {
                onLabelItemClickListener!!.onLabelItemClick(item)
                dismiss()
            }
        }
    }

    interface OnLabelItemClickListener {
        fun onLabelItemClick(label: String)
    }

    companion object {
        fun newInstance(labels: List<String>): WorkLabelDialogFragment {
            val fragment = WorkLabelDialogFragment()
            fragment.labels = labels
            return fragment
        }
    }
}