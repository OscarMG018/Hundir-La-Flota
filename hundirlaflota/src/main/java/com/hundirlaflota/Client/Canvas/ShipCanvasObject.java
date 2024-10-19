package com.hundirlaflota.Client.Canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.*;

public class ShipCanvasObject extends CanvasObject {

    boolean Draggable;
    private String ShipName;
    private double XOffset;
    private double YOffset;

    public ShipCanvasObject(String ShipName, double X, double Y, double Width, double Height, int zIndex, boolean Draggable) {
        super(X, Y, Width, Height, zIndex);
        this.ShipName = ShipName;
        this.Draggable = Draggable;
    }

    @Override
    public void draw(GraphicsContext gc) {
        Image ship = new Image(getClass().getResourceAsStream("/com/hundirlaflota/Client/Assets/ship.png"));
        gc.drawImage(ship, x, y, width, height);
    }

    @Override
    public void OnDragStart(MouseEvent event) {
        if (Draggable) {
            XOffset = event.getX() - x;
            YOffset = event.getY() - y;
        }
    }

    @Override
    public void OnDrag(MouseEvent event) {
        if (Draggable) {
            x = event.getX() - XOffset;
            y = event.getY() - YOffset;
        }
    }

}
