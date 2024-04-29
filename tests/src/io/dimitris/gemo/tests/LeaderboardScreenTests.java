package io.dimitris.gemo.tests;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.skloch.game.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class LeaderboardScreenTests {
    @Before
    public void setUp(){

    }

    @Test
    public void testGetLeaderboard10() {
        LeaderboardScreen leaderboardScreen = new LeaderboardScreen(mock(HustleGame.class),
                mock(Screen.class),false);

        // TEST 1: There are less than 10 scores recorded
        LeaderboardScreen.leaderboardPath = "../tests/testAssets/testLeaderboard.csv";
        try {
            FileWriter writer = new FileWriter(LeaderboardScreen.leaderboardPath, false);
            writer.write("Player Name,Score\nGeorge,10\nTom,11\nSammy,52\n"); // Write the data to the file
            writer.close(); // Close the FileWriter
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String[]>leaderboard10 = leaderboardScreen.getLeaderboard10();

        String[] l1 = {"Sammy","52"};
        String[] l2 = {"Tom","11"};
        String[] l3 = {"George","10"};
        String[][] targetLeaderboard = {l1,l2,l3};
        // We convert leaderboard10 to an array to allow us to use the
        // assertArrayEquals assertion.
        assertArrayEquals(targetLeaderboard,leaderboard10.toArray());

        // TEST 2: There are 10 scores recorded

        //TEST 3: There are more than 10 scores recorded

    }


    // For leaderboard screen tests I need a test which tests the getting the top 10 from the leaderboard.
}