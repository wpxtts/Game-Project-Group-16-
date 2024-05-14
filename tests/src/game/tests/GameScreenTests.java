package game.tests;
import com.RichTeam.game.EventManager;
import com.RichTeam.game.GameScreen;
import com.RichTeam.game.HustleGame;
import com.RichTeam.game.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(GdxTestRunner.class)
public class GameScreenTests {
    private GameScreen game;
    private HustleGame hGame;
    private EventManager eventManager;
    @Test
    public void testPlaceholder(){
        assertTrue(true);
    }
//    @Before
//    public void setUp(){
//        eventManager = mock(EventManager.class);
//        int avatarChoice = 1;
//        boolean draw = true;
//        game = new GameScreen(hGame, avatarChoice, draw);
//    }
//    @Test
//    public void testPOIAssetsExists() {
//        //Check POI sprites exist
//    }
}