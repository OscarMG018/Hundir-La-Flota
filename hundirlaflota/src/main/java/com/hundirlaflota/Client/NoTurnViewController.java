package com.hundirlaflota.Client;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import com.hundirlaflota.Client.Utils.ObservableCollection;
import com.hundirlaflota.Client.Utils.UtilsWS;
import com.hundirlaflota.Common.*;

public class NoTurnViewController implements Initializable, OnSceneVisible {

    private final int CELL_SIZE = 30;
    private final int GRID_SIZE = 10;
    private final int BORDER_SIZE = 1;
    private final int SHIP_PADDING = 5;

    private ObservableCollection<Position> myAttacks = new ObservableCollection<>(new CopyOnWriteArrayList<Position>());
    private ObservableCollection<Ship> opponentsShips = new ObservableCollection<>(new CopyOnWriteArrayList<Ship>());

    @FXML
    private Canvas canvas;
    private UtilsWS ws;

    @Override
    public void onSceneVisible() {
        drawGrid();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        opponentsShips.add(new Ship("Destroyer", 5, new Position(0, 0), Ship.ShipPosition.HORIZONTAL));
        opponentsShips.add(new Ship("Destroyer", 4, new Position(0, 5), Ship.ShipPosition.VERTICAL));
        opponentsShips.add(new Ship("Destroyer", 3, new Position(6, 2), Ship.ShipPosition.HORIZONTAL));
        opponentsShips.add(new Ship("Destroyer", 3, new Position(3, 3), Ship.ShipPosition.HORIZONTAL));
        opponentsShips.add(new Ship("Destroyer", 3, new Position(0, 9), Ship.ShipPosition.HORIZONTAL));
        opponentsShips.add(new Ship("Destroyer", 2, new Position(9, 7), Ship.ShipPosition.VERTICAL));
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
        //We change the size of the canvas so it is centered
        canvas.setWidth(GRID_SIZE * CELL_SIZE + BORDER_SIZE*(GRID_SIZE+1));
        canvas.setHeight(GRID_SIZE * CELL_SIZE + BORDER_SIZE*(GRID_SIZE+1));
        //SET LISTENERS
        myAttacks.addCollectionAddListener(event -> {
            drawGrid();
        });
        opponentsShips.addCollectionAddListener(event -> {
            drawGrid();
        });
    }

    void drawGrid() {

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //Draw opponents attacks
        for (Position pos : myAttacks) {
            if (opponentsShips.stream().anyMatch(ship -> ship.HasPosition(pos))) {
                drawCell(gc, pos, Color.RED);
            } else {
                drawCell(gc, pos, Color.BLUE);
            }
        }
        //Draw gridLines
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(BORDER_SIZE);
        for (int i = 0; i < GRID_SIZE+1; i++) {//+1 to close the grid
            gc.strokeLine(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE);//Vertical lines
            gc.strokeLine(0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE, i * CELL_SIZE);//Horizontal lines
        }
        //Draw my ships
        for (Ship ship : opponentsShips) {
            if (!ship.isSunk(myAttacks.stream().collect(Collectors.toCollection(ArrayList::new))))
                continue;
            Image image = new Image(getClass().getResourceAsStream("/images/" + ship.getName() + ".png"));
            drawShipImage(gc, ship.getPosition(), image, ship);
        }
    }

    void drawCell(GraphicsContext gc, Position pos, Color color) {
        gc.setFill(color);
        gc.fillRect(pos.getX()* CELL_SIZE, pos.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }

    void drawShipImage(GraphicsContext gc, Position pos, Image image, Ship ship) {
        gc.save();
        gc.translate(pos.getX() * CELL_SIZE, pos.getY() * CELL_SIZE);
        if (ship.getShipPosition() == Ship.ShipPosition.HORIZONTAL) {
            gc.rotate(90);
            gc.translate(0, -CELL_SIZE*ship.getSize());
        }
        gc.drawImage(image, SHIP_PADDING, SHIP_PADDING, CELL_SIZE-SHIP_PADDING*2, CELL_SIZE*ship.getSize()-SHIP_PADDING*2);
        gc.restore();
    }
}
