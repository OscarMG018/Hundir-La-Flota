package com.hundirlaflota.Common.ServerMessages;

import com.hundirlaflota.Common.Room;

public class JoinRoomMessage extends ServerMessage {

    private Room room;

    public JoinRoomMessage(Room room) {
        this.type = MessageType.JOIN_ROOM;
        this.room = room;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", room:\"" + room.toString() + "\"" +
                "}";
    }
}

