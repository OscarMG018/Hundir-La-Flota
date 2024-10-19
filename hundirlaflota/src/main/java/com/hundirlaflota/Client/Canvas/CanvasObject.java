package com.hundirlaflota.Client.Canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public abstract class CanvasObject {

    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected int zIndex;


    public CanvasObject(double x, double y, double width, double height, int zIndex) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
    }

    public abstract void draw(GraphicsContext gc);

    public void OnClick(MouseEvent event) {}

    public void OnMouseEnter(MouseEvent event) {}

    public void OnMouseExit(MouseEvent event) {}

    public void OnDragStart(MouseEvent event) {}

    public void OnDragEnd(MouseEvent event) {}

    public void OnDrag(MouseEvent event) {}

    public void OnDrop(MouseEvent event, Object source) {} 

    public boolean isPointInObject(double x, double y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public int getzIndex() {
        return zIndex;
    }
    
}
