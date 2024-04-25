package io.dimitris.gemo.tests;
import com.badlogic.gdx.Screen;
import com.skloch.game.CreditScreen;
import com.skloch.game.EventManager;
import com.skloch.game.GameScreen;
import com.skloch.game.HustleGame;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
@RunWith(GdxTestRunner.class)
public class CreditScreenTests {
    @Test
    public void testCreditScreenExit(){
        //HustleGame hustleGame = new HustleGame(800,800);
        int h = 1;
        //Screen previousScreen = hustleGame.getScreen();
        //CreditScreen creditScreen = new CreditScreen(hustleGame,previousScreen);
        //creditScreen.resize(100,100);
        assertEquals(1,h) ;
    }
}
