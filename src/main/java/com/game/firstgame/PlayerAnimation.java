package com.game.firstgame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.File;

public class PlayerAnimation {
    private ImageView runner;
    int number = 1;

    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.1), event -> {
        if(number == 1){
            runner.setImage(new Image("com/game/firstgame/images/SpaceInvaderAnim/Space-Invaders-Ship-fire-1.png"));
            number = 2;
        } else if( number == 2){
            runner.setImage(new Image("com/game/firstgame/images/SpaceInvaderAnim/Space-Invaders-Ship-fire-2.png"));
            number = 3;
        }
        else {
            runner.setImage(new Image("com/game/firstgame/images/SpaceInvaderAnim/Space-Invaders-Ship-fire-3.png"));
            number = 1;
        }
    }));

    public PlayerAnimation(ImageView runner) {
        this.runner = runner;
        timeline.setCycleCount(Animation.INDEFINITE);
    }

    public void startAnimation(){
        timeline.play();
    }

    public void stopAnimation(){
        runner.setImage(new Image("com/game/firstgame/images/SpaceInvaderAnim/Space-Invaders-Ship.png"));
        number = 1;
        timeline.stop();
    }
}
