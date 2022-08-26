package com.game.firstgame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class PowerUpAnimation {
    private ImageView runner;
    private AnchorPane scene;
    private int animationNumber;
    int number = 1;

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
        animations();
    }));

    public void animations() {
        if (number == 1) {
            // SET THE LOCATION OF EXPLOSION
            runner.setX(runner.getX() - 20);
            runner.setY(runner.getY() - 20);
            runner.setImage(new Image("com/game/firstgame/images/Powerup/powerup01_1.png"));
            number = 2;
        } else if (number == 2) {
            runner.setImage(new Image("com/game/firstgame/images/Powerup/powerup01_2.png"));
            number = 3;
        } else if (number == 3) {
            runner.setImage(new Image("com/game/firstgame/images/Powerup/powerup01_3.png"));
            number = 4;
        } else if (number == 4) {
            runner.setImage(new Image("com/game/firstgame/images/Powerup/powerup01_4.png"));
            number = 5;
        } else if (number == 5) {
            runner.setImage(new Image("com/game/firstgame/images/Powerup/powerup01_5.png"));
            number = 6;
        } else {
            runner.setImage(new Image("com/game/firstgame/images/Powerup/powerup01_6.png"));
            number = 1;
        }
    }

    public PowerUpAnimation(AnchorPane scene, ImageView runner) {
        this.runner = runner;
        this.scene = scene;
        this.runner.setFitHeight(25);
        this.runner.setFitWidth(25);
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void startAnimation(){
        timeline.play();
    }

    public void stopAnimation(){
        number = 1;
        timeline.stop();
    }
}
