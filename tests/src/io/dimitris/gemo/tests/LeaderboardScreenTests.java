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

        String[] x1 = {"Sammy","52"};
        String[] x2 = {"Tom","11"};
        String[] x3 = {"George","10"};
        String[][] targetLeaderboardX = {x1,x2,x3};
        // We convert leaderboard10 to an array to allow us to use the
        // assertArrayEquals assertion.
        assertArrayEquals(targetLeaderboardX,leaderboard10.toArray());

        // TEST 2: There are 10 scores recorded
        try {
            FileWriter writer = new FileWriter(LeaderboardScreen.leaderboardPath, false);
            writer.write("Player Name,Score\nGeorge,1\nTom,2\nSammy,3\n" +
                    "Ellie,4\nLibby,5\nFred,6\nGeorge,7\nGeorge,8\n" +
                    "Albert,9\nAmelie,10\n"); // Write the data to the file
            writer.close(); // Close the FileWriter
        } catch (IOException e) {
            e.printStackTrace();
        }
        leaderboard10 = leaderboardScreen.getLeaderboard10();

        String[] y1 = {"Amelie","10"};
        String[] y2 = {"Albert","9"};
        String[] y3 = {"George","8"};
        String[] y4 = {"George","7"};
        String[] y5 = {"Fred","6"};
        String[] y6 = {"Libby","5"};
        String[] y7 = {"Ellie","4"};
        String[] y8 = {"Sammy","3"};
        String[] y9 = {"Tom","2"};
        String[] y10 = {"George","1"};
        String[][] targetLeaderboardY = {y1,y2,y3,y4,y5,y6,y7,y8,y9,y10};
        assertArrayEquals(targetLeaderboardY,leaderboard10.toArray());

        //TEST 3: There are more than 10 scores recorded
        try {
            FileWriter writer = new FileWriter(LeaderboardScreen.leaderboardPath, false);
            writer.write("Player Name,Score\nGandelf,13\nGeorge,1\nTom,2\nSammy,3\n" +
                    "Ellie,4\nLibby,5\nFred,6\nGeorge,7\nGeorge,8\n" +
                    "Albert,9\nDilbert,11\nAmelie,10\nAragorn,12\n"); // Write the data to the file
            writer.close(); // Close the FileWriter
        } catch (IOException e) {
            e.printStackTrace();
        }
        leaderboard10 = leaderboardScreen.getLeaderboard10();
        String[] z1 = {"Gandelf","13"};
        String[] z2 = {"Aragorn","12"};
        String[] z3 = {"Dilbert","11"};
        String[] z4 = {"Amelie","10"};
        String[] z5 = {"Albert","9"};
        String[] z6 = {"George","8"};
        String[] z7 = {"George","7"};
        String[] z8 = {"Fred","6"};
        String[] z9 = {"Libby","5"};
        String[] z10 = {"Ellie","4"};
        String[][] targetLeaderboardZ = {z1,z2,z3,z4,z5,z6,z7,z8,z9,z10};
        assertArrayEquals(targetLeaderboardZ,leaderboard10.toArray());
    }


    // For leaderboard screen tests I need a test which tests the getting the top 10 from the leaderboard.
}