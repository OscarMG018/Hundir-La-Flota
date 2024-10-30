package com.hundirlaflota.Client.ViewControllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Client.Utils.UtilsViews;
import com.hundirlaflota.Client.Utils.UtilsWS;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ConnectionViewController implements Initializable, OnSceneVisible {

    @FXML
    private ChoiceBox<String> ConnectionChoiceBox;
    @FXML
    private TextField ConnectionIPField;
    @FXML
    private TextField ConnectionPortField;

    @Override
    public void onSceneVisible() {
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> items = FXCollections.observableArrayList("Local", "Server", "Custom");
        ConnectionChoiceBox.setItems(items);
        ConnectionChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            checkCustom(newValue);
        });
        ConnectionChoiceBox.setValue("Local");
    }

    private void checkCustom(String value) {
        if (value.equals("Custom")) {
            ConnectionIPField.setDisable(false);
            ConnectionPortField.setDisable(false);
        }
        else {
            ConnectionIPField.setDisable(true);
            ConnectionPortField.setDisable(true);
        }

    }

    public void Connect() {
        if (ConnectionChoiceBox.getValue().equals("Local")) {
            Main.UsedLocation = Main.LocalLocation;
        }
        else if (ConnectionChoiceBox.getValue().equals("Server")) {
            Main.UsedLocation = Main.ServerLocation;
        }
        else if (ConnectionChoiceBox.getValue().equals("Custom")) {
            Main.UsedLocation = "ws://" + ConnectionIPField.getText() + ":" + ConnectionPortField.getText();
        }
        UtilsWS.getSharedInstance(Main.UsedLocation);
        Platform.runLater(() -> {
            UtilsViews.setView("Login");
        });
    }
    
}
