package com.hundirlaflota.Client.ViewControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.CopyOnWriteArrayList;
import org.json.JSONObject;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert;

import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Client.Utils.*;
import com.hundirlaflota.Client.Utils.Observable.ObservableCollection;
import com.hundirlaflota.Common.ServerMessages.MessageType;
import com.hundirlaflota.Common.ServerMessages.MessageType;
import com.hundirlaflota.Client.Canvas.*;

public class NoTurnViewController implements Initializable, OnSceneVisible {

    private final int CELL_SIZE = 30;
    private final int GRID_SIZE = 10;
    private final int BORDER_SIZE = 1;

    private ObservableCollection<Position> opponentAttacks = new ObservableCollection<>(new CopyOnWriteArrayList<Position>());
    private ObservableCollection<ShipCanvasObject> myShips = new ObservableCollection<>(new CopyOnWriteArrayList<ShipCanvasObject>());
    private Position opponentPosition = new Position(0, 0);

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
        opponentPosition = new Position(-1, -1);

        //SET LISTENERS
        opponentAttacks.addCollectionAddListener(event -> updateCanvas());
        myShips.addCollectionAddListener(event -> updateCanvas());
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
            canvasManager.addObject(grid);
            canvasManager.clear();

            GridCanvasObject grid = new GridCanvasObject(0, 0, canvas.getWidth(), 0, GRID_SIZE, 0);
            canvasManager.addObject(grid);
            // Draw my attacks
            for (Position pos : opponentAttacks) {
                double[] cellCenter = grid.getCellCenter(pos.getY(), pos.getX());
                if (myShips.stream().anyMatch(ship -> ship.isPointInObject(cellCenter[0], cellCenter[1]))) {
            for (Position pos : opponentAttacks) {
                double[] cellCenter = grid.getCellCenter(pos.getY(), pos.getX());
                if (myShips.stream().anyMatch(ship -> ship.isPointInObject(cellCenter[0], cellCenter[1]))) {
                    grid.setCellColor(pos.getY(), pos.getX(), Color.RED);
                } else {
                    grid.setCellColor(pos.getY(), pos.getX(), Color.BLUE);
                }
            }
            if (opponentPosition.getX() != -1 || opponentPosition.getY() != -1) {
                grid.setCellColor(opponentPosition.getY(), opponentPosition.getX(), Color.YELLOW);
            }
            // Draw opponent's ships
            for (ShipCanvasObject ship : myShips) {
                canvasManager.addObject(ship);
            for (ShipCanvasObject ship : myShips) {
                canvasManager.addObject(ship);
            }
            
            canvasManager.draw();
        });
    }

    public void setMyShips(ArrayList<ShipCanvasObject> myShips) {
        this.myShips.set(myShips);
    }

    private void handleMessage(String message) {
        JSONObject json = new JSONObject(message);
        MessageType type = MessageType.valueOf(json.getString("type"));
        if (type == MessageType.SHOOT_RESULT) {
            System.out.println("NoTurn message: " + message);
            Position pos = new Position(json.getInt("x"), json.getInt("y"));
            opponentAttacks.add(pos);//triggers listener
            String result = json.getString("result");
            if (result.equals("MISS")) {
                // opponent missed, my turn
                Platform.runLater(() -> {
                    UtilsViews.setView("Turn");
                });
            }
        }
        else if (type == MessageType.MOUSE_POSITION) {
            opponentPosition = new Position(json.getInt("x"), json.getInt("y"));
            updateCanvas();
        }
        else if (type == MessageType.END_GAME) {
            System.out.println("NoTurn message: " + message);
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Game Over");
                alert.setHeaderText(null);
                alert.setContentText("You lost!");
                alert.showAndWait();
                opponentAttacks.get().clear();
                UtilsViews.setView("Room");
            });
        }
    }
}
