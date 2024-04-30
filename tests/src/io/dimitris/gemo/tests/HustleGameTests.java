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

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(GdxTestRunner.class)
public class HustleGameTests {

    @Test
    public void testHustleGameAssetsExists(){
        assertTrue("The game skin doesn't exist",
                Gdx.files.internal(HustleGame.skinPath).exists());
        assertTrue("The map doesn't exist",
                Gdx.files.internal(HustleGame.mapPath).exists());
        assertTrue("The white square image doesn't exist",
                Gdx.files.internal(HustleGame.whiteSquarePath).exists());
        assertTrue("The credits text file doesn't exist",
                Gdx.files.internal(HustleGame.creditsPath).exists());
        assertTrue("The tutorial text file doesn't exist",
                Gdx.files.internal(HustleGame.tutorialTextPath).exists());
    }

    @Test
    public void testReadTextFile() throws FileNotFoundException {
        HustleGame game = new HustleGame(100,100);

        //Test readTextFile on a pre-made test file with the contents
        //"Hello World!\nThis is some text."
        String textFileContents = game.readTextFile("../tests/testAssets/testFile.txt");
        // Given that depending on what system we are using linebreaks can be represented
        // as \r\n or just \n we check both
        assertTrue("Hello World!\r\nThis is some text.".equals(textFileContents)||
                "Hello World!\nThis is some text.".equals(textFileContents));

        //Test readTextFile one a pre-made test file which is completely
        //empty.
        String emptyFileContents = game.readTextFile("../tests/testAssets/emptyFile.txt");
        assertEquals("",emptyFileContents);

        //Test if readTextFile throws the correct exception when the file doesn't exist
        assertThrows(FileNotFoundException.class,()->game.readTextFile("a/fake/file/path"));
    }

}