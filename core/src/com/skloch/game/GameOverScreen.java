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
import java.util.HashMap;
import java.util.Map;

/**
 * A screen that displays the player's stats at the end of the game.
 * Currently doesn't calculate a score
 */
public class GameOverScreen implements Screen {
    public static HustleGame game;
    private EventManager events;
    Stage gameOverStage;
    Viewport viewport;
    OrthographicCamera camera;
    public static int score;

    // Hidden achievement badges
    public static HashMap<String, Integer> streakGoals;
    private Texture rch, flower, bus, shopping_basket, fire, long_boi, talk, chest;
    public static String rch_path = "Sprites/achievements/hub.png";
    public static String flower_path = "Sprites/achievements/flower.png";
    public static String bus_path = "Sprites/achievements/bus.png";

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
    public GameOverScreen (final HustleGame game, int hoursStudied, int hoursRecreational, int hoursSlept, int timesStudied) {
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

        // Final score calculation
        if (timesStudied >= 7){
            int total = hoursStudied + hoursRecreational + hoursSlept;
            score = 40 + (1/-total);
        }else{
            score = (int) (Math.random() * 39);
        }
        String scoreString = "Total Score: " + String.valueOf(score);

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
        scoresTable.add(new Label(String.valueOf(hoursSlept), game.skin, "button"));

        // Hidden achievements

        streakGoals = new HashMap<String, Integer>();
        streakGoals.put("eating", 11);
        streakGoals.put("flowers", 5);
        streakGoals.put("town", 8);
        streakGoals.put("shop", 11);
        streakGoals.put("determined", 10);
        streakGoals.put("early_bird", 10);
        streakGoals.put("talkative", 10);
        streakGoals.put("secretive", 5);

        // Load your texture

        rch = new Texture(Gdx.files.internal(rch_path)); //for eating at the right times
        flower  = new Texture(Gdx.files.internal(flower_path)); //
        bus = new Texture(Gdx.files.internal(bus_path)); //
        shopping_basket = new Texture(Gdx.files.internal("Sprites/achievements/shopping_basket.png")); //
        fire = new Texture(Gdx.files.internal("Sprites/achievements/fire.png")); //
        long_boi = new Texture(Gdx.files.internal("Sprites/achievements/early_bird.png")); //bird
        talk = new Texture(Gdx.files.internal("Sprites/achievements/talk.png")); //
        chest = new Texture(Gdx.files.internal("Sprites/achievements/chest.png")); //use actual

        // Create an Image widget with the texture
        Image rch_image = new Image(rch);
        Image flower_image = new Image(flower);
        Image bus_image = new Image(bus);
        Image shopping_basket_image = new Image(shopping_basket);
        Image fire_image = new Image(fire);
        Image long_boi_image = new Image(long_boi);
        Image talk_image = new Image(talk);
        Image chest_image = new Image(chest);

        // Set position of the image
        rch_image.setPosition(100, 20);
        flower_image.setPosition(950, 100);
        bus_image.setPosition(50, 300);
        shopping_basket_image.setPosition(1050, 300);
        fire_image.setPosition(950, 500);
        long_boi_image.setPosition(100, 500);
        talk_image.setPosition(1000, 400);
        chest_image.setPosition(200, 200);

        //Iterate through the streaks to see which achievements were completed
        HashMap<String, Integer> streaksAchieved = EventManager.getStreaks();
        for (Map.Entry<String, Integer> entry : streaksAchieved.entrySet()) {
            String task = entry.getKey();
            //render the achievement on the GameOver screen if the achievement requirements have been met
            Integer goal = streakGoals.get(task);
            if (goal != null && streaksAchieved.get(task) >= goal) {
                if (streaksAchieved.get(task) >= streakGoals.get(task)) {
                    switch (task) {
                        case "eating":
                            gameOverStage.addActor(rch_image);
                            break;
                        case "flowers":
                            gameOverStage.addActor(flower_image);
                            break;
                        case "town":
                            gameOverStage.addActor(bus_image);
                            break;
                        case "shop":
                            gameOverStage.addActor(shopping_basket_image);
                            break;
                        case "determined":
                            gameOverStage.addActor(fire_image);
                            break;
                        case "early_bird":
                            gameOverStage.addActor(long_boi_image);
                            break;
                        case "secretive":
                            gameOverStage.addActor(chest_image);
                            break;
                        case "talktative":
                            gameOverStage.addActor(talk_image);
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        // Exit button
        TextButton exitButton = new TextButton("Next", game.skin);
        gameOverTable.add(exitButton).bottom().width(300).padTop(10);

        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButton();
                game.setScreen(new SaveScreen(game, score,true));
                dispose();
                game.setScreen(new SaveScreen(game, score,true));
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
        gameOverStage.dispose();


    }
}


