package com.hundirlaflota.Client.Canvas;

import java.util.ArrayList;

import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Client.Utils.UtilsWS;

import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Client.Utils.UtilsWS;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

import com.hundirlaflota.Common.ServerMessages.MousePositionMessage;
import com.hundirlaflota.Common.ServerMessages.ShootMessage;

public class GridCanvasObject extends CanvasObject {
    private int gridSize;
    private double borderSize;
    private GridCell[][] cells;
    private GridCell hoveredCell;
    private ArrayList<ShipCanvasObject> ships;
    private UtilsWS ws;
    private boolean sendMouseOver = false;
    private Runnable onShipsSet;
    private UtilsWS ws;
    private boolean sendMouseOver = false;
    private Runnable onShipsSet;

    public GridCanvasObject(double x, double y, double size, int zIndex, int gridSize, double borderSize) {
        super(x, y, size, size, zIndex);
        this.gridSize = gridSize;
        this.borderSize = borderSize;
        this.ships = new ArrayList<>();
        this.ships = new ArrayList<>();
        initializeCells();
        this.isClickable = false;
        this.sendMouseOver = false;
        this.ws = UtilsWS.getSharedInstance(Main.UsedLocation);
        this.onShipsSet = null;
    }

    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    public void setSendMouseOver(boolean sendMouseOver) {
        this.sendMouseOver = sendMouseOver;
    }

    public void setOnShipsSet(Runnable onShipsSet) {
        this.onShipsSet = onShipsSet;
        this.isClickable = false;
        this.sendMouseOver = false;
        this.ws = UtilsWS.getSharedInstance(Main.UsedLocation);
        this.onShipsSet = null;
    }

    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    public void setSendMouseOver(boolean sendMouseOver) {
        this.sendMouseOver = sendMouseOver;
    }

    public void setOnShipsSet(Runnable onShipsSet) {
        this.onShipsSet = onShipsSet;
    }

    public void setShipOnGrid(ArrayList<ShipCanvasObject> ships) {
        this.ships = ships;
    }

    private void initializeCells() {
        double cellWidth = (width - (gridSize + 1) * borderSize) / gridSize;
        double cellHeight = (height - (gridSize + 1) * borderSize) / gridSize;
        cells = new GridCell[gridSize][gridSize];

        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                double cellX = x + borderSize + col * (cellWidth + borderSize);
                double cellY = y + borderSize + row * (cellHeight + borderSize);
                cells[row][col] = new GridCell(cellX, cellY, cellWidth, cellHeight, row, col);
            }
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        // Draw grid background
        gc.setFill(Color.BLACK);
        gc.fillRect(x, y, width, height);

        // Draw cells
        for (GridCell[] row : cells) {
            for (GridCell cell : row) {
                cell.draw(gc);
            }
        }
    }

    public void setCellColor(int row, int col, Color color) {
        if (row >= 0 && row < gridSize && col >= 0 && col < gridSize) {
            cells[row][col].setColor(color);
        }
    }

    public double[] getCellCenter(int row, int col) {
        if (row >= 0 && row < gridSize && col >= 0 && col < gridSize) {
            return cells[row][col].getCenter();
        }
        return null;
    }

    public double[] getCellUpperLeftCorner(int row, int col) {
        if (row >= 0 && row < gridSize && col >= 0 && col < gridSize) {
            return cells[row][col].getUpperLeftCorner();
        }
        return null;
    }

    public boolean isPositionValid(int row, int col,int SectionHeld,int size,boolean isHorizontal,ShipCanvasObject placedShip) {
        if (isHorizontal) {
            if (col + size - SectionHeld > gridSize || col - SectionHeld < 0) {
                return false;
            }
        } else {
            if (row + size - SectionHeld > gridSize || row - SectionHeld < 0) {
                return false;
            }
        }
        ArrayList<int[]> shipPositions = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (isHorizontal) {
                shipPositions.add(new int[]{row,col+i-SectionHeld});
            }
            else {
                shipPositions.add(new int[]{row+i-SectionHeld,col});
            }
        }

        for (int[] pos : shipPositions) {
            double[] center = getCellCenter(pos[0], pos[1]);
            for (ShipCanvasObject ship : ships) {
                if (ship.equals(placedShip))
                    continue;
                if (ship.isPointInObject(center[0],center[1])) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void OnDrop(MouseEvent event, CanvasObject source) {
        if (source instanceof ShipCanvasObject) {
            GridCell cell = getCellFromPoint(event.getX(), event.getY());
            if (cell != null) {
                boolean horizontal = ((ShipCanvasObject) source).isHorizontal();
                int size = ((ShipCanvasObject) source).getSize();
                int SectionHeld = ((ShipCanvasObject) source).getShipSection();
                if (!isPositionValid(cell.getRow(), cell.getCol(),SectionHeld, size, horizontal,(ShipCanvasObject) source)) {
                    ((ShipCanvasObject) source).CancelDrag();
                    return;
                }
                double[] center = cell.getCenter();
                if (horizontal) {
                    center[0] += getCellSize() / 2 * (size - 1);
                    center[0] -= getCellSize() * SectionHeld;
                } else {
                    center[1] += getCellSize() / 2 * (size - 1);
                    center[1] -= getCellSize() * SectionHeld;
                }
                ((ShipCanvasObject) source).setPosition(center[0], center[1]);
                ((ShipCanvasObject) source).setCellPosition(cell.getRow(), cell.getCol());
                if (onShipsSet != null) {
                    onShipsSet.run();
                }
            }
        }
    }

    @Override
    public void OnMouseOver(MouseEvent event) {
        if (!sendMouseOver)
            return;
        if (!sendMouseOver)
            return;
        GridCell cell = getCellFromPoint(event.getX(), event.getY());
        if (cell != null && cell != hoveredCell) {
            ws.safeSend(new MousePositionMessage(cell.getCol(), cell.getRow()).toString());
            hoveredCell = cell;
        }
    }


    @Override
    public void OnClick(MouseEvent event) {
        GridCell cell = getCellFromPoint(event.getX(), event.getY());
        if (cell != null) {
            System.out.println("Shooting at: " + cell.getCol() + ", " + cell.getRow());
            ws.safeSend(new ShootMessage(cell.getCol(), cell.getRow()).toString());
        }
    }

    private GridCell getCellFromPoint(double x, double y) {
        for (GridCell[] row : cells) {
            for (GridCell cell : row) {
                if (cell.contains(x, y)) {
                    return cell;
                }
            }
        }
        return null;
    }

    public double getCellSize() {
        return (width - (gridSize + 1) * borderSize) / gridSize;
    }

    public void clearColors() {
        for (GridCell[] row : cells) {
            for (GridCell cell : row) {
                cell.setColor(Color.WHITE);
            }
        }
    }
}
