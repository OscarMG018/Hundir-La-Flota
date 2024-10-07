package com.hundirlaflota.Common.ServerMessages;

import com.hundirlaflota.Common.Position;

public class MousePositionMessage extends ServerMessage {

    private Position position;

    public MousePositionMessage(Position position) {
        this.type = MessageType.MOUSE_POSITION;
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