package com.hundirlaflota.Common;

import java.util.*;

public class Game {
    Player player1;
    Player player2;
    ArrayList<Ship> player1Ships = new ArrayList<>();
    ArrayList<Ship> player2Ships = new ArrayList<>();
    boolean turn = false;

    public Game(Player player1,Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void SetSPlayerShips(int player, ArrayList<Ship> ships) {
        if (player == 1) {
            player1Ships = ships;
        }
        else if (player == 2) {
            player2Ships = ships;
        }
    }

    public void PassTurn() {
        turn = !turn;
    }

    public boolean Shoot(Position position) {
        if (turn) {//Player 1 shoots
            for (Ship ship : player2Ships) {
                if (ship.HasPosition(position)) {
                    ship.hit(position);
                    if (ship.isSunk()) {
                        sinkShip(2, ship);
                    }
                    return true;//Hit
                }
            }
        }
        else {//Player 2 shoots
            for (Ship ship : player1Ships) {
                if (ship.HasPosition(position)) {
                    ship.hit(position);
                    if (ship.isSunk()) {
                        sinkShip(1, ship);
                    }
                    return true;//Hit
                }
            }
        }
        return false;//Miss
    }

    public void sinkShip(int player, Ship ship) {
        if (player == 1) {
            player1Ships.remove(ship);
        }
        else if (player == 2) {
            player2Ships.remove(ship);
        }
    }

    public int isGameOver() {
        if (player1Ships.isEmpty()) {
            return 2;
        }
        else if (player2Ships.isEmpty()) {
            return 1;
        }
        return 0;
    }
}
