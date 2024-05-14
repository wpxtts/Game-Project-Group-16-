package game.tests;
import com.RichTeam.game.HustleGame;
import com.badlogic.gdx.Gdx;
import com.RichTeam.game.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

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