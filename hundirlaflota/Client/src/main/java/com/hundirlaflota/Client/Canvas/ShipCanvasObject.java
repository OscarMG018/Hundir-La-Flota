
package com.hundirlaflota.Client.Canvas;

import java.util.ArrayList;

import org.json.JSONObject;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import com.hundirlaflota.Client.Utils.*;
import com.hundirlaflota.Common.ServerMessages.ShipData;

public class ShipCanvasObject extends CanvasObject {

    private String ShipName;
    private double XOffset;
    private double YOffset;
    boolean horizontal;
    private int size;
    private boolean dragging;
    private double cellSize;
    private double dragx = 0;
    private double dragy = 0;
    private Position cellPosition;

    public ShipCanvasObject(String ShipName, double X, double Y, double cellSize, int size, int zIndex, boolean isDraggable, boolean horizontal) {
        super(X, Y, horizontal ? cellSize * size : cellSize, horizontal ? cellSize : cellSize * size, zIndex, false, isDraggable);
        this.size = size;
        this.ShipName = ShipName;
        this.horizontal = horizontal;
        this.cellSize = cellSize;
        this.cellPosition = new Position(-1, -1);
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
            dragx = x;
            dragy = y;
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

    public void CancelDrag() {
        x = dragx;
        y = dragy;
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
        return section;
    }

    public static ShipCanvasObject fromJson(JSONObject jsonObject, double cellSize, double Gridx, double Gridy) {
        String ShipName = jsonObject.getString("shipName");
        boolean horizontal = !jsonObject.getBoolean("isVertical");
        double x = jsonObject.getInt("x") * cellSize + Gridx;
        double y = jsonObject.getInt("y") * cellSize + Gridy;
        int size = getSizeFromShipName(ShipName);
        return new ShipCanvasObject(ShipName, x, y, cellSize, size, 1, false, horizontal);
    }

    public ShipData getShipData(double Gridx, double Gridy, double cellSize) {
        int[] position = getPosition(Gridx, Gridy, cellSize);
        return new ShipData(ShipName, !horizontal, position[0], position[1]);
    }

    private static int getSizeFromShipName(String shipName) {
        switch (shipName.toLowerCase()) {
            case "aircraftcarrier": return 5;
            case "battleship": return 4;
            case "cruiser": return 3;
            case "submarine": return 3;
            case "destroyer": return 2;
        }
        return 0;
    }

    public int[] getPosition(double Gridx, double Gridy, double cellSize) {
        return new int[] {(int) ((x-Gridx)/cellSize), (int) ((y-Gridy)/cellSize)};
    }

    public void setCellPosition(int row, int col) {
        this.cellPosition = new Position(row, col);
    }

    public Position getCellPosition() {
        return cellPosition;
    }
}
