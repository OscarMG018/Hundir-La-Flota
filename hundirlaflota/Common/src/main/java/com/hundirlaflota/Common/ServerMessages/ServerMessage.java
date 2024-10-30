package com.hundirlaflota.Common.ServerMessages;

import java.io.Serializable;

public abstract class ServerMessage implements Serializable {
    protected MessageType type;

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                "}";
    }
}