package com.hundirlaflota.Server;

import org.java_websocket.server.WebSocketServer;
import java.net.InetSocketAddress;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.util.ArrayList;
import org.json.*;

import com.hundirlaflota.Common.*;
import com.hundirlaflota.Common.ServerMessages.*;

public class Server extends WebSocketServer {

  private ArrayList<Player> players = new ArrayList<>();
  private ArrayList<Room> rooms = new ArrayList<>();

  public static void main(String[] args) {
    int port = 8080;
    Server server = new Server(new InetSocketAddress("localhost", port));
    server.start();
    System.out.println("Server started on port " + port);
  }

  public Server(InetSocketAddress address) {
    super(address);
  }

  @Override
  public void onOpen(WebSocket conn, ClientHandshake handshake) {
    System.out.println("New connection: " + conn.getRemoteSocketAddress());
    players.add(new Player(conn));
  }

  @Override
  public void onClose(WebSocket conn, int code, String reason, boolean remote) {
    System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
    Player player = getPlayerByConn(conn);
    if (player != null) {
      players.remove(player);
    }
    Room room = player.getRoom();
    if (room == null) {
      return;
    }
    if (room.isHost(player)) {
      if (room.getInvite() == null) {
        rooms.remove(room);
      }
      else {
        room.PromoteInvite();
      }
    }
    else {
      room.removePlayer(player);
    }
  }

  @Override
  public void onMessage(WebSocket conn, String message) {
    //System.out.println("Received message: " + message);
    Player player = getPlayerByConn(conn);
    JSONObject json = new JSONObject(message);
    MessageType type;
    try {
      type = MessageType.valueOf(json.getString("type"));
    } catch (IllegalArgumentException e) {
      System.out.println("Invalid message type");
      player.sendMessage(new ErrorMessage("Invalid message type", null).toString());
      return;
    } catch (JSONException | NullPointerException e) {
      System.out.println("Invalid message format");
      player.sendMessage(new ErrorMessage("Invalid message format", null).toString());
      return;
    } catch (Exception e) {
      System.out.println("Server error: " + e.getMessage());
      player.sendMessage(new ErrorMessage("Server error: " + e.getMessage(), null).toString());
      return;
    }
    switch (type) {
      case LOGIN:
        String playerName = json.getString("playerName");
        if (playerName.isEmpty()) {
          player.sendMessage(new ErrorMessage("Player name cannot be empty", MessageType.LOGIN).toString());
          break;
        }
        if (playerName.equals("Unnamed")) {
          player.sendMessage(new ErrorMessage("Player name cannot be \"Unnamed\"", MessageType.LOGIN).toString());
          break;
        }
        for (Player p : players) {
          if (p.getName().equals(playerName)) {
            player.sendMessage(new ErrorMessage("Player name already taken", MessageType.LOGIN).toString());
            break;
          }
        }
        player.setName(playerName);
        player.sendMessage(new AckMessage("", MessageType.LOGIN).toString());
        break;
      case LIST_ROOMS:
        JSONArray roomsJson = new JSONArray();
        for (Room room : rooms) {
          roomsJson.put(room.toJSON());
        }
        player.sendMessage(new AckMessage(roomsJson.toString(), MessageType.LIST_ROOMS, false).toString());
        break;
      case CREATE_ROOM:
        String roomName = json.getString("roomName");
        for (Room room : rooms) {
          if (room.getName().equals(roomName)) {
            player.sendMessage(new ErrorMessage("Room name already taken", MessageType.CREATE_ROOM).toString());
            break;
          }
        }
        //TODO: Check if roomName is not empty
        Room room = new Room(player, roomName);
        rooms.add(room);
        player.setRoom(room);
        player.sendMessage(new AckMessage("", MessageType.CREATE_ROOM).toString());
        break;
      case JOIN_ROOM:
        String roomName2 = json.getString("roomName");
        Room room2 = getRoomByName(roomName2);
        if (room2 != null) {
          if (room2.isFull()) {
            player.sendMessage(new ErrorMessage("Room is full", MessageType.JOIN_ROOM).toString());
            break;
          }
          room2.addPlayer(player);
        }
        else {
          player.sendMessage(new ErrorMessage("Room not found", MessageType.JOIN_ROOM).toString());
          break;
        }
        player.setRoom(room2);
        player.sendMessage(new AckMessage("", MessageType.JOIN_ROOM).toString());
        break;
      case ROOM_INFO:
        Room playerRoom = player.getRoom();
        JSONObject roomInfo = playerRoom.toJSON();
        roomInfo.put("isHost", playerRoom.isHost(player));
        if (playerRoom != null) {
          player.sendMessage(new AckMessage(roomInfo.toString(), MessageType.ROOM_INFO,false).toString());
        }
        else {
          player.sendMessage(new ErrorMessage("You are not in a room", MessageType.ROOM_INFO).toString());
        }
        break;
      case LEAVE_ROOM:
        if (player.getRoom() == null)  {
          player.sendMessage(new ErrorMessage("You are not in a room", MessageType.LEAVE_ROOM).toString());
        }
        if (player.getRoom().isHost(player)) {
          if (player.getRoom().getInvite() == null) {
            rooms.remove(player.getRoom());
          }
          else {
            player.getRoom().PromoteInvite();
          }
        }
        else {
          player.getRoom().removePlayer(player);
        }
        player.setRoom(null);
        player.sendMessage(new AckMessage("", MessageType.LEAVE_ROOM).toString());
        break;
      case SET_READY:
        System.out.println("SET_READY" + json.toString());
        boolean ready = json.getBoolean("ready");
        if (player.getRoom() != null) {
          player.getRoom().setReady(player, ready);
          player.sendMessage(new AckMessage("", MessageType.SET_READY).toString());
        }
        else {
          player.sendMessage(new ErrorMessage("You are not in a room", MessageType.SET_READY).toString());
        }
        break;
      //TODO: Add cases for the game messages
      default:
        player.sendMessage(new ErrorMessage("Invalid message type", type).toString());
        break;
    }

  }

  @Override
  public void onError(WebSocket conn, Exception ex) {
    System.out.println("Server error: " + ex.getMessage());
    Player player = getPlayerByConn(conn);
    if (player != null) {
      player.sendMessage(new ErrorMessage("Server error: " + ex.getMessage(), null).toString());
    }
  }

  @Override
  public void onStart() {
    System.out.println("Server started!");
  }

  private Player getPlayerByConn(WebSocket conn) {
    for (Player player : players) {
      if (player.getConn().equals(conn)) {
        return player;
      }
    }
    return null;
  }

  private Room getRoomByName(String name) {
    for (Room room : rooms) {
      if (room.getName().equals(name)) {
        return room;
      }
    }
    return null;
  }
}
