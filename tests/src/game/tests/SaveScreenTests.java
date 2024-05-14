package game.tests;
import com.RichTeam.game.HustleGame;
import com.RichTeam.game.SaveScreen;
import com.RichTeam.game.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.RichTeam.game.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class SaveScreenTests {

    @Before
    public void setUp(){
        // Set up test leaderboard file
        SaveScreen.leaderboardPath = "../tests/testAssets/testLeaderboard.csv";
        try {
            FileWriter writer = new FileWriter(SaveScreen.leaderboardPath, false);
            writer.write("Player Name,Score\n"); // Write the data to the file
            writer.close(); // Close the FileWriter
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void testLeaderboardCsvExists() {
        assertTrue("The leaderboard CSV doesn't exist",
                Gdx.files.internal(SaveScreen.leaderboardPath).exists());
    }

    @Test
    public void testSaveScore(){
        HustleGame game = mock(HustleGame.class);
        Integer score = 10;
        SaveScreen saveScreen = new SaveScreen(game,score,false);

        saveScreen.saveScore("George",0);
        saveScreen.saveScore("Seyi",32);
        saveScreen.saveScore("Will",16);
        String leaderboardContents ="";
        try {
            leaderboardContents = Files.readString(Path.of(SaveScreen.leaderboardPath));
        }catch(IOException e){
            e.printStackTrace();
        }
        assertEquals("Player Name,Score\n" +
                "George,0\n" +
                "Seyi,32\n" +
                "Will,16\n",
                leaderboardContents);
        assertThrows(IllegalArgumentException.class,()->saveScreen.saveScore("George",-1));
    }

    @Test
    public void testSaveButtonPress(){
        HustleGame game = mock(HustleGame.class);
        Integer score = 10;
        SaveScreen saveScreen = new SaveScreen(game,score,false);
        game.soundManager = mock(SoundManager.class);
        TextButton button = mock(TextButton.class);

        // On first press button should save input
        saveScreen.saveButtonPress("Ben",10,button);
        String leaderboardContents ="";
        try {
            leaderboardContents = Files.readString(Path.of(SaveScreen.leaderboardPath));
        }catch(IOException e){
            e.printStackTrace();
        }
        assertEquals("Player Name,Score\n" +
                        "Ben,10\n",
                leaderboardContents);

        // But if pressed multiple times, the button should not continue to save
        // as a score should only be saved once
        saveScreen.saveButtonPress("Ben",10,button);
        try {
            leaderboardContents = Files.readString(Path.of(SaveScreen.leaderboardPath));
        }catch(IOException e){
            e.printStackTrace();
        }
        assertEquals("Player Name,Score\n" +
                        "Ben,10\n",
                leaderboardContents);
    }

    @Test
    public void testExitButtonPress(){
        HustleGame game = mock(HustleGame.class);
        game.soundManager = mock(SoundManager.class);
        Integer score = 10;
        SaveScreen saveScreen = new SaveScreen(game,score,false);
        saveScreen.drawOnExit=false;
        saveScreen.exitButtonPress();

        // On exit, game should have its screen set back to the menu
        verify(game).setScreen(saveScreen.menuScreen);
    }

}