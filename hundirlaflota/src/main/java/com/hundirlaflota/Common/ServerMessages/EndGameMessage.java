package com.hundirlaflota.Common.ServerMessages;

public class EndGameMessage extends ServerMessage {

    boolean winner;

    public EndGameMessage(boolean winner) {
        this.type = MessageType.END_GAME;
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", winner:\"" + winner + "\"" +
                "}";
    }
}
