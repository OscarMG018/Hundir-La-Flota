package com.hundirlaflota.Common;

import org.java_websocket.WebSocket;

public class Player {
    private WebSocket conn;//id
    private String name = "Unnamed";
    private Room room;

    public Player(WebSocket conn) {
        this.conn = conn;
    }

    public void sendMessage(String message) {
        conn.send(message);
    }

    public WebSocket getConn() {
        return conn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
