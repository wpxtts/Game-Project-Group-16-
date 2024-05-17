package game.tests;
import com.RichTeam.game.EventManager;
import com.RichTeam.game.GameScreen;
import com.RichTeam.game.HustleGame;
import com.RichTeam.game.*;
import com.badlogic.gdx.Gdx;
import org.junit.Before;
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
    public int hoursStudied, hoursRecreational, hoursSlept;

    @Before
    public void setUp() {
        //game = mock(GameScreen.class);
        hGame = mock(HustleGame.class);
        eventManager = mock(EventManager.class);
        game = new GameScreen(hGame,1,false);
    }
    @Test
    public void formatTimeTest(){
        //end of day
        int daySeconds = 1439;
        String result = game.formatTime(daySeconds);
        int hour = Math.floorDiv(daySeconds, 60);
        String minutes = String.format("%02d", (daySeconds - hour * 60));
        assertEquals( String.format("%d:%spm", hour-12,minutes),result);

        //start of day
        daySeconds = 0;
        result = game.formatTime(daySeconds);
        hour = Math.floorDiv(daySeconds, 60);
        minutes = String.format("%02d", (daySeconds - hour * 60));
        assertEquals( String.format("12:%sam", minutes),result);

        //mid day - 1
        daySeconds = 719;
        result = game.formatTime(daySeconds);
        hour = Math.floorDiv(daySeconds, 60);
        minutes = String.format("%02d", (daySeconds - hour * 60));
        assertEquals( String.format("%d:%sam", hour, minutes),result);

        //mid day
        daySeconds = 720;
        result = game.formatTime(daySeconds);
        hour = Math.floorDiv(daySeconds, 60);
        minutes = String.format("%02d", (daySeconds - hour * 60));
        assertEquals( String.format("12:%spm", minutes),result);

        // afternoon
        daySeconds = 780;
        result = game.formatTime(daySeconds);
        hour = Math.floorDiv(daySeconds, 60);
        minutes = String.format("%02d", (daySeconds - hour * 60));
        assertEquals( String.format("%d:%spm", hour-12, minutes),result);

        //past midnight
        daySeconds = 1500;
        result = game.formatTime(daySeconds);
        hour = Math.floorDiv(daySeconds, 60);
        minutes = String.format("%02d", (daySeconds - hour * 60));
        assertFalse(hour<=12);
    }
    @Test
    public void testMealNames(){
        game.setSeconds(480);
        String meal = game.getMeal();
        assertEquals(meal,"breakfast");

        game.setSeconds(720);
        meal = game.getMeal();
        assertEquals(meal,"lunch");

        game.setSeconds(1080);
        meal = game.getMeal();
        assertEquals(meal,"dinner");
    }
    @Test
    public void testSetSeconds(){
        //test to show an illegal amount of seconds
        int seconds = 1550;
        game.setSeconds(seconds);
        assertEquals(game.getSeconds(),1550,0.01);
        game.formatTime(seconds);
        assertEquals(game.getSeconds(),110,0.01);
    }
    @Test
    public void testStudyHours(){
        int hours = 2;
        game.addStudyHours(hours);
        game.addStudyHours(hours);
        game.addStudyHours(hours);
        assertEquals(hours+hours+hours,game.getStudyingHours(),0.01);
    }
    @Test
    public void testRecreationalHours(){
        int hours = 2;
        game.addRecreationalHours(hours);
        game.addRecreationalHours(hours);
        game.addRecreationalHours(hours);
        assertEquals(hours+hours+hours,game.getRecreationalHours(),0.01);
    }
    @Test
    public void testSleepingHours(){
        int hours = 2;
        game.addSleptHours(hours);
        game.addSleptHours(hours);
        game.addSleptHours(hours);
        assertEquals(hours+hours+hours,game.getHoursSlept(),0.01);
    }
}