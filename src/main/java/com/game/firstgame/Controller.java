package com.game.firstgame;

import javafx.animation.*;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();
    private BooleanProperty spacePressed = new SimpleBooleanProperty();
    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed).or(spacePressed);
    private double playerSize;
    private int movementVariable = 3;
    private PlayerAnimation playerAnimation;
    private PlayerAnimationInvisible playerAnimationInvisible;
    private int missileCounter = 4;
    private int switchMissile = 0;
    private boolean missileFired = false;

    private TranslateTransition transition;
    private ArrayList<ImageView> missiles;
    private List<ImageView> enemies;
    private ArrayList<TranslateTransition> translations;
    private ExplosionAnimation explosionAnimation;
    private ImageView alien;
    private List<PathTransition> enemyLocation;
    private List<Boolean> outOfBounds;
    private List<Timeline> enemyStopping;
    private ArrayList<ImageView> asteroids;
    private ArrayList<PathTransition> asteroidsLocation;
    private ArrayList<Boolean> outOfBoundsAsteroids;
    private ArrayList<AsteroidAnimation> asteroidAnimations;
    private int addEnemy = 3;
    private int addAsteroid = 4;
    private PlayerMovement playerMovement;
    private Enemy enemy;
    private Asteroids asteroidBelt;

    private ArrayList<ImageView> missileBoxes;
    private ArrayList<PathTransition> missileBoxLocation;
    private ArrayList<PowerUpAnimation> powerUpAnimations;



    @FXML
    private ImageView background1;
    @FXML
    private ImageView background2;
    @FXML
    private AnchorPane scene;
    @FXML
    private ImageView player;

    @FXML
    private Pane root;



    AnimationTimer movementTimer = new AnimationTimer() {
        @Override
        public void handle(long timestamp) {
            if(wPressed.get()) {
                player.setLayoutY(player.getLayoutY() - movementVariable);
            }

            if(sPressed.get()){
                player.setLayoutY(player.getLayoutY() + movementVariable);
            }

            if(aPressed.get()){
                player.setLayoutX(player.getLayoutX() - movementVariable);
            }

            if(dPressed.get()) {
                player.setLayoutX(player.getLayoutX() + movementVariable);
            }
            playerMovement.squareAtBorder();
        }
    };



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize the player
        playerMovement = new PlayerMovement(scene, player, wPressed, aPressed, sPressed, dPressed, spacePressed);
        playerSize = playerMovement.getPlayerSize();
        playerMovement.movementSetup();
        playerAnimation = new PlayerAnimation(player);
        playerAnimationInvisible = new PlayerAnimationInvisible(player);

        // Initialize the background
        BackgroundAnimation backgroundAnimation = new BackgroundAnimation(background1, background2);
        backgroundAnimation.loopingBackground();
        backgroundAnimation.getParallelTransition().play();

        // Initialize the enemies
        enemy = new Enemy(alien, scene);
        asteroidBelt = new Asteroids(scene);

        // Constantly check if the player was hit by an alien or asteroid
        ChangeListener<Number> enemyListener = (ov, oldValue, newValue) -> {
            Bounds playerBounds = player.localToScene(player.getBoundsInLocal());
            double playerLocation_x = playerBounds.getMinX();
            double playerLocation_y = playerBounds.getMaxY() - 38;
            if (missileFired) {
                if (!enemies.isEmpty())  {
                    // Check every alien
                    for (int i = 0; i < enemies.size(); i++) {
                        Bounds enemyBounds = enemies.get(i).localToScene(enemies.get(i).getBoundsInLocal());
                        double alienLocationMaxY = enemyBounds.getMaxY();
                        double alienLocationMinY = enemyBounds.getMinY();
                        double enemyLocation_x = enemyBounds.getCenterX();

                        // Check if the player was hit by an alien
                        if (playerBounds.intersects(enemyBounds) && playerLocation_y > alienLocationMinY) {
                            System.out.println("PLAYER HIT BY ALIEN");
                        }
                        // If an alien went past the screen, spawn new enemy
                        if (Boolean.TRUE.equals(alienLocationMaxY > 760)) {
                            enemyLocation.get(i).stop();
                            enemyLocation.remove(enemyLocation.get(i));

                            scene.getChildren().remove(enemies.get(i));
                            enemies.remove(enemies.get(i));

                            if (enemies.size() < 15)
                                enemy.spawnEnemies();


                        }
                        // Alien is in the screen
                        else if (alienLocationMaxY >= 0 && alienLocationMaxY <= 760) {
                            //outOfBounds.set(i, false);
                        }
                    }
                }

                // If there are no more enemies, spawn more
                else {
                    System.out.println("No more enemies, spawned " + addEnemy + " more");
                    for (int i = 0; i < addEnemy; i++){
                        enemy.spawnEnemies();
                    }
                    addEnemy++;
                }

                if (!asteroids.isEmpty()) {
                    //Check every asteroid
                    for (int i = 0; i < asteroids.size(); i++) {
                        Bounds asteroidBounds = asteroids.get(i).localToScene(asteroids.get(i).getBoundsInLocal());
                        double asteroidLocationMinY = asteroidBounds.getMinY() + 10;
                        double asteroidLocationCenterX = asteroidBounds.getCenterX();

                        // Check if the asteroid hits the player
                        if (playerBounds.intersects(asteroidBounds) && playerLocation_y > asteroidLocationMinY && missileFired) {
                            System.out.println("PLAYER HIT BY ASTEROID");
                        }



                        // If the asteroid went past the screen, spawn new asteroids in a different location
                        if (Boolean.TRUE.equals(asteroidLocationCenterX < -20 || asteroidLocationCenterX > 510)) {

                            asteroidsLocation.get(i).stop();
                            asteroidsLocation.remove(asteroidsLocation.get(i));

                            asteroidAnimations.get(i).stopAnimation();
                            asteroidAnimations.remove(asteroidAnimations.get(i));

                            scene.getChildren().remove(asteroids.get(i));
                            asteroids.remove(asteroids.get(i));

                            if (asteroids.size() <= 8) {
                                System.out.println("SPAWN MORE, asteroid size = " + asteroids.size());
                                asteroidBelt.spawnAsteroids();
                            }

                        }
                        // Asteroid is in the screen
                        else if (asteroidLocationCenterX > 0 && asteroidLocationCenterX < 500) {
                            //outOfBoundsAsteroids.set(i, false);
                        }
                    }
                }
                // If there are no more enemies, spawn more
                else {
                    System.out.println("No more asteroids, spawned " + addAsteroid + " more");
                    System.out.println(asteroids.size());
                    for (int i = 0; i < addAsteroid; i++){
                        asteroidBelt.spawnAsteroids();
                    }
                    addAsteroid++;
                }

                if (!missileBoxes.isEmpty()) {
                    //Check every asteroid
                    for (int i = 0; i < missileBoxes.size(); i++) {
                        Bounds missileBoxesBounds = missileBoxes.get(i).localToScene(missileBoxes.get(i).getBoundsInLocal());
                        double missileBoxLocationMinY = missileBoxesBounds.getMinY() + 10;
                        double missileBoxLocationCenterX = missileBoxesBounds.getCenterX();
                        double missileBoxLocationMaxY = missileBoxesBounds.getMaxY();

                        // Check if the powerUp hits the player
                        if (playerBounds.intersects(missileBoxesBounds) && playerLocation_y > missileBoxLocationMinY) {
                            System.out.println("PLAYER HIT BY THE POWERUP");
                            missileCounter += 10;

                            missileBoxLocation.get(i).stop();
                            missileBoxLocation.remove(missileBoxLocation.get(i));

                            scene.getChildren().remove(missileBoxes.get(i));
                            missileBoxes.remove(missileBoxes.get(i));

                            for (int x = 0; x < 10; x++){
                                Image image = new Image("com/game/firstgame/images/SpaceInvaderAnim/Missile.png", 10, 30, true, false);
                                ImageView missile = new ImageView(image);
                                missiles.add(missile);
                            }
                        }


                        // If the powerUp went past the screen remove its existence
                        if (Boolean.TRUE.equals(missileBoxLocationMaxY > 750)) {
                            missileBoxLocation.get(i).stop();
                            missileBoxLocation.remove(missileBoxLocation.get(i));

                            scene.getChildren().remove(missileBoxes.get(i));
                            missileBoxes.remove(missileBoxes.get(i));
                        }
                    }
                }
            }
        };

        // Spawn aliens every 8 seconds
        Timeline spawnAlienTimeline = new Timeline(
                new KeyFrame(Duration.seconds(8), event -> {
                    if (enemies.size() <= 15) {
                        enemy.spawnEnemies();
                        enemyStopping.get(enemy.getEnemies().size() - 1).play();
                        enemyStopping.get(0).play();

                        for (ImageView alienSpaceShip : enemies) {
                            alienSpaceShip.translateXProperty().addListener(enemyListener);
                            alienSpaceShip.translateYProperty().addListener(enemyListener);
                        }
                    }
                })
        );
        spawnAlienTimeline.setCycleCount(Animation.INDEFINITE);
        spawnAlienTimeline.play();
        enemies = enemy.getEnemies();
        enemyLocation = enemy.getEnemyLocation();
        outOfBounds = enemy.getOutOfBounds();
        enemyStopping = enemy.getEnemyStopping();

        // Initialize the asteroids and spawn asteroids every 5 seconds
        Timeline spawnAsteroidsTimeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    if (asteroids.size() <= 8) {
                        System.out.println(asteroids.size());
                        asteroidBelt.spawnAsteroids();

                        for (ImageView asteroid: asteroids) {
                            asteroid.translateXProperty().addListener(enemyListener);
                            asteroid.translateYProperty().addListener(enemyListener);
                        }
                    }
                })
        );
        spawnAsteroidsTimeline.setCycleCount(Animation.INDEFINITE);
        spawnAsteroidsTimeline.play();
        asteroids = asteroidBelt.getAsteroids();
        asteroidsLocation = asteroidBelt.getAsteroidsLocation();
        outOfBoundsAsteroids = asteroidBelt.getOutOfBoundsAsteroids();
        asteroidAnimations = asteroidBelt.getAsteroidAnimations();

        missileBoxes = new ArrayList<>();
        missileBoxLocation = new ArrayList<>();
        powerUpAnimations = new ArrayList<>();
        Timeline spawnMissileTimeline = new Timeline(
                new KeyFrame(Duration.seconds(10), event -> {
                    if (missileFired) {
                        System.out.println("Spawned missile box");

                        for (ImageView powerUp: missileBoxes) {
                            powerUp.translateXProperty().addListener(enemyListener);
                            powerUp.translateYProperty().addListener(enemyListener);
                        }

                        spawnMissiles();
                    }
                })
        );
        spawnMissileTimeline.setCycleCount(Animation.INDEFINITE);
        spawnMissileTimeline.play();



        // Initialize the missiles
        missiles = new ArrayList<>();
        translations = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            Image image = new Image("com/game/firstgame/images/SpaceInvaderAnim/Missile.png", 10, 30, true, false);
            ImageView missile = new ImageView(image);
            missiles.add(missile);
        }

        // When SPACE key was released, the player will shoot missile
        transition = new TranslateTransition();
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.SPACE && missileCounter >= 0) {
                // Change the player ship from being invisible to normal
                Image changePlayer = new Image("com/game/firstgame/images/Player/tile000.png");
                player.setImage(changePlayer);

                // Loads up the missile
                ImageView getMissile = missiles.get(missiles.size() - 1);
                // Switches the location of where the missile will be fired
                if (switchMissile == 0) {
                    getMissile.setX(player.getLayoutX() + 5);
                    switchMissile = 1;
                } else if (switchMissile == 1) {
                    getMissile.setX(player.getLayoutX() + 30);
                    switchMissile = 0;
                }
                getMissile.setY(player.getLayoutY() - 20);

                // Set the fire rate for the missile
                transition.setNode(getMissile);
                transition.setDuration(Duration.millis(135));
                transition.setToY(-750);
                transition.setByY(player.getLayoutY() - 20);
                transition.play();
                translations.add(transition);
                // Deploy the missile
                scene.getChildren().add(getMissile);

                // Constantly check if the alien or asteroid was hit by the missile
                ChangeListener<Number> missileListener = (ov, oldValue, newValue) -> {
                    if (!enemies.isEmpty() && missileCounter > 0) {
                        Bounds missileBounds = getMissile.localToScene(getMissile.getBoundsInLocal());

                        // Check every enemy if it was hit by a missile
                        for (int i = 0; i < enemies.size(); i++) {
                            Bounds enemyBounds = enemies.get(i).localToScene(enemies.get(i).getBoundsInLocal());

                            // Check if the missile hit an alien
                            if (missileBounds.intersects(enemyBounds)) {
                                System.out.println("------------------------------------");
                                System.out.println("ENEMY #" + i + " HIT ");
                                System.out.println("ENEMY Y POS = " + enemyBounds.getMinX());
                                System.out.println("ENEMY X POS = " + enemyBounds.getMinY());
                                System.out.println("------------------------------------");
                                System.out.println();

                                // Play explosion animation
                                explosionAnimation = new ExplosionAnimation(scene, enemies.get(i));
                                explosionAnimation.startAnimation();
                                // If an alien was hit, remove it from the List and Scene
                                enemyLocation.get(i).stop();
                                enemyLocation.remove(enemyLocation.get(i));
                                enemies.remove(enemies.get(i));
                            }
                        }

                        // Check every asteroid if it was hit by a missile
                        for (int i = 0; i < asteroids.size(); i++) {
                            Bounds asteroidBounds = asteroids.get(i).localToScene(asteroids.get(i).getBoundsInLocal());

                            if (missileBounds.intersects(asteroidBounds)) {
                                System.out.println("------------------------------------");
                                System.out.println("ASTEROID #" + i + " HIT ");
                                System.out.println("ASTEROID Y POS = " + asteroidBounds.getMinX());
                                System.out.println("ASTEROID X POS = " + asteroidBounds.getMinY());
                                System.out.println("------------------------------------");
                                System.out.println();

                                // Play explosion animation for the asteroid
                                explosionAnimation = new ExplosionAnimation(scene, asteroids.get(i));
                                explosionAnimation.startAnimation();

                                // Remove the asteroid
                                asteroidAnimations.get(i).stopAnimation();
                                asteroidAnimations.remove(asteroidAnimations.get(i));

                                asteroidsLocation.get(i).stop();
                                asteroidsLocation.remove(asteroidsLocation.get(i));

                                asteroids.remove(asteroids.get(i));
                            }
                        }
                    }
                };

                // Add a listener for the missile to constantly check its location
                addListener(getMissile, missileListener);

                // Decrease the missile that the player have
                missileCounter--;
                missiles.remove(missiles.size() - 1);
                missileFired = true;
            }
        });
        addListener(player, enemyListener);

        // Controls the movement of the player
        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if(!aBoolean){
                if (!missileFired) {
                    movementTimer.start();
                    if(Boolean.FALSE.equals(sPressed.getValue())){
                        playerAnimationInvisible.startAnimation();
                    }
                    else {
                        playerAnimationInvisible.playerIdle();
                    }
                }
                else {
                    movementTimer.start();
                    if(Boolean.FALSE.equals(sPressed.getValue())){
                        playerAnimationInvisible.stopAnimation();
                        playerAnimation.startAnimation();
                    }
                    else {
                        playerAnimationInvisible.stopAnimation();
                        playerAnimation.stopAnimation();
                    }
                }

            } else {
                if (!missileFired) {
                    movementTimer.stop();
                    playerAnimationInvisible.playerIdle();
                }
                else {
                    movementTimer.stop();
                    playerAnimation.stopAnimation();
                }
            }
        }));
    }


    private void spawnMissiles() {
        Random rand = new Random();
        PathTransition move = new PathTransition();

        int randomPosition = (rand.nextInt(15 - 1) + 1) * 30;

        Image image = new Image("com/game/firstgame/images/Powerup/powerup01_1.png", 25, 25, true, true);
        ImageView missileBox = new ImageView(image);
        missileBox.setY(-20);
        missileBox.setX(randomPosition);

        PowerUpAnimation powerUpAnimation = new PowerUpAnimation(scene, missileBox);
        powerUpAnimation.startAnimation();

        scene.getChildren().add(missileBox);
        missileBoxes.add(missileBox);

        move.setNode(missileBoxes.get(missileBoxes.size() - 1));

        Path path = new Path();
        path.getElements().add(new MoveTo(missileBox.getX(),0));
        path.getElements().add(new VLineTo(780));

        move.setDuration(Duration.seconds(8));
        //move.setCycleCount(PathTransition.INDEFINITE);
        move.setPath(path);

        missileBoxLocation.add(move);
        powerUpAnimations.add(powerUpAnimation);

        move.play();
    }
    private void addListener(ImageView getMissile, ChangeListener<Number> missileListener) {
        for (ImageView asteroid: asteroids) {
            asteroid.translateXProperty().addListener(missileListener);
            asteroid.translateYProperty().addListener(missileListener);
        }

        for (ImageView alienSpaceShip : enemies) {
            alienSpaceShip.translateXProperty().addListener(missileListener);
            alienSpaceShip.translateYProperty().addListener(missileListener);
        }

        getMissile.translateXProperty().addListener(missileListener);
        getMissile.translateYProperty().addListener(missileListener);
    }
}