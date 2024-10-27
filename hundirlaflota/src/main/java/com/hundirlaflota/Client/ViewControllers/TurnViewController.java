package com.hundirlaflota.Client.ViewControllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Common.ServerMessages.MessageType;

import com.hundirlaflota.Common.ServerMessages.MessageType;

import java.util.ArrayList;


import org.json.JSONObject;


import org.json.JSONObject;

import com.hundirlaflota.Client.Utils.*;
import com.hundirlaflota.Client.Utils.Observable.ObservableCollection;
import com.hundirlaflota.Client.Canvas.*;

public class TurnViewController implements Initializable, OnSceneVisible {

    private final int CELL_SIZE = 30;
    private final int GRID_SIZE = 10;
    private final int BORDER_SIZE = 1;

    private ObservableCollection<Position> myAttacks = new ObservableCollection<>(new ArrayList<Position>());
    private ObservableCollection<ShipCanvasObject> opponentShips = new ObservableCollection<>(new ArrayList<ShipCanvasObject>());
    private ObservableCollection<Position> myAttacks = new ObservableCollection<>(new ArrayList<Position>());
    private ObservableCollection<ShipCanvasObject> opponentShips = new ObservableCollection<>(new ArrayList<ShipCanvasObject>());

    @FXML
    private Canvas canvas;

    private UtilsWS ws;
    private CanvasManager canvasManager;
    private GridCanvasObject grid;

    @Override
    public void onSceneVisible() {
        ws = UtilsWS.getSharedInstance(Main.UsedLocation);
        ws.setOnMessage(this::handleMessage);
        ws.setOnMessage(this::handleMessage);
        canvasManager.clear();
        canvasManager.addObject(grid);
        updateCanvas();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        canvas.setWidth(GRID_SIZE * CELL_SIZE + BORDER_SIZE*(GRID_SIZE+1));
        canvas.setHeight(GRID_SIZE * CELL_SIZE + BORDER_SIZE*(GRID_SIZE+1));
        
        grid = new GridCanvasObject(0, 0, canvas.getWidth(), 0, GRID_SIZE, 0);
        canvasManager = new CanvasManager(canvas);
        
        //SET LISTENERS
        myAttacks.addCollectionAddListener(event -> updateCanvas());
        opponentShips.addCollectionAddListener(event -> updateCanvas());
    }

    public double getCellSize() {
        return grid.getCellSize();
    }

    public double getGridX() {
        return grid.getX();
    }

    public double getGridY() {
        return grid.getY();
    }

    private void updateCanvas() {
        Platform.runLater(() -> {
            canvasManager.clear();

            GridCanvasObject grid = new GridCanvasObject(0, 0, canvas.getWidth(), 0, GRID_SIZE, 0);
            grid.setClickable(true);
            grid.setSendMouseOver(true);
            canvasManager.addObject(grid);
            // Draw opponents attacks
            for (Position pos : myAttacks) {
                double[] cellCenter = grid.getCellCenter(pos.getY(), pos.getX());
                if (opponentShips.stream().anyMatch(ship -> ship.isPointInObject(cellCenter[0], cellCenter[1]))) {
            for (Position pos : myAttacks) {
                double[] cellCenter = grid.getCellCenter(pos.getY(), pos.getX());
                if (opponentShips.stream().anyMatch(ship -> ship.isPointInObject(cellCenter[0], cellCenter[1]))) {
                    grid.setCellColor(pos.getY(), pos.getX(), Color.RED);
                } else {
                    grid.setCellColor(pos.getY(), pos.getX(), Color.BLUE);
                }
            }
            
            // Draw my ships
            for (ShipCanvasObject ship : opponentShips) {
                if (isSunk(new ArrayList<>(myAttacks.get()), ship)) {
                    canvasManager.addObject(ship);
                }
            for (ShipCanvasObject ship : opponentShips) {
                if (isSunk(new ArrayList<>(myAttacks.get()), ship)) {
                    canvasManager.addObject(ship);
                }
            }
            
            canvasManager.draw();
        });
    }

    public void setOpponentShips(ArrayList<ShipCanvasObject> opponentShips) {
        this.opponentShips.set(opponentShips);
    }

    private void handleMessage(String message) {
        System.out.println("Turn message: " + message);
        JSONObject json = new JSONObject(message);
        MessageType type = MessageType.valueOf(json.getString("type"));
        if (type == MessageType.SHOOT_RESULT) {
            Position pos = new Position(json.getInt("x"), json.getInt("y"));
            myAttacks.add(pos);//triggers listener
            String result = json.getString("result");
            if (result.equals("MISS")) {
                //miss, end turn
                Platform.runLater(() -> {
                    UtilsViews.setView("NoTurn");
                });
            }
        }
        else if (type == MessageType.END_GAME) {
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText(null);
                alert.setContentText("You won!");
                alert.showAndWait();
                myAttacks.get().clear();
                UtilsViews.setView("Room");
            });
        }
    }

    private boolean isSunk(ArrayList<Position> attacks, ShipCanvasObject ship) {
        int hitCount = 0;
        int shipSize = ship.getSize();
        boolean isHorizontal = ship.isHorizontal();
        
        // Get the ship's position on the grid
        int shipStartX = (int) ((ship.getX() - grid.getX()) / CELL_SIZE);
        int shipStartY = (int) ((ship.getY() - grid.getY()) / CELL_SIZE);
        
        // Check each cell the ship occupies
        for (int i = 0; i < shipSize; i++) {
            int cellX = isHorizontal ? shipStartX + i : shipStartX;
            int cellY = isHorizontal ? shipStartY : shipStartY + i;
            
            // Check if this cell has been hit
            if (attacks.stream().anyMatch(pos -> pos.getX() == cellX && pos.getY() == cellY)) {
                hitCount++;
            }
        }
        
        // The ship is sunk if all its cells have been hit
        return hitCount == shipSize;
    }
}
