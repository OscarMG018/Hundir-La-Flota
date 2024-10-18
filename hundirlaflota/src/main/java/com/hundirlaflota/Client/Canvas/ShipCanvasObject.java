package com.hundirlaflota.Client.Canvas;

import javafx.scene.canvas.GraphicsContext;

import com.hundirlaflota.Common.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.*;

public class ShipCanvasObject extends CanvasObject {

    public Ship ship;
    public boolean draggable;
    public boolean isDragging;
    private double dragOffsetX;
    private double dragOffsetY;

    public ShipCanvasObject(Ship ship, boolean draggable) {
        this.ship = ship;
        this.draggable = draggable;
    }

    @Override
    public void draw(GraphicsContext gc) {
        Image image = new Image(getClass().getResourceAsStream("/ship.png"));
        if (isDragging) {
            gc.drawImage(image, event.getX() - dragOffsetX, event.getY() - dragOffsetY, 30, 30);
        }
        else {
            gc.drawImage(image, ship.getPosition().getX() * 30, ship.getPosition().getY() * 30, 30, 30);
        }
    }

    @Override
    public void OnDragStart(MouseDragEvent event) {
        if (draggable) {
            dragOffsetX = event.getX() - ship.getPosition().getX() * 30;
            dragOffsetY = event.getY() - ship.getPosition().getY() * 30;
        }
    }

    @Override
    public void OnDrag(MouseDragEvent event) {
        if (draggable) {
            double newX = (event.getX() - dragOffsetX) / 30;
            double newY = (event.getY() - dragOffsetY) / 30;
            ship.setPosition(new Position((int)newX, (int)newY));
        }
    }
}
