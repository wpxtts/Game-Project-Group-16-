package io.dimitris.gemo.tests;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.skloch.game.DialogueBox;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(GdxTestRunner.class)
public class DialogueBoxTests {
    private Window dialogueWindow;
    private Label textLabel;
    private Skin skin;
    private DialogueBox dialogueBox;
    private float textCounter = 0;
    private int choiceIndex = 0;
    //private final int MAXCHARS;
    //private DialogueBox.SelectBox selectBox;
    private Array<String> textLines;
    private int linePointer = 0;
    private String eventKey = null;
    private boolean scrollingText = false;

    @Before
    public void setUp() {
        skin = new Skin();
        //dialogueBox = new DialogueBox(skin);
    }
    @Test
    public void testSetTexta() {
//        String text = "Are you sure you want to sleep at the Piazza? This will cost you 10 energy";
//        dialogueBox.setText(text);
//        assertEquals(text, selectBox.getText(text));
    }

    @Test
    public void testSetText() {
//        String newText = "This is a new text.";
//        dialogueBox.setText(newText);
//        assertEquals(newText, textLabel.getText().toString());
    }

    @Test
    public void testSetPos() {
//        float newX = 100f;
//        float newY = 200f;
//        dialogueBox.setPos(newX, newY);
//        assertEquals(newX, dialogueWindow.getX(), 0.001);
//        assertEquals(newY, dialogueWindow.getY(), 0.001);
    }

    @Test
    public void testScrollText() {
//        // Assuming speed is set to 1 for this test
//        dialogueBox.setText("Test text");
//        float initialCounter = textCounter;
//        dialogueBox.scrollText(1f);
//        assertTrue(textCounter > initialCounter);
    }

    @Test
    public void testSetChoice() {
//        int newIndex = 1;
//        selectBox.setOptions(new String[]{"Option 1", "Option 2"}, new String[]{"event1", "event2"});
//        selectBox.setChoice(newIndex);
//        assertEquals(newIndex, choiceIndex);
    }

    @Test
    public void testAdvanceText() {
//        dialogueBox.setText("First line\nSecond line");
//        dialogueBox.advanceText(null);
//        assertEquals("Second line", textLabel.getText().toString());
    }

    @Test
    public void testHide() {
//        dialogueBox.hide();
//        assertFalse(dialogueWindow.isVisible());
    }
}