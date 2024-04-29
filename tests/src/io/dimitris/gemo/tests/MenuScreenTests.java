package io.dimitris.gemo.tests;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.skloch.game.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class MenuScreenTests {
    private HustleGame game;
    private MenuScreen menuScreen;

    @Before
    public void setUp(){
        game = mock(HustleGame.class);
        game.soundManager = mock(SoundManager.class);
        menuScreen = new MenuScreen(game,false);
    }

    @Test
    public void testStartButtonTask() {
        // Set up
        Window tutorialWindow = mock(Window.class);
        Table buttonTable = mock(Table.class);

        menuScreen.startButtonTask(buttonTable,tutorialWindow);

        // When the start button is pressed it needs to make
        // the button table vanish and the tutorial window appear
        verify(buttonTable).setVisible(false);
        verify(tutorialWindow).setVisible(true);
    }
    @Test
    public void testSettingsButtonTask(){
        menuScreen.settingsButtonTask(menuScreen);

        // When the settings button is pressed we need to set
        // the screen to a SettingsScreen
        verify(game).setScreen(any(SettingsScreen.class));
    }
    @Test
    public void testCreditsButtonTask(){
        menuScreen.creditsButtonTask(menuScreen);
        // When the credits button is pressed we need to set
        // the screen to a CreditScreen
        verify(game).setScreen(any(CreditScreen.class));
    }

    @Test
    public void testLeaderboardButtonTask(){
        menuScreen.leaderboardButtonTask(menuScreen);

        // When the settings button is pressed we need to set
        // the screen to a SettingsScreen
        verify(game).setScreen(any(LeaderboardScreen.class));
    }

    @Test
    public void testButton1Press(){
        menuScreen.avatar1ButtonTask();
        verify(game).setScreen(any(GameScreen.class));
    }

    @Test
    public void testButton2Press(){
        menuScreen.avatar2ButtonTask();
        verify(game).setScreen(any(GameScreen.class));
    }

    // Note: We cannot test the exit button functionality without
    // excessive effort and using tools like PowerMockito, so we test it manually
    // instead
}