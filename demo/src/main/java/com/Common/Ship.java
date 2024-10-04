package com.example.Common;

import java.util.ArrayList;
import java.util.Collections;

public class Ship {
    String name;
    int size;
    Position position;
    boolean isHorizontal;
    ArrayList<Boolean> hits;

    public Ship(String name, int size, Position position, boolean isHorizontal) {
        this.name = name;
        this.size = size;
        this.position = position;
        this.isHorizontal = isHorizontal;
        this.hits = new ArrayList<Boolean>(Collections.nCopies(size, false));
    }

    public boolean HasPosition(Position position) {
        if (isHorizontal) {
            return this.position.x >= position.x-size && this.position.y == position.y;
        } else 
            return this.position.y >= position.y-size && this.position.x == position.x;
    }
}
