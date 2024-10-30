package com.hundirlaflota.Common.ServerMessages;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class PutShipsMessage extends ServerMessage {

    private ArrayList<ShipData> ships;

    public PutShipsMessage(ArrayList<ShipData> ships) {
        this.type = MessageType.PUT_SHIPS;
        this.ships = ships;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", ships:[" + ships.stream().map(ShipData::toString).collect(Collectors.joining(", ")) + "]" +
                "}";
    }
}