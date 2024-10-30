package com.hundirlaflota.Common.ServerMessages;

public class CreateRoomMessage extends ServerMessage {

    private String roomName = "";

    public CreateRoomMessage(String roomName) {
        this.type = MessageType.CREATE_ROOM;
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
