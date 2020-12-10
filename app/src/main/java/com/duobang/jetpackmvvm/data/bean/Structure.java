package com.duobang.jetpackmvvm.data.bean;

import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

public class Structure extends BaseNode {

    /**
     * createAt : 2020-05-12T09:24:41.112Z
     * id : 84597cc2-0469-4437-b1e4-6b60345d7f14
     * name : K7+720人行天桥
     * type : 1
     * description : 这个是啥为也不清楚
     * orgId : 391a6c13-63e8-4052-91bb-dd7254e9d1ed
     * state : 0
     * groupId : edb300ae-746f-427f-9116-7bf8b6d283d1
     * bimUrl :
     * extraInfo : null
     */

    private Date createAt;
    private String id;
    private String name;
    private int type;
    private String description;
    private String orgId;
    private int state;
    private String bimUrl;
    private Object extraInfo;
    private List<StructureGroup> structureGroup;

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getBimUrl() {
        return bimUrl;
    }

    public void setBimUrl(String bimUrl) {
        this.bimUrl = bimUrl;
    }

    public Object getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(Object extraInfo) {
        this.extraInfo = extraInfo;
    }

    public List<StructureGroup> getStructureGroup() {
        return structureGroup;
    }

    public void setStructureGroup(List<StructureGroup> structureGroup) {
        this.structureGroup = structureGroup;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return null;
    }
}
