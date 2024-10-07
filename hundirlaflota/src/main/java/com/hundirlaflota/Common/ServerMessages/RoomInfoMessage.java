package com.hundirlaflota.Common.ServerMessages;

public class RoomInfoMessage extends ServerMessage {

    public RoomInfoMessage() {
        type = MessageType.ROOM_INFO;
    }
    
    @Override
    public String toString() {
        return "{" +
            "type: " + type + ", " +
        "}";
    }
    
    
}