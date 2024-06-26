package com.RichTeam.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * A screen to display the game menu to the player has the buttons "Start", "Settings", "Credits, "Exit"
 * ALso displays a tutorial window and an avatar select screen
 */
public class MenuScreen implements Screen {
    final HustleGame game;
    private Stage menuStage;
    OrthographicCamera camera;
    private Viewport viewport;
    private Image titleImage = new Image();
    public boolean draw;
    private Texture[] cloudTextures; // Declare an array to hold cloud textures
    private ArrayList<Vector2> clouds; // Use List interface here
    int cloudSpeed;
    private int[] cloudTextureIndices; // Store the texture index for each cloud

    /**
     * A class to display a menu screen, initially gives the player 4 options, Start, Settings, Credits, Quit
     * Upon hitting start, a tutorial window is shown, and then an avatar select screen is shown, and then it is
     * switched to GameScreen.
     * Settings switches to SettingsScreen
     * Credits switches to CreditsScreen
     * Quit exits the game
     *
     * @param game An instance of HustleGame with loaded variables
     */
    public MenuScreen(final HustleGame game, boolean draw) {

        this.game = game;
        this.game.menuScreen = this;
        this.draw = draw;

        if (draw) {
            cloudTextures = new Texture[] {
                    new Texture(Gdx.files.internal("Sprites/cloud1.png")),
                    new Texture(Gdx.files.internal("Sprites/cloud2.png")),
                    new Texture(Gdx.files.internal("Sprites/cloud3.png")),
                    new Texture(Gdx.files.internal("Sprites/cloud4.png")),
                    new Texture(Gdx.files.internal("Sprites/cloud5.png")),
                    new Texture(Gdx.files.internal("Sprites/cloud6.png")),
            };
            cloudSpeed = 10; // Adjust speed as needed
            clouds = new ArrayList<Vector2>(); // Initialize the clouds ArrayList
            // Create stage to draw UI on
            menuStage = new Stage(new FitViewport(game.WIDTH, game.HEIGHT));
            Gdx.input.setInputProcessor(menuStage);

            camera = new OrthographicCamera();
            viewport = new FitViewport(game.WIDTH, game.HEIGHT, camera);
            camera.setToOrtho(false, game.WIDTH, game.HEIGHT);

            // Set the size of the background to the viewport size, only need to do this once, this is then used by all
            // screens as an easy way of having a blue background
            game.blueBackground.getRoot().findActor("blue image").setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
            Texture randomCloudTexture = cloudTextures[MathUtils.random(cloudTextures.length - 1)];

            // Initialize cloudTextureIndices array with random texture indices
            cloudTextureIndices = new int[4];
            for (int i = 0; i < 4; i++) {
                float x = MathUtils.random(0, game.WIDTH); // Random x position
                float y = MathUtils.random(0, game.HEIGHT); // Random y position
                clouds.add(new Vector2(x, y));
                cloudTextureIndices[i] = MathUtils.random(cloudTextures.length - 1);
            }
            // Title image
            titleImage = new Image(new Texture(Gdx.files.internal("title.png")));
            titleImage.setPosition((viewport.getWorldWidth() / 2f) - (titleImage.getWidth() / 2f), 525);
            menuStage.addActor(titleImage);

            // Play menu music
            game.soundManager.playMenuMusic();


            // Make avatar select table
            Table avatarSelectTable = makeAvatarSelectTable();
            menuStage.addActor(avatarSelectTable);
            avatarSelectTable.setVisible(false);


            // Make tutorial window
            Window tutorialWindow = makeTutorialWindow(avatarSelectTable);
            menuStage.addActor(tutorialWindow);
            tutorialWindow.setVisible(false);


            // Make table to draw buttons and title
            Table buttonTable = new Table();
            buttonTable.setFillParent(true);
            menuStage.addActor(buttonTable);


            // Create the buttons
//        Label title = new Label("Heslington Hustle", game.skin, "title"); // Old title, new uses a texture
            TextButton startButton = new TextButton("New Game", game.skin);
            TextButton settingsButton = new TextButton("Settings", game.skin);
            TextButton creditsButton = new TextButton("Credits", game.skin);
            TextButton leaderboardButton = new TextButton("Leaderboard", game.skin);
            TextButton exitButton = new TextButton("Exit", game.skin);


            // Add everything to the table using row() to go to a new line
            int buttonWidth = 340;
            buttonTable.add(startButton).uniformX().width(buttonWidth).padBottom(10).padTop(240);
            buttonTable.row();
            buttonTable.add(leaderboardButton).uniformX().width(buttonWidth).padBottom(10);
            buttonTable.row();
            buttonTable.add(settingsButton).uniformX().width(buttonWidth).padBottom(10);
            buttonTable.row();
            buttonTable.add(creditsButton).uniformX().width(buttonWidth).padBottom(10);
            buttonTable.row();
            buttonTable.add(exitButton).uniformX().width(buttonWidth);
            buttonTable.top();

            // Add listeners to the buttons so they do things when pressed

            // START GAME BUTTON - Displays the tutorial window
            startButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    startButtonTask(buttonTable, tutorialWindow);
                }
            });

            // SETTINGS BUTTON
            Screen thisScreen = this;
            settingsButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    settingsButtonTask(thisScreen);
                }
            });

            // CREDITS BUTTON
            creditsButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    creditsButtonTask(thisScreen);
                }
            });

            // EXIT BUTTON
            exitButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    exitButtonTask();
                }
            });
            // LEADERBOARD BUTTON
            leaderboardButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    leaderboardButtonTask(thisScreen);
                }
            });

            game.batch.setProjectionMatrix(camera.combined);
        }

    }
    public void startButtonTask(Table buttonTable, Window tutorialWindow){
        game.soundManager.playButton();
        buttonTable.setVisible(false);
        titleImage.setVisible(false);
        tutorialWindow.setVisible(true);
    }

    public void settingsButtonTask(Screen thisScreen){
        game.soundManager.playButton();
        SettingsScreen screen = new SettingsScreen(game,thisScreen,draw);
        game.setScreen(screen);
    }

    public void creditsButtonTask(Screen thisScreen){
        game.soundManager.playButton();
        game.setScreen(new CreditScreen(game, thisScreen,draw));
    }

    public void exitButtonTask(){
        game.soundManager.playButton();
        Gdx.app.exit();
    }

    public void leaderboardButtonTask(Screen thisScreen){
        game.soundManager.playButton();
        game.setScreen(new LeaderboardScreen(game, thisScreen,draw));
    }

    /**
     * Renders the main menu, and any windows that are displaying information
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        game.blueBackground.draw();
        for (int i = 0; i < clouds.size(); i++) {
            Vector2 cloud = clouds.get(i);
            int textureIndex = cloudTextureIndices[i];
            Texture cloudTexture = cloudTextures[textureIndex];
            cloud.x += cloudSpeed * delta; // Move cloud horizontally
            // Wrap around if cloud moves out of screen
            if (cloud.x > game.WIDTH) {
                cloud.x = -cloudTexture.getWidth(); // Use cloudTexture instead of randomCloudTexture
                cloud.y = MathUtils.random(0, game.HEIGHT); // Randomize y position
                cloudTextureIndices[i] = MathUtils.random(cloudTextures.length - 1); // Update texture index
            }
        }

        // Draw clouds
        game.batch.begin();
        for (int i = 0; i < clouds.size(); i++) {
            Vector2 cloud = clouds.get(i);
            int textureIndex = cloudTextureIndices[i];
            Texture cloudTexture = cloudTextures[textureIndex];
            game.batch.draw(cloudTexture, cloud.x, cloud.y); // Use cloudTexture here
        }
        game.batch.end();

        // Make the stage follow actions and draw itself
        menuStage.setViewport(viewport);
        menuStage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        menuStage.draw();
    }

    /**
     * Correctly resizes the menu screen
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        menuStage.getViewport().update(width, height);
    }

    // Other required methods
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
    /**
     * Correctly sizes the game when resuming it after a pause or switching screens
     * Fixes a small graphical bug
     */
    public void resume() {
        Gdx.input.setInputProcessor(menuStage);

        // See the comment in the resume() function in GameScreen to see why this pointless line exists
        Gdx.input.setCursorPosition(Gdx.input.getX(), Gdx.input.getY());
    }

    /**
     * Dispose of all menu assets
     */
    @Override
    public void dispose() {
        menuStage.dispose();
    }

    /**
     * Generates a window to teach the player how to play the game
     * Displays the tutorial text shown in Text/tutorial_text.txt
     *
     * @return A small window to explain the game
     */
    public Window makeTutorialWindow(Table nextTable) {
        Window tutWindow = new Window("", game.skin);
        Table tutTable = new Table();
        tutWindow.add(tutTable).prefHeight(600).prefWidth(800-20);

        // Title
        Label title = new Label("How to play", game.skin, "button");
        tutTable.add(title).padTop(10);
        tutTable.row();

        // Table for things inside the scrollable widget
        Table scrollTable = new Table();

        // Scrollable widget
        ScrollPane scrollWindow = new ScrollPane(scrollTable, game.skin);
        scrollWindow.setFadeScrollBars(false);

        tutTable.add(scrollWindow).padTop(20).height(350).width(870);
        tutTable.row();

        Label text = new Label(game.tutorialText, game.skin, "interaction");
        text.setWrap(true);
        scrollTable.add(text).width(820f).padLeft(20);

        // Exit button
        TextButton continueButton = new TextButton("Continue", game.skin);
        tutTable.add(continueButton).bottom().width(300).padTop(10);

        tutWindow.pack();

        tutWindow.setSize(900, 600);

        // Centre the window
        tutWindow.setX((viewport.getWorldWidth() / 2) - (tutWindow.getWidth() / 2));
        tutWindow.setY((viewport.getWorldHeight() / 2) - (tutWindow.getHeight() / 2));



        continueButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.soundManager.playButton();
                tutWindow.setVisible(false);
                nextTable.setVisible(true);
            }
        });



        return tutWindow;
    }


    /**
     * Creates an avatar selection screen, consisting of a label and two buttons
     *
     * @return A table containing UI elements
     */
    public Table makeAvatarSelectTable () {
        Table table = new Table();
        table.setFillParent(true);
        table.top();

        // Prompt
        Label title = new Label("Select your avatar", game.skin, "button");
        table.add(title).padBottom(120).padTop(80);
        table.row();

        // Image buttons
        Table buttonTable = new Table();
        table.add(buttonTable).width(600);

        ImageButton choice1 = new ImageButton(game.skin, "avatar1");
        buttonTable.add(choice1).left().expandX();
        ImageButton choice2 = new ImageButton(game.skin, "avatar2");
        buttonTable.add(choice2).right().expandX();

        choice1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                avatar1ButtonTask();
                dispose();
            }
        });

        choice2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                avatar2ButtonTask();
                dispose();
            }
        });

        return table;
    }

    public void avatar1ButtonTask(){
        game.soundManager.playButton();
        game.setScreen(new GameScreen(game, 1,draw));
        game.soundManager.stopMenuMusic();
    }

    public void avatar2ButtonTask(){
        game.soundManager.playButton();
        game.setScreen(new GameScreen(game, 2,draw));
        game.soundManager.stopMenuMusic();
    }

}
