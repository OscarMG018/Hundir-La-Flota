package com.hundirlaflota.Common.ServerMessages;

public class StartGameMessage extends ServerMessage {

    String player1Name = "";
    String player2Name = "";

    public StartGameMessage() {
        this.type = MessageType.START_GAME;
    }

    public StartGameMessage(String player1Name,String player2Name) {
        this.type = MessageType.START_GAME;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }

    @Override
    public String toString() {
        return "{" +
            "type: " + type + ", " +
            "player1: " + "\"" + player1Name + "\"" +
            "player2: " + "\"" + player2Name + "\"" +
        "}";
    }
}
