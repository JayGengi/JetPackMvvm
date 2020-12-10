package com.duobang.jetpackmvvm.data.bean;

import com.chad.library.adapter.base.entity.node.BaseExpandNode;
import com.chad.library.adapter.base.entity.node.BaseNode;

import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

public class StructureGroup extends BaseExpandNode {

    /**
     * groupId : edb300ae-746f-427f-9116-7bf8b6d283d1
     * groupName : 桥梁
     * groupType : 1
     * structureList : [{"createAt":"2020-05-12T09:24:41.112Z","id":"84597cc2-0469-4437-b1e4-6b60345d7f14","name":"K7+720人行天桥","type":1,"description":"这个是啥为也不清楚","orgId":"391a6c13-63e8-4052-91bb-dd7254e9d1ed","state":0,"groupId":"edb300ae-746f-427f-9116-7bf8b6d283d1","bimUrl":"","extraInfo":null}]
     */

    private Date createAt;
    private String id;
    private String name;
    private int groupType;
    private int type;
    private int scopeType;
    private String orgId;
    private Object modelHierarchys;
    private Object modelTypes;
    private List<Structure> structures;
    private List<BaseNode> childNode;

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

    public int getGroupType() {
        return groupType;
    }

    public void setGroupType(int groupType) {
        this.groupType = groupType;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getScopeType() {
        return scopeType;
    }

    public void setScopeType(int scopeType) {
        this.scopeType = scopeType;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public Object getModelHierarchys() {
        return modelHierarchys;
    }

    public void setModelHierarchys(Object modelHierarchys) {
        this.modelHierarchys = modelHierarchys;
    }

    public Object getModelTypes() {
        return modelTypes;
    }

    public void setModelTypes(Object modelTypes) {
        this.modelTypes = modelTypes;
    }

    public List<Structure> getStructures() {
        return structures;
    }

    public void setStructures(List<Structure> structures) {
        this.structures = structures;
    }

    public void setChildNode(List<BaseNode> childNode) {
        this.childNode = childNode;
    }

    @Nullable
    @Override
    public List<BaseNode> getChildNode() {
        return childNode;
    }
}
