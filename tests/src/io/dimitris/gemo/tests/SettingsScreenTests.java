package io.dimitris.gemo.tests;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.skloch.game.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(GdxTestRunner.class)
public class SettingsScreenTests {
    @Test
    public void testUpdateVolume() {
        // Create mock dependencies
        HustleGame hustleGameMock = mock(HustleGame.class);
        hustleGameMock.soundManager = mock(SoundManager.class);
        Screen prevScreen = mock(Screen.class);
        // Create SettingsScreen instance
        SettingsScreen settingsScreen = new SettingsScreen(hustleGameMock, prevScreen,false);
        settingsScreen.updateVolume(50,50);
        verify(hustleGameMock.soundManager).setMusicVolume(0.5f);
        verify(hustleGameMock.soundManager).setSfxVolume(0.5f);
    }
}