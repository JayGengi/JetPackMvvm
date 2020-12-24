package com.duobang.common.socket.i;


import com.duobang.common.api.ApiService;

public interface IConstants {

    String SERVER_URL = ApiService.SERVER_URL;

    String DATA = "Data";

    String EVENT_USER_MSG = " UserOrg";
    String EVENT_TEST_MSG = "testMsg";


    interface NAMESPACE {
        /* 日事日毕 */ String APP_DAILY_TASK = "app.daily-task";
    }

    interface ACTION {
        /* 创建 */ String CREATE = "Create";
        /* 更新 */ String UPDATE = "Update";
        /* 删除 */ String DELETE = "Delete";
        /* 其他 */ String OTHER = "Other";
    }

}
