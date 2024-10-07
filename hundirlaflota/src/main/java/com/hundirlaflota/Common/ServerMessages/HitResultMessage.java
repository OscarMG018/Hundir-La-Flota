package com.hundirlaflota.Common.ServerMessages;

public class HitResultMessage extends ServerMessage {

    private boolean hit;

    public HitResultMessage(boolean hit) {
        this.type = MessageType.HIT_RESULT;
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

