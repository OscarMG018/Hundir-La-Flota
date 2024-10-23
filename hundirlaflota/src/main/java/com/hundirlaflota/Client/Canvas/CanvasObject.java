package com.hundirlaflota.Client.Canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

public abstract class CanvasObject {

    protected double x;
    protected double y;
    protected double width;
    protected double height;
    protected int zIndex;
    protected ArrayList<CanvasObject> children = new ArrayList<>();
    protected boolean isDraggable = false;

    public CanvasObject(double x, double y, double width, double height, int zIndex) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.zIndex = zIndex;
    }

    public void addChild(CanvasObject child) {
        children.add(child);
    }

    public void removeChild(CanvasObject child) {
        children.remove(child);
    }

    public ArrayList<CanvasObject> getChildren() {
        return children;
    }

    public abstract void draw(GraphicsContext gc);

    public void OnClick(MouseEvent event) {}

    public void OnMouseEnter(MouseEvent event) {}

    public void OnMouseExit(MouseEvent event) {}

    public void OnMouseOver(MouseEvent event) {}

    public void OnDragStart(MouseEvent event) {}

    public void OnDragEnd(MouseEvent event) {}

    public void OnDrag(MouseEvent event) {}

    public void OnDrop(MouseEvent event, CanvasObject source) {} 

    public boolean isPointInObject(double x, double y) {
        return x >= this.x && x <= this.x + this.width && y >= this.y && y <= this.y + this.height;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getzIndex() {
        return zIndex;
    }

    public void setzIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    public void setPosition(double x, double y) {
        this.x = x - width / 2;
        this.y = y - height / 2;
    }
}
