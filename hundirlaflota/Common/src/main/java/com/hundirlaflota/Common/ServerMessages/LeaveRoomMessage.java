package com.hundirlaflota.Common.ServerMessages;

public class LeaveRoomMessage extends ServerMessage {

    public LeaveRoomMessage() {
        this.type = MessageType.LEAVE_ROOM;
    }
}
