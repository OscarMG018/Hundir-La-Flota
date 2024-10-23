package com.hundirlaflota.Client.ViewControllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Common.ServerMessages.*;

import javafx.fxml.Initializable;
import org.json.JSONObject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import com.hundirlaflota.Client.Utils.*;

public class RoomViewController implements Initializable,OnSceneVisible {

    UtilsWS ws;

    @FXML
    private Label RoomName;
    @FXML
    private Label UserName;
    @FXML
    private Label UserReady;
    @FXML
    private Label OtherName;
    @FXML
    private Label OtherReady;
    @FXML
    private Button ReadyButton;
    @FXML
    private Button StartButton;
    @FXML
    private Button LeaveButton;

    private UpdateRoomInfo updateRoomInfo;
    private boolean isReady = false;


    public UpdateRoomInfo getUpdateThread() {
        return updateRoomInfo;
    }

    @Override
    public void onSceneVisible() {
        ws = UtilsWS.getSharedInstance(Main.UsedLocation);
        System.out.println("RoomViewController onSceneVisible");
        ws.setOnMessage(this::handleMessage);
        ws.safeSend(new RoomInfoMessage().toString());
        updateRoomInfo = new UpdateRoomInfo();
        Thread updateRoomInfoThread = new Thread(updateRoomInfo);
        updateRoomInfoThread.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ReadyButton.setOnAction(event -> {
            ws.safeSend(new SetReadyMessage(!isReady).toString());
        });
        StartButton.setOnAction(event -> {
            ws.safeSend(new StartGameMessage().toString());
        });
        LeaveButton.setOnAction(event -> {
            ws.safeSend(new LeaveRoomMessage().toString());
        });
    }

    public void handleMessage(String message) {
        JSONObject json = new JSONObject(message);
        MessageType type = MessageType.valueOf(json.getString("type"));
        if (type == MessageType.ACK) {
            MessageType requestType = MessageType.valueOf(json.getString("requestType"));
            if (requestType == MessageType.ROOM_INFO) {
                JSONObject data = json.getJSONObject("data");
                setRoomInfo(data);
            }
            else if (requestType == MessageType.SET_READY) {
                isReady = !isReady;
            }
            else if (requestType == MessageType.START_GAME) {
                updateRoomInfo.stop();
                //TODO: Handle game start
            }
            else if (requestType == MessageType.LEAVE_ROOM) {
                updateRoomInfo.stop();
                Platform.runLater(() -> {
                    UtilsViews.setView("RoomList");
                });
            }
        }
        else if (type == MessageType.ERROR) {
            String errorMessage = json.getString("message");
            System.out.println(errorMessage); //TODO: Show error in UI
        }
    }

    public void setRoomInfo(JSONObject data) {
        String name = data.getString("name");
        String hostName = data.getString("hostName");
        String inviteName = data.getString("inviteName");
        boolean hostReady = data.getBoolean("hostReady");
        boolean inviteReady = data.getBoolean("inviteReady");
        boolean isHost = data.getBoolean("isHost");
        Platform.runLater(() -> {
            RoomName.setText(name);
            UserName.setText(hostName);
            if (hostReady) {
                UserReady.setText("Ready");
                UserReady.getStyleClass().clear();
                UserReady.getStyleClass().add("Ready");
            }
            else {
                UserReady.setText("Not Ready");
                UserReady.getStyleClass().clear();
                UserReady.getStyleClass().add("NotReady");
            }
            if (inviteName.isEmpty()) {
                OtherReady.setText("");
                OtherReady.getStyleClass().clear();
            }
            else if (inviteReady) {
                OtherReady.setText("Ready");
                OtherReady.getStyleClass().clear();
                OtherReady.getStyleClass().add("Ready");
            }
            else {
                OtherReady.setText("Not Ready");
                OtherReady.getStyleClass().clear();
                OtherReady.getStyleClass().add("NotReady");
            }
            OtherName.setText(inviteName.isEmpty() ? "Waiting for opponent" : inviteName);
            if (isHost) {
                if (hostReady) {
                    ReadyButton.setText("Cancel");
                }
                else {
                    ReadyButton.setText("Ready");
                }
            }
            else {
                if (inviteReady) {
                    ReadyButton.setText("Cancel");
                }
                else {
                    ReadyButton.setText("Ready");
                }
            }
            if (isHost) {
                if (hostReady && inviteReady) {
                    StartButton.setDisable(false);
                }
                else {
                    StartButton.setDisable(true);
                }
            }
            else {
                StartButton.setDisable(true);
            }
        });
    }
    
}
