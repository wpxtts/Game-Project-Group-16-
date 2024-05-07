package io.dimitris.gemo.tests;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.skloch.game.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(GdxTestRunner.class)
public class EventManagerTests {
    private GameScreen game;
    private EventManager eventManager;
    private DialogueBox dialogueBox;
    private Window dialogueWindow;
    @Before
    public void setUp(){
        game = new GameScreen(mock(HustleGame.class),1,false);
        game = mock(GameScreen.class);
        dialogueBox = mock(DialogueBox.class);
        dialogueWindow = mock(Window.class);
        eventManager = new EventManager(game);
    }
    @Test
    public void testPOIAssetsExists() {
        //Check POI sprites exist
        assertTrue("The sprite asset for the POI doesn't exist",
                Gdx.files.internal(Player.playerSprite).exists());
    }

    @Test
    public void testActivityEnergiesExist() {
        //Check activity energies exist
        System.out.println(eventManager.activityEnergies.entrySet());
        HashMap<String, Integer> activityEnergies = eventManager.activityEnergies;
        // Studying activity energy:
        assertTrue(activityEnergies.containsKey("studying"));
        assertNotNull(activityEnergies.get("studying"));
        // Meet friends activity energy:
        assertTrue(activityEnergies.containsKey("meet_friends"));
        assertNotNull(activityEnergies.get("meet_friends"));
        // Eating activity energy
        assertTrue(activityEnergies.containsKey("eating"));
        assertNotNull(activityEnergies.get("eating"));
        // Flowers activity energy
        assertTrue(activityEnergies.containsKey("flowers"));
        assertNotNull(activityEnergies.get("flowers"));
        // Town activity energy
        assertTrue(activityEnergies.containsKey("town"));
        assertNotNull(activityEnergies.get("town"));
        // Shop activity energy
        assertTrue(activityEnergies.containsKey("shop"));
        assertNotNull(activityEnergies.get("shop"));
        // Gym activity energy
        assertTrue(activityEnergies.containsKey("gym"));
        assertNotNull(activityEnergies.get("gym"));
        // Library activity energy
        assertTrue(activityEnergies.containsKey("library"));
        assertNotNull(activityEnergies.get("library"));
        // East activity energy
        assertTrue(activityEnergies.containsKey("east"));
        assertNotNull(activityEnergies.get("east"));
    }

    @Test
    public void testAchievementExists() {
        //Check achievement targets exist
        HashMap<String, Integer> streaksAchieved = eventManager.getStreaks();
        for (Map.Entry<String, Integer> entry : streaksAchieved.entrySet()) {
            String task = entry.getKey();
            assertNotNull("The target value to gain this achievement doesn't exist",
                    task);
        }
    }

    @Test
    public void testGetObjectInteraction(){
        eventManager.objectInteractions.put("activity1","result1");

        String piazzaInteraction = eventManager.getObjectInteraction("piazza");
        System.out.println(piazzaInteraction);
        assertTrue(piazzaInteraction.startsWith("Eat ")&
                piazzaInteraction.endsWith(" at the Piazza?"));

        assertSame("result1", eventManager.getObjectInteraction("activity1"));
    }
    @Test
    public void testHasCustomObjectInteraction(){
        eventManager.objectInteractions.put("object1","result1");
        assertTrue(eventManager.hasCustomObjectInteraction("object1"));
        assertFalse(eventManager.hasCustomObjectInteraction("This isn't an object"));
    }

    @Test
    public void testRonCookeEvent(){
        game.setSeconds(10);
        String[] args = new String[1];
        args[0] = "rch";
        String result = eventManager.ronCookEvent(args);
        assertEquals("It's too early in the morning to meet your friends, go to bed!",result);
    }

    @Test
    public void testTreeEvent(){
        eventManager = mock(EventManager.class);
        eventManager.treeEvent();
        int current_energy = game.getEnergy();
        assertEquals(current_energy,game.getEnergy());
    }
    @Test
    public void testChestEvent(){
        eventManager = mock(EventManager.class);
        eventManager.chestEvent();
        int current_energy = game.getEnergy();
        assertEquals(current_energy,game.getEnergy());
    }
    @Test
    public void testObjectEvent(){
        eventManager = mock(EventManager.class);
        String pineapple = "Pineapple";
        eventManager.objectEvent(pineapple);
        int current_energy = game.getEnergy();
        assertEquals(current_energy,game.getEnergy());
    }
    //add fade to black test?
    //add events trigger test?
//    @Test
//    public void testEventTriggered() {
//        //event trigger
//        assertTrue("This event is not able to be triggered",
//                )
//    }
//
//    @Test
//    public void testEventUsesEnergy() {
//        int current_energy = game.getEnergy();
//        int energyCost = 100;
//        String[] args = new String[]{"flowers-1", "flowers-2", "flowers-3"};
//        int hours = Integer.parseInt(args[1]);
//        eventManager.flowersEvent(args);
//        assertEquals("The streak for smelling flowers was not increased",
//                current_energy, game.getEnergy() - (energyCost * hours));
//    }
//
//    @Test
//    public void testEventPassesTime() {
//        float current_time = game.getSeconds();
//        String[] args = new String[]{"flowers-1", "flowers-2", "flowers-3"};
//        int hours = Integer.parseInt(args[1]);
//        eventManager.flowersEvent(args);
//        assertEquals("The streak for smelling flowers was not increased",
//                current_time, current_time - (hours * 60));
//    }
//
//    @Test
//    public void testStreaksIncrease() {
//        int current_streak = eventManager.streaks.get("flowers");
//        String[] args = new String[]{"flowers-1", "flowers-2", "flowers-3"};
//        eventManager.flowersEvent(args);
//        assertEquals("The streak for smelling flowers was not increased",
//                Integer.valueOf((current_streak + 1)), eventManager.streaks.get("flowers"));
//    }
//
//    @Test
//    public void testEventBlockedTired() {
//        String[] args = new String[]{"flowers-1", "flowers-2", "flowers-3"};
//        eventManager.flowersEvent(args);
//        game.setEnergy(0);
//        DialogueBox dialogueBox = mock(DialogueBox.class);
//        assertTrue(dialogueBox.isVisible());
//    }
//
//    @Test
//    public void testEventBlockedLate() {
//        game.passTime(9*60);
//        String[] args = new String[]{"flowers-1", "flowers-2", "flowers-3"};
//        eventManager.flowersEvent(args);
//        DialogueBox dialogueBox = mock(DialogueBox.class);
//        assertFalse(dialogueBox.isVisible());
//    }
//
//    @Test
//    public void testCatchupUsable() {
//        int daily_study = 2;
//        String[] args = new String[]{"fadefromblack"};
//        eventManager.compSciEvent(args);
//        assertTrue(eventManager.catchup_used);
//    }
//
//    @Test
//    public void testPostSleepEnergy() {
//        String[] args = new String[]{"fadefromblack"};
//        eventManager.accomEvent(args);
//        assertEquals("The player did not regain full energy after sleeping",
//                game.getEnergy(), 100);
//    }
//
//    @Test
//    public void testPostSleepTime() {
//        String[] args = new String[]{"fadefromblack"};
//        eventManager.accomEvent(args);
//        assertEquals("The player did not regain full energy after sleeping",
//                game.getSeconds(), 8*60);
//    }
//
//    @Test
//    public void testPostSleepStudyHours() {
//        String[] args = new String[]{"comp_sci-1", "comp_sci-2", "comp_sci-3"};
//        eventManager.accomEvent(args);
//        assertEquals("The player did not regain full energy after sleeping",
//                eventManager.daily_study, 0);
//    }
}
