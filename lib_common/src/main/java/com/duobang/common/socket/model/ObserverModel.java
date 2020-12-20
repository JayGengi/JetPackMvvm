package com.duobang.common.socket.model;

import java.util.List;

public class ObserverModel {

    private String eventType;
    private List<SocketMessage> socketMessages;
    private SocketMessage socketMessage;

    public SocketMessage getSocketMessage() {
        return socketMessage;
    }

    public void setSocketMessage(SocketMessage socketMessage) {
        this.socketMessage = socketMessage;
    }

    public List<SocketMessage> getSocketMessages() {
        return socketMessages;
    }

    public void setSocketMessages(List<SocketMessage> socketMessages) {
        this.socketMessages = socketMessages;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }


}
