package com.hundirlaflota.Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.application.Platform;

import com.hundirlaflota.Common.*;
import java.util.ArrayList;
public class TurnViewController implements Initializable, OnSceneVisible {


    private final int CELL_SIZE = 30;
    private final int GRID_SIZE = 9;
    private final int BORDER_SIZE = 3;

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
        Ship ship = new Ship("Aircraft Carrier", 5, new Position(0, 0), Ship.ShipPosition.HORIZONTAL);
        System.out.println(ship.HasPosition(new Position(0, 0)));
        System.out.println(ship.HasPosition(new Position(1, 0)));
        System.out.println(ship.HasPosition(new Position(2, 0)));
        System.out.println(ship.HasPosition(new Position(3, 0)));
        System.out.println(ship.HasPosition(new Position(4, 0)));
        System.out.println(ship.HasPosition(new Position(5, 0)));
        //TEST:
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                opponentsAttacks.add(new Position(i, j));
            }
        }
        myShips.add(new Ship("Aircraft Carrier", 5, new Position(0, 0), Ship.ShipPosition.HORIZONTAL));
        myShips.add(new Ship("Battleship", 4, new Position(1, 1), Ship.ShipPosition.HORIZONTAL));
        myShips.add(new Ship("Destroyer", 3, new Position(2, 2), Ship.ShipPosition.HORIZONTAL));
        myShips.add(new Ship("Submarine", 3, new Position(3, 3), Ship.ShipPosition.HORIZONTAL));
        myShips.add(new Ship("Patrol Boat", 2, new Position(4, 4), Ship.ShipPosition.HORIZONTAL));

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
    }

    void drawCell(GraphicsContext gc, Position pos, Color color) {
        gc.setFill(color);
        gc.fillRect(pos.getX()* CELL_SIZE, pos.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
    }
}
