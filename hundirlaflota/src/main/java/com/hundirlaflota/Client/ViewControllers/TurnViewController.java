package com.hundirlaflota.Client.ViewControllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;

import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Common.*;
import java.util.ArrayList;

import java.util.concurrent.*;
import com.hundirlaflota.Client.Utils.*;
import com.hundirlaflota.Client.Utils.Observable.ObservableCollection;
import com.hundirlaflota.Client.Canvas.*;
import javafx.application.Platform;

public class TurnViewController implements Initializable, OnSceneVisible {

    private final int CELL_SIZE = 30;
    private final int GRID_SIZE = 10;
    private final int BORDER_SIZE = 1;

    private ObservableCollection<Position> opponentsAttacks = new ObservableCollection<>(new ArrayList<Position>());
    private ObservableCollection<Ship> myShips = new ObservableCollection<>(new ArrayList<Ship>());

    @FXML
    private Canvas canvas;

    private UtilsWS ws;
    private CanvasManager canvasManager;
    private GridCanvasObject grid;

    @Override
    public void onSceneVisible() {
        canvasManager.clear();
        grid = new GridCanvasObject(0, 0, canvas.getWidth(), 0, GRID_SIZE, 0);
        canvasManager.addObject(grid);
        
        updateCanvas();
        //TEST:
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                for (int x = 0; x < 10; x++) {
                    for (int y = 0; y < 10; y++) {
                        if (!opponentsAttacks.get().contains(new Position(x, y))) {
                            opponentsAttacks.add(new Position(x, y));
                        }
                        Thread.sleep(1000);
                    }
                }
                opponentsAttacks.add(new Position(0, 0));//Already attacked
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        });
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ws = UtilsWS.getSharedInstance(Main.location);
        canvas.setWidth(GRID_SIZE * CELL_SIZE + BORDER_SIZE*(GRID_SIZE+1));
        canvas.setHeight(GRID_SIZE * CELL_SIZE + BORDER_SIZE*(GRID_SIZE+1));

        //TEST:
        myShips.add(new Ship("Destroyer", 5, new Position(0, 0), Ship.ShipPosition.VERTICAL));
        myShips.add(new Ship("Destroyer", 4, new Position(2, 2), Ship.ShipPosition.HORIZONTAL));
        myShips.add(new Ship("Destroyer", 3, new Position(4, 4), Ship.ShipPosition.VERTICAL));
        myShips.add(new Ship("Destroyer", 3, new Position(6, 6), Ship.ShipPosition.HORIZONTAL));
        
        canvasManager = new CanvasManager(canvas);
        
        //SET LISTENERS
        opponentsAttacks.addCollectionAddListener(event -> updateCanvas());
        myShips.addCollectionAddListener(event -> updateCanvas());
    }

    private void updateCanvas() {
        Platform.runLater(() -> {
            canvasManager.clear();
            // Draw opponents attacks
            for (Position pos : opponentsAttacks) {
                if (myShips.stream().anyMatch(ship -> ship.HasPosition(pos))) {
                    grid.setCellColor(pos.getY(), pos.getX(), Color.RED);
                } else {
                    grid.setCellColor(pos.getY(), pos.getX(), Color.BLUE);
                }
            }
            
            // Draw my ships
            for (Ship ship : myShips) {
                ShipCanvasObject shipObject = createShipObject(ship);
                canvasManager.addObject(shipObject);
            }
            
            canvasManager.draw();
        });
    }

    private ShipCanvasObject createShipObject(Ship ship) {
        double[] cellPos = grid.getCellUpperLeftCorner(ship.getPosition().getY(), ship.getPosition().getX());
        boolean isHorizontal = ship.getShipPosition() == Ship.ShipPosition.HORIZONTAL;
        
        return new ShipCanvasObject(
            ship.getName(),
            cellPos[0],
            cellPos[1],
            CELL_SIZE,
            ship.getSize(),
            1,
            false,
            isHorizontal
        );
    }
}
