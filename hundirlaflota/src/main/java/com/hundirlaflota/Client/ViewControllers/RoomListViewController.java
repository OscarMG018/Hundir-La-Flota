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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.hundirlaflota.Client.Utils.*;


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
        CreateRoomButton.setOnAction(event -> {
            CreateRoom();
        });
    }

    @Override
    public void onSceneVisible() {
        ws = UtilsWS.getSharedInstance(Main.UsedLocation);
        ws.setOnMessage(this::handleListRoomsMessage);
        ws.safeSend(new ListRoomsMessage().toString());
        updateRoomList = new UpdateRoomList();
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
                        RoomsList.getChildren().clear();
                        RoomsList.getChildren().add(new Label("No rooms found"));
                    }  
                    else {
                        UpdateRoomList(rooms);
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
            System.out.println("Error: " + json.getString("message"));
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
       
    public void UpdateRoomList(JSONArray rooms) {
        Map<String, RoomUI> originalMap = new HashMap<String, RoomUI>();
        Map<String, JSONObject> modifiedMap = new HashMap<String, JSONObject>();
        
        for (Node node : RoomsList.getChildren()) {
            if (node instanceof RoomUI) {
                RoomUI roomUI = (RoomUI) node;
                String roomName = roomUI.getName();
                originalMap.put(roomName, roomUI);
            }
        }
        for (int i = 0; i < rooms.length(); i++) {
            JSONObject room = rooms.getJSONObject(i);
            modifiedMap.put(room.getString("name"), room);
        }
        for (String key : originalMap.keySet()) {
            if (!modifiedMap.containsKey(key)) {
                //Room has been deleted
                RoomsList.getChildren().remove(originalMap.get(key));
            }
            else {
                //Room may have been modified
                RoomUI roomUI = originalMap.get(key);
                roomUI.setPlayers(modifiedMap.get(key).getInt("players"));
            }
        }
        for (String key : modifiedMap.keySet()) {
            if (!originalMap.containsKey(key)) {
                //Room has been created
                if (RoomsList.getChildren().size() == 1 && RoomsList.getChildren().get(0) instanceof Label) {
                    //the room was the first created
                    RoomsList.getChildren().remove(0);
                }
                RoomsList.getChildren().add(new RoomUI(modifiedMap.get(key).getString("name"), modifiedMap.get(key).getInt("players")));
            }
        }
    }
}
