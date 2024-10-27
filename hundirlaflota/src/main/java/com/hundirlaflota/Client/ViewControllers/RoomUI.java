package com.hundirlaflota.Client.ViewControllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import com.hundirlaflota.Client.Utils.UtilsWS;
import com.hundirlaflota.Client.Main;
import com.hundirlaflota.Common.ServerMessages.JoinRoomMessage;

public class RoomUI extends Region {
    // Properties
    private final StringProperty name = new SimpleStringProperty();
    private final IntegerProperty players = new SimpleIntegerProperty();
    
    // UI Components
    private final HBox container;
    private final Label nameLabel;
    private final Label playersLabel;
    private final Button joinButton;

    public RoomUI(String name, int players) {
        // Initialize properties
        this.name.set(name);
        this.players.set(players);

        // Create UI components
        container = new HBox();
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);
        container.getStyleClass().add("room-container");

        nameLabel = new Label();
        nameLabel.getStyleClass().add("room-name");
        nameLabel.textProperty().bind(this.name);

        playersLabel = new Label();
        playersLabel.getStyleClass().add("room-players");
        playersLabel.textProperty().bind(
            this.players.asString().concat("/2")
        );

        joinButton = new Button("Join");
        joinButton.getStyleClass().add("room-join-button");
        joinButton.setOnAction(event -> {
            UtilsWS.getSharedInstance(Main.UsedLocation)
                  .safeSend(new JoinRoomMessage(getName()).toString());
        });

        // Add components to container
        container.getChildren().addAll(nameLabel, playersLabel, joinButton);
        
        // Add container to this region
        getChildren().add(container);

        // Initialize size constraints
        setPrefSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
    }

    @Override
    protected void layoutChildren() {
        // Layout the container within this region
        container.resizeRelocate(
            getInsets().getLeft(),
            getInsets().getTop(),
            getWidth() - getInsets().getLeft() - getInsets().getRight(),
            getHeight() - getInsets().getTop() - getInsets().getBottom()
        );
    }

    // Property getters/setters
    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getPlayers() {
        return players.get();
    }

    public IntegerProperty playersProperty() {
        return players;
    }

    public void setPlayers(int players) {
        this.players.set(players);
    }

    // Button action customization
    public void setOnJoin(EventHandler<ActionEvent> handler) {
        joinButton.setOnAction(handler);
    }
}