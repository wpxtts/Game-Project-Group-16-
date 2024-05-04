package io.dimitris.gemo.tests;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.skloch.game.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(GdxTestRunner.class)
public class GameOverScreenTests {
    @Test
    public void testRCHAssetExists() {
        assertTrue("The sprite asset for this achievement doesn't exist",
                Gdx.files.internal(GameOverScreen.rch_path).exists());
    }

    @Test
    public void testFlowerAssetExists() {
        assertTrue("The sprite asset for this achievement doesn't exist",
                Gdx.files.internal(GameOverScreen.flower_path).exists());
    }

    @Test
    public void testBusAssetExists() {
        assertTrue("The sprite asset for this achievement doesn't exist",
                Gdx.files.internal(GameOverScreen.bus_path).exists());
    }

    @Test
    public void testPlayerScoreFailed() {
        assertTrue("The players score is between 0 and 39 (they failed",
                GameOverScreen.score < 40);
    }

    @Test
    public void testPlayerScorePassed() {
        int total = GameScreen.hoursStudied + GameScreen.hoursRecreational + GameScreen.hoursSlept;
        assertEquals("The players score is above or equal to 40 (they passed)",
                GameOverScreen.score, 40 + (1 / -total));
    }

    @Test
    public void testAchievementsAchievable() {
        // achievement goal must exist and the existing streaks must be callable
        HashMap<String, Integer> streaksAchieved = EventManager.getStreaks();
        assertEquals(EventManager.streaks, streaksAchieved);
        for (Map.Entry<String, Integer> entry : GameOverScreen.streakGoals.entrySet()) {
            String task = entry.getKey();
            assertNotNull("The target value to gain this achievement doesn't exist",
                    task);
        }
    }

    @Test
    public void testExitButtonTask() {
        // Set up
        Window gameOverWindow = mock(Window.class);
        Table gameOverTable = mock(Table.class);

        GameOverScreen.game.soundManager.playButton();
        GameOverScreen.game.setScreen(new SaveScreen(GameOverScreen.game, GameOverScreen.score, true));

        // When the exit button is pressed it needs to make
        // the button table vanish and the score window appear
        verify(gameOverTable).setVisible(false);
        verify(gameOverWindow).setVisible(true);
    }
}