package com.hundirlaflota.Common.ServerMessages;

public class ErrorMessage extends ServerMessage {

    private String message;
    private MessageType requestType;

    public ErrorMessage(String message, MessageType requestType) {
        this.type = MessageType.ERROR;
        this.message = message;
        this.requestType = requestType;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", requestType:\"" + requestType.toString() + "\"" +
                ", message:\"" + message + "\"" +
                "}";
    }
}