package com.hundirlaflota.Common.ServerMessages;

public enum MessageType {
    LOGIN,
    DISCONNECTED,
    ACK,
    LIST_ROOMS,
    CREATE_ROOM,
    JOIN_ROOM,
    LEAVE_ROOM,
    SET_READY,
    MOUSE_POSITION,
    PUT_SHIPS,
    STARTING_PLAYER,
    PLAY_MOVE,
    HIT_RESULT, //hit or water
    PASS_TURN,
    END_GAME,
    ERROR
}
