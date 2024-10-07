package com.hundirlaflota.Common.ServerMessages;

public class SetReadyMessage extends ServerMessage {

    public SetReadyMessage() {
        this.type = MessageType.SET_READY;
    }
}
