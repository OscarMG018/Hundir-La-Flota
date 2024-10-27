package com.hundirlaflota.Common.ServerMessages;

public class ShootResultMessage extends ServerMessage {

    private String coordinate;
    private String result;

    public ShootResultMessage(String coordinate, String result) {
        this.type = MessageType.SHOOT_RESULT;
        this.coordinate = coordinate;
        this.result = result;
    }

    @Override
    public String toString() {
        System.out.println("ShootResultMessage: " + getX() + ", " + getY());
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", coordinate:\"" + coordinate + "\"" +
                ", x:\"" + getX() + "\"" +
                ", y:\"" + getY() + "\"" +
                ", result:\"" + result + "\"" +
                "}";
    }

    public int getX() {
        return Integer.parseInt(coordinate.substring(1)) - 1;
    }

    public int getY() {
        return coordinate.charAt(0) - 'A';
    }
}

