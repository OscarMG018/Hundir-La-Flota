package com.hundirlaflota.Client.ViewControllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import com.hundirlaflota.Client.Canvas.*;
import com.hundirlaflota.Common.Ship;


public class ShipsViewController implements Initializable, OnSceneVisible {

    @FXML
    private VBox root;
    @FXML
    private Canvas canvas;
    private CanvasManager canvasManager;
    private GridCanvasObject grid;

    private ArrayList<ShipCanvasObject> ships;
    private double cellSize;

    @Override
    public void onSceneVisible() {
        System.out.println("Ships visible");
        canvasManager.clear();
        grid = new GridCanvasObject(0, 0, canvas.getHeight(), 0, 10, 0);
        canvasManager.addObject(grid);
        // Add ships to the side
        cellSize = grid.getCellSize();
        ShipCanvasObject ship1 = new ShipCanvasObject("Destroyer", canvas.getHeight() + cellSize, 0, cellSize, 2, 1, true, true);
        ShipCanvasObject ship2 = new ShipCanvasObject("Destroyer", canvas.getHeight() + cellSize, cellSize, cellSize, 2, 1, true, true);
        ShipCanvasObject ship3 = new ShipCanvasObject("Destroyer", canvas.getHeight() + cellSize, 2 * cellSize, cellSize, 3, 1, true, true);
        ShipCanvasObject ship4 = new ShipCanvasObject("Destroyer", canvas.getHeight() + cellSize, 3 * cellSize, cellSize, 3, 1, true, true);
        ShipCanvasObject ship5 = new ShipCanvasObject("Destroyer", canvas.getHeight() + cellSize, 4 * cellSize, cellSize, 4, 1, true, true);
        ShipCanvasObject ship6 = new ShipCanvasObject("Destroyer", canvas.getHeight() + cellSize, 5 * cellSize, cellSize, 5, 1, true, true);
        canvasManager.addObject(ship1);
        canvasManager.addObject(ship2);
        canvasManager.addObject(ship3);
        canvasManager.addObject(ship4);
        canvasManager.addObject(ship5);
        canvasManager.addObject(ship6);
        ships = new ArrayList<>();
        ships.add(ship1);
        ships.add(ship2);
        ships.add(ship3);
        ships.add(ship4);
        ships.add(ship5);
        ships.add(ship6);

        canvasManager.draw();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvasManager = new CanvasManager(canvas);

        root.setFocusTraversable(true);
        root.requestFocus();
        root.setOnKeyPressed(event -> {
            System.out.println("Key pressed: " + event.getCode());
            if (event.getCode() == KeyCode.R) {
                CanvasObject dragObject = canvasManager.getDragObject();
                if (dragObject != null && dragObject instanceof ShipCanvasObject) {
                    ShipCanvasObject ship = (ShipCanvasObject) dragObject;
                    //move the ships to have the same offset when rotated
                    int section = ship.getShipSection();
                    int size = ship.getSize();
                    if (ship.isHorizontal()) {
                        ship.setY(ship.getY() - (cellSize * (size-section-1)));
                        ship.setX(ship.getX() + (cellSize * section));
                        ship.setYOffset(ship.getYOffset() + (cellSize * (size-section-1)));
                        ship.setXOffset(ship.getXOffset() - (cellSize * section));
                    } else {
                        System.out.println("Vertical:" + cellSize);
                        ship.setY(ship.getY() + (cellSize * (section)));
                        ship.setX(ship.getX() - (cellSize * (size-section-1)));
                        ship.setYOffset(ship.getYOffset() - (cellSize * section));
                        ship.setXOffset(ship.getXOffset() + (cellSize * (size-section-1)));
                    }
                    //change the orientation
                    ship.setHorizontal(!ship.isHorizontal());
                    ship.setWidth(ship.isHorizontal() ? cellSize * size : cellSize);
                    ship.setHeight(ship.isHorizontal() ? cellSize : cellSize * size);
                    canvasManager.draw();
                }
            }
        });
    }

    public ArrayList<Ship> getShips() {
        ArrayList<Ship> ships = new ArrayList<>();
        for(ShipCanvasObject ship : this.ships) {
            ships.add(ship.getShip());
        }
        return ships;
    }
}
