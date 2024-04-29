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

public class LeaderboardScreen implements Screen {

    private HustleGame game;
    private Stage leaderboardStage;
    private OrthographicCamera camera;
    private Viewport viewport;

    public static String leaderboardPath = "../assets/Text/leaderboard.csv";

    public LeaderboardScreen(final HustleGame game, Screen previousScreen,boolean draw) {
        this.game = game;
        if(draw){
            leaderboardStage = new Stage(new FitViewport(game.WIDTH, game.HEIGHT));
            Gdx.input.setInputProcessor(leaderboardStage);

            camera = new OrthographicCamera();
            viewport = new FitViewport(game.WIDTH, game.HEIGHT, camera);
            camera.setToOrtho(false, game.WIDTH, game.HEIGHT);

            Window leaderboardMenu = new Window("", game.skin);
            leaderboardStage.addActor(leaderboardMenu);
            leaderboardMenu.setModal(true);

            Table leaderboardTable = new Table();
            leaderboardMenu.add(leaderboardTable).prefHeight(600);

            Label title = new Label("Leaderboard", game.skin, "button");
            leaderboardTable.add(title).padTop(10);
            leaderboardTable.row();

            Table scrollTable = new Table();

            ScrollPane scrollWindow = new ScrollPane(scrollTable, game.skin);
            scrollWindow.setFadeScrollBars(false);
            scrollWindow.setScrollingDisabled(true, false);

            leaderboardTable.add(scrollWindow).padTop(20).height(350);
            leaderboardTable.row();

            TextButton exitButton = new TextButton("Exit", game.skin);
            leaderboardTable.add(exitButton).bottom().width(300).padTop(10);

            leaderboardMenu.pack();
            leaderboardMenu.setSize(600, 600);
            leaderboardMenu.setX((viewport.getWorldWidth() / 2) - (leaderboardMenu.getWidth() / 2));
            leaderboardMenu.setY((viewport.getWorldHeight() / 2) - (leaderboardMenu.getHeight() / 2));

            exitButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.soundManager.playButton();
                    dispose();
                    game.setScreen(previousScreen);
                    previousScreen.resume();
                }
            });

            ArrayList<String[]> leaderboard10 = getLeaderboard10();

            for (String[] entry : leaderboard10) {
                Label nameLabel = new Label(entry[0], game.skin, "interaction");
                Label scoreLabel = new Label(entry[1], game.skin, "interaction");

                scrollTable.add(nameLabel).width(230f).padLeft(30);
                scrollTable.add(scoreLabel).width(230f).padLeft(60);
                scrollTable.row();
            }
        }

    }

    public ArrayList<String[]> getLeaderboard10(){
        ArrayList<String[]> leaderboardData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(leaderboardPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 2) {
                    leaderboardData.add(values);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Collections.sort(leaderboardData.subList(leaderboardData.size() > 0 ? 1 : 0, leaderboardData.size()), new Comparator<String[]>() {
            @Override
            public int compare(String[] o1, String[] o2) {
                return Integer.parseInt(o2[1]) - Integer.parseInt(o1[1]); // Sort in descending order
            }
        });

        ArrayList<String[]> leaderboard10 = new ArrayList<>(leaderboardData.subList(1, Math.min(leaderboardData.size(), 11)));
        return leaderboard10;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.blueBackground.draw();

        leaderboardStage.act(delta);
        leaderboardStage.draw();

        camera.update();
    }

    @Override
    public void resize(int width, int height) {
        leaderboardStage.getViewport().update(width, height);
        viewport.update(width, height);
    }

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
        leaderboardStage.dispose();
    }
}
