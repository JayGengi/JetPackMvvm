package com.duobang.common.socket.listener;

public interface IEmitterListener {

    /**
     * 监听结果
     *
     * @param key
     * @param args
     */
    void emitterListenerResut(String key, Object... args);

    /**
     * 请求结果
     *
     * @param key
     * @param args
     */
    void requestSocketResult(String key, Object... args);

}
