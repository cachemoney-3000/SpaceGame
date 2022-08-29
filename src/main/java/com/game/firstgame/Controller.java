package com.game.firstgame;

import javafx.animation.*;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class Controller implements Initializable {
    private final BooleanProperty wPressed = new SimpleBooleanProperty();
    private final BooleanProperty aPressed = new SimpleBooleanProperty();
    private final BooleanProperty sPressed = new SimpleBooleanProperty();
    private final BooleanProperty dPressed = new SimpleBooleanProperty();
    private final BooleanProperty spacePressed = new SimpleBooleanProperty();
    private final BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed).or(spacePressed);
    private PlayerAnimation playerAnimation;
    private PlayerAnimationInvisible playerAnimationInvisible;
    private int missileCounter;
    private int switchMissile;
    private boolean missileFired;

    private TranslateTransition transition;
    private List<ImageView> missiles;
    private List<ImageView> enemies;

    private ExplosionAnimation explosionAnimation;
    private ImageView alien;
    private List<PathTransition> enemyLocation;
    private List<Boolean> outOfBounds;
    private List<Timeline> enemyStopping;
    private List<ImageView> asteroids;
    private List<PathTransition> asteroidsLocation;
    private List<Boolean> outOfBoundsAsteroids;
    private List<AsteroidAnimation> asteroidAnimations;
    private int addEnemy;
    private int addAsteroid;
    private PlayerMovement playerMovement;
    private Enemy enemy;
    private Asteroids asteroidBelt;

    private List<ImageView> powerUps;
    private List<PathTransition> powerUpLocations;
    private List<PowerUpAnimation> powerUpAnimations;
    private List <Integer> powerUpTypes;

    private int remainingLife;
    private int playerScore;
    private boolean popUpShowed;
    private PowerUpObject powerUpObject;




    @FXML
    private ImageView background1;
    @FXML
    private ImageView background2;
    @FXML
    private AnchorPane scene;

    private ImageView player;
    @FXML
    private Text scoreBoard;
    @FXML
    private Text lifeBoard;
    @FXML
    private Text missileBoard;



    AnimationTimer movementTimer = new AnimationTimer() {
        @Override
        public void handle(long timestamp) {
            int movementVariable = 3;
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
        spawnPlayer();
        populateTheCounters();
        initBackground();
        spawnedEntities();
        shootTheMissile();
        gamePlayTimeline();
        playerMovement();
    }

    private void getPowerUp(PowerUpObject powerUpObject, int i) {
        if (powerUpTypes.get(i) == 0) {
            // Increase player missile
            missileCounter += 10;
            powerUpObject.loadMissileImages();
            List <ImageView> getAddedMissiles = powerUpObject.getMissiles();
            missiles.addAll(getAddedMissiles);

            missileBoard.setText(String.valueOf(missileCounter));
        }
        else if (powerUpTypes.get(i) == 1) {
            // Increase player life
            remainingLife += 500;
            lifeBoard.setText(String.valueOf(remainingLife));
        }
        else {
            // Increase playerScore
            playerScore += 250;
            scoreBoard.setText(String.valueOf(playerScore));
        }

        powerUpObject.removePowerUp(i);
    }

    private boolean isGameOver() {
        if (remainingLife == 0) {
            return true;
        }
        else {
            remainingLife--;
            lifeBoard.setText(String.valueOf(remainingLife));
        }

        return false;
    }

    private void cleanUp () {
        // REMOVE ALL ASTEROIDS
        asteroidsLocation.clear();
        asteroidAnimations.clear();
        scene.getChildren().removeAll(asteroids);
        asteroids.clear();

        // REMOVE ALL ENEMY ALIENS
        enemyLocation.clear();
        enemyStopping.clear();
        scene.getChildren().removeAll(enemies);
        enemies.clear();

        // REMOVE ALL SPAWNED POWERUPS
        powerUpLocations.clear();
        powerUpTypes.clear();
        powerUpAnimations.clear();
        scene.getChildren().removeAll(powerUps);
        powerUps.clear();

        // REMOVE ALL SPAWNED MISSILES
        scene.getChildren().removeAll(missiles);
        missiles.clear();




    }

    private void removeAsteroid(int k) {
        asteroidsLocation.get(k).stop();
        asteroidsLocation.remove(asteroidsLocation.get(k));

        asteroidAnimations.get(k).stopAnimation();
        asteroidAnimations.remove(asteroidAnimations.get(k));

        scene.getChildren().remove(asteroids.get(k));
        asteroids.remove(asteroids.get(k));
    }

    private void popUpResetButton () {
        if (!popUpShowed) {
            Stage primaryStage = new Stage();
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(primaryStage);
            VBox dialogVbox = new VBox(20);

            Button btn = new Button("Reset");
            dialogVbox.getChildren().add(btn);


            Scene dialogScene = new Scene(dialogVbox, 300, 200);
            dialog.setScene(dialogScene);
            dialog.show();

            btn.setOnAction(event -> {
                dialog.close();
                primaryStage.close();

                spawnPlayer();
                populateTheCounters();
                //spawnedEntities();
                //gamePlayTimeline();
                movementTimer.stop();

            });

            popUpShowed = true;
        }
    }


    private void addMissileListener(ImageView getMissile, ChangeListener<Number> missileListener) {
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

    public void populateTheCounters () {
        missileCounter = 5;
        switchMissile = 0;
        missileFired = false;
        addEnemy = 3;
        addAsteroid = 4;
        popUpShowed = false;
        transition = new TranslateTransition();

        // Initialize the scoreboard
        remainingLife = 1000;
        playerScore = 0;
        lifeBoard.setText(String.valueOf(remainingLife));
        missileBoard.setText(String.valueOf(missileCounter));
        scoreBoard.setText(String.valueOf(playerScore));


        missiles = new ArrayList<>();
        for (int i = 0; i < missileCounter; i++) {
            Image image = new Image("com/game/firstgame/images/Player/missile.png", 10, 30, true, false);
            ImageView missile = new ImageView(image);
            missiles.add(missile);
        }
    }

    public void spawnPlayer() {
        player = new ImageView(new Image("com/game/firstgame/images/Player/invisible/inv1.png"));
        player.setFitHeight(85);
        player.setLayoutX(231);
        player.setLayoutY(658);
        player.setPreserveRatio(true);
        scene.getChildren().add(player);

        // Initialize the player
        playerMovement = new PlayerMovement(scene, player, wPressed, aPressed, sPressed, dPressed, spacePressed);
        playerMovement.movementSetup();
        playerAnimation = new PlayerAnimation(player);
        playerAnimationInvisible = new PlayerAnimationInvisible(player);

        player.setFocusTraversable(true);
        player.requestFocus();
    }

    public void initBackground() {
        // Initialize the background
        BackgroundAnimation backgroundAnimation = new BackgroundAnimation(background1, background2);
        backgroundAnimation.loopingBackground();
        backgroundAnimation.getParallelTransition().play();
    }

    public void spawnedEntities() {
        // Initialize the powerUps
        powerUpObject = new PowerUpObject(scene);

        // Initialize the enemies
        enemy = new Enemy(alien, scene);
        asteroidBelt = new Asteroids(scene);

        // Constantly check if the player was hit by an alien or asteroid
        ChangeListener<Number> enemyListener = (ov, oldValue, newValue) -> {
            Bounds playerBounds = player.localToScene(player.getBoundsInLocal());
            double playerLocationY = playerBounds.getMaxY() - 40;
            if (missileFired) {
                if (!enemies.isEmpty())  {
                    // Check every alien
                    for (int i = 0; i < enemies.size(); i++) {
                        Bounds enemyBounds = enemies.get(i).localToScene(enemies.get(i).getBoundsInLocal());
                        double alienLocationMaxY = enemyBounds.getMaxY();
                        double alienLocationMinY = enemyBounds.getMinY();

                        // Check if the player was hit by an alien
                        if (playerBounds.intersects(enemyBounds) && playerLocationY > alienLocationMinY && isGameOver()) {
                            cleanUp();
                            popUpResetButton();
                        }
                        // If an alien went past the screen, spawn new enemy
                        if (Boolean.TRUE.equals(alienLocationMaxY > 760)) {
                            enemyLocation.get(i).stop();
                            enemyLocation.remove(enemyLocation.get(i));

                            scene.getChildren().remove(enemies.get(i));
                            enemies.remove(enemies.get(i));

                            if (enemies.size() < 15 && remainingLife != 0) enemy.spawnEnemies();
                        }
                    }
                }

                // If there are no more enemies, spawn more
                else {
                    if (remainingLife != 0) {
                        for (int i = 0; i < addEnemy; i++){
                            enemy.spawnEnemies();
                        }
                        addEnemy++;
                    }
                }

                if (!asteroids.isEmpty()) {
                    //Check every asteroid
                    for (int i = 0; i < asteroids.size(); i++) {
                        Bounds asteroidBounds = asteroids.get(i).localToScene(asteroids.get(i).getBoundsInLocal());
                        double asteroidLocationMinY = asteroidBounds.getMinY() + 15;
                        double asteroidLocationCenterX = asteroidBounds.getCenterX();

                        // Check if the asteroid hits the player
                        if (playerBounds.intersects(asteroidBounds) && playerLocationY > asteroidLocationMinY && isGameOver()) {
                            cleanUp();
                            popUpResetButton();
                        }


                        // If the asteroid went past the screen, spawn new asteroids in a different location
                        if (Boolean.TRUE.equals(asteroidLocationCenterX < -20 || asteroidLocationCenterX > 510)) {
                            removeAsteroid(i);
                            if (asteroids.size() <= 8 && remainingLife != 0) {
                                asteroidBelt.spawnAsteroids();
                            }
                        }
                    }
                }
                // If there are no more enemies, spawn more
                else {
                    if (remainingLife != 0) {
                        for (int i = 0; i < addAsteroid; i++){
                            asteroidBelt.spawnAsteroids();
                        }
                        addAsteroid++;
                    }
                }

                if (!powerUps.isEmpty()) {
                    //Check every asteroid
                    for (int i = 0; i < powerUps.size(); i++) {
                        Bounds missileBoxesBounds = powerUps.get(i).localToScene(powerUps.get(i).getBoundsInLocal());
                        double missileBoxLocationMinY = missileBoxesBounds.getMinY() + 10;
                        double missileBoxLocationMaxY = missileBoxesBounds.getMaxY();

                        // Check if the powerUp hits the player
                        if (playerBounds.intersects(missileBoxesBounds) && playerLocationY > missileBoxLocationMinY) {
                            getPowerUp(powerUpObject, i);
                        }

                        // If the powerUp went past the screen remove its existence
                        if (Boolean.TRUE.equals(missileBoxLocationMaxY > 750)) {
                            powerUpObject.removePowerUp(i);
                        }
                    }
                }
            }
        };

        // Spawn aliens every 5 seconds
        Timeline spawnAlienTimeline = new Timeline(
                new KeyFrame(Duration.seconds(5), event -> {
                    if (enemies.size() <= 15 && remainingLife != 0) {
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
                    if (asteroids.size() <= 8 && remainingLife != 0) {
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

        // Timeline for spawning power ups on screen
        Timeline spawnPowerUpsTimeline = new Timeline(
                new KeyFrame(Duration.seconds(2), event -> {
                    if (missileFired && remainingLife != 0) {
                        for (ImageView powerUp: powerUps ) {
                            powerUp.translateXProperty().addListener(enemyListener);
                            powerUp.translateYProperty().addListener(enemyListener);
                        }

                        powerUpObject.spawnPowerUp();
                    }

                })
        );
        spawnPowerUpsTimeline.setCycleCount(Animation.INDEFINITE);
        spawnPowerUpsTimeline.play();

        powerUps = powerUpObject.getMissileBoxes();
        powerUpLocations = powerUpObject.getMissileBoxLocation();
        powerUpAnimations = powerUpObject.getPowerUpAnimations();
        powerUpTypes = powerUpObject.getPowerUpTypes();
        // LOAD MISSILES



    }

    public void gamePlayTimeline () {
        Timeline gameTimeline = new Timeline(
                new KeyFrame(Duration.millis(250), event -> {
                    if (remainingLife == 0) {
                        explosionAnimation = new ExplosionAnimation(scene, player);
                        explosionAnimation.startAnimation();
                    }

                })
        );
        gameTimeline.setCycleCount(Animation.INDEFINITE);
        gameTimeline.play();
    }

    public void shootTheMissile() {
        // When SPACE key was released, the player will shoot missile
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.SPACE && missileCounter > 0) {
                // Loads up the missile
                ImageView getMissile = missiles.get(missiles.size() - 1);
                // Switches the location of where the missile will be fired
                if (switchMissile == 0) {
                    getMissile.setLayoutX(player.getLayoutX() + 3);
                    switchMissile = 1;
                } else if (switchMissile == 1) {
                    getMissile.setLayoutX(player.getLayoutX() + 25);
                    switchMissile = 0;
                }
                getMissile.setLayoutY(player.getLayoutY() - 20);


                // Set the fire rate for the missile
                transition.setNode(getMissile);
                transition.setDuration(Duration.millis(135));
                transition.setToY(-750);
                transition.setByY(player.getLayoutY() - 20);
                transition.play();

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
                                // Increase playerScore
                                playerScore += 100;
                                scoreBoard.setText(String.valueOf(playerScore));

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
                                // Increase playerScore
                                playerScore += 50;
                                scoreBoard.setText(String.valueOf(playerScore));

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

                        if (missileBounds.getMinY() < 0){
                            scene.getChildren().remove(getMissile);
                            missiles.remove(getMissile);

                        }
                    }
                };

                // Add a listener for the missile to constantly check its location
                addMissileListener(getMissile, missileListener);

                // Decrease the missile that the player have
                missileCounter--;
                missiles.remove(missiles.size() - 1);
                missileFired = true;

                missileBoard.setText(String.valueOf(missileCounter));
            }
        });
    }

    public void playerMovement() {
        // Controls the movement of the player
        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if(!aBoolean){
                movementTimer.start();
                if (!missileFired) {
                    if(Boolean.FALSE.equals(sPressed.getValue())){
                        playerAnimationInvisible.startAnimation();
                    }
                    else {
                        playerAnimationInvisible.playerIdle();
                    }
                }
                else {
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
                movementTimer.stop();
                if (!missileFired) {
                    playerAnimationInvisible.playerIdle();
                }
                else {
                    playerAnimation.stopAnimation();
                }
            }
        }));
    }
}