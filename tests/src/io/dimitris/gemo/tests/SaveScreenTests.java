package io.dimitris.gemo.tests;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.skloch.game.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class SaveScreenTests {
    private SaveScreen saveScreen;
    @Before
    public void setUp(){
        HustleGame game = mock(HustleGame.class);
        Integer score = 10;
        SaveScreen saveScreen = new SaveScreen(game,score,false);
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



}