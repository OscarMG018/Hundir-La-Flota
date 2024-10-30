package com.hundirlaflota.Client.Canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GridCell {
    private double x;
    private double y;
    private double width;
    private double height;
    private int row;
    private int col;
    private Color color;

    public GridCell(double x, double y, double width, double height, int row, int col) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.row = row;
        this.col = col;
        this.color = Color.WHITE;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(color);
        gc.fillRect(x, y, width, height);
        gc.setStroke(Color.BLACK);
        gc.strokeRect(x, y, width, height);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double[] getCenter() {
        return new double[]{x + width / 2, y + height / 2};
    }

    public double[] getUpperLeftCorner() {
        return new double[]{x, y};
    }

    public boolean contains(double px, double py) {
        return px >= x && px < x + width && py >= y && py < y + height;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
