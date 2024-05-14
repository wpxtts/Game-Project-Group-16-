package game.tests;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.RichTeam.game.DialogueBox;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;


@RunWith(GdxTestRunner.class)
public class DialogueBoxTests {
    private Window dialogueWindow;
    private Label textLabel;
    private Skin skin;
    private DialogueBox dialogueBox;
    private float textCounter = 0;
    private int choiceIndex = 0;
    //private final int MAXCHARS;
    private DialogueBox.SelectBox selectBox;
    private Array<String> textLines;
    private int linePointer = 0;
    private String eventKey = null;
    private boolean scrollingText = false;

    @Before
    public void setUp() {
        skin = new Skin();

        dialogueBox = mock(DialogueBox.class);
        selectBox = mock(DialogueBox.SelectBox.class);
    }
    @Test
    public void testSetText() {
        String text = "Are you sure you want to sleep at the Piazza? This will cost you 10 energy";
        dialogueBox.setText(text);
        assertEquals(text, DialogueBox.SelectBox.getText(text));
    }
    @Test
    public void testSetPos() {
        float newX = 100f;
        float newY = 200f;
        dialogueBox.setPos(newX, newY);
        assertEquals(newX, DialogueBox.getX(newX), 0.001);
        assertEquals(newY, DialogueBox.getY(newY), 0.001);
    }
    @Test
    public void testHide() {
        dialogueBox.hide();
        assertFalse(dialogueBox.isVisible());
    }
}