package com.hundirlaflota.Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.application.Platform;
import javafx.scene.image.Image;

import com.hundirlaflota.Common.*;
import java.util.ArrayList;
public class TurnViewController implements Initializable, OnSceneVisible {


    private final int CELL_SIZE = 30;
    private final int GRID_SIZE = 10;
    private final int BORDER_SIZE = 2;
    private final int SHIP_PADDING = 5;

    private ArrayList<Position> opponentsAttacks = new ArrayList<>();
    private ArrayList<Ship> myShips = new ArrayList<>();

    @FXML
    private Canvas canvas;


    private UtilsWS ws;
    @Override
    public void onSceneVisible() {
        drawGrid();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ws = UtilsWS.getSharedInstance(Main.location);
        //We change the size of the canvas so it is centered
        canvas.setWidth(GRID_SIZE * CELL_SIZE + BORDER_SIZE*(GRID_SIZE+1));
        canvas.setHeight(GRID_SIZE * CELL_SIZE + BORDER_SIZE*(GRID_SIZE+1));
        //TEST:
        opponentsAttacks.add(new Position(0, 0));
        opponentsAttacks.add(new Position(1, 1));
        opponentsAttacks.add(new Position(2, 2));
        opponentsAttacks.add(new Position(2, 1));
        //TEST:
        myShips.add(new Ship("Aircraft Carrier", 5, new Position(2, 2), Ship.ShipPosition.VERTICAL));
        //myShips.add(new Ship("Battleship", 4, new Position(1, 1), Ship.ShipPosition.HORIZONTAL));
        //myShips.add(new Ship("Destroyer", 3, new Position(2, 2), Ship.ShipPosition.HORIZONTAL));
        //myShips.add(new Ship("Submarine", 3, new Position(3, 3), Ship.ShipPosition.HORIZONTAL));
        //myShips.add(new Ship("Patrol Boat", 2, new Position(4, 4), Ship.ShipPosition.HORIZONTAL));

    }

    void drawGrid() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
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
            System.out.println(ship.getName());
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
