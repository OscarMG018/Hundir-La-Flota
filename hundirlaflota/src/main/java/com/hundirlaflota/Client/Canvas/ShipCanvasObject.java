package com.hundirlaflota.Client.Canvas;

import com.hundirlaflota.Common.Ship;
import com.hundirlaflota.Common.Position;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.paint.Color;

public class ShipCanvasObject extends CanvasObject {

    private String ShipName;
    private double XOffset;
    private double YOffset;
    boolean horizontal;
    private int size;
    private boolean dragging;
    private double cellSize;

    public ShipCanvasObject(String ShipName, double X, double Y, double cellSize, int size, int zIndex, boolean isDraggable, boolean horizontal) {
        super(X, Y, horizontal ? cellSize * size : cellSize, horizontal ? cellSize : cellSize * size, zIndex);
        this.size = size;
        this.ShipName = ShipName;
        this.isDraggable = isDraggable;
        this.horizontal = horizontal;
        this.cellSize = cellSize;
    }

    public int getSize() {
        return size;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public double getXOffset() {
        return XOffset;
    }

    public void setXOffset(double xOffset) {
        this.XOffset = xOffset;
    }

    public double getYOffset() {
        return YOffset;
    }

    public void setYOffset(double yOffset) {
        this.YOffset = yOffset;
    }

    @Override
    public void draw(GraphicsContext gc) {
        Image ship = new Image(getClass().getResourceAsStream("/images/" + ShipName + (horizontal ? "_H" : "_V") + ".png"));
        gc.drawImage(ship, x, y, width, height);
        /*gc.setFill(Color.RED);
        gc.fillRect(x, y, 10, 10);//Debug position*/
    }

    @Override
    public void OnDragStart(MouseEvent event) {
        if (isDraggable) {
            dragging = true;
            XOffset = event.getX() - x;
            YOffset = event.getY() - y;
        }
    }

    @Override
    public void OnDrag(MouseEvent event) {
        if (isDraggable) {
            x = event.getX() - XOffset;
            y = event.getY() - YOffset;
        }
    }

    @Override
    public void OnDragEnd(MouseEvent event) {
        dragging = false;
    }

    public void OnClick(MouseEvent event) {
        if (dragging && event.getButton() == MouseButton.SECONDARY) {
            this.horizontal = !this.horizontal;
        }
    }

    public int getShipSection() {
        double cellWidth = horizontal ? width / size : width;
        double cellHeight = horizontal ? height : height / size;
        
        int section;
        if (horizontal) {
            section = (int) (XOffset / cellWidth);
        } else {
            section = (int) (YOffset / cellHeight);
        }
        
        // Ensure the section is within the valid range
        section = Math.max(0, Math.min(section, size - 1));
        System.out.println("Ship section: " + section);
        return section;
    }

    public Ship getShip() {
        int gridX = (int) (x / cellSize);
        int gridY = (int) (y / cellSize);
        Position position = new Position(gridX, gridY);
        return new Ship(ShipName, size, position, horizontal ? Ship.ShipPosition.HORIZONTAL : Ship.ShipPosition.VERTICAL);
    }
}
