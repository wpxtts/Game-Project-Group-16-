package io.dimitris.gemo.tests;
import com.badlogic.gdx.Gdx;
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
    @Test
    public void testStartButtonTask() {
        // Set up
        HustleGame game = mock(HustleGame.class);
        game.soundManager = mock(SoundManager.class);
        MenuScreen menuScreen = new MenuScreen(game,false);
        Window tutorialWindow = mock(Window.class);
        Table buttonTable = mock(Window.class);
        menuScreen.startButtonTask(buttonTable,tutorialWindow);

        // When the start button is pressed it needs to make
        // the button table vanish and the tutorial window appear
        verify(buttonTable).setVisible(false);
        verify(tutorialWindow).setVisible(true);
    }
}