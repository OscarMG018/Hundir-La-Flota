package com.hundirlaflota.Client.ViewControllers;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.application.Platform;

import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Client.Canvas.*;
import com.hundirlaflota.Client.Utils.Position;
import com.hundirlaflota.Client.Utils.UtilsViews;
import com.hundirlaflota.Client.Utils.UtilsWS;
import com.hundirlaflota.Common.ServerMessages.*;

public class ShipsViewController implements Initializable, OnSceneVisible {

    @FXML
    private VBox root;
    @FXML
    private Canvas canvas;
    @FXML
    private Button SetShipsButton;
    private CanvasManager canvasManager;
    private GridCanvasObject grid;

    private ArrayList<ShipCanvasObject> ships;
    private double cellSize;

    private UtilsWS ws;

    @Override
    public void onSceneVisible() {

        ws = UtilsWS.getSharedInstance(Main.UsedLocation);
        ws.setOnMessage(this::handleMessage);
        System.out.println("Ships visible");
        canvasManager.clear();
        grid = new GridCanvasObject(0, 0, canvas.getHeight(), 0, 10, 0);
        grid.setOnShipsSet(this::checkAllShipsSet);

        canvasManager.addObject(grid);
        // Add ships to the side
        cellSize = grid.getCellSize();
        ShipCanvasObject ship1 = new ShipCanvasObject("Destroyer", canvas.getHeight() + cellSize, 0, cellSize, 2, 1, true, true);
        ShipCanvasObject ship2 = new ShipCanvasObject("Cruiser", canvas.getHeight() + cellSize, cellSize, cellSize, 3, 1, true, true);
        ShipCanvasObject ship3 = new ShipCanvasObject("Submarine", canvas.getHeight() + cellSize, 2 * cellSize, cellSize, 3, 1, true, true);
        ShipCanvasObject ship4 = new ShipCanvasObject("Battleship", canvas.getHeight() + cellSize, 3 * cellSize, cellSize, 4, 1, true, true);
        ShipCanvasObject ship5 = new ShipCanvasObject("AircraftCarrier", canvas.getHeight() + cellSize, 4 * cellSize, cellSize, 5, 1, true, true);
        canvasManager.addObject(ship1);
        canvasManager.addObject(ship2);
        canvasManager.addObject(ship3);
        canvasManager.addObject(ship4);
        canvasManager.addObject(ship5);
        ships = new ArrayList<>();
        ships.add(ship1);
        ships.add(ship2);
        ships.add(ship3);
        ships.add(ship4);
        ships.add(ship5);
        grid.setShipOnGrid(ships);
        canvasManager.draw();
        SetShipsButton.setDisable(true);
        SetShipsButton.setOnAction(event -> {
            ArrayList<ShipData> shipsData = new ArrayList<>();
            for (ShipCanvasObject ship : ships) {
                shipsData.add(ship.getShipData(grid.getX(), grid.getY(), cellSize));
            }
            NoTurnViewController c = (NoTurnViewController) UtilsViews.getController("NoTurn");
            ArrayList<ShipCanvasObject> adjustedShips = new ArrayList<>();
            for (ShipData ship : shipsData) {
                adjustedShips.add(ShipCanvasObject.fromJson(ship.toJson(), c.getCellSize(), c.getGridX(), c.getGridY()));
            }
            c.setMyShips(adjustedShips);
            ws.safeSend(new PutShipsMessage(shipsData).toString());
            SetShipsButton.setDisable(true);
            SetShipsButton.setText("Ships are set");
        });

        UtilsViews.parentContainer.setFocusTraversable(true);
        UtilsViews.parentContainer.requestFocus();
        UtilsViews.parentContainer.setOnKeyPressed(event -> {
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvasManager = new CanvasManager(canvas);
    }

    public void checkAllShipsSet() {
        SetShipsButton.setText("Set ships");
        for (ShipCanvasObject ship : ships) {
            Position pos = ship.getCellPosition();
            if (pos.getX() == -1 || pos.getY() == -1) {
                SetShipsButton.setDisable(true);
                return;
            }
        }
        SetShipsButton.setDisable(false);
    }

    public void handleMessage(String message) {
        JSONObject json = new JSONObject(message);
        MessageType type = MessageType.valueOf(json.getString("type"));
        if (type == MessageType.STARTING_PLAYER) {
            boolean starts = json.getBoolean("isStarting");
            ArrayList<ShipCanvasObject> ships = new ArrayList<>();
            JSONArray shipsJson = json.getJSONArray("opponentsShips");
            TurnViewController c = (TurnViewController) UtilsViews.getController("Turn");
            c.setOpponentShips(ships);
            for (int i = 0; i < shipsJson.length(); i++) {
                ships.add(ShipCanvasObject.fromJson(shipsJson.getJSONObject(i), c.getCellSize(), c.getGridX(), c.getGridY()));
            }
            Platform.runLater(() -> {
                UtilsViews.parentContainer.setFocusTraversable(false);
                UtilsViews.parentContainer.setOnKeyPressed(null);
                if (starts) {
                    UtilsViews.setView("Turn");
                }
                else {
                    UtilsViews.setView("NoTurn");
                }
            });
        }
    }
}
