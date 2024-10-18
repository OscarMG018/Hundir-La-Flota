package com.hundirlaflota.Client.Canvas;

import javafx.scene.canvas.GraphicsContext;

import com.hundirlaflota.Common.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;

public class ShipCanvasObject extends CanvasObject {

    public Ship ship;
    public boolean draggable;

    public ShipCanvasObject(Ship ship, boolean draggable) {
        this.ship = ship;
        this.draggable = draggable;
    }

    @Override
    public void draw(GraphicsContext gc) {
        Image image = new Image(getClass().getResourceAsStream("/ship.png"));
        gc.drawImage(image, ship.getPosition().getX() * 30, ship.getPosition().getY() * 30, 30, 30);
    }
}
