package com.hundirlaflota.Common.ServerMessages;

public class LoginMessage extends ServerMessage {

    String playerName;

    public LoginMessage(String playerName) {
        this.type = MessageType.LOGIN;
        this.playerName = playerName;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", playerName:\"" + playerName + "\"" +
                "}";
    }
}