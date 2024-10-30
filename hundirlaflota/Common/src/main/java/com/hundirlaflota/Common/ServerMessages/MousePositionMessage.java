package com.hundirlaflota.Common.ServerMessages;

public class MousePositionMessage extends ServerMessage {

    private int x;
    private int y;

    public MousePositionMessage(int x, int y) {
        this.type = MessageType.MOUSE_POSITION;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", x:\"" + x + "\"" +
                ", y:\"" + y + "\"" +
                "}";
    }
}