package com.hundirlaflota.Common.ServerMessages;

public class DisconnectedMessage extends ServerMessage {

    public DisconnectedMessage() {
        this.type = MessageType.DISCONNECTED;
    }
}