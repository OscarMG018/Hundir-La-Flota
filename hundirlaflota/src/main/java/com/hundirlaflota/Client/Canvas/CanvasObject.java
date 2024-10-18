package com.hundirlaflota.Client.Canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;

public abstract class CanvasObject {

    public abstract void draw(GraphicsContext gc);

    public void OnClick(MouseEvent event) {}

    public void OnMouseEnter(MouseEvent event) {}

    public void OnMouseExit(MouseEvent event) {}

    public void OnDragStart(MouseEvent event) {}

    public void OnDragEnd(MouseEvent event) {}

    public void OnDrag(MouseEvent event) {}

    public void OnDrop(MouseEvent event, Object source) {} 
}
