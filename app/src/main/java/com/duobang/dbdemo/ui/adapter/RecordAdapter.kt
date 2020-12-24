package com.duobang.dbdemo.ui.adapter

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.duobang.base.ext.util.toList
import com.duobang.dbdemo.R
import com.duobang.common.data.bean.*
import com.duobang.common.data.constant.IConstant
import com.duobang.common.data.constant.IPmsConstant
import com.duobang.common.ext.setAdapterAnimation
import com.duobang.common.ext.setNbOnItemClickListener
import com.duobang.common.room.repository.PmsRepository
import com.duobang.common.util.AppImageLoader
import com.duobang.common.util.DateUtil
import com.duobang.common.util.SettingUtil
import com.duobang.common.weight.recyclerview.DuobangLinearLayoutManager
import com.duobang.base.ext.util.notNull
import com.google.gson.Gson
import java.util.*

class RecordAdapter(list: MutableList<Record>?) :BaseQuickAdapter<Record, BaseViewHolder>(
        R.layout.record_list_item,list) {

    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }
    override fun convert(holder: BaseViewHolder, item: Record) {
        val creator = PmsRepository(context).getUserById(item.userId!!)
        creator.notNull({
            AppImageLoader.displayAvatar(creator.avatar, creator.nickname, holder.getView(R.id.avatar_user_record_item))
            holder.setText(R.id.name_user_record_item,creator.nickname)
        },{})
        holder.setText(R.id.create_date_record_item,
            DateUtil.formatMinute(Date(item.createTime!!.toLong())))
            .setText(R.id.title_record_item, item.completeName)
            .setText(R.id.type_sign_record_item, item.templateName)
//        val typeSign: MaterialButton = holder.getView(R.id.type_sign_record_item)
//        typeSign.text =
        val container: LinearLayout = holder.getView(R.id.container_record_item)
        container.removeAllViews()

        if(null != item.items){
            addView(container, item)
        }
    }

    private fun addView(container: LinearLayout, record: Record) {
        for (i in record.items!!.indices) {
            when (record.items!![i].fieldType) {
                IPmsConstant.FIELD_TYPE.STRING,
                IPmsConstant.FIELD_TYPE.TEXT,
                IPmsConstant.FIELD_TYPE.NUMBER ->
                    addTextView(container,record, record.items!![i])

                IPmsConstant.FIELD_TYPE.DATE ->
                    addDateView(container, record.items!![i])

                IPmsConstant.FIELD_TYPE.TIME ->
                    addTimeView(container,record.items!![i])

                IPmsConstant.FIELD_TYPE.DATETIME ->
                    addDateTimeView(container,record.items!![i])

                IPmsConstant.FIELD_TYPE.IMAGE ->
                    addImageView(container, record.items!![i])

                IPmsConstant.FIELD_TYPE.FILE ->
                    addFileView(container, record.items!![i])

                IPmsConstant.FIELD_TYPE.METAQ ->
                    addProgressView(container,record.items!![i])

                IPmsConstant.FIELD_TYPE.MATERIAL ->
                    addMaterialView(container,record.items!![i])

                IPmsConstant.FIELD_TYPE.PROCEDURE ->
                    addProcedureView(container,record.items!![i])

                IPmsConstant.FIELD_TYPE.LABOR ->
                    addLaborTeamView(container,record.items!![i])

                IPmsConstant.FIELD_TYPE.LABORS ->
                    addLaborTeamsView(container,record.items!![i])

                else -> {
                }
            }
        }
    }

    private fun addDateTimeView(container: LinearLayout,recordField: RecordField) {

        val v = View.inflate(container.context, R.layout.record_text_show_view, null)
        container.addView(v)
        val title = v.findViewById<TextView>(R.id.title_record_text_show)
        val content = v.findViewById<TextView>(R.id.content_record_text_show)
        title.text = recordField.fieldName
        try {
            content.text =
                DateUtil.formatMinute(Date((recordField.getFieldValue<Double>() as Double).toLong()))
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("数据错误！")
        }
    }

    private fun addTimeView(container: LinearLayout, recordField: RecordField) {
        val v = View.inflate(container.context, R.layout.record_text_show_view, null)
        container.addView(v)
        val title = v.findViewById<TextView>(R.id.title_record_text_show)
        val content = v.findViewById<TextView>(R.id.content_record_text_show)
        title.text = recordField.fieldName
        try {
            content.text =
                DateUtil.formatTime(Date((recordField.getFieldValue<Double>() as Double).toLong()))
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("数据错误！")
        }
    }

    private fun addDateView(container: LinearLayout, recordField: RecordField) {
        val v = View.inflate(container.context, R.layout.record_text_show_view, null)
        container.addView(v)
        val title = v.findViewById<TextView>(R.id.title_record_text_show)
        val content = v.findViewById<TextView>(R.id.content_record_text_show)
        title.text = recordField.fieldName
        try {
            content.text =
                DateUtil.formatDate(Date((recordField.getFieldValue<Double>() as Double).toLong()))
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("数据错误！")
        }
    }

    private fun addTextView(
        container: LinearLayout,
        record: Record,
        recordField: RecordField
    ) {
        val v = View.inflate(container.context, R.layout.record_text_show_view, null)
        container.addView(v)
        val title = v.findViewById<TextView>(R.id.title_record_text_show)
        val content = v.findViewById<TextView>(R.id.content_record_text_show)
        title.text = recordField.fieldName
        try {
            content.text = recordField.getFieldValue<String>().toString()
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("数据错误！")
        }
    }

    private fun addImageView(container: LinearLayout, recordField: RecordField) {
        val v = View.inflate(container.context, R.layout.record_list_show_view, null)
        container.addView(v)
        val photoView: RecyclerView = v.findViewById(R.id.list_record_show)
        try {
            val photoPaths: List<String>? = recordField.getFieldValue<List<String>>()
            val adapter = PhotoAdapter(
                photoPaths as MutableList<String>?,
                IConstant.PHOTO.SHOW
            )
            photoView.layoutManager = GridLayoutManager(container.context, 3)
            photoView.adapter = adapter
            adapter.setNbOnItemClickListener { adapter, view, position ->
                //TODO 查看图片
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("数据错误！")
        }
    }

    private fun addFileView(container: LinearLayout, recordField: RecordField) {}

    private fun addProgressView(
        container: LinearLayout,
        recordField: RecordField
    ) {
        val v = View.inflate(container.context, R.layout.record_progress_show, null)
        container.addView(v)
        val title = v.findViewById<TextView>(R.id.title_record_progress_show)
        val content = v.findViewById<TextView>(R.id.content_record_progress_show)
        val time = v.findViewById<TextView>(R.id.time_record_progress_show)
        title.text = recordField.fieldName
        val json: String = Gson().toJson(recordField.getFieldValue<Any>() as Any)
        try {
            val progress: RecordProgress = Gson().fromJson(json, RecordProgress::class.java)
            time.text = DateUtil.formatMinute(Date(progress.time))
            //本次完成  == 累计完成 - 上次完成
            val nowValue: Float = progress.toValue - progress.fromValue
            val progressStr = "本次" + nowValue + "%    累计" + progress.toValue + "%"
            content.text = progressStr
            content.setTextColor(Color.parseColor("#000000"))
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("数据错误！")
        }
    }

    private fun addMaterialView(
        container: LinearLayout,
        recordField: RecordField
    ) {
        val v = View.inflate(container.context, R.layout.record_material_show, null)
        container.addView(v)
        val materialView: RecyclerView = v.findViewById(R.id.list_record_material_show)
        val time = v.findViewById<TextView>(R.id.time_record_material_show)
        val json: String = Gson().toJson(recordField.getFieldValue<Any>() as Any)
        try {
            val wrapper: RecordMaterialWrapper =
                Gson().fromJson(json, RecordMaterialWrapper::class.java)
            if (wrapper != null) {
                time.text = DateUtil.formatMinute(Date(wrapper.time))
                val materials: List<Material> = wrapper.data!!
                val adapter =
                    RecordShowMaterialAdapter(
                        materials
                    )
                materialView.layoutManager = LinearLayoutManager(context)
                materialView.adapter = adapter
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("数据错误！")
        }
    }

    private fun addProcedureView(
        container: LinearLayout,
        recordField: RecordField
    ) {
        val v =
            View.inflate(container.context, R.layout.record_procedure_show_view, null)
        container.addView(v)
        val title = v.findViewById<TextView>(R.id.title_record_procedure_show)
        val content = v.findViewById<TextView>(R.id.content_record_procedure_show)
        val time = v.findViewById<TextView>(R.id.time_record_procedure_show)
        val json: String = Gson().toJson(recordField.getFieldValue<Any>() as Any)
        try {
            val procedure: Procedure =
                Gson().fromJson(json, Procedure::class.java)
            title.text = recordField.fieldName
            content.text = procedure.procedure
            time.text = DateUtil.formatMinute(Date(procedure.time!!))
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("数据错误！")
        }
    }

    private fun addLaborTeamView(
        container: LinearLayout,
        recordField: RecordField
    ) {
        val v = View.inflate(container.context, R.layout.record_labor_show_view, null)
        container.addView(v)
        val title = v.findViewById<TextView>(R.id.title_record_labor_show)
        val content = v.findViewById<TextView>(R.id.content_record_labor_show)
        val json: String = Gson().toJson(recordField.getFieldValue<Any>() as Any)
        try {
            val team: RecordLaborTeam =
                Gson().fromJson(json, RecordLaborTeam::class.java)
            if (team != null) {
                title.text = recordField.fieldName
                content.text = team.teamName
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("数据错误！")
        }
    }

    private fun addLaborTeamsView(
        container: LinearLayout,
        recordField: RecordField
    ) {
        val v = View.inflate(container.context, R.layout.record_labors_show_view, null)
        container.addView(v)
        val title = v.findViewById<TextView>(R.id.title_record_labors_show)
        val teamsView: RecyclerView = v.findViewById(R.id.content_record_labors_show)
        val json: String = Gson().toJson(recordField.getFieldValue<Any>() as Any)
        try {
            val teams: List<RecordLaborTeam> = toList(json,RecordLaborTeam::class.java)!!
            title.text = recordField.fieldName
            setupTeamsView(teamsView, teams)
        } catch (e: Exception) {
            e.printStackTrace()
            ToastUtils.showShort("数据错误！")
        }
    }

    private fun setupTeamsView(
        teamsView: RecyclerView,
        teams: List<RecordLaborTeam>
    ) {
        val adapter =
            SimpleTeamsAdapter(teams)
        teamsView.layoutManager = DuobangLinearLayoutManager(
            teamsView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        teamsView.adapter = adapter
    }
}