package com.hundirlaflota.Common.ServerMessages;

public class AckMessage extends ServerMessage {

    private String data;
    private MessageType requestType;
    boolean useString;


    public AckMessage(String data, MessageType requestType) {
        this.type = MessageType.ACK;
        this.data = data;
        this.requestType = requestType;
        this.useString = true;
    }

    public AckMessage(String data, MessageType requestType, boolean useString) {
        this.type = MessageType.ACK;
        this.data = data;
        this.requestType = requestType;
        this.useString = useString;
    }

    @Override
    public String toString() {
        return "{" +
                "type:\"" + type.toString() + "\"" +
                ", requestType:\"" + requestType.toString() + "\"" +
                ", data:" + (useString ? "\"" + data + "\"" : data) +
                "}";
    }
}
