package com.RichTeam.game;

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

import java.util.HashMap;
import java.util.Map;

/**
 * A screen that displays the player's stats at the end of the game.
 * Currently doesn't calculate a score
 */
public class GameOverScreen implements Screen {
    public static HustleGame game;
    Stage gameOverStage;
    Viewport viewport;
    OrthographicCamera camera;
    public int score;

    // Hidden achievement badges
    public static HashMap<String, Integer> streakGoals;
    private Texture apple, flower, bus, shopping_basket, early_bird, night_owl, book, library_book;
    public static String apple_path = "../assets/Sprites/achievements/apple.png";
    public static String flower_path = "../assets/Sprites/achievements/flower.png";
    public static String bus_path = "../assets/Sprites/achievements/bus.png";
    public static String book_path = "../assets/Sprites/achievements/book.png";
    public static String library_book_path = "../assets/Sprites/achievements/library_book.png";
    public static String night_owl_path = "../assets/Sprites/achievements/night_owl.png";

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


        // Create Game over window
        Window gameOverWindow = new Window("", game.skin);
        gameOverStage.addActor(gameOverWindow);

        // Table for UI elements in Game over window
        Table gameOverTable = new Table();
        gameOverWindow.add(gameOverTable);


        // Title
        Label title = new Label("Game Over!", game.skin, "button");
        gameOverTable.add(title).padTop(10);
        gameOverTable.row();

        Table scoresTable = new Table();
        gameOverTable.add(scoresTable).prefHeight(190).prefWidth(225);
        gameOverTable.row();

        // Final score calculation
        //int total = hoursStudied + hoursRecreational + hoursSlept;
        score = Math.max(40,Math.min(100,
                Math.round((((float)hoursStudied/32)*60 + ((float)hoursRecreational/32)*20 + (((float)hoursSlept)/144)*20))));
        if (timesStudied >= 7){
            String result;
            if (score >= 70){
                result = "First";
            }else if (score >= 60){
                result = "2:1";
            }else if (score >= 50){
                result = "2:2";
            }else{
                result = "Third";
            }
            scoresTable.add(new Label(String.format("You scored %d%% in you exam.\nWell done, you got a %s!", score, result), game.skin, "interaction")).padBottom(5);
            scoresTable.row();
        }else{
            //score = 39 + (1/-total);
            score = (int) (Math.random() * 39);
            Label message = new Label(String.format("You scored %d%% in you exam.\nThat's not a pass :(", score), game.skin, "interaction");
            scoresTable.add(message).padBottom(5);
            scoresTable.row();
        }

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

        // All achievements are 5 days in a row except eating, which is 3 meals 3 days in a row, and studying, which is every day
        streakGoals = new HashMap<String, Integer>();
        //debugging
//        for (String streak_activity : EventManager.streak_activities){
//            streakGoals.put(streak_activity, 0);
//        }
        streakGoals.put("studying", 7);
        streakGoals.put("eating", 3);
        streakGoals.put("flowers", 5);
        streakGoals.put("town", 5);
        streakGoals.put("shop", 5);
        streakGoals.put("library", 5);
        streakGoals.put("early_bird", 5);
        streakGoals.put("night_owl", 5);

        // Load your texture

        book = new Texture(Gdx.files.internal(book_path)); //for studying and library
        apple = new Texture(Gdx.files.internal(apple_path)); //for eating at the right times
        flower  = new Texture(Gdx.files.internal(flower_path)); //
        bus = new Texture(Gdx.files.internal(bus_path)); //
        shopping_basket = new Texture(Gdx.files.internal("Sprites/achievements/shopping_basket.png")); //
        library_book = new Texture(Gdx.files.internal(library_book_path)); //
        early_bird = new Texture(Gdx.files.internal("Sprites/achievements/early_bird.png")); //bird
        night_owl = new Texture(Gdx.files.internal(night_owl_path));

        // Create an Image widget with the texture
        Image apple_image = new Image(apple);
        Image flower_image = new Image(flower);
        Image bus_image = new Image(bus);
        Image shopping_basket_image = new Image(shopping_basket);
        Image early_bird_image = new Image(early_bird);
        Image night_owl_image = new Image(night_owl);
        Image book_image = new Image(book);
        Image library_book_image = new Image(library_book);


        // Set position of the image
        apple_image.setPosition(175, 225);
        flower_image.setPosition(950, 75);
        bus_image.setPosition(50, 325);
        shopping_basket_image.setPosition(950, 350);
        early_bird_image.setPosition(100, 500);
        night_owl_image.setPosition(1050, 500);
        book_image.setPosition(30, 30);
        library_book_image.setPosition(1100, 150);

        //Iterate through the streaks to see which achievements were completed
        HashMap<String, Integer> streaksAchieved = EventManager.getStreaks();
        for (Map.Entry<String, Integer> entry : streaksAchieved.entrySet()) {
            String task = entry.getKey();
            //render the achievement on the GameOver screen if the achievement requirements have been met
            Integer goal = streakGoals.get(task);
            if (goal != null && streaksAchieved.get(task) >= goal) {
                if (streaksAchieved.get(task) >= streakGoals.get(task)) {
                    switch (task) {
                        case "studying":
                            gameOverStage.addActor(book_image);
                            break;
                        case "eating":
                            gameOverStage.addActor(apple_image);
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
                        case "early_bird":
                            gameOverStage.addActor(early_bird_image);
                            break;
                        case "night_owl":
                            gameOverStage.addActor(night_owl_image);
                            break;
                        case "library":
                            gameOverStage.addActor(library_book_image);
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
    public static int scoreCalculation(int hoursStudied, int hoursRecreational, int hoursSlept){
        int newScore = Math.max(40,Math.min(100,Math.round((((float)hoursStudied/32)*60 + ((float)hoursRecreational/30)*20 + (((float)hoursSlept)/144)*20))));
        return newScore;
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


