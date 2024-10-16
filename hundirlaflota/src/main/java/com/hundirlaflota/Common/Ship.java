package com.hundirlaflota.Common;

import java.util.ArrayList;
import java.util.Collections;



public class Ship {

    public enum ShipPosition {
        HORIZONTAL,
        VERTICAL
    }

    String name;
    int size;
    Position position;
    ShipPosition shipPosition;
    ArrayList<Boolean> hits;


    public Ship(String name, int size, Position position, ShipPosition shipPosition) {
        this.name = name;
        this.size = size;
        this.position = position;
        this.shipPosition = shipPosition;
        this.hits = new ArrayList<Boolean>(Collections.nCopies(size, false));
    }

    public boolean HasPosition(Position position) {
        if (shipPosition == ShipPosition.HORIZONTAL) {
            return this.position.getX() > position.getX()-size && this.position.getX() <= position.getX() && this.position.getY() == position.getY();
        } else {
            return this.position.getY() > position.getY()-size && this.position.getY() <= position.getY() && this.position.getX() == position.getX();
        }
    }

    public Position getPosition() {
        return position;
    }

    public ShipPosition getShipPosition() {
        return shipPosition;
    }

    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }
}
