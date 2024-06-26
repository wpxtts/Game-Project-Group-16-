package game.tests;
import com.RichTeam.game.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
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
        game.energyBar = mock(Image.class);
        game.dialogueBox = new DialogueBox(mock(Skin.class),false);
        game.dialogueBox.selectBox = mock(DialogueBox.SelectBox.class);
        eventManager = new EventManager(game);
    }
    @Test
    public void testPOIAssetsExists() {
        //Check POI sprites exist
        assertTrue("The sprite asset for the POI doesn't exist",
                Gdx.files.internal("../assets/East Campus/Textures/buildings.png").exists());
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
        // Test too early in morning case
        game.setSeconds(10);
        game.setEnergy(0);
        String[] args = new String[1];
        args[0] = "rch";
        String result = eventManager.ronCookeEvent(args);
        assertEquals("It's too early in the morning to meet your friends, go to bed!",result);
        assertEquals(1,(int)EventManager.daily.get("early_bird"));

        game.setSeconds(8*60);
        game.setEnergy(0);
        result = eventManager.ronCookeEvent(args);
        assertEquals("It's too early in the morning to meet your friends, go to bed!",result);
        assertEquals(2,(int)EventManager.daily.get("early_bird"));


        game.setSeconds(8*60);
        game.setEnergy(0);
        result = eventManager.ronCookeEvent(args);
        result = eventManager.ronCookeEvent(args);
        assertEquals("It's too early in the morning to meet your friends, go to bed!",result);
        assertEquals(4,(int)EventManager.daily.get("early_bird"));

        // Correct time + enough energy
        game.setSeconds(10*60+1);
        game.setEnergy(30);
        result = eventManager.ronCookeEvent(args);
        assertEquals("What do you want to chat about?",result);

        args = new String[2];
        args[0] = "rch";
        args[1] = "testing";
        game.setSeconds(10*60+1);
        result = eventManager.ronCookeEvent(args);
        assertTrue(result.equals("You talked about testing for 1 hours!")||
                result.equals("You talked about testing for 2 hours!")||
                result.equals("You talked about testing for 3 hours!"));
        assertTrue(0==game.getEnergy()||
                10==game.getEnergy()||
                20==game.getEnergy());
        assertTrue(11*60+1==game.getSeconds()||
                12*60+1==game.getSeconds()||
                13*60+1==game.getSeconds());
        // Recreational hours are initialised to 0
        // so after interacting with the Ron Cooke hub
        // it will randomly either be 1, 2, or 3
        assertTrue(game.getRecreationalHours()==1||
                game.getRecreationalHours()==2||
                game.getRecreationalHours()==3);

    }

    @Test
    public void testTreeEvent(){
        String result = eventManager.treeEvent();
        int current_energy = game.getEnergy();
        assertEquals(current_energy,game.getEnergy());
        assertEquals("The tree doesn't say anything back.",result);
    }
    @Test
    public void testChestEvent(){
        String result = eventManager.chestEvent();
        int current_energy = game.getEnergy();
        assertEquals(current_energy,game.getEnergy());
        assertEquals("Wow! These barrels is full of so many magical items! I wonder how they will help you out on your journey! Boy, this is an awfully long piece of text, I wonder if someone is testing something?\n...\n...\n...\nHow cool!",result);
    }
    @Test
    public void testObjectEvent(){
        String pineapple = "Pineapple";
        String result = eventManager.objectEvent(pineapple);
        int current_energy = game.getEnergy();
        assertEquals(current_energy,game.getEnergy());
        assertEquals("This is a " +  pineapple + "!", result);
    }
    @Test
    public void testPiazzaEvent(){
        game.setSeconds(5);
        game.setEnergy(0);
        String[] args = new String[1];
        args[0] = "piazza";
        String result = eventManager.piazzaEvent(args);
        assertEquals("food", game.getMeal());
        assertEquals("It's too early in the morning to eat food, go to bed!",result);
        assertEquals(1,(int)EventManager.daily.get("early_bird"));

        game.setSeconds(8*60);
        game.setEnergy(0);
        result = eventManager.piazzaEvent(args);
        assertEquals("It's too early in the morning to eat food, go to bed!",result);
        assertEquals(2,(int)EventManager.daily.get("early_bird"));


        game.setSeconds(8*60);
        game.setEnergy(0);
        result = eventManager.piazzaEvent(args);
        result = eventManager.piazzaEvent(args);
        assertEquals("It's too early in the morning to eat food, go to bed!",result);
        assertEquals(4,(int)EventManager.daily.get("early_bird"));

        // Test too tired case
        game.setSeconds(10*60+1);
        game.setEnergy(0);
        result = eventManager.piazzaEvent(args);
        assertEquals("You are too tired to eat right now!",result);

        // Correct time + enough energy
        game.setSeconds(10*60+1);
        game.setEnergy(30);
        result = eventManager.piazzaEvent(args);
        assertEquals("You took an hour to eat lunch at the Piazza!\nYou lost 10 energy!",result);
        assertEquals(20,game.getEnergy(),0.0001);
        assertEquals(11*60+1,game.getSeconds(),0.001);

        args = new String[2];
        args[0] = "piazza";
        args[1] = "testing";
        game.setSeconds(9*60+1);
        game.setEnergy(30);
        result = eventManager.piazzaEvent(args);
        assertEquals("You took an hour to eat breakfast at the Piazza!\nYou lost 10 energy!", result);
        assertEquals(20, game.getEnergy());
        assertEquals(10 * 60 + 1, game.getSeconds(), 0.0);
        // Checking different meal times
        game.setSeconds(9*60 +1);
        assertSame("breakfast", game.getMeal());
        game.setSeconds(12*60+1);
        assertSame("lunch", game.getMeal());
        game.setSeconds(18*60+1);
        assertSame("dinner", game.getMeal());
    }
    @Test
    public void testCompSciEvent(){
        game.setSeconds(10*60+1);
        game.setEnergy(10);
        String[] args = new String[2];
        args[0] = "comp_sci";
        args[1] = "2";
        String result = eventManager.compSciEvent(args);
        assertEquals("You don't have the energy to study for this long!",result);

        game.setSeconds(5);
        game.setEnergy(0);
        result = eventManager.compSciEvent(args);
        assertEquals("It's too early in the morning to study, go to bed!",result);
        assertEquals(1,(int)EventManager.daily.get("early_bird"));


        game.setSeconds(8*60);
        game.setEnergy(0);
        result = eventManager.compSciEvent(args);
        result = eventManager.compSciEvent(args);
        result = eventManager.compSciEvent(args);
        assertEquals("It's too early in the morning to study, go to bed!",result);
        assertEquals(4,(int)EventManager.daily.get("early_bird"));

        // Test too tired case
        game.setSeconds(10*60+1);
        game.setEnergy(0);
        result = eventManager.compSciEvent(args);
        assertEquals("You are too tired to study right now!",result);

        // Correct time + enough energy
        game.setSeconds(10*60+1);
        game.setEnergy(10);
        args[1] = "1";
        result = eventManager.compSciEvent(args);
        assertEquals("You studied for 1 hours!\nYou lost 10 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(11*60+1,game.getSeconds(),0.001);


        game.setSeconds(10*60+1);
        game.setEnergy(20);
        args[1] = "2";
        result = eventManager.compSciEvent(args);
        assertEquals("You studied for 2 hours!\nYou lost 20 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(12*60+1,game.getSeconds(),0.001);

        game.setSeconds(10*60+1);
        game.setEnergy(30);
        args[1] = "3";
        result = eventManager.compSciEvent(args);
        assertEquals("You studied for 3 hours!\nYou lost 30 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(13*60+1,game.getSeconds(),0.001);

        // Check to stop overstudying
        args = new String[1];
        args[0] = "comp_sci";
        game.setSeconds(10*60+1);
        game.setEnergy(40);
        result = eventManager.libraryEvent(args);
        assertEquals("You have already studied today!",result);
    }

    @Test
    public void testFlowersEvent(){
        game.setSeconds(10*60+1);
        game.setEnergy(10);
        String[] args = new String[2];
        args[0] = "flowers";
        args[1] = "2";
        String result = eventManager.flowersEvent(args);
        assertEquals("What if you fell asleep? You don't have the energy!",result);

        game.setSeconds(5);
        game.setEnergy(0);
        result = eventManager.flowersEvent(args);
        assertEquals("It's too early in the morning to smell the flowers, go to bed!",result);
        assertEquals(1,(int)EventManager.daily.get("early_bird"));


        game.setSeconds(8*60);
        game.setEnergy(0);
        result = eventManager.flowersEvent(args);
        result = eventManager.flowersEvent(args);
        result = eventManager.flowersEvent(args);
        assertEquals("It's too early in the morning to smell the flowers, go to bed!",result);
        assertEquals(4,(int)EventManager.daily.get("early_bird"));

        // Test too tired case
        game.setSeconds(10*60+1);
        game.setEnergy(0);
        result = eventManager.flowersEvent(args);
        assertEquals("You are too tired to smell the flowers right now!",result);

        // Correct time + enough energy
        game.setSeconds(10*60+1);
        game.setEnergy(10);
        args[1] = "1";
        result = eventManager.flowersEvent(args);
        assertEquals("You smelled the flowers for 1 hours!\nYou lost 10 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(11*60+1,game.getSeconds(),0.001);

        game.setSeconds(10*60+1);
        game.setEnergy(20);
        args[1] = "2";
        result = eventManager.flowersEvent(args);
        assertEquals("You smelled the flowers for 2 hours!\nYou lost 20 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(12*60+1,game.getSeconds(),0.001);

        game.setSeconds(10*60+1);
        game.setEnergy(30);
        args[1] = "3";
        result = eventManager.flowersEvent(args);
        assertEquals("You smelled the flowers for 3 hours!\nYou lost 30 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(13*60+1,game.getSeconds(),0.001);
    }

    @Test
    public void testShopEvent(){
        game.setSeconds(10*60+1);
        game.setEnergy(0);
        String[] args = new String[1];
        args[0] = "shop";
        String result = eventManager.shopEvent(args);
        assertEquals("You are too tired to eat right now!",result);

        game.setSeconds(5);
        game.setEnergy(0);
        result = eventManager.shopEvent(args);
        assertEquals("It's too early in the morning to eat food, go to bed!",result);
        assertEquals(1,(int)EventManager.daily.get("early_bird"));

        game.setSeconds(1*60);
        game.setEnergy(30);
        result = eventManager.shopEvent(args);
        result = eventManager.shopEvent(args);
        result = eventManager.shopEvent(args);
        assertEquals("It's too early in the morning to eat food, go to bed!",result);
        assertEquals(4,(int)EventManager.daily.get("early_bird"));

        // Test too tired case
        game.setSeconds(10*60+1);
        game.setEnergy(0);
        result = eventManager.shopEvent(args);
        assertEquals("You are too tired to eat right now!",result);

        // Correct time + enough energy
        game.setSeconds(9*60+1);
        game.setEnergy(10);
        result = eventManager.shopEvent(args);
        assertEquals("You took an hour to buy and eat breakfast at nisa!\nYou lost 10 energy!",result);
        assertEquals(0,game.getEnergy());
        assertEquals(10*60+1,game.getSeconds(),0.001);

        game.setSeconds(12*60+1);
        game.setEnergy(10);
        result = eventManager.shopEvent(args);
        assertEquals("You took an hour to buy and eat lunch at nisa!\nYou lost 10 energy!",result);
        assertEquals(0,game.getEnergy());
        assertEquals(13*60+1,game.getSeconds(),0.001);

        game.setSeconds(19*60+1);
        game.setEnergy(10);
        result = eventManager.shopEvent(args);
        assertEquals("You took an hour to buy and eat dinner at nisa!\nYou lost 10 energy!",result);
        assertEquals(0,game.getEnergy());
        assertEquals(20*60+1,game.getSeconds(),0.001);
    }

    @Test
    public void testGymEvent(){
        game.setSeconds(10*60+1);
        game.setEnergy(10);
        String[] args = new String[2];
        args[0] = "gym";
        args[1] = "2";
        String result = eventManager.gymEvent(args);
        assertEquals("You don't have the energy to go to work out right now! Head back to east!",result);

        game.setSeconds(5);
        game.setEnergy(0);
        result = eventManager.gymEvent(args);
        assertEquals("It's too early to work out, the gym's not open yet!",result);
        assertEquals(1,(int)EventManager.daily.get("early_bird"));


        game.setSeconds(8*60);
        game.setEnergy(0);
        result = eventManager.gymEvent(args);
        result = eventManager.gymEvent(args);
        result = eventManager.gymEvent(args);
        assertEquals("It's too early to work out, the gym's not open yet!",result);
        assertEquals(4,(int)EventManager.daily.get("early_bird"));

        // Test too tired case
        game.setSeconds(10*60+1);
        game.setEnergy(0);
        result = eventManager.gymEvent(args);
        assertEquals("You are too tired to go to the gym right now!",result);

        // Correct time + enough energy
        game.setSeconds(10*60+1);
        game.setEnergy(20);
        args[1] = "2";
        result = eventManager.gymEvent(args);
        assertEquals("You spent 2 hours working out at the gym!\nYou lost 20 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(12*60+1,game.getSeconds(),0.001);

        game.setSeconds(10*60+1);
        game.setEnergy(30);
        args[1] = "3";
        result = eventManager.gymEvent(args);
        assertEquals("You spent 3 hours working out at the gym!\nYou lost 30 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(13*60+1,game.getSeconds(),0.001);

        game.setSeconds(10*60+1);
        game.setEnergy(40);
        args[1] = "4";
        result = eventManager.gymEvent(args);
        assertEquals("You spent 4 hours working out at the gym!\nYou lost 40 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(14*60+1,game.getSeconds(),0.001);
    }

    @Test
    public void testDuckPondEvent(){
        game.setSeconds(10*60+1);
        game.setEnergy(10);
        String[] args = new String[2];
        args[0] = "duck_pond";
        args[1] = "2";
        String result = eventManager.duckPondEvent(args);
        assertEquals("You don't have the energy to feed the ducks right now! Head back to east!",result);

        game.setSeconds(5);
        game.setEnergy(0);
        result = eventManager.duckPondEvent(args);
        assertEquals("It's too early to feed the ducks, they're not hungry!",result);
        assertEquals(1,(int)EventManager.daily.get("early_bird"));

        game.setSeconds(8*60);
        game.setEnergy(0);
        result = eventManager.duckPondEvent(args);
        result = eventManager.duckPondEvent(args);
        result = eventManager.duckPondEvent(args);
        assertEquals("It's too early to feed the ducks, they're not hungry!",result);
        assertEquals(4,(int)EventManager.daily.get("early_bird"));

        // Test too tired case
        game.setSeconds(10*60+1);
        game.setEnergy(0);
        result = eventManager.duckPondEvent(args);
        assertEquals("You are too tired to feed the ducks right now!",result);

        // Correct time + enough energy
        game.setSeconds(10*60+1);
        game.setEnergy(10);
        args[1] = "1";
        result = eventManager.duckPondEvent(args);
        assertEquals("You spent 1 hours feeding the ducks!\nYou lost 10 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(11*60+1,game.getSeconds(),0.001);

        game.setSeconds(10*60+1);
        game.setEnergy(20);
        args[1] = "2";
        result = eventManager.duckPondEvent(args);
        assertEquals("You spent 2 hours feeding the ducks!\nYou lost 20 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(12*60+1,game.getSeconds(),0.001);

        game.setSeconds(10*60+1);
        game.setEnergy(30);
        args[1] = "3";
        result = eventManager.duckPondEvent(args);
        assertEquals("You spent 3 hours feeding the ducks!\nYou lost 30 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(13*60+1,game.getSeconds(),0.001);
    }

    @Test
    public void testLibraryEvent(){
        game.setSeconds(10*60+1);
        game.setEnergy(10);
        String[] args = new String[2];
        args[0] = "comp_sci";
        args[1] = "2";
        String result = eventManager.libraryEvent(args);
        assertEquals("You don't have the energy to study for this long! Head back to east!",result);

        game.setSeconds(5);
        game.setEnergy(0);
        result = eventManager.libraryEvent(args);
        assertEquals("It's too early in the morning to study, go to bed!",result);
        assertEquals(1,(int)EventManager.daily.get("early_bird"));


        game.setSeconds(8*60);
        game.setEnergy(0);
        result = eventManager.libraryEvent(args);
        result = eventManager.libraryEvent(args);
        result = eventManager.libraryEvent(args);
        assertEquals("It's too early in the morning to study, go to bed!",result);

        // Test too tired case
        game.setSeconds(10*60+1);
        game.setEnergy(0);
        result = eventManager.libraryEvent(args);
        assertEquals("You are too tired to study at the library right now!",result);

        // Correct time + enough energy
        game.setSeconds(10*60+1);
        game.setEnergy(20);
        args[1] = "2";
        result = eventManager.libraryEvent(args);
        assertEquals("You studied for 2 hours!\nYou lost 20 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(12*60+1,game.getSeconds(),0.001);

        game.setSeconds(10*60+1);
        game.setEnergy(30);
        args[1] = "3";
        result = eventManager.libraryEvent(args);
        assertEquals("You studied for 3 hours!\nYou lost 30 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(13*60+1,game.getSeconds(),0.001);

        game.setSeconds(10*60+1);
        game.setEnergy(40);
        args[1] = "4";
        result = eventManager.libraryEvent(args);
        assertEquals("You studied for 4 hours!\nYou lost 40 energy",result);
        assertEquals(0,game.getEnergy());
        assertEquals(14*60+1,game.getSeconds(),0.001);

        // Check to stop overstudying
        args = new String[1];
        args[0] = "comp_sci";
        game.setSeconds(10*60+1);
        game.setEnergy(40);
        result = eventManager.libraryEvent(args);
        assertEquals("You have already studied today!",result);
    }

    @Test
    public void testPostSleepEnergy() {
        String[] args = new String[]{"fadefromblack"};
        game.dialogueBox = mock(DialogueBox.class);
        game.setEnergy(0);
        game.setSeconds(10*60);
        game.dayLabel = mock(Label.class);
        eventManager.sleepAction(10,60*10);
        assertEquals("The player did not regain full energy after sleeping",100,
                game.getEnergy());
    }

    @Test
    public void testPostSleepTime() {
        String[] args = new String[]{"fadefromblack"};
        game.dialogueBox = mock(DialogueBox.class);
        game.setEnergy(0);
        game.setSeconds(23*60);
        game.dayLabel = mock(Label.class);
        eventManager.sleepAction(9,60*9);
        assertEquals(8*60,game.getSeconds(),0.001);
    }

    @Test
    public void testResetDaily() {
        String[] args = new String[]{"fadefromblack"};
        game.dialogueBox = mock(DialogueBox.class);
        game.setEnergy(0);
        game.setSeconds(23*60);
        game.dayLabel = mock(Label.class);
        EventManager.daily.put("early_bird",1);
        eventManager.sleepAction(9,60*9);
        assertEquals(0,EventManager.daily.get("early_bird"),0.001);
    }

    @Test
    public void testUpdateStreak() {
        String[] args = new String[]{"fadefromblack"};
        game.dialogueBox = mock(DialogueBox.class);
        game.setEnergy(0);
        game.setSeconds(23*60);
        game.dayLabel = mock(Label.class);
        EventManager.daily.put("studying",1);
        eventManager.sleepAction(9,60*9);
        assertEquals(1,EventManager.streaks.get("studying"),0.001);
    }
}
