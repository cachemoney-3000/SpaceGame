package com.game.firstgame;

import javafx.animation.PathTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.List;
import java.util.List;
import java.util.Random;

public class PowerUpObject {
    private final ArrayList<ImageView> missiles;
    private final AnchorPane scene;
    private final ArrayList<ImageView> missileBoxes;
    private final ArrayList<PathTransition> missileBoxLocation;
    private final ArrayList<PowerUpAnimation> powerUpAnimations;
    private final ArrayList <Integer> powerUpTypes;

    public PowerUpObject (AnchorPane scene) {
        this.scene = scene;
        missiles = new ArrayList<>();
        missileBoxes = new ArrayList<>();
        missileBoxLocation = new ArrayList<>();
        powerUpAnimations = new ArrayList<>();
        powerUpTypes = new ArrayList<>();
    }

    public void loadMissileImages () {
        for (int i = 0; i < 10; i++) {
            Image image = new Image("com/game/firstgame/images/Player/missile.png", 10, 30, true, false);
            ImageView missile = new ImageView(image);
            missiles.add(missile);
        }

    }

    public void spawnPowerUp() {
        Random rand = new Random();
        PathTransition move = new PathTransition();

        int randomPosition = (rand.nextInt(15 - 1) + 1) * 30;

        Image image = new Image("com/game/firstgame/images/Powerup/powerup01_1.png", 25, 25, true, true);
        ImageView missileBox = new ImageView(image);
        missileBox.setY(-20);
        missileBox.setX(randomPosition);

        int powerUpType = rand.nextInt(3);
        PowerUpAnimation powerUpAnimation = new PowerUpAnimation(missileBox, powerUpType);
        powerUpAnimation.startAnimation();
        powerUpTypes.add(powerUpType);

        scene.getChildren().add(missileBox);
        missileBoxes.add(missileBox);

        move.setNode(missileBoxes.get(missileBoxes.size() - 1));

        Path path = new Path();
        path.getElements().add(new MoveTo(missileBox.getX(),0));
        path.getElements().add(new VLineTo(780));

        move.setDuration(Duration.seconds(8));
        move.setPath(path);

        missileBoxLocation.add(move);
        powerUpAnimations.add(powerUpAnimation);

        move.play();
    }


    public void removePowerUp(int i) {
        missileBoxLocation.get(i).stop();
        missileBoxLocation.remove(missileBoxLocation.get(i));

        scene.getChildren().remove(missileBoxes.get(i));
        missileBoxes.remove(missileBoxes.get(i));

        powerUpAnimations.get(i).stopAnimation();
        powerUpAnimations.remove(powerUpAnimations.get(i));

        powerUpTypes.remove(powerUpTypes.get(i));
    }

    public List<ImageView> getMissileBoxes() {
        return missileBoxes;
    }

    public List<ImageView> getMissiles() {
        return missiles;
    }

    public List<PathTransition> getMissileBoxLocation() {
        return missileBoxLocation;
    }

    public List<PowerUpAnimation> getPowerUpAnimations() {
        return powerUpAnimations;
    }

    public List<Integer> getPowerUpTypes() {
        return powerUpTypes;
    }
}
