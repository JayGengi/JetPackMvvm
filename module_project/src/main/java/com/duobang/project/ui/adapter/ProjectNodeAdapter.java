package com.duobang.project.ui.adapter;

import com.chad.library.adapter.base.BaseNodeAdapter;
import com.chad.library.adapter.base.entity.node.BaseNode;
import com.duobang.common.data.bean.Structure;
import com.duobang.common.data.bean.StructureGroup;
import org.jetbrains.annotations.NotNull;
import com.duobang.project.ui.adapter.provider.*;
import java.util.List;

public class ProjectNodeAdapter extends BaseNodeAdapter {
    public ProjectNodeAdapter() {
        super();
        // 注册Provider，总共有如下三种方式
        addNodeProvider(new FirstProvider());
        addNodeProvider(new SecondProvider());
    }

    @Override
    protected int getItemType(@NotNull List<? extends BaseNode> data, int position) {
        BaseNode node = data.get(position);
        if (node instanceof StructureGroup) {
            return 1;
        } else if (node instanceof Structure) {
            return 2;
        }
        return -1;
    }

    public static final int EXPAND_COLLAPSE_PAYLOAD = 110;
}
