package com.hundirlaflota.Client.Canvas;

import javafx.scene.canvas.GraphicsContext;

import com.hundirlaflota.Common.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.*;

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

    @Override
    public void OnDragStart(MouseDragEvent event) {
        if (draggable) {
            // Store the initial mouse position relative to the ship's position
            double initialMouseX = event.getX() - ship.getPosition().getX() * 30;
            double initialMouseY = event.getY() - ship.getPosition().getY() * 30;
            // Calculate the new position based on the mouse movement
            int newX = (int) ((event.getX() - initialMouseX) / 30);
            int newY = (int) ((event.getY() - initialMouseY) / 30);
        }
    }
}
