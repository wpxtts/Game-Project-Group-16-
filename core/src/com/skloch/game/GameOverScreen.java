package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.*;

/**
 * A screen that displays the player's stats at the end of the game.
 * Currently doesn't calculate a score
 */
public class GameOverScreen implements Screen {
    private HustleGame game;
    Stage gameOverStage;
    Viewport viewport;
    OrthographicCamera camera;

    // Hidden achievement badges
    private Texture rch;
    private Texture flower;
    private Texture bus;
    private Texture shopping_basket;
    private Texture fire;
    private Texture long_boi;
    private Texture talk;
    private Texture chest;

    /**
     * A screen to display a 'Game Over' screen when the player finishes their exams
     * Currently does not calculate a score, just shows the player's stats to them, as requested in assessment 1
     * Tracking them now will make win conditions easier to implement for assessment 2
     *
     * @param game An instance of HustleGame
     * @param hoursStudied The hours studied in the playthrough
     * @param hoursRecreational The hours of fun had in the playthrough
     * @param hoursSlept The hours slept in the playthrough
     */
    public GameOverScreen (final HustleGame game, int hoursStudied, int hoursRecreational, int hoursSlept) {
        this.game = game;
        gameOverStage = new Stage(new FitViewport(game.WIDTH, game.HEIGHT));
        Gdx.input.setInputProcessor(gameOverStage);

        camera = new OrthographicCamera();
        viewport = new FitViewport(game.WIDTH, game.HEIGHT, camera);
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);

        // Create the window
        Window gameOverWindow = new Window("", game.skin);
        gameOverStage.addActor(gameOverWindow);

        // Table for UI elements in window
        Table gameOverTable = new Table();
        gameOverWindow.add(gameOverTable);

        // Title
        Label title = new Label("Game Over!", game.skin, "button");
        gameOverTable.add(title).padTop(10);
        gameOverTable.row();

        Table scoresTable = new Table();
        gameOverTable.add(scoresTable).prefHeight(380).prefWidth(450);
        gameOverTable.row();

        Integer score = Integer.valueOf(hoursStudied) + Integer.valueOf(hoursRecreational) + Integer.valueOf(hoursSlept);
        String scoreString = "Total Score: " + String.valueOf(Integer.valueOf(hoursStudied) + Integer.valueOf(hoursRecreational) + Integer.valueOf(hoursSlept));
        // Display scores
        scoresTable.add(new Label("Hours Studied", game.skin, "interaction")).padBottom(5);
        scoresTable.row();
        scoresTable.add(new Label(String.valueOf(hoursStudied), game.skin, "button")).padBottom(15);
        scoresTable.row();
        scoresTable.add(new Label("Recreational hours", game.skin, "interaction")).padBottom(5);
        scoresTable.row();
        scoresTable.add(new Label(String.valueOf(hoursRecreational), game.skin, "button")).padBottom(15);
        scoresTable.row();
        scoresTable.add(new Label("Hours Slept", game.skin, "interaction")).padBottom(5);
        scoresTable.row();
        scoresTable.add(new Label(String.valueOf(hoursSlept), game.skin, "button")).padBottom(15);
        scoresTable.row();
        scoresTable.add(new Label(scoreString, game.skin, "interaction")).padBottom(5);

//
//        // Hidden achievements
//        // Load your texture
//        rch = new Texture(Gdx.files.internal("Sprites/achievements/hub.png"));
//        flower  = new Texture(Gdx.files.internal("Sprites/achievements/flower.png"));
//        bus = new Texture(Gdx.files.internal("Sprites/achievements/bus.png"));
//        shopping_basket = new Texture(Gdx.files.internal("Sprites/achievements/shopping_basket.png"));
//        fire = new Texture(Gdx.files.internal("Sprites/achievements/fire.png"));
//        long_boi = new Texture(Gdx.files.internal("Sprites/achievements/early_bird.png"));
//        talk = new Texture(Gdx.files.internal("Sprites/achievements/talk.png"));
//        chest = new Texture(Gdx.files.internal("Sprites/achievements/chest.png"));
//
//        // Create an Image widget with the texture
//        com.badlogic.gdx.scenes.scene2d.ui.Image rch_image = new com.badlogic.gdx.scenes.scene2d.ui.Image(rch);
//        com.badlogic.gdx.scenes.scene2d.ui.Image flower_image = new com.badlogic.gdx.scenes.scene2d.ui.Image(flower);
//        com.badlogic.gdx.scenes.scene2d.ui.Image bus_image = new com.badlogic.gdx.scenes.scene2d.ui.Image(bus);
//        com.badlogic.gdx.scenes.scene2d.ui.Image shopping_basket_image = new com.badlogic.gdx.scenes.scene2d.ui.Image(shopping_basket);
//        com.badlogic.gdx.scenes.scene2d.ui.Image fire_image = new com.badlogic.gdx.scenes.scene2d.ui.Image(fire);
//        com.badlogic.gdx.scenes.scene2d.ui.Image long_boi_image = new com.badlogic.gdx.scenes.scene2d.ui.Image(long_boi);
//        com.badlogic.gdx.scenes.scene2d.ui.Image talk_image = new com.badlogic.gdx.scenes.scene2d.ui.Image(talk);
//        com.badlogic.gdx.scenes.scene2d.ui.Image chest_image = new Image(chest);
//
//        // Set position of the image
//        rch_image.setPosition(10, 100);
//        flower_image.setPosition(20, 100);
//        bus_image.setPosition(30, 100);
//        shopping_basket_image.setPosition(40, 100);
//        fire_image.setPosition(50, 100);
//        long_boi_image.setPosition(60, 100);
//        talk_image.setPosition(70, 100);
//        chest_image.setPosition(80, 100);
//
//        // Add the image to the stage
//        gameOverStage.addActor(rch_image);
//        gameOverStage.addActor(flower_image);
//        gameOverStage.addActor(bus_image);
//        gameOverStage.addActor(shopping_basket_image);
//        gameOverStage.addActor(fire_image);
//        gameOverStage.addActor(long_boi_image);
//        gameOverStage.addActor(chest_image);

//        for (String goal : streakGoals){
//            if (streak > streakGoals){
//                // Events related to objects
//                switch (streak) {
//                    case "talktative":
//                        gameOverStage.addActor(talk_image);
//                        break;
//                    case "rch":
//                        gameOverStage.addActor(rch_image);
//                        break;
//                    default:
//                        break;
//                }
//            }
//        }


        // Exit button
        TextButton exitButton = new TextButton("Next", game.skin);
        gameOverTable.add(exitButton).bottom().width(300).padTop(10);

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButton();
                game.setScreen(new SaveScreen(game, score));
                dispose();

            }
        });




        gameOverWindow.pack();

        gameOverWindow.setSize(600, 600);

        // Centre the window
        gameOverWindow.setX((viewport.getWorldWidth() / 2) - (gameOverWindow.getWidth() / 2));
        gameOverWindow.setY((viewport.getWorldHeight() / 2) - (gameOverWindow.getHeight() / 2));
    }


    /**
     * Renders the screen and the background each frame
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        game.blueBackground.draw();

        gameOverStage.act(delta);
        gameOverStage.draw();

        camera.update();

    }



    /**
     * Correctly resizes the onscreen elements when the window is resized
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        gameOverStage.getViewport().update(width, height);
        viewport.update(width, height);
    }

    // Other required methods from Screen
    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
}
