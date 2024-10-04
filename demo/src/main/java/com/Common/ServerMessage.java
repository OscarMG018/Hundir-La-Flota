package com.Common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

enum MessageType {
    CONNECTED,
    DISCONNECTED,
    ACK,
    LIST_ROOMS,
    CREATE_ROOM,
    JOIN_ROOM,
    LEAVE_ROOM,
    MOUSE_POSITION,
    PUT_SHIPS,
    STARTING_PLAYER,
    PLAY_MOVE,
    HIT_RESULT, //hit or water
    PASS_TURN,
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

class ListRoomsMessage extends ServerMessage {

    public ListRoomsMessage(MessageType type) {
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

class MousePositionMessage extends ServerMessage {

    private Position position;

    public MousePositionMessage(MessageType type, Position position) {
        this.type = type;
        this.position = position;
    }
}

class PutShipsMessage extends ServerMessage {

    private ArrayList<Ship> ships;

    public PutShipsMessage(MessageType type, ArrayList<Ship> ships) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", ships:[" + ships.stream().map(Ship::toString).collect(Collectors.joining(", ")) + "]" +
                "}";
    }
}

class StartingPlayerMessage extends ServerMessage {

    boolean isStarting;

    public StartingPlayerMessage(MessageType type, boolean isStarting) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", isStarting:\"" + isStarting + "\"" +
                "}";
    }
}

class PlayMoveMessage extends ServerMessage {

    private Position position;

    public PlayMoveMessage(MessageType type, Position position) {
        this.type = type;
    }
}

class StartGameMessage extends ServerMessage {

    public StartGameMessage(MessageType type) {
        this.type = type;
    }
}

class HitResultMessage extends ServerMessage {

    private boolean hit;

    public HitResultMessage(MessageType type, boolean hit) {
        this.type = type;
        this.hit = hit;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", hit:\"" + hit + "\"" +
                "}";
    }
}

class PassTurnMessage extends ServerMessage {

    public PassTurnMessage(MessageType type) {
        this.type = type;
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