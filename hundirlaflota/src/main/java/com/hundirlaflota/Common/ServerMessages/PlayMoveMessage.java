package com.hundirlaflota.Common.ServerMessages;

import com.hundirlaflota.Common.Position;

public class PlayMoveMessage extends ServerMessage {

    private Position position;

    public PlayMoveMessage(Position position) {
        this.type = MessageType.PLAY_MOVE;
        this.position = position;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", position:\"" + position.toString() + "\"" +
                "}";
    }
}