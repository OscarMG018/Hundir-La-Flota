package com.hundirlaflota.Common.ServerMessages;

public class PassTurnMessage extends ServerMessage {

    public PassTurnMessage() {
        this.type = MessageType.PASS_TURN;
    }
}