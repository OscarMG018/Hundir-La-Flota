package com.hundirlaflota.Client.ViewControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;
import org.json.*;

import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Common.ServerMessages.*;
import javafx.application.Platform;
import java.util.Optional;

import com.hundirlaflota.Client.Utils.*;

class RoomUI {
    private String name;
    private int players;

    public RoomUI(String name, int players) {
        this.name = name;
        this.players = players;
    }

    public Node getUI() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);
        Label nameLabel = new Label(name);
        nameLabel.getStyleClass().add("room-name");
        Label playersLabel = new Label(players + "/2");
        playersLabel.getStyleClass().add("room-players");
        Button joinButton = new Button("Join");
        joinButton.getStyleClass().add("room-join-button");
        joinButton.setOnAction(event -> {
            UtilsWS.getSharedInstance(Main.location).safeSend(new JoinRoomMessage(name).toString());
        });
        hbox.getChildren().addAll(nameLabel, playersLabel, joinButton);
        hbox.getStyleClass().add("room-container");
        return hbox;
    }
}

public class RoomListViewController implements Initializable, OnSceneVisible {
    @FXML
    private VBox RoomsList;
    @FXML
    private Button CreateRoomButton;

    private UtilsWS ws;
    private UpdateRoomList updateRoomList;


    public UpdateRoomList getUpdateThread() {
        return updateRoomList;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ws = UtilsWS.getSharedInstance(Main.location);
        CreateRoomButton.setOnAction(event -> {
            CreateRoom();
        });
    }

    @Override
    public void onSceneVisible() {
        ws.setOnMessage(this::handleListRoomsMessage);
        ws.safeSend(new ListRoomsMessage().toString());
        updateRoomList = new UpdateRoomList(ws);
        Thread updateRoomListThread = new Thread(updateRoomList);
        updateRoomListThread.start();
    }

    public void handleListRoomsMessage(String message) {
        JSONObject json = new JSONObject(message);
        MessageType type = MessageType.valueOf(json.getString("type"));
        if (type == MessageType.ACK) {
            MessageType requestType = MessageType.valueOf(json.getString("requestType"));
            if (requestType == MessageType.LIST_ROOMS) {
                JSONArray rooms = json.getJSONArray("data");
                Platform.runLater(() -> {
                    if (rooms.length() == 0) {
                        System.out.println("No rooms found");
                        RoomsList.getChildren().clear();
                        RoomsList.getChildren().add(new Label("No rooms found"));
                    }  
                    else {
                        RoomsList.getChildren().clear();
                        for (int i = 0; i < rooms.length(); i++) {
                            JSONObject room = rooms.getJSONObject(i);
                            RoomsList.getChildren().add(new RoomUI(room.getString("name"), room.getInt("players")).getUI());
                        } 
                    }
                });
            }
            else if (requestType == MessageType.CREATE_ROOM) {
                updateRoomList.stop();
                Platform.runLater(() -> {
                    UtilsViews.setView("Room");
                });
            }
            else if (requestType == MessageType.JOIN_ROOM) {
                updateRoomList.stop();
                Platform.runLater(() -> {
                    UtilsViews.setView("Room");
                });
            }
        } else if (type == MessageType.ERROR) {
            System.out.println("Error: " + json.getString("message"));//TODO: Show error in the UI 
        }
    }

    public void CreateRoom() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Create Room");
        dialog.setHeaderText("Enter the room name");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField roomNameField = new TextField();
        dialog.getDialogPane().setContent(roomNameField);
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return roomNameField.getText();
            }
            return null;
        });
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            ws.safeSend(new CreateRoomMessage(result.get()).toString());
        }
    }
        
        
}
