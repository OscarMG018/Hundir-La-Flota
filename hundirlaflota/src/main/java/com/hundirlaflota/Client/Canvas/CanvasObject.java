package com.hundirlaflota.Client.Canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseDragEvent;

public abstract class CanvasObject {

    public abstract void draw(GraphicsContext gc);

    public void OnClick(MouseEvent event) {}

    public void OnMouseEnter(MouseEvent event) {}

    public void OnMouseExit(MouseEvent event) {}

    public void OnDragStart(MouseDragEvent event) {}

    public void OnDragEnd(MouseDragEvent event) {}

    public void OnDrag(MouseDragEvent event) {}

    public void OnDrop(MouseDragEvent event, Object source) {} 
}
