package com.game.firstgame;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class BackgroundAnimation {
    private ImageView background1;
    private ImageView background2;
    private ParallelTransition parallelTransition;
    private int BACKGROUND_HEIGHT = 750;
    public BackgroundAnimation(ImageView background1, ImageView background2) {
        this.background1 = background1;
        this.background2 = background2;
        this.parallelTransition = parallelTransition;
    }


    public void loopingBackground() {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(10000), background1);
        translateTransition.setFromY(0);
        translateTransition.setToY(BACKGROUND_HEIGHT);
        translateTransition.setInterpolator(Interpolator.LINEAR);

        TranslateTransition translateTransition2 = new TranslateTransition(Duration.millis(10000), background2);
        translateTransition2.setFromY(0);
        translateTransition2.setToY(BACKGROUND_HEIGHT);
        translateTransition2.setInterpolator(Interpolator.LINEAR);

        parallelTransition = new ParallelTransition(translateTransition, translateTransition2);
        parallelTransition.setCycleCount(Animation.INDEFINITE);
    }

    public ParallelTransition getParallelTransition() {
        return parallelTransition;
    }
}
