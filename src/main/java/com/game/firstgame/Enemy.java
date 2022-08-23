package com.game.firstgame;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Enemy {
    private ArrayList<ImageView> enemies;
    private AnchorPane scene;
    private ImageView alien;
    private ArrayList<PathTransition> enemyLocation;
    private ArrayList<Boolean> outOfBounds;
    private ArrayList<Timeline> enemyStopping;
    public Enemy (ImageView alien, AnchorPane scene){
        this.scene = scene;
        this.alien = alien;

        enemies = new ArrayList<>();
        enemyLocation = new ArrayList<>();
        outOfBounds = new ArrayList<>();
        enemyStopping = new ArrayList<>();
    }

    public void spawnEnemies(){
        Random rand = new Random();
        PathTransition move = new PathTransition();

        int randomPosition = (rand.nextInt(15 - 1) + 1) * 30;

        // Spawn random alien spaceship
        Image image;
        int randomEnemies = (rand.nextInt(10 - 1) + 1);
        if (randomEnemies == 1) {
            image = new Image("com/game/firstgame/images/Enemies/alien.png", 30, 30, true, false);
        } else if (randomEnemies == 2){
            image = new Image("com/game/firstgame/images/Enemies/alien2.png", 30, 30, true, false);
        } else if (randomEnemies == 3) {
            image = new Image("com/game/firstgame/images/Enemies/alien3.png", 30, 30, true, false);
        } else if (randomEnemies == 4) {
            image = new Image("com/game/firstgame/images/Enemies/alien4.png", 30, 30, true, false);
        } else if (randomEnemies == 5) {
            image = new Image("com/game/firstgame/images/Enemies/alien5.png", 30, 30, true, false);
        } else if (randomEnemies == 2){
            image = new Image("com/game/firstgame/images/Enemies/alien6.png", 30, 30, true, false);
        } else if (randomEnemies == 3) {
            image = new Image("com/game/firstgame/images/Enemies/alien7.png", 30, 30, true, false);
        } else if (randomEnemies == 4) {
            image = new Image("com/game/firstgame/images/Enemies/alien8.png", 30, 30, true, false);
        } else if (randomEnemies == 5) {
            image = new Image("com/game/firstgame/images/Enemies/alien9.png", 30, 30, true, false);
        } else {
            image = new Image("com/game/firstgame/images/Enemies/alien10.png", 30, 30, true, false);
        }


        alien = new ImageView(image);
        alien.setX(randomPosition);

        scene.getChildren().add(alien);
        enemies.add(alien);

        move.setNode(enemies.get(enemies.size() - 1));

        alien.setY(-20);
        Path path = new Path();
        path.getElements().add(new MoveTo(alien.getX(),0));
        path.getElements().add(new VLineTo(760));

        move.setDuration(Duration.seconds(7));
        //move.setCycleCount(PathTransition.INDEFINITE);
        move.setDelay(Duration.millis(200));
        move.setPath(path);

        enemyLocation.add(move);
        outOfBounds.add(false);
        move.play();

        int stopRandomly = rand.nextInt((5 - 1) + 1) + 1;

        // ENEMY STOPPING
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(stopRandomly), event -> move.play()),
                new KeyFrame(Duration.seconds(5), event -> move.pause()),
                new KeyFrame(Duration.seconds(2), event -> move.play())
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        enemyStopping.add(timeline);
    }

    public List<Boolean> getOutOfBounds() {
        return outOfBounds;
    }

    public List<ImageView> getEnemies() {
        return enemies;
    }

    public List<PathTransition> getEnemyLocation() {
        return enemyLocation;
    }

    public List<Timeline> getEnemyStopping() {
        return enemyStopping;
    }
}
