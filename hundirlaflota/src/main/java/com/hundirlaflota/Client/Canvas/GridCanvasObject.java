package com.hundirlaflota.Client.Canvas;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;

public class GridCanvasObject extends CanvasObject {
    private int gridSize;
    private double borderSize;
    private GridCell[][] cells;
    private GridCell hoveredCell;

    public GridCanvasObject(double x, double y, double size, int zIndex, int gridSize, double borderSize) {
        super(x, y, size, size, zIndex);
        this.gridSize = gridSize;
        this.borderSize = borderSize;
        initializeCells();
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

    @Override
    public void OnDrop(MouseEvent event, CanvasObject source) {
        if (source instanceof ShipCanvasObject) {
            GridCell cell = getCellFromPoint(event.getX(), event.getY());
            if (cell != null) {
                double[] center = cell.getCenter();
                boolean horizontal = ((ShipCanvasObject) source).isHorizontal();
                int SectionHeld = ((ShipCanvasObject) source).getShipSection();
                int size = ((ShipCanvasObject) source).getSize();
                if (horizontal) {
                    center[0] += getCellSize() / 2 * (size - 1);
                    center[0] -= getCellSize() * SectionHeld;
                } else {
                    center[1] += getCellSize() / 2 * (size - 1);
                    center[1] -= getCellSize() * SectionHeld;
                }
                ((ShipCanvasObject) source).setPosition(center[0], center[1]);
                System.out.println("Ship dropped on cell: " + cell.getRow() + ", " + cell.getCol());
            }
        }
    }

    @Override
    public void OnMouseOver(MouseEvent event) {
        GridCell cell = getCellFromPoint(event.getX(), event.getY());
        if (cell != null && cell != hoveredCell) {
            //System.out.println("Mouse entered cell: " + cell.getRow() + ", " + cell.getCol());
            hoveredCell = cell;
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
