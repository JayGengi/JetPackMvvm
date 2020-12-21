package com.duobang.common.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.Date;
import java.util.List;

public class DiskBean implements MultiItemEntity, Parcelable {
    /**
     * id : 51e7998b-46b4-4264-a698-dfd4742b5621
     * name : 测试目录
     * size : 0
     * type : 0
     * subType : 0
     * userId : 8713796d-859f-4fb6-a40f-059257be033b
     * createAt : 2020-10-19T09:17:10.911Z
     * ossPath : null
     * pid : null
     * orgId : 1618dcf2-8d11-44f7-b212-767b204e4925
     * level : 0
     * manager : null
     * members : []
     * privacy : 1
     * writeable : true
     */
    public static final int FOLDER = 0;// 文件夹
    public static final int FILE = 1; // 文档
    public static final int IMG = 2; // 图片

    public static final int VISITOR = 0;    // 访客 非文件夹成员访问，只可查看
    public static final int USER = 1;       // 使用者   成员在管理员创建的文件夹中访问(可上传并修改自己上传的文件，但不可创建文件夹)
    public static final int OWNER = 2;      // 归属者   成员访问自己创建的文件夹
    private String id;
    private String name;
    private int size;
    private int type;
    private int subType = FILE;
    private String userId;
    private Date createAt;
    private String ossPath;
    private String pid;
    private String orgId;
    private int level;
    private String manager;
    private int privacy;
    private int dirRole;
    private List<String> members;
    private boolean showEdit = false;
    private boolean edit = false;


    protected DiskBean(Parcel in) {
        id = in.readString();
        name = in.readString();
        size = in.readInt();
        type = in.readInt();
        subType = in.readInt();
        userId = in.readString();
        ossPath = in.readString();
        pid = in.readString();
        orgId = in.readString();
        level = in.readInt();
        manager = in.readString();
        privacy = in.readInt();
        dirRole = in.readInt();
        members = in.createStringArrayList();
        showEdit = in.readByte() != 0;
        edit = in.readByte() != 0;
    }

    public static final Creator<DiskBean> CREATOR = new Creator<DiskBean>() {
        @Override
        public DiskBean createFromParcel(Parcel in) {
            return new DiskBean(in);
        }

        @Override
        public DiskBean[] newArray(int size) {
            return new DiskBean[size];
        }
    };

    public boolean isShowEdit() {
        return showEdit;
    }

    public void setShowEdit(boolean showEdit) {
        this.showEdit = showEdit;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public String getOssPath() {
        return ossPath;
    }

    public void setOssPath(String ossPath) {
        this.ossPath = ossPath;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public int isWriteable() {
        return dirRole;
    }

    public void setWriteable(int dirRole) {
        this.dirRole = dirRole;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }

    @Override
    public int getItemType() {
        return subType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeInt(size);
        parcel.writeInt(type);
        parcel.writeInt(subType);
        parcel.writeString(userId);
        parcel.writeString(ossPath);
        parcel.writeString(pid);
        parcel.writeString(orgId);
        parcel.writeInt(level);
        parcel.writeString(manager);
        parcel.writeInt(privacy);
        parcel.writeInt(dirRole);
        parcel.writeStringList(members);
        parcel.writeByte((byte) (showEdit ? 1 : 0));
        parcel.writeByte((byte) (edit ? 1 : 0));
    }

}
