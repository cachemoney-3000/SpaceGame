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
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;


public class Controller implements Initializable {
    private BooleanProperty wPressed = new SimpleBooleanProperty();
    private BooleanProperty aPressed = new SimpleBooleanProperty();
    private BooleanProperty sPressed = new SimpleBooleanProperty();
    private BooleanProperty dPressed = new SimpleBooleanProperty();
    private BooleanProperty spacePressed = new SimpleBooleanProperty();


    private BooleanBinding keyPressed = wPressed.or(aPressed).or(sPressed).or(dPressed).or(spacePressed);
    private double shapeSize;
    private int movementVariable = 3;
    private int BACKGROUND_HEIGHT = 750;
    private ParallelTransition parallelTransition;
    private PlayerAnimation playerAnimation;
    private int missileCounter = 49;
    private int switchMissile = 0;
    private int counter = 0;

    private TranslateTransition transition;
    private ArrayList<ImageView> missiles;
    private ArrayList<ImageView> enemies;
    private ArrayList<TranslateTransition> translations;
    private ExplosionAnimation explosionAnimation;
    private ImageView alien;
    private ArrayList<PathTransition> enemyLocation;


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



    AnimationTimer timer = new AnimationTimer() {
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

            squareAtBorder();
        }
    };



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // LOAD MISSILES
        shapeSize = player.getFitHeight();
        movementSetup();

        playerAnimation = new PlayerAnimation(player);
        loopingBackground();
        parallelTransition.play();


        enemies = new ArrayList<>();
        enemyLocation = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            spawnEnemies(i);
        }


        /*
        if (!enemies.isEmpty()) {
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(0).translateXProperty().addListener(listener);
                enemies.get(0).translateYProperty().addListener(listener);
            }
        }

         */

        missiles = new ArrayList<>();
        translations = new ArrayList<>();
        for (int i = 0; i < 50; i++){
            Image image = new Image("com/game/firstgame/images/SpaceInvaderAnim/Missile.png", 10, 30, true, false);
            ImageView missile = new ImageView(image);
            missiles.add(missile);
        }


        // SHOOTING
        transition = new TranslateTransition();
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.SPACE && missileCounter >= 0) {
                ImageView getMissile = missiles.get(missiles.size() - 1);

                if (switchMissile == 0) {
                    getMissile.setX(player.getLayoutX() + 5);
                    switchMissile = 1;
                } else if (switchMissile == 1) {
                    getMissile.setX(player.getLayoutX() + 30);
                    switchMissile = 0;
                }
                getMissile.setY(player.getLayoutY() - 20);


                // Fire Rate
                transition.setNode(getMissile);
                transition.setDuration(Duration.millis(135));
                transition.setToY(-750);
                transition.setByY(player.getLayoutY() - 20);

                transition.play();
                translations.add(transition);

                scene.getChildren().add(getMissile);


                ChangeListener<Number> listener = (ov, oldValue, newValue) -> {
                    if (!enemies.isEmpty() && missileCounter > 0) {
                        Bounds missileBounds = getMissile.localToScene(getMissile.getBoundsInLocal());

                        Bounds playerBounds = player.localToScene(player.getBoundsInLocal());
                        double xInScene = playerBounds.getMinX();
                        double yInScene = playerBounds.getMinY();

                        for (int i = 0; i < enemies.size(); i++) {
                            Bounds enemyBounds = enemies.get(i).localToScene(enemies.get(i).getBoundsInLocal());
                            if (missileBounds.intersects(enemyBounds)) {
                                System.out.println("------------------------------------");
                                System.out.println("ENEMY #" + i + " HIT ");
                                System.out.println("ENEMY Y POS = " + enemyBounds.getMinX());
                                System.out.println("ENEMY X POS = " + enemyBounds.getMinY());
                                System.out.println("X POSITION = " + xInScene);
                                System.out.println("Y POSITION = " + yInScene);
                                System.out.println("------------------------------------");
                                System.out.println();


                                explosionAnimation = new ExplosionAnimation(scene, enemies.get(i));
                                explosionAnimation.startAnimation();

                                enemyLocation.get(i).stop();
                                enemyLocation.remove(enemyLocation.get(i));
                                enemies.remove(i);
                                //scene.getChildren().remove(alien);
                            }

                            if (playerBounds.intersects(enemyBounds)) {
                                System.out.println("PLAYER HIT");
                            }
                        }
                    }

                    //System.out.println("MISSILE COUNTER = " + missileCounter);
                    //System.out.println("MISSILE SIZE = " +  (missiles.size() - 1));
                };

                player.translateXProperty().addListener(listener);
                player.translateYProperty().addListener(listener);

                for (ImageView enemy : enemies) {
                    enemy.translateXProperty().addListener(listener);
                    enemy.translateYProperty().addListener(listener);
                }


                getMissile.translateXProperty().addListener(listener);
                getMissile.translateYProperty().addListener(listener);


                missileCounter--;
                missiles.remove(missiles.size() - 1);
            }
        });





        // MOVEMENT
        keyPressed.addListener(((observableValue, aBoolean, t1) -> {
            if(!aBoolean){
                timer.start();

                if(Boolean.FALSE.equals(sPressed.getValue())){
                    playerAnimation.startAnimation();
                }
                else {
                    playerAnimation.stopAnimation();
                }


            } else {
                timer.stop();
                playerAnimation.stopAnimation();
            }
        }));


    }


    // ENEMY
    public void spawnEnemies(int i){
        Random rand = new Random();
        PathTransition move = new PathTransition();

        int num = (rand.nextInt(15 - 1) + 1) * 30;
        //System.out.println(num);

        Image image = new Image("com/game/firstgame/images/Alien.png", 30, 30, true, false);
        alien = new ImageView(image);
        alien.setX(num);
        alien.setY(num);

        scene.getChildren().add(alien);
        enemies.add(alien);

        move.setNode(enemies.get(enemies.size() - 1));

        alien.setY(-5);
        Path path = new Path();
        path.getElements().add(new MoveTo(alien.getX(),-5));
        path.getElements().add(new VLineTo(760));

        move.setDuration(Duration.seconds(10));
        move.setCycleCount(PathTransition.INDEFINITE);
        move.setPath(path);

        /*
        else {
            Circle circle = new Circle();
            circle.setCenterX(alien.getX());
            circle.setLayoutY(rand.nextInt(650 - 300) + 300);
            circle.setRadius(30);
            move.setAutoReverse(false);

            move.setDuration(Duration.millis(2000));
            move.setCycleCount(PathTransition.INDEFINITE);
            move.setPath(circle);
        }

         */
        move.play();
        enemyLocation.add(move);

        //move.add(transition);
        //scene.getChildren().add(getMissile);
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




    public void movementSetup(){
        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.W) {
                wPressed.set(true);
            }

            if(e.getCode() == KeyCode.A) {
                aPressed.set(true);
            }

            if(e.getCode() == KeyCode.S) {
                sPressed.set(true);
            }

            if(e.getCode() == KeyCode.D) {
                dPressed.set(true);
            }
            if(e.getCode() == KeyCode.SPACE) {
                spacePressed.set(true);
            }
        });

        scene.setOnKeyReleased(e ->{
            if(e.getCode() == KeyCode.W) {
                wPressed.set(false);
            }

            if(e.getCode() == KeyCode.A) {
                aPressed.set(false);
            }

            if(e.getCode() == KeyCode.S) {
                sPressed.set(false);
            }

            if(e.getCode() == KeyCode.D) {
                dPressed.set(false);
            }
            if(e.getCode() == KeyCode.SPACE) {
                spacePressed.set(false);
            }
        });
    }

    public void squareAtBorder() {
        double leftBound = 0;
        double rightBound = scene.getWidth() - shapeSize;
        double bottomBound = scene.getHeight() - shapeSize;
        double topBound = 0;


        if(player.getLayoutX() <= leftBound)
            player.setLayoutX(leftBound);

        if (player.getLayoutX() >= rightBound)
            player.setLayoutX(rightBound);

        if (player.getLayoutY() <= topBound)
            player.setLayoutY(topBound);

        if (player.getLayoutY() >= bottomBound)
            player.setLayoutY(bottomBound);
    }



}