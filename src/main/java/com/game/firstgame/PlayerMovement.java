package com.game.firstgame;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class PlayerMovement {
    private final AnchorPane scene;
    private final ImageView player;
    private final double playerSize;
    private final BooleanProperty wPressed;
    private final BooleanProperty aPressed;
    private final BooleanProperty sPressed;
    private final BooleanProperty dPressed;
    private final BooleanProperty spacePressed;

    public PlayerMovement (AnchorPane scene, ImageView player, BooleanProperty wPressed, BooleanProperty aPressed, BooleanProperty sPressed,
                   BooleanProperty dPressed, BooleanProperty spacePressed) {
        this.scene = scene;
        this.player = player;
        playerSize = player.getFitHeight();

        this.wPressed = wPressed;
        this.aPressed = aPressed;
        this.sPressed = sPressed;
        this.dPressed = dPressed;
        this.spacePressed = spacePressed;
    }
    public void movementSetup(){
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.W) {
                wPressed.set(true);
            }

            if(e.getCode() == KeyCode.A) {
                aPressed.set(true);
            }

            if(e.getCode() == KeyCode.S) {
                sPressed.set(true);
            }

            if(e.getCode() == KeyCode.D) {
                dPressed.set(true);
            }
            if(e.getCode() == KeyCode.SPACE) {
                spacePressed.set(true);
            }
        });

        scene.setOnKeyReleased(e ->{
            if(e.getCode() == KeyCode.W) {
                wPressed.set(false);
            }

            if(e.getCode() == KeyCode.A) {
                aPressed.set(false);
            }

            if(e.getCode() == KeyCode.S) {
                sPressed.set(false);
            }

            if(e.getCode() == KeyCode.D) {
                dPressed.set(false);
            }
            if(e.getCode() == KeyCode.SPACE) {
                spacePressed.set(false);
            }
        });
    }

    public void squareAtBorder() {
        double leftBound = 0;
        double rightBound = scene.getWidth() - playerSize  + 52;
        double bottomBound = scene.getHeight() - playerSize + 30;
        double topBound = 0;


        if(player.getLayoutX() <= leftBound)
            player.setLayoutX(leftBound);

        if (player.getLayoutX() >= rightBound)
            player.setLayoutX(rightBound);

        if (player.getLayoutY() <= topBound)
            player.setLayoutY(topBound);

        if (player.getLayoutY() >= bottomBound)
            player.setLayoutY(bottomBound);
    }

    public double getPlayerSize() {
        return playerSize;
    }
}
