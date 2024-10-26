package com.hundirlaflota.Common.ServerMessages;

import java.util.ArrayList;
import java.util.stream.Collectors;
public class StartingPlayerMessage extends ServerMessage {

    boolean isStarting;
    public ArrayList<ShipData> opponentsShips;

    public StartingPlayerMessage(boolean isStarting, ArrayList<ShipData> opponentsShips) {
        this.type = MessageType.STARTING_PLAYER;
        this.isStarting = isStarting;
        this.opponentsShips = opponentsShips;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", isStarting:\"" + isStarting + "\"" +
                ", opponentsShips:[" + opponentsShips.stream().map(ShipData::toString).collect(Collectors.joining(", ")) + "]" +
                "}";
    }
}