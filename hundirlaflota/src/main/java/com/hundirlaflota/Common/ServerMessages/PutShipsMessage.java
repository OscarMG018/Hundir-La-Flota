package com.hundirlaflota.Common.ServerMessages;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.hundirlaflota.Common.Ship;

public class PutShipsMessage extends ServerMessage {

    private ArrayList<Ship> ships;

    public PutShipsMessage(ArrayList<Ship> ships) {
        this.type = MessageType.PUT_SHIPS;
        this.ships = ships;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", ships:[" + ships.stream().map(Ship::toString).collect(Collectors.joining(", ")) + "]" +
                "}";
    }
}
