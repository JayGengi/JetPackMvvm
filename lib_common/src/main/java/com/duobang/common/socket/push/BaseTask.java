package com.duobang.common.socket.push;


import com.blankj.utilcode.util.LogUtils;
import com.duobang.common.socket.i.IConstants;
import com.duobang.common.socket.i.IEventType;
import com.duobang.common.socket.i.ITask;
import com.duobang.common.socket.model.ObserverModel;
import com.duobang.common.socket.model.SocketMessage;
import com.duobang.common.util.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.Observable;

import io.socket.client.Manager;
import io.socket.client.Socket;

/**
 * 基类
 */
public class BaseTask extends Observable implements ITask {

    private String TAG = BaseTask.class.getSimpleName();

    BaseTask() {

    }


    private void connectSuccess() {
        setChanged();
        ObserverModel model = new ObserverModel();
        model.setEventType(IEventType.CONNECT_MESSAGE);
        notifyObservers(model);
    }

    private void socketMsg(List<SocketMessage> socketMessageList) {
        setChanged();
        ObserverModel model = new ObserverModel();
        model.setSocketMessages(socketMessageList);
        model.setEventType(IEventType.DATA);
        notifyObservers(model);
    }

    @Override
    public void emitterListenerResut(String key, Object... args) {
        switch (key) {
            case Manager.EVENT_TRANSPORT:
//                requestCookie(args);
                break;
            case Socket.EVENT_CONNECT_ERROR:
                LogUtils.e(TAG, "EVENT_CONNECT_ERROR");
                break;

            case Socket.EVENT_CONNECT_TIMEOUT:
                LogUtils.e(TAG, "EVENT_CONNECT_TIMEOUT");
                break;

            // Socket连接成功
            case Socket.EVENT_CONNECT:
                connectSuccess();
                LogUtils.i(TAG, " Socket连接成功");
                break;

            // Socket断开连接
            case Socket.EVENT_DISCONNECT:
                LogUtils.i(TAG, " Socket断开连接");
                break;

            // Socket连接错误
            case Socket.EVENT_ERROR:
                LogUtils.i(TAG, " Socket连接错误");
                break;

            // Socket重新连接
            case Socket.EVENT_RECONNECT:
                LogUtils.i(TAG, " Socket重新连接");
                break;

            case Socket.EVENT_RECONNECT_ATTEMPT:
                LogUtils.e(TAG, "EVENT_RECONNECT_ATTEMPT");
                break;

            case Socket.EVENT_RECONNECT_ERROR:
                LogUtils.e(TAG, "EVENT_RECONNECT_ERROR");
                break;

            case Socket.EVENT_RECONNECT_FAILED:
                LogUtils.e(TAG, "EVENT_RECONNECT_FAILED");
                break;

            case Socket.EVENT_RECONNECTING:
                LogUtils.e(TAG, "EVENT_RECONNECTING");
                break;

            case IConstants.DATA:
                //后台下发的消息
                JSONArray jsonArray = (JSONArray) args[0];
                List<SocketMessage> socketMessageList;
                try {
                    LogUtils.e(TAG, jsonArray.toString());
                    socketMessageList = JsonUtil.toList(jsonArray.toString(), SocketMessage.class);
                    socketMsg(socketMessageList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void requestSocketResult(String key, Object... args) {

        switch (key) {

        }
    }


}
