package com.hundirlaflota.Client.ViewControllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CopyOnWriteArrayList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.application.Platform;

import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Client.Utils.*;
import com.hundirlaflota.Client.Utils.Observable.*;
import com.hundirlaflota.Common.*;
import com.hundirlaflota.Client.Canvas.*;

public class NoTurnViewController implements Initializable, OnSceneVisible {

    private final int CELL_SIZE = 30;
    private final int GRID_SIZE = 10;
    private final int BORDER_SIZE = 1;

    private ObservableCollection<Position> myAttacks = new ObservableCollection<>(new CopyOnWriteArrayList<Position>());
    private ObservableCollection<Ship> opponentsShips = new ObservableCollection<>(new CopyOnWriteArrayList<Ship>());

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
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                for (int x = 0; x < 10; x++) {
                    for (int y = 0; y < 10; y++) {
                        if (!myAttacks.get().contains(new Position(x, y))) {
                            myAttacks.add(new Position(x, y));
                        }
                        Thread.sleep(100);
                    }
                }
                myAttacks.add(new Position(0, 0));//Already attacked
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
        
        canvasManager = new CanvasManager(canvas);
        //TEST:
        opponentsShips.add(new Ship("Destroyer", 5, new Position(0, 0), Ship.ShipPosition.VERTICAL));
        opponentsShips.add(new Ship("Destroyer", 4, new Position(2, 2), Ship.ShipPosition.HORIZONTAL));
        opponentsShips.add(new Ship("Destroyer", 3, new Position(4, 4), Ship.ShipPosition.VERTICAL));
        opponentsShips.add(new Ship("Destroyer", 3, new Position(6, 6), Ship.ShipPosition.HORIZONTAL));

        //SET LISTENERS
        myAttacks.addCollectionAddListener(event -> updateCanvas());
        opponentsShips.addCollectionAddListener(event -> updateCanvas());
    }

    private void updateCanvas() {
        Platform.runLater(() -> {
            // Draw my attacks
            for (Position pos : myAttacks) {
                if (opponentsShips.stream().anyMatch(ship -> ship.HasPosition(pos))) {
                    grid.setCellColor(pos.getY(), pos.getX(), Color.RED);
                } else {
                    grid.setCellColor(pos.getY(), pos.getX(), Color.BLUE);
                }
            }
            
            // Draw opponent's ships
            for (Ship ship : opponentsShips) {
                if (ship.isSunk(new ArrayList<>(myAttacks.get()))) {
                    ShipCanvasObject shipObject = createShipObject(ship);
                    canvasManager.addObject(shipObject);
                }
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
