package com.hundirlaflota.Common.ServerMessages;

public class ShootMessage extends ServerMessage {

    private int x;
    private int y;

    public ShootMessage(int y, int x) {
        this.type = MessageType.SHOOT;
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", x:\"" + x + "\"" +
                ", y:\"" + y + "\"" +
                ", coordinate:\"" + toCoordinate() + "\"" +
                "}";
    }

    public String toCoordinate() {
        return (char) ('A' + y) + "" + (x + 1);
    }
}