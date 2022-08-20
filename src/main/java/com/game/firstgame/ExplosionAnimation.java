package com.game.firstgame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class ExplosionAnimation {
    private ImageView runner;
    private AnchorPane scene;
    int number = 1;

    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {

        if(number == 1){
            // SET THE LOCATION OF EXPLOSION
            runner.setX(runner.getX() - 20);
            runner.setY(runner.getY() - 20);
            runner.setImage(new Image("com/game/firstgame/images/ExplosionAnimation/tile000.png"));
            number = 2;
        } else if( number == 2){
            runner.setImage(new Image("com/game/firstgame/images/ExplosionAnimation/tile001.png"));
            number = 3;
        } else if( number == 3){
            runner.setImage(new Image("com/game/firstgame/images/ExplosionAnimation/tile002.png"));
            number = 4;
        } else if( number == 4){
            runner.setImage(new Image("com/game/firstgame/images/ExplosionAnimation/tile003.png"));
            number = 5;
        } else if( number == 5){
            runner.setImage(new Image("com/game/firstgame/images/ExplosionAnimation/tile004.png"));
            number = 6;
        } else if( number == 6){
            runner.setImage(new Image("com/game/firstgame/images/ExplosionAnimation/tile005.png"));
            number = 7;
        } else if( number == 7){
            runner.setImage(new Image("com/game/firstgame/images/ExplosionAnimation/tile006.png"));
            number = 8;
        } else if( number == 8){
            runner.setImage(new Image("com/game/firstgame/images/ExplosionAnimation/tile007.png"));
            number = 9;
        } else if( number == 9){
            runner.setImage(new Image("com/game/firstgame/images/ExplosionAnimation/tile008.png"));
            number = 10;
        } else if( number == 10){
            runner.setImage(new Image("com/game/firstgame/images/ExplosionAnimation/tile009.png"));
            number = 11;
        } else if( number == 11){
            runner.setImage(new Image("com/game/firstgame/images/ExplosionAnimation/tile010.png"));
            number = 12;
        }
        else {
            runner.setImage(new Image("com/game/firstgame/images/ExplosionAnimation/tile011.png"));
            stopAnimation();
            scene.getChildren().remove(runner);

        }
    }));

    public ExplosionAnimation(AnchorPane scene, ImageView runner) {
        this.runner = runner;
        this.scene = scene;
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
