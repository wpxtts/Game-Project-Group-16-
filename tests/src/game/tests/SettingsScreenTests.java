package game.tests;
import com.RichTeam.game.HustleGame;
import com.RichTeam.game.SettingsScreen;
import com.RichTeam.game.SoundManager;
import com.badlogic.gdx.Screen;
import com.RichTeam.game.*;
import org.junit.Test;
import org.junit.runner.RunWith;

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

        // If music volume is set to 50 and sfx is set to 75
        // then music volume will be 0.5 and sfx volume will be 0.75
        settingsScreen.updateVolume(50,75);
        verify(hustleGameMock.soundManager).setMusicVolume(0.5f);
        verify(hustleGameMock.soundManager).setSfxVolume(0.75f);

        // Testing boundary cases
        settingsScreen.updateVolume(0,0);
        verify(hustleGameMock.soundManager).setMusicVolume(0);
        verify(hustleGameMock.soundManager).setSfxVolume(0);
        settingsScreen.updateVolume(100,100);
        verify(hustleGameMock.soundManager).setMusicVolume(1);
        verify(hustleGameMock.soundManager).setSfxVolume(1);

        // Testing invalid cases
        assertThrows(IllegalArgumentException.class,()->settingsScreen.updateVolume(-1,50));
        assertThrows(IllegalArgumentException.class,()->settingsScreen.updateVolume(50,-1));
        assertThrows(IllegalArgumentException.class,()->settingsScreen.updateVolume(50,-1));
        assertThrows(IllegalArgumentException.class,()->settingsScreen.updateVolume(-1,50));
    }
}