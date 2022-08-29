package com.game.firstgame;

import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.List;
import java.util.Random;

public class Asteroids {
    private final AnchorPane scene;
    private final ArrayList<ImageView> asteroids;
    private final ArrayList<PathTransition> asteroidsLocation;
    private final ArrayList<Boolean> outOfBoundsAsteroids;
    private final ArrayList<AsteroidAnimation> asteroidAnimations;

    public Asteroids(AnchorPane scene) {
        this.scene = scene;

        asteroids = new ArrayList<>();
        asteroidsLocation = new ArrayList<>();
        outOfBoundsAsteroids = new ArrayList<>();
        asteroidAnimations = new ArrayList<>();
    }

    public void spawnAsteroids(){
        Random rand = new Random();
        PathTransition move = new PathTransition();

        int randomNum = rand.nextInt((6 - 1) + 1) + 1;
        int randomY = (rand.nextInt(750 - 1) + 1);

        Image image = new Image("com/game/firstgame/images/Asteroids1/tile000.png", 20, 20, true, true);
        ImageView asteroid = new ImageView(image);


        AsteroidAnimation asteroidAnimation = new AsteroidAnimation(scene, asteroid, randomNum);
        asteroidAnimation.startAnimation();

        scene.getChildren().add(asteroid);
        asteroids.add(asteroid);

        move.setNode(asteroids.get(asteroids.size() - 1));

        Path path = new Path();

        int coinFlip = rand.nextInt((2 - 1) + 1) + 1;
        if (coinFlip % 2 == 0) {
            path.getElements().add(new MoveTo(-20, randomY));
            path.getElements().add(new HLineTo(520));
        } else {
            path.getElements().add(new MoveTo(500,randomY));
            path.getElements().add(new HLineTo(-20));
        }


        move.setDuration(Duration.seconds(4));
        move.setPath(path);

        asteroidsLocation.add(move);
        outOfBoundsAsteroids.add(false);

        asteroidAnimations.add(asteroidAnimation);

        move.play();
    }

    public List<AsteroidAnimation> getAsteroidAnimations() {
        return asteroidAnimations;
    }

    public List<Boolean> getOutOfBoundsAsteroids() {
        return outOfBoundsAsteroids;
    }

    public List<ImageView> getAsteroids() {
        return asteroids;
    }

    public List<PathTransition> getAsteroidsLocation() {
        return asteroidsLocation;
    }
}
