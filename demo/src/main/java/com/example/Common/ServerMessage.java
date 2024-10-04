package com.example.Common;

import java.io.Serializable;

enum MessageType {
    CONNECTED,
    DISCONNECTED,
    ACK,
    JOIN_ROOM,
    LEAVE_ROOM,
    LIST_ROOMS,
    CREATE_ROOM,
    START_GAME,
    PASS_TURN,
    PUT_SHIP,
    PLAY_MOVE,
    END_GAME,
}


public abstract class ServerMessage implements Serializable {
    protected MessageType type;

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                "}";
    }
}

class ConnectedMessage extends ServerMessage {

    String playerName;

    public ConnectedMessage(MessageType type, String playerName) {
        this.type = type;
        this.playerName = playerName;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", playerName:\"" + playerName + "\"" +
                "}";
    }
}

class DisconnectedMessage extends ServerMessage {

    public DisconnectedMessage(MessageType type) {
        this.type = type;
    }
}

class AckMessage extends ServerMessage {

    public AckMessage(MessageType type, String data) {
        this.type = type;
    }
}

class JoinRoomMessage extends ServerMessage {

    private Room room;

    public JoinRoomMessage(MessageType type, Room room) {
        this.type = type;
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

class LeaveRoomMessage extends ServerMessage {

    public LeaveRoomMessage(MessageType type) {
        this.type = type;
    }
}

class CreateRoomMessage extends ServerMessage {

    private Room room;

    public CreateRoomMessage(MessageType type, Room room) {
        this.type = type;
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

class ListRoomsMessage extends ServerMessage {

    public ListRoomsMessage(MessageType type) {
        this.type = type;
    }
}

class StartGameMessage extends ServerMessage {

    public StartGameMessage(MessageType type) {
        this.type = type;
    }
}

class PassTurnMessage extends ServerMessage {

    public PassTurnMessage(MessageType type) {
        this.type = type;
    }
}

class PutShipMessage extends ServerMessage {

    private Ship ship;

    public PutShipMessage(MessageType type, Ship ship) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", ship:\"" + ship.toString() + "\"" +
                "}";
    }
}

class PlayMoveMessage extends ServerMessage {

    private Move move;

    public PlayMoveMessage(MessageType type, Move move) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", move:\"" + move.toString() + "\"" +
                "}";
    }
}

class EndGameMessage extends ServerMessage {

    boolean winner;

    public EndGameMessage(MessageType type, boolean winner) {
        this.type = type;
        this.winner = winner;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", winner:\"" + winner + "\"" +
                "}";
    }
}









