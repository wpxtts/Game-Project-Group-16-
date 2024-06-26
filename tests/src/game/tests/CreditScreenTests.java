package game.tests;
import com.RichTeam.game.CreditScreen;
import com.RichTeam.game.HustleGame;
import com.RichTeam.game.MenuScreen;
import com.RichTeam.game.SoundManager;
import com.RichTeam.game.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(GdxTestRunner.class)
public class CreditScreenTests {
    private HustleGame game;
    private MenuScreen menuScreen;
    private CreditScreen creditScreen;
    @Before
    public void setUp(){
        game = mock(HustleGame.class);
        game.soundManager = mock(SoundManager.class);
        menuScreen = mock(MenuScreen.class);
        creditScreen = new CreditScreen(game, menuScreen,false);
    }
    @Test
    public void testCreditScreenExit(){
        creditScreen.exitButtonTask();
        // When the exit button is pressed we need to make sure
        // the screen is returned to the MainScreen
        verify(game).setScreen(any(MenuScreen.class));
    }
}
