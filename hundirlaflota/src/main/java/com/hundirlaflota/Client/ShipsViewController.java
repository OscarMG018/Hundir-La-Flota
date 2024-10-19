package com.hundirlaflota.Client;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import com.hundirlaflota.Client.Canvas.CanvasManager;

public class ShipsViewController implements Initializable, OnSceneVisible {

    @FXML
    private Canvas canvas;
    private CanvasManager canvasManager;

    @Override
    public void onSceneVisible() {
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
