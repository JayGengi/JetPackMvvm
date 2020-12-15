package com.duobang.project.ui.adapter.provider;

import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.duobang.project.R;
import com.duobang.common.data.bean.Structure;

import org.jetbrains.annotations.NotNull;

public class SecondProvider extends BaseNodeProvider {

    @Override
    public int getItemViewType() {
        return 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_project_main_node_second;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        Structure entity = (Structure) data;
        helper.setText(R.id.name_structure_item, entity.getName());
    }

    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        super.onClick(helper, view, data, position);
        Structure entity = (Structure) data;
        ToastUtils.showShort(entity.getName());

    }
}
