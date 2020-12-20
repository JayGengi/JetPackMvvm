package com.duobang.common.socket.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


import com.duobang.common.socket.i.IEventType;
import com.duobang.common.socket.model.ObserverModel;
import com.duobang.common.socket.push.BaseTask;
import com.duobang.common.socket.push.MainTask;

import java.util.Observable;
import java.util.Observer;

/**
  * @作者　: JayGengi
  * @时间　: 2020/12/7 9:25
  * @描述　: push服务，先保留
 */
public class PushService extends Service implements Observer {

    private static final String TAG = "PushService";

    public PushService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MainTask.getInstance().addObserver(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof BaseTask) {
            final ObserverModel model = (ObserverModel) arg;
            switch (model.getEventType()) {
                case IEventType.DATA:
//                    sendUpdateStateBroadcast();
                    break;
            }
        }
    }

    /**
     * 发送广播
     */
//    protected void sendNewApprovalBroadcast(Approval approval) {
//        Intent intent = new Intent();
//        intent.setAction(PushReceiverAction.APPROVAL_PUSH);
//        intent.putExtra("approval", JsonUtil.toJson(approval));
//        sendBroadcast(intent);
//    }
//
//    protected void sendUnApprovalBroadcast(List<Approval> approvals) {
//        Intent intent = new Intent();
//        intent.setAction(PushReceiverAction.APPROVAL_PUSH);
//        intent.putExtra("approvals", JsonUtil.toJson(approvals));
//        sendBroadcast(intent);
//    }
//
//    protected void sendUpdateStateBroadcast() {
//        Intent intent = new Intent();
//        intent.setAction(PushReceiverAction.APPROVAL_UPDATE);
//        sendBroadcast(intent);
//    }

    public final class LocalBinder extends Binder {

        public PushService getService() {
            return PushService.this;
        }
    }
}
