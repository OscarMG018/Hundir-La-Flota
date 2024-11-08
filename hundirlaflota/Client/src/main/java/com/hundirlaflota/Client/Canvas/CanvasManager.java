package com.hundirlaflota.Client.Canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class CanvasManager {
    private Canvas canvas;
    private GraphicsContext gc;
    private CopyOnWriteArrayList<CanvasObject> objects;

    private ArrayList<CanvasObject> hoverObjects;
    private CanvasObject dragObject;
    private double lastDragX;
    private double lastDragY;

    public CanvasManager(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.objects = new CopyOnWriteArrayList<>();
        this.hoverObjects = new ArrayList<>();
        setupEventHandlers();
    }

    public ArrayList<CanvasObject> getHoverObjects() {
        return hoverObjects;
    }

    public CanvasObject getDragObject() {
        return dragObject;
    }

    private void setupEventHandlers() {
        canvas.setOnMouseClicked(this::handleMouseClick);
        canvas.setOnMouseMoved(this::handleMouseMove);
        canvas.setOnMouseDragged(this::handleMouseDrag);
        canvas.setOnMouseReleased(this::handleMouseRelease);
    }

    public void addObject(CanvasObject object) {
        objects.add(object);
        for (CanvasObject child : object.getChildren()) {
            addObjectRecursive(child);
        }
        sortObjects();
    }

    private void addObjectRecursive(CanvasObject object) {
        objects.add(object);
        for (CanvasObject child : object.getChildren()) {
            addObjectRecursive(child);
        }
    }

    public void removeObject(CanvasObject object) {
        objects.remove(object);
        sortObjects();
    }

    public void clear() {
        objects.clear();
    }

    public void sortObjects() {
        objects.sort((o1, o2) -> o2.getzIndex() - o1.getzIndex());
    }

    public void draw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int i = objects.size() - 1; i >= 0; i--) {
            if (objects.get(i) == dragObject)
                continue;
            objects.get(i).draw(gc);
        }
        if (dragObject != null) {
            dragObject.draw(gc);
        }
    }

    private void handleMouseClick(MouseEvent event) {
        for (CanvasObject object : objects) {
            if (object.isClickable() && isPointInObject(event.getX(), event.getY(), object)) {
                object.OnClick(event);
                break;
            }
        }
        draw();
    }

    private void handleMouseMove(MouseEvent event) {
        for (CanvasObject object : objects) {
            if (isPointInObject(event.getX(), event.getY(), object)) {
                if (!hoverObjects.contains(object)) {
                    hoverObjects.add(object);
                    object.OnMouseEnter(event);
                }
                object.OnMouseOver(event);
            } else if (hoverObjects.contains(object)) {
                hoverObjects.remove(object);
                object.OnMouseExit(event);
            }
        }
        draw();
    }

    private void handleMouseDrag(MouseEvent event) {
        double deltaX = event.getX() - lastDragX;
        double deltaY = event.getY() - lastDragY;
        
        if (dragObject != null) {
            dragObject.OnDrag(event);
            draw();
            lastDragX = event.getX();
            lastDragY = event.getY();
            return;
        }

        for (CanvasObject object : objects) {
            if (object.isDraggable && isPointInObject(event.getX(), event.getY(), object)) {
                dragObject = object;
                dragObject.OnDragStart(event);
                dragObject.OnDrag(event);
                break;
            }
        }
        
        draw();
        lastDragX = event.getX();
        lastDragY = event.getY();
    }

    private void handleMouseRelease(MouseEvent event) {
        if (dragObject == null)
            return;
        dragObject.OnDragEnd(event);
        for (CanvasObject object : objects) {
            if (dragObject != object && isPointInObject(event.getX(), event.getY(), object)) {
                object.OnDrop(event, dragObject);
                break;
            }
        }
        dragObject = null;
        draw();
    }

    private boolean isPointInObject(double x, double y, CanvasObject object) {
        return object.isPointInObject(x, y);
    }

    private boolean isPointInObject(double x, double y, CanvasObject object, double offsetX, double offsetY) {
        return object.isPointInObject(x - offsetX, y - offsetY);
    }
}
