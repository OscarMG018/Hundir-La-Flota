package com.hundirlaflota.Common.ServerMessages;

public class JoinRoomMessage extends ServerMessage {

    private String roomName = "";

    public JoinRoomMessage(String roomName) {
        this.type = MessageType.JOIN_ROOM;
        this.roomName = roomName;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", roomName:\"" + roomName + "\"" +
                "}";
    }
}

