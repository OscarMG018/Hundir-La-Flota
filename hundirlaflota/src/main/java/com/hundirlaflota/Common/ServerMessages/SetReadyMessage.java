package com.hundirlaflota.Common.ServerMessages;

public class SetReadyMessage extends ServerMessage {

    boolean ready;

    public SetReadyMessage(boolean ready) {
        this.type = MessageType.SET_READY;
        this.ready = ready;
    }

    @Override
    public String toString() {
        return "{" +
                "type:" + type +
                ", ready:" + ready +
                "}";
    }
}
