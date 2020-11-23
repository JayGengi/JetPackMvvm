package com.duobang.jetpackmvvm.pms.ui.adapter;


import android.icu.text.AlphabeticIndex;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.duobang.jetpackmvvm.pms.R;
import com.duobang.jetpackmvvm.pms.data.model.bean.Record;
import com.duobang.jetpackmvvm.pms.data.model.bean.User;

import java.util.List;

public class RecordAdapter extends BaseQuickAdapter<Record, BaseViewHolder> {

    public RecordAdapter(List<Record> list) {
        super(R.layout.record_list_item, list);
    }

    @Override
    protected void convert(BaseViewHolder holder, Record item) {
//        holder.setText(R.id.name_report_item, item.getNickname())
//                .setVisible(R.id.status_report_item, false);
//        AppImageLoader.displayAvatar(item.getAvatar(), item.getNickname(), (AvatarView) holder.getView(R.id.avatar_report_item));
    }


}
