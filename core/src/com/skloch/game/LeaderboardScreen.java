package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A scene2d window consisting of a title, a scrollable widget and an exit button.
 * Used to display the assets used in the game
 */
public class LeaderboardScreen implements Screen{

    private HustleGame game;
    private Stage leaderboardStage;
    private OrthographicCamera camera;
    private Viewport viewport;

    /**
     * A scene2d window consisting of a title, a scrollable widget and an exit button.
     * Credits are loaded from assets/Text/credits.txt and displayed in the scrollable widget
     * Thus any changes to assets or licenses must be reflected in credits.txt
     *
     * @param game An instance of the HustleGame class
     * @param previousScreen The screen to return to when the exit button is pressed
     */
    public LeaderboardScreen(final HustleGame game, Screen previousScreen) {



        // Basically all the same code as the settings menu
        this.game = game;
        leaderboardStage = new Stage(new FitViewport(game.WIDTH, game.HEIGHT));
        Gdx.input.setInputProcessor(leaderboardStage);

        camera = new OrthographicCamera();
        viewport = new FitViewport(game.WIDTH, game.HEIGHT, camera);
        camera.setToOrtho(false, game.WIDTH, game.HEIGHT);

        // Create the window
        Window leaderboardMenu = new Window("", game.skin);
        leaderboardStage.addActor(leaderboardMenu);
        leaderboardMenu.setModal(true);

        // Table for UI elements in window
        Table leaderboardTable = new Table();
        leaderboardMenu.add(leaderboardTable).prefHeight(600);

        // Title
        Label title = new Label("Leaderboard", game.skin, "button");
        leaderboardTable.add(title).padTop(10);
        leaderboardTable.row();

        // Table for things inside the scrollable widget
        Table scrollTable = new Table();

        // Scrollable widget
        ScrollPane scrollWindow = new ScrollPane(scrollTable, game.skin);
        scrollWindow.setFadeScrollBars(false);
        scrollWindow.setScrollingDisabled(true, false); // Disable horizontal scrolling


        // scrollWindow.setDebug(true);

        // scrollWindow.setFillParent(true);
        leaderboardTable.add(scrollWindow).padTop(20).height(350);
        leaderboardTable.row();

        // Actual credits
        // Credits are loaded when the game is initialised
        // Label text = new Label(game.credits, game.skin, "interaction");
        // text.setWrap(true);
        // scrollTable.add(text).width(520f).padLeft(15);

        // Exit button
        TextButton exitButton = new TextButton("Exit", game.skin);
        leaderboardTable.add(exitButton).bottom().width(300).padTop(10);

        leaderboardMenu.pack();

        leaderboardMenu.setSize(600, 600);

        // Centre the window
        leaderboardMenu.setX((viewport.getWorldWidth() / 2) - (leaderboardMenu.getWidth() / 2));
        leaderboardMenu.setY((viewport.getWorldHeight() / 2) - (leaderboardMenu.getHeight() / 2));

        // Listener for the exit button
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButton();
                dispose();
                game.setScreen(previousScreen);
                previousScreen.resume();
            }
        });

        // Read and display data from CSV file
        ArrayList<String[]> leaderboardData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("Text/leaderboard.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Split the line by comma
                String[] values = line.split(",");

                // Check if the line has at least two values (name and score)
                if (values.length >= 2) {
                        // Extract name and score
                    // String name = values[0];
                    // String score = values[1];
                    leaderboardData.add(values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Sort the leaderboard data based on the scores (assuming scores are in index 1)
        // Skip the first entry if present (assuming it's a header)
        Collections.sort(leaderboardData.subList(leaderboardData.size() > 0 ? 1 : 0, leaderboardData.size()), new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return Integer.parseInt(o2[1]) - Integer.parseInt(o1[1]); // Sort in descending order
            }
        });

        // Populate the scroll window with the sorted data
        for (String[] entry : leaderboardData) {
            // Create labels for name and score
            Label nameLabel = new Label(entry[0], game.skin, "interaction");
            Label scoreLabel = new Label(entry[1], game.skin, "interaction");

            // Add labels to the scrollable table
            scrollTable.add(nameLabel).width(230f).padLeft(30);
            scrollTable.add(scoreLabel).width(230f).padLeft(60);
            scrollTable.row();
        }

    }

    /**
     * Renders the leaderboard window
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render (float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.blueBackground.draw();

        leaderboardStage.act(delta);
        leaderboardStage.draw();

        camera.update();
    }


    /**
     * Correctly resizes the onscreen elements when the window is resized
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        leaderboardStage.getViewport().update(width, height);
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
