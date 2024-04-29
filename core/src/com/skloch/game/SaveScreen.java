package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A scene2d window consisting of a title, a scrollable widget and an exit button.
 * Used to display the assets used in the game
 */
public class SaveScreen implements Screen{

    private HustleGame game;
    private Stage leaderboardStage;
    private OrthographicCamera camera;
    private Viewport viewport;
    private boolean saved;
    public static String leaderboardPath = "../assets/Text/leaderboard.csv";

    public boolean drawOnExit;
    public MenuScreen menuScreen;

    /**
     * A scene2d window consisting of a title, a scrollable widget and an exit button.
     * Credits are loaded from assets/Text/credits.txt and displayed in the scrollable widget
     * Thus any changes to assets or licenses must be reflected in credits.txt
     *
     */
    public SaveScreen(HustleGame game, Integer score,boolean draw) {
        
        // Basically all the same code as the settings menu
        this.game = game;
        this.saved = false;
        drawOnExit = draw;

        if(draw){
            drawScreen(score);
        }
    }

    public void drawScreen(Integer score){
        leaderboardStage = new Stage(new FitViewport(this.game.WIDTH, this.game.HEIGHT));
        Gdx.input.setInputProcessor(leaderboardStage);

        camera = new OrthographicCamera();
        viewport = new FitViewport(this.game.WIDTH, this.game.HEIGHT, camera);
        camera.setToOrtho(false, this.game.WIDTH, this.game.HEIGHT);

        // Create the window
        Window leaderboardMenu = new Window("", this.game.skin);
        leaderboardStage.addActor(leaderboardMenu);
        leaderboardMenu.setModal(true);

        // Table for UI elements in window
        Table leaderboardTable = new Table();
        leaderboardMenu.add(leaderboardTable).prefHeight(600);
        Table saveTable = new Table();
        // Title
        Label title = new Label("Save Score", this.game.skin, "button");
        leaderboardTable.add(title).padTop(10).padLeft(60);
        leaderboardTable.row();

        // Table for things inside the scrollable widget

        saveTable.add(new Label("Save total score of ", game.skin, "interaction")).padBottom(5).padLeft(60);
        saveTable.row();
        saveTable.add(new Label((String.valueOf(score)+"?"), game.skin, "button")).padBottom(15).padLeft(60);
        saveTable.row();
        leaderboardTable.add(saveTable).padTop(10).row();


        // User text input row
        TextField userInputField = new TextField("", game.skin);
        userInputField.setMessageText("Enter player name");
        userInputField.setAlignment(Align.center); // Set alignment to right
        leaderboardTable.add(userInputField).colspan(2).width(600).padTop(10); // colspan to span across both columns
        leaderboardTable.row(); // Move to the next row after adding the text input field


        // Padding for labels to move them to the right


        // Save button
        TextButton saveButton = new TextButton("Save", this.game.skin);
        leaderboardTable.add(saveButton).bottom().width(300).padTop(10).padLeft(60);
        leaderboardTable.row();


        // Exit button
        TextButton exitButton = new TextButton("Exit", this.game.skin);
        leaderboardTable.add(exitButton).bottom().width(300).padTop(10).padLeft(60);

        leaderboardMenu.pack();

        leaderboardMenu.setSize(600, 600);

        // Centre the window
        leaderboardMenu.setX((viewport.getWorldWidth() / 2) - (leaderboardMenu.getWidth() / 2));
        leaderboardMenu.setY((viewport.getWorldHeight() / 2) - (leaderboardMenu.getHeight() / 2));


        // Listener for the exit button
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                menuScreen = new MenuScreen(SaveScreen.this.game,true);
                exitButtonPress();
            }
        });

        // Listener for the save button
        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveButtonPress(userInputField.getText(),score,saveButton);
            }
        });
    }

    public void saveButtonPress(String userInput,Integer score,TextButton button){
        if (!saved) { // Check if the button is not already marked as Saved
            SaveScreen.this.game.soundManager.playButton();
            if (!userInput.isEmpty()) { // Check if the text is not empty
                saveScore(userInput, score); // Append the name and score to the CSV file
                button.setText("Saved"); // Change the button text to Saved
                saved = true;
                button.setDisabled(true); // Disable the button
            }
        }
    }

    public void exitButtonPress(){
        SaveScreen.this.game.soundManager.playButton();
        SaveScreen.this.game.soundManager.stopOverworldMusic();
        dispose();
        SaveScreen.this.game.setScreen(menuScreen);
    }

    /**
     * Renders the savemenu window
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

    public void saveScore(String name, int score) {
        if(score<0){
            throw new IllegalArgumentException("Score has to be 0 or greater");
        }
        String filename = leaderboardPath; // Specify the CSV file name
        String data = name + "," + score + "\n"; // Format the data to append

        try {
            FileWriter writer = new FileWriter(filename, true); // Create a FileWriter in append mode
            writer.write(data); // Write the data to the file
            writer.close(); // Close the FileWriter
        } catch (IOException e) {
            e.printStackTrace();
        }
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
