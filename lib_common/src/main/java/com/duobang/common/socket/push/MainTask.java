package com.duobang.common.socket.push;


import com.duobang.common.socket.AppSocket;
import com.duobang.common.socket.i.IConstants;

public class MainTask extends BaseTask {

    private static volatile MainTask INSTANCE = null;

    public static MainTask getInstance() {
        if (INSTANCE == null) {
            throw new NullPointerException("must first call the init() method");
        }
        return INSTANCE;
    }

    public static void init() {
        new MainTask();
    }

    private MainTask() {
        super();
        INSTANCE = this;
        initAppSocket();
    }

    /**
     * 初始化Socket
     */
    public void initAppSocket() {
        AppSocket.Builder builder = new AppSocket.Builder(IConstants.SERVER_URL)
                .setEmitterListener(this);
        AppSocket.init(builder).connect();
    }

}
