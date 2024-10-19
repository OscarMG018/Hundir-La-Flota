package com.hundirlaflota.Client.ViewControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import org.json.JSONObject;

import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Common.ServerMessages.*;
import com.hundirlaflota.Client.Utils.*;

public class LoginViewController implements Initializable, OnSceneVisible {
    @FXML
    private TextField LoginTextField;
    @FXML
    private Button LoginButton;

    private UtilsWS ws;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ws = UtilsWS.getSharedInstance(Main.location);
        LoginButton.setOnAction(event -> {
            if (ws.isConnected()) {
                System.out.println("Login button pressed");
                String username = LoginTextField.getText();
                ws.safeSend(new LoginMessage(username).toString());
            }
            else {
                //TODO: Show Error message (unable to connect to the server)
            }
        });
    }

    @Override
    public void onSceneVisible() {
        ws.setOnMessage(this::handleLoginMessage);
    }

    public void handleLoginMessage(String message) {
        JSONObject json = new JSONObject(message);
        MessageType type = MessageType.valueOf(json.getString("type"));
        if (type == MessageType.ACK) {
            MessageType requestType = MessageType.valueOf(json.getString("requestType"));
            if (requestType == MessageType.LOGIN) {
                System.out.println("Login successful");
                Platform.runLater(() -> {
                    UtilsViews.setView("RoomList");
                });
            }
        } else if (type == MessageType.ERROR) {
            System.out.println("Error: " + json.getString("message"));//TODO: Show error in the UI
        }
    }
    
}
