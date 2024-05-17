package game.tests;
import com.RichTeam.game.GameScreen;
import com.RichTeam.game.*;
import com.badlogic.gdx.Gdx;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(GdxTestRunner.class)
public class GameOverScreenTests {
    private GameScreen game;
    @Before
    public void setUp(){
        game = mock(GameScreen.class);
    }
    @Test
    public void testAppleAssetExists() {
        assertTrue("The sprite asset for this achievement doesn't exist",
                Gdx.files.internal("../assets/"+GameOverScreen.apple_path).exists());
    }

    @Test
    public void testFlowerAssetExists() {
        assertTrue("The sprite asset for this achievement doesn't exist",
                Gdx.files.internal("../assets/"+GameOverScreen.flower_path).exists());
    }

    @Test
    public void testBusAssetExists() {
        assertTrue("The sprite asset for this achievement doesn't exist",
                Gdx.files.internal("../assets/"+GameOverScreen.bus_path).exists());
    }
    @Test
    public void testBookAssetExists() {
        assertTrue("The sprite asset for this achievement doesn't exist",
                Gdx.files.internal("../assets/"+GameOverScreen.book_path).exists());
    }
    @Test
    public void testLibraryBookAssetExists() {
        assertTrue("The sprite asset for this achievement doesn't exist",
                Gdx.files.internal("../assets/"+GameOverScreen.library_book_path).exists());
    }
    @Test
    public void testNightOwlAssetExists() {
        assertTrue("The sprite asset for this achievement doesn't exist",
                Gdx.files.internal("../assets/"+GameOverScreen.night_owl_path).exists());
    }

    @Test
    public void testPlayerScoreFailed() {
        int hoursStudied = 0;
        int hoursRecreational = 0;
        int hoursSlept = 0;
        int result = GameOverScreen.scoreCalculation(hoursStudied,hoursRecreational,hoursSlept);
        assertTrue(result <= 40);
    }

    @Test
    public void testPlayerScorePassed() {
        // Example
        int hoursStudied = 30;
        int hoursRecreational = 50;
        int hoursSlept = 144;
        int result =  GameOverScreen.scoreCalculation(hoursStudied,hoursRecreational,hoursSlept);
        assertTrue(result >= 40);
    }
}