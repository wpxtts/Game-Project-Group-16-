package game.tests;

import com.RichTeam.game.EventManager;
import com.RichTeam.game.GameOverScreen;
import com.RichTeam.game.GameScreen;
import com.RichTeam.game.HustleGame;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
@RunWith(GdxTestRunner.class)
public class IntegrationTests {
    private GameScreen game;
    private HustleGame hGame;
    private EventManager eventManager;
    public int hoursStudied, hoursRecreational, hoursSlept;

    @Before
    public void setUp(){
        hGame = mock(HustleGame.class);
        eventManager = mock(EventManager.class);
        game = new GameScreen(hGame,1,false);
    }
    @Test
    public void testScoreSystem(){
        int hours = 2;
        for(int i=0; i<5;i++){
            game.addRecreationalHours(hours);
            game.addSleptHours(hours);
            game.addStudyHours(hours);}
        int score = GameOverScreen.scoreCalculation(hoursStudied,hoursRecreational,hoursSlept);
        assertTrue(score<=40);
        hoursStudied = 0;
        hoursRecreational = 0;
        hoursSlept = 0;
        for(int i=0; i<40;i++){
            game.addRecreationalHours(hours);
            game.addSleptHours(hours);
            game.addStudyHours(hours);}
        score = GameOverScreen.scoreCalculation(hoursStudied,hoursRecreational,hoursSlept);
        assertTrue(score>=40);
    }

}
