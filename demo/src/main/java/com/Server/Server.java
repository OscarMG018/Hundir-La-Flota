package com.Server;

import org.java_websocket.server.WebSocketServer;


public class Server extends WebSocketServer {

  public Server(InetSocketAddress address) {
    super(address);
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    System.out.println("New connection: " + conn.getRemoteSocketAddress());
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    System.out.println("Received message: " + message);
  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    System.err.println("ERROR from " + conn.getRemoteSocketAddress() + ": " + ex);
  }

  @Override
  public void onStart() {
    System.out.println("Server started!");
  }

  public static void main(String[] args) {
    int port = 8080;
    Server server = new Server(new InetSocketAddress("localhost", port));
    server.start();
    System.out.println("Server started on port " + port);
  }
}
