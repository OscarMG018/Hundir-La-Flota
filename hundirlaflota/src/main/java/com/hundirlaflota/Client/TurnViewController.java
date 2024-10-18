package com.hundirlaflota.Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import com.hundirlaflota.Common.*;
import java.util.ArrayList;

import java.util.concurrent.*;

public class TurnViewController implements Initializable, OnSceneVisible {


    private final int CELL_SIZE = 30;
    private final int GRID_SIZE = 10;
    private final int BORDER_SIZE = 1;
    private final int SHIP_PADDING = 5;

    private ObservableCollection<Position> opponentsAttacks = new ObservableCollection<>(new ArrayList<Position>());
    private ObservableCollection<Ship> myShips = new ObservableCollection<>(new ArrayList<Ship>());

    @FXML
    private Canvas canvas;


    private UtilsWS ws;
    @Override
    public void onSceneVisible() {
        drawGrid();
        //TEST:
        myShips.add(new Ship("Destroyer", 5, new Position(2, 2), Ship.ShipPosition.VERTICAL));
        myShips.add(new Ship("Destroyer", 4, new Position(2, 2), Ship.ShipPosition.VERTICAL));
        myShips.add(new Ship("Destroyer", 3, new Position(2, 2), Ship.ShipPosition.VERTICAL));
        myShips.add(new Ship("Destroyer", 3, new Position(2, 2), Ship.ShipPosition.VERTICAL));
        myShips.add(new Ship("Destroyer", 3, new Position(2, 2), Ship.ShipPosition.VERTICAL));
        myShips.add(new Ship("Destroyer", 2, new Position(2, 2), Ship.ShipPosition.VERTICAL));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    Thread.sleep(1000);
                    int x = (int) (Math.random() * GRID_SIZE);
                    int y = (int) (Math.random() * GRID_SIZE);
                    opponentsAttacks.add(new Position(x, y));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            
        });
        //TEST:
        opponentsAttacks.add(new Position(2, 2));//already attacked

    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ws = UtilsWS.getSharedInstance(Main.location);
        //We change the size of the canvas so it is centered
        canvas.setWidth(GRID_SIZE * CELL_SIZE + BORDER_SIZE*(GRID_SIZE+1));
        canvas.setHeight(GRID_SIZE * CELL_SIZE + BORDER_SIZE*(GRID_SIZE+1));
        //SET LISTENERS
        opponentsAttacks.addCollectionAddListener(event -> {
            drawGrid();
        });
        myShips.addCollectionAddListener(event -> {
            drawGrid();
        });
    }

    void drawGrid() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        //Draw opponents attacks
        for (Position pos : opponentsAttacks) {
            if (myShips.stream().anyMatch(ship -> ship.HasPosition(pos))) {
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
        //TODO: draw ships in the right places
        for (Ship ship : myShips) {
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
