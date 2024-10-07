package com.hundirlaflota.Common.ServerMessages;

public class StartGameMessage extends ServerMessage {
    public StartGameMessage() {
        this.type = MessageType.START_GAME;
    }
}
