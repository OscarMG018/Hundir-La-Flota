package com.hundirlaflota.Client;

import com.hundirlaflota.Client.ViewControllers.RoomListViewController;
import com.hundirlaflota.Client.ViewControllers.RoomViewController;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.hundirlaflota.Client.Utils.*;

public class Main extends Application {
    public static String LocalLocation = "ws://localhost:3000";
    public static String ServerLocation = "wss://omedinagalvez.ieti.site:443";
    public static String UsedLocation = "";
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        UtilsViews.addView(Main.class, "Connection", "/layout_connection.fxml", "/style_connection.css");
        UtilsViews.addView(Main.class, "Login", "/layout_login.fxml", "/style_login.css");
        UtilsViews.addView(Main.class, "RoomList", "/layout_room_list.fxml", "/style_room_list.css");
        UtilsViews.addView(Main.class, "Room", "/layout_room.fxml", "/style_room.css");
        UtilsViews.addView(Main.class, "Ships", "/layout_ships.fxml", "/style_ships.css");
        UtilsViews.addView(Main.class, "Turn", "/layout_turn.fxml", "/style_turn.css");
        UtilsViews.addView(Main.class, "NoTurn", "/layout_no_turn.fxml", "/style_no_turn.css");
        UtilsViews.setView("Connection");
        Scene scene = new Scene(UtilsViews.parentContainer);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hundir la Flota");
        primaryStage.setOnCloseRequest(event -> {
            RoomListViewController c1 = (RoomListViewController) UtilsViews.getController("RoomList");
            RoomViewController c2 = (RoomViewController) UtilsViews.getController("Room");
            if (c1.getUpdateThread() != null) {
                c1.getUpdateThread().stop();
            }
            if (c2.getUpdateThread() != null) {
                c2.getUpdateThread().stop();
            }
            UtilsWS ws = UtilsWS.getSharedInstance(UsedLocation);
            if (ws != null) {
                ws.close();
            }
        });
        primaryStage.show();
    }
}
