package com.game.firstgame;

import javafx.animation.*;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


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
    private int missileCounter = 9;
    private int switchMissile = 0;
    private Rectangle brick = new Rectangle();
    private TranslateTransition transition;
    private ArrayList<ImageView> missiles;


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
        createBricks();


        shapeSize = player.getFitHeight();
        movementSetup();

        playerAnimation = new PlayerAnimation(player);
        loopingBackground();
        parallelTransition.play();




        // LOAD MISSILES
        missiles = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            Image image = new Image("com/game/firstgame/images/SpaceInvaderAnim/Missile.png", 10, 30, false, false);
            ImageView missile = new ImageView(image);
            missiles.add(missile);
        }

        // SHOOTING
        transition = new TranslateTransition();
        scene.addEventFilter(KeyEvent.KEY_RELEASED, event->{
            if (event.getCode() == KeyCode.SPACE && missileCounter >= 0) {
                ImageView getMissile = missiles.get(missileCounter);

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
                transition.setDuration(Duration.millis(130));
                transition.setToY(-750);

                scene.getChildren().add(getMissile);

                if (getMissile.getX() >= brick.getX() && getMissile.getX() <= brick.getX() + brick.getWidth()){
                    System.out.println("MISSILE = " + missiles.get(missileCounter).getX());
                    System.out.println("BRICK = " + brick.getX());
                    System.out.println("COLLISION");
                }

                System.out.println(missileCounter);

                missileCounter--;
            }
        });



        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2.5), e->{
            transition.play();

        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();



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
    public void createBricks(){
        brick = new Rectangle(player.getLayoutX(),player.getLayoutY() - 200,30,30);
        brick.setFill(Color.RED);
        scene.getChildren().add(brick);

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