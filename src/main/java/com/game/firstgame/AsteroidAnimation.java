package com.game.firstgame;


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class AsteroidAnimation {
    private ImageView runner;
    private AnchorPane scene;
    private int animationNumber;
    int number = 1;

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
        animations();
    }));

    public void animations() {
        if (number == 1) {
            // SET THE LOCATION OF EXPLOSION
            runner.setX(runner.getX() - 20);
            runner.setY(runner.getY() - 20);
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile000.png"));
            number = 2;
        } else if (number == 2) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile001.png"));
            number = 3;
        } else if (number == 3) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile002.png"));
            number = 4;
        } else if (number == 4) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile003.png"));
            number = 5;
        } else if (number == 5) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile004.png"));
            number = 6;
        } else if (number == 6) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile005.png"));
            number = 7;
        } else if (number == 7) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile006.png"));
            number = 8;
        } else if (number == 8) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile007.png"));
            number = 9;
        } else if (number == 9) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile008.png"));
            number = 10;
        } else if (number == 10) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile009.png"));
            number = 11;
        } else if (number == 11) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile010.png"));
            number = 12;
        } else if (number == 12) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile011.png"));
            number = 13;
        } else if (number == 13) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile012.png"));
            number = 14;
        } else if (number == 14) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile013.png"));
            number = 15;
        } else if (number == 15) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids1/tile014.png"));
            number = 1;
        }
    }

    public AsteroidAnimation(AnchorPane scene, ImageView runner, int animationNumber) {
        this.runner = runner;
        this.scene = scene;
        this.number = animationNumber;
        this.runner.setFitHeight(70);
        this.runner.setFitWidth(70);
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
