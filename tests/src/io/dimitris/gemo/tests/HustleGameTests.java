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
    public void testReadTextFile() throws FileNotFoundException {
        HustleGame game = new HustleGame(100,100);
        String textFileContents = game.readTextFile("../tests/testAssets/testFile.txt");
        // Given that depending on what system we are using linebreaks can be represented
        // as \r\n or just \n we check both
        assertTrue("Hello World!\r\nThis is some text.".equals(textFileContents)||
                "Hello World!\nThis is some text.".equals(textFileContents));
        String emptyFileContents = game.readTextFile("../tests/testAssets/emptyFile.txt");
        assertEquals("",emptyFileContents);

        assertThrows(FileNotFoundException.class,()->game.readTextFile("a/fake/file/path"));
    }

}