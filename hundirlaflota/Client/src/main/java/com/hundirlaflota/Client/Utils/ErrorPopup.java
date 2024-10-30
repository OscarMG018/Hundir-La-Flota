package com.hundirlaflota.Client.Utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;
public class ErrorPopup extends Popup {
    private Label title;
    private Label header;
    private Label content;

    public ErrorPopup(String title, String header, String content, double duration, Stage owner) {
        setAutoHide(true);
        Pane pane = new Pane();
        this.title = new Label(title);
        this.header = new Label(header);
        this.content = new Label(content);
        pane.getChildren().add(this.title);
        pane.getChildren().add(this.header);
        pane.getChildren().add(this.content);
        this.setWidth(300);
        this.setHeight(200);
        pane.getStyleClass().add("error-popup");
        this.show(owner);
        this.setX(owner.getX() + owner.getWidth() / 2 - this.getWidth() / 2);
        this.setY(owner.getY() + owner.getHeight() / 2 - this.getHeight() / 2);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(duration), 
                event -> {
                    this.hide();
                    pane.getChildren().clear();
                    this.getContent().clear();
                }));
        timeline.setCycleCount(1);
        timeline.play();
    }
}
