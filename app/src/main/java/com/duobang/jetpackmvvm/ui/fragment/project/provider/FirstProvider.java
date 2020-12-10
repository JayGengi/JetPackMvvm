package com.duobang.jetpackmvvm.ui.fragment.project.provider;

import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.chad.library.adapter.base.provider.BaseNodeProvider;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.duobang.jetpackmvvm.R;
import com.duobang.jetpackmvvm.data.bean.StructureGroup;
import com.duobang.jetpackmvvm.ui.adapter.project.ProjectNodeAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FirstProvider extends BaseNodeProvider {


    @Override
    public int getItemViewType() {
        return 1;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_project_main_node;
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data) {
        StructureGroup entity = (StructureGroup) data;
        helper.setText(R.id.name_group_project_item, entity.getName());
        helper.setImageResource(R.id.fold_project_item, R.drawable.ic_drop_down);

        setArrowSpin(helper, data, false);
    }

    @Override
    public void convert(@NotNull BaseViewHolder helper, @NotNull BaseNode data, @NotNull List<?> payloads) {
        for (Object payload : payloads) {
            if (payload instanceof Integer && (int) payload == ProjectNodeAdapter.EXPAND_COLLAPSE_PAYLOAD) {
                // 增量刷新，使用动画变化箭头
                setArrowSpin(helper, data, true);
            }
        }
    }

    private void setArrowSpin(BaseViewHolder helper, BaseNode data, boolean isAnimate) {
        StructureGroup entity = (StructureGroup) data;

        ImageView imageView = helper.getView(R.id.fold_project_item);

        if (entity.isExpanded()) {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                        .setInterpolator(new DecelerateInterpolator())
                        .rotation(0f)
                        .start();
            } else {
                imageView.setRotation(0f);
            }
        } else {
            if (isAnimate) {
                ViewCompat.animate(imageView).setDuration(200)
                        .setInterpolator(new DecelerateInterpolator())
                        .rotation(180f)
                        .start();
            } else {
                imageView.setRotation(180f);
            }
        }
    }
    @Override
    public void onClick(@NotNull BaseViewHolder helper, @NotNull View view, BaseNode data, int position) {
        super.onClick(helper, view, data, position);
        ToastUtils.showShort("first" + position);
        // 这里使用payload进行增量刷新（避免整个item刷新导致的闪烁，不自然）
        getAdapter().expandOrCollapse(position, true, true, ProjectNodeAdapter.EXPAND_COLLAPSE_PAYLOAD);
    }

}
