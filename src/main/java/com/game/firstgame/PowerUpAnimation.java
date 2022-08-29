package com.game.firstgame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.util.ArrayList;

public class PowerUpAnimation {
    private final ImageView runner;
    private final int powerUpType;
    private int number = 1;

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
        animations();
    }));

    public void animations() {
        // powerUpType = 0 means spawn missile
        if (powerUpType == 0) {
            if (number == 1) {
                // SET THE LOCATION OF EXPLOSION
                runner.setX(runner.getX() - 20);
                runner.setY(runner.getY() - 20);
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Blue/powerup01_1.png"));
                number = 2;
            } else if (number == 2) {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Blue/powerup01_2.png"));
                number = 3;
            } else if (number == 3) {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Blue/powerup01_3.png"));
                number = 4;
            } else if (number == 4) {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Blue/powerup01_4.png"));
                number = 5;
            } else if (number == 5) {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Blue/powerup01_5.png"));
                number = 6;
            } else {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Blue/powerup01_6.png"));
                number = 1;
            }
        }

        // Type 1 gives life
        if (powerUpType == 1) {
            if (number == 1) {
                // SET THE LOCATION OF EXPLOSION
                runner.setX(runner.getX() - 20);
                runner.setY(runner.getY() - 20);
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Yellow/powerup02_1.png"));
                number = 2;
            } else if (number == 2) {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Yellow/powerup02_2.png"));
                number = 3;
            } else if (number == 3) {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Yellow/powerup02_3.png"));
                number = 4;
            } else if (number == 4) {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Yellow/powerup02_4.png"));
                number = 5;
            } else if (number == 5) {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Yellow/powerup02_5.png"));
                number = 6;
            } else {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Yellow/powerup02_6.png"));
                number = 1;
            }
        }

        // Type 2 gives counts as bonus
        if (powerUpType == 2) {
            if (number == 1) {
                // SET THE LOCATION OF EXPLOSION
                runner.setX(runner.getX() - 20);
                runner.setY(runner.getY() - 20);
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Silver/powerup03_1.png"));
                number = 2;
            } else if (number == 2) {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Silver/powerup03_2.png"));
                number = 3;
            } else if (number == 3) {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Silver/powerup03_3.png"));
                number = 4;
            } else if (number == 4) {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Silver/powerup03_4.png"));
                number = 5;
            } else if (number == 5) {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Silver/powerup03_5.png"));
                number = 6;
            } else {
                runner.setImage(new Image("com/game/firstgame/images/Powerup/Silver/powerup03_6.png"));
                number = 1;
            }
        }
    }

    public PowerUpAnimation(ImageView runner, int powerUpType) {
        this.runner = runner;
        this.runner.setFitHeight(25);
        this.runner.setFitWidth(25);
        this.powerUpType = powerUpType;
        timeline.setCycleCount(Animation.INDEFINITE);


    }



    public void startAnimation(){
        timeline.play();
    }

    public void stopAnimation(){
        number = 1;
        timeline.stop();
    }

    public int getPowerUpType() {
        return powerUpType;
    }
}
