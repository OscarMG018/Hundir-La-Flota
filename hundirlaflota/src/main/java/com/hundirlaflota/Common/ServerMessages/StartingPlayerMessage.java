package com.hundirlaflota.Common.ServerMessages;

public class StartingPlayerMessage extends ServerMessage {

    boolean isStarting;

    public StartingPlayerMessage(boolean isStarting) {
        this.type = MessageType.STARTING_PLAYER;
        this.isStarting = isStarting;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", isStarting:\"" + isStarting + "\"" +
                "}";
    }
}