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
        //this.position = (0,0)
        //size = 5
        //position = (5,0)
        if (shipPosition == ShipPosition.HORIZONTAL) {
            //0 >= 0 && 0 <= 5 && 0 == 0
            return this.position.getX() > position.getX()-size && this.position.getX() <= position.getX() && this.position.getY() == position.getY();
        } else {
            //0 >= 0-5 && 0 <= 0 && 0 == 0
            return this.position.getY() >  position.getY()-size && this.position.getY() <= position.getY() && this.position.getX() == position.getX();
        }
    }
}
