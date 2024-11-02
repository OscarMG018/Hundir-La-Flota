package com.hundirlaflota.Server;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONObject;
import com.hundirlaflota.Common.ServerMessages.ShipData;

public class Room implements Serializable {
   private String name = "";
   private Player host;
   private Player invite;
   private boolean hostReady;
   private boolean inviteReady;
   private ArrayList<ShipData> hostShips;
   private ArrayList<ShipData> inviteShips;

   public Room(Player host, String name) {
      this.host = host;
      this.name = name;
      this.hostReady = false;
      this.inviteReady = false;
      this.hostShips = new ArrayList<>();
      this.inviteShips = new ArrayList<>();
   }

   public int getPlayerCount() {
      if (invite == null) {
         return 1;
      }
      return 2;
   }

   @Override
   public String toString() {
      return "{" +
            "name:" + name +
            ", players:" + getPlayerCount() +
            "}";
   }

   public JSONObject toJSON() {
      return new JSONObject()
            .put("name", name)
            .put("players", getPlayerCount())
            .put("hostName", host.getName())
            .put("inviteName", invite != null ? invite.getName() : "")
            .put("hostReady", hostReady)
            .put("inviteReady", inviteReady);
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setReady(Player player, boolean ready) {
      if (player == host) {
         hostReady = ready;
      }
      else {
         inviteReady = ready;
      }
   }

   public boolean isReady(Player player) {
      if (player == host) {
         return hostReady;
      }
      else {
         return inviteReady;
      }
   }

   public Player getHost() {
      return host;
   }

   public Player getInvite() {
      return invite;
   }

   public boolean isBothReady() {
      return hostReady && inviteReady;
   }

   public void addPlayer(Player player) {
      if (invite == null) {
         invite = player;
      }
   }

   public void removePlayer(Player player) {
      player.setRoom(null);
      if (isHost(player)) {
         PromoteInvite();
      }
      else {
         invite = null;
         inviteReady = false;
      }
   }

   public boolean isFull() {
      return invite != null;
   }

   public boolean isEmpty() {
      return host == null && invite == null;
   }

   public boolean isHost(Player player) {
      return host == player;
   }

   public boolean isInvite(Player player) {
      return invite == player;
   }

   public void PromoteInvite() {
      host = invite;
      invite = null;
      hostReady = inviteReady;
      inviteReady = false;
   }

   public ArrayList<ShipData> getHostShips() {
      return hostShips;
   }

   public void setHostShips(ArrayList<ShipData> hostShips) {
      this.hostShips = hostShips;
   }

   public ArrayList<ShipData> getInviteShips() {
      return inviteShips;
   }

   public void setInviteShips(ArrayList<ShipData> inviteShips) {
      this.inviteShips = inviteShips;
   }

   public boolean areShipsReady() {
      return hostShips.size() == inviteShips.size() && hostShips.size() > 0;
}
}
