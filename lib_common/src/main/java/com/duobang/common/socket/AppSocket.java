package com.duobang.common.socket;


import com.duobang.common.socket.i.IConstants;

/**
 * AppSocket
 */
public class AppSocket extends BaseSocket {

    private static volatile AppSocket INSTANCE = null;

    public static AppSocket getInstance() {
        if (INSTANCE == null) {
            throw new NullPointerException("must first call the build() method");
        }
        return INSTANCE;
    }

    public static AppSocket init(Builder builder) {
        return new AppSocket(builder);
    }

    private AppSocket(Builder builder) {
        super(builder);
        INSTANCE = this;
    }

    public void socketConnectPath(String key, String value) {
        mSocket.emit("path", "/socket.io");
        mSocket.emit(key, value);
    }

    public void userMsg(String userInfo) {
        mSocket.emit(IConstants.EVENT_USER_MSG, userInfo);
    }

    public void testMsg(String msg) {
        mSocket.emit(IConstants.EVENT_TEST_MSG, msg);
    }


}
