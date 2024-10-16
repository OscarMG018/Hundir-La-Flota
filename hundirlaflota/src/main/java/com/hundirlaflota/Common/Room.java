package com.hundirlaflota.Common;

import java.io.Serializable;
import org.json.JSONObject;

public class Room implements Serializable {
   private String name = "";
   private Player host;
   private Player invite;
   private boolean hostReady;
   private boolean inviteReady;

   public Room(Player host, String name) {
    this.host = host;
    this.name = name;
    this.hostReady = false;
    this.inviteReady = false;
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
      if (invite == player) {
         invite = null;
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
   }
}
