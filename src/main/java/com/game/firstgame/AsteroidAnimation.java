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
            number = 1;
        } else if (number == 10) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile009.png"));
            number = 11;
        } else if (number == 11) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile010.png"));
            number = 12;
        } else if (number == 12) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile011.png"));
            number = 13;
        } else if (number == 13) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile012.png"));
            number = 14;
        } else if (number == 14) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile013.png"));
            number = 15;
        } else if (number == 15) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile014.png"));
            number = 16;
        } else if (number == 16) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile015.png"));
            number = 17;
        } else if (number == 17) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile016.png"));
            number = 18;
        } else if (number == 18) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile017.png"));
            number = 19;
        } else if (number == 19) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile018.png"));
            number = 20;
        } else if (number == 20) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile019.png"));
            number = 21;
        } else if (number == 21) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile020.png"));
            number = 22;
        } else if (number == 22) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile021.png"));
            number = 23;
        } else if (number == 23) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile022.png"));
            number = 24;
        } else if (number == 24) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile023.png"));
            number = 25;
        } else if (number == 25) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile019.png"));
            number = 26;
        } else if (number == 26) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile025.png"));
            number = 27;
        } else if (number == 27) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile026.png"));
            number = 28;
        } else if (number == 28) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile027.png"));
            number = 29;
        } else if (number == 29) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile028.png"));
            number = 30;
        } else if (number == 30) {
            runner.setImage(new Image("com/game/firstgame/images/Asteroids/tile029.png"));
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
