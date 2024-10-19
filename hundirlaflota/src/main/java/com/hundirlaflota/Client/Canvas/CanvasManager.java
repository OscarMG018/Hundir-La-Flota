package com.hundirlaflota.Client.Canvas;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.MouseButton;

public class CanvasManager {
    private Canvas canvas;
    private GraphicsContext gc;
    private List<CanvasObject> objects;

    private CanvasObject hoverObject;
    private CanvasObject dragObject;

    public CanvasManager(Canvas canvas) {
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
        this.objects = new ArrayList<>();

        setupEventHandlers();
    }

    public CanvasObject getHoverObject() {
        return hoverObject;
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
        sortObjects(false);// only sort after recursive addition
    }

    private void addObjectRecursive(CanvasObject object) {
        objects.add(object);
        for (CanvasObject child : object.getChildren()) {
            addObjectRecursive(child);
        }
    }

    public void removeObject(CanvasObject object) {
        objects.remove(object);
        sortObjects(false);
    }

    public void clear() {
        objects.clear();
    }

    public void sortObjects(boolean ascending) {
        objects.sort((o1, o2) -> ascending ? o1.getzIndex() - o2.getzIndex() : o2.getzIndex() - o1.getzIndex());
    }

    public void draw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        sortObjects(true);
        for (CanvasObject object : objects) {
            object.draw(gc);
        }
        sortObjects(false);
    }

    private void handleMouseClick(MouseEvent event) {
        System.out.println("Mouse clicked");
        for (CanvasObject object : objects) {
            if (isPointInObject(event.getX(), event.getY(), object)) {
                object.OnClick(event);
                break;
            }
        }
        draw();
    }

    private void handleMouseMove(MouseEvent event) {
        for (CanvasObject object : objects) {
            if (isPointInObject(event.getX(), event.getY(), object)) {
                if (hoverObject != object) {
                    hoverObject = object;
                    hoverObject.OnMouseEnter(event);
                }
                object.OnMouseOver(event);
                break;
            } else if (hoverObject == object) {
                hoverObject = null;
                object.OnMouseExit(event);
            }
        }
        draw();
    }

    private void handleMouseDrag(MouseEvent event) {
        for (CanvasObject object : objects) {
            if (isPointInObject(event.getX(), event.getY(), object)) {
                if (dragObject != object) {
                    dragObject = object;
                    dragObject.OnDragStart(event);
                }
                dragObject.OnDrag(event);
                break;
            }
        }
        draw();
    }

    private void handleMouseRelease(MouseEvent event) {
        for (CanvasObject object : objects) {
            if (isPointInObject(event.getX(), event.getY(), object)) {
                System.out.println("OnDragEnd");
                object.OnDragEnd(event);
                for (CanvasObject object2 : objects) {
                    if (object2 == object)
                        continue;
                    if (isPointInObject(event.getX(), event.getY(), object2)) {
                        System.out.println("OnDrop");
                        object2.OnDrop(event, object);
                        break;
                    }
                }
                dragObject = null;
                break;
            }
        }
        draw();
    }

    private boolean isPointInObject(double x, double y, CanvasObject object) {
        return object.isPointInObject(x, y);
    }
}
