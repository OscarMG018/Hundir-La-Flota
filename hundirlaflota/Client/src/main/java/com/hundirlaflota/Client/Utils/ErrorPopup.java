package com.hundirlaflota.Client.Utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

public class ErrorPopup extends Popup {
    private VBox pane;
    private HBox titleContainer;
    private Label title;
    private Label header;
    private Label content;

    public static void showError(String header, String content, double duration, Stage owner) {
        new ErrorPopup(header, content, duration, owner);
    }

    private ErrorPopup(String header, String content, double duration, Stage owner) {
        super();
        
        pane = new VBox();
        pane.setSpacing(10);
        pane.getStyleClass().add("error-popup");

        titleContainer = new HBox();
        titleContainer.setSpacing(10);
        titleContainer.getStyleClass().add("error-popup-title-container");

        this.title = new Label("Error");
        this.title.getStyleClass().add("error-popup-title");
        this.title.setWrapText(true);
        
        this.header = new Label(header);
        this.header.getStyleClass().add("error-popup-header");
        this.header.setWrapText(true);
        
        this.content = new Label(content);
        this.content.getStyleClass().add("error-popup-content");
        this.content.setWrapText(true);
        this.content.setPadding(new Insets(0,15,0,15));
        
        titleContainer.getChildren().addAll(this.title, this.header);
        pane.getChildren().addAll(titleContainer, this.content);
        
        pane.setMinWidth(200);
        pane.setMaxWidth(400);
        pane.setPadding(new Insets(0,0,15,0));

        titleContainer.setMinWidth(200);
        titleContainer.setMaxWidth(400);
        titleContainer.setPadding(new Insets(10));
        
        this.getContent().add(pane);
        
        this.setOnShown(e -> {
            double popupWidth = pane.getBoundsInLocal().getWidth();
            double popupHeight = pane.getBoundsInLocal().getHeight();
            
            this.setX(owner.getX() + owner.getWidth() / 2 - popupWidth / 2);
            this.setY(owner.getY() + owner.getHeight() / 2 - popupHeight / 2);
        });

        Timeline timeline = new Timeline(new KeyFrame(
            Duration.millis(duration), 
            event -> {
                System.out.println("ErrorPopup hide");
                destroy();
        }));
        timeline.setCycleCount(1);
        timeline.play();
        
        this.show(owner);
        this.getScene().getStylesheets().add(this.getClass().getResource("/style_error_popup.css").toExternalForm());

        pane.setOnMouseClicked(event -> {
            destroy();
        });
    }

    private void destroy() {
        this.hide();
        pane.getChildren().clear();
        this.getContent().clear();
    }
}
