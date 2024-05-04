package io.dimitris.gemo.tests;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.skloch.game.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(GdxTestRunner.class)
public class EventManagerTests {
    private GameScreen game;
    private EventManager eventManager;
    @Before
    public void setUp(){
        game = mock(GameScreen.class);
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
        for (Map.Entry<String, Integer> entry : eventManager.activityEnergies.entrySet()) {
            String activity = entry.getKey();
            assertEquals("The activity does not have the correct energy set (10)",
                    activity, 10);
        }
    }

    @Test
    public void testObjectInteractionResponsesExist() {
        //Check activity energies exist
        for (Map.Entry<String, String> object : eventManager.objectInteractions.entrySet()) {
            String response = object.getKey();
            assertNotNull("Ths object does not have an interaction response",
                    response);
        }
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

    //add fade to black test?
    //add events trigger test?
//    @Test
//    public void testEventTriggered() {
//        //event trigger
//        assertTrue("This event is not able to be triggered",
//                )
//    }

    @Test
    public void testEventUsesEnergy() {
        int current_energy = game.getEnergy();
        int energyCost = 100;
        String[] args = new String[]{"flowers-1", "flowers-2", "flowers-3"};
        int hours = Integer.parseInt(args[1]);
        eventManager.flowersEvent(args);
        assertEquals("The streak for smelling flowers was not increased",
                current_energy, game.getEnergy() - (energyCost * hours));
    }

    @Test
    public void testEventPassesTime() {
        float current_time = game.getSeconds();
        String[] args = new String[]{"flowers-1", "flowers-2", "flowers-3"};
        int hours = Integer.parseInt(args[1]);
        eventManager.flowersEvent(args);
        assertEquals("The streak for smelling flowers was not increased",
                current_time, current_time - (hours * 60));
    }

    @Test
    public void testStreaksIncrease() {
        int current_streak = eventManager.streaks.get("flowers");
        String[] args = new String[]{"flowers-1", "flowers-2", "flowers-3"};
        eventManager.flowersEvent(args);
        assertEquals("The streak for smelling flowers was not increased",
                Integer.valueOf((current_streak + 1)), eventManager.streaks.get("flowers"));
    }

    @Test
    public void testEventBlockedTired() {
        String[] args = new String[]{"flowers-1", "flowers-2", "flowers-3"};
        eventManager.flowersEvent(args);
        game.setEnergy(0);
        DialogueBox dialogueBox = mock(DialogueBox.class);
        assertTrue(dialogueBox.isVisible());
    }

    @Test
    public void testEventBlockedLate() {
        game.passTime(9*60);
        String[] args = new String[]{"flowers-1", "flowers-2", "flowers-3"};
        eventManager.flowersEvent(args);
        DialogueBox dialogueBox = mock(DialogueBox.class);
        assertFalse(dialogueBox.isVisible());
    }

    @Test
    public void testCatchupUsable() {
        int daily_study = 2;
        String[] args = new String[]{"fadefromblack"};
        eventManager.compSciEvent(args);
        assertTrue(eventManager.catchup_used);
    }

    @Test
    public void testPostSleepEnergy() {
        String[] args = new String[]{"fadefromblack"};
        eventManager.accomEvent(args);
        assertEquals("The player did not regain full energy after sleeping",
                game.getEnergy(), 100);
    }

    @Test
    public void testPostSleepTime() {
        String[] args = new String[]{"fadefromblack"};
        eventManager.accomEvent(args);
        assertEquals("The player did not regain full energy after sleeping",
                game.getSeconds(), 8*60);
    }

    @Test
    public void testPostSleepStudyHours() {
        String[] args = new String[]{"comp_sci-1", "comp_sci-2", "comp_sci-3"};
        eventManager.accomEvent(args);
        assertEquals("The player did not regain full energy after sleeping",
                eventManager.daily_study, 0);
    }
















    @Test
    public void testPlayerMovement(){
        Player player = new Player("avatar1");
        // Sprite starts at (0,0)

        //Check left movement
        assertEquals(player.movePlayer(Player.left,10, 1),Player.left);
        assertEquals(player.getX(),-10,0.0001);

        //Check right movement
        assertEquals(player.movePlayer(Player.right,10, 1),Player.right);
        assertEquals(player.getX(),0,0.0001);

        //Check up movement
        assertEquals(player.movePlayer(Player.up,10, 1),Player.up);
        assertEquals(player.getY(),10,0.0001);

        //Check down movement
        assertEquals(player.movePlayer(Player.down,10, 1),Player.down);
        assertEquals(player.getY(),0,0.0001);

        //Check invalid direction
        Exception exception = assertThrows(IllegalArgumentException.class,()->player.movePlayer(1323,10,1));
    }
    @Test
    public void testCollisionCheck(){
        Player player = new Player("avatar1");
        ArrayList<GameObject> collidingObjects = new ArrayList<>();
        collidingObjects.add(new GameObject(player.feet.getX() -100, player.feet.getY()-100, 2000, 2000));
        // Edge case
        collidingObjects.add(new GameObject(player.feet.getX() -10,player.feet.getY()-10,11,11));

        ArrayList<GameObject> notCollidingObjects = new ArrayList<>();
        notCollidingObjects.add(new GameObject(player.feet.getX()+200,player.feet.getY()+200,10,10));
        //
        notCollidingObjects.add(new GameObject(player.feet.getX()-10,player.feet.getY()-10,9,9));

        player.addCollidable(collidingObjects.get(0));
        player.addCollidable(collidingObjects.get(1));
        player.addCollidable(notCollidingObjects.get(0));
        player.addCollidable(notCollidingObjects.get(1));

        ArrayList<GameObject> detectedCollisions = player.collisionCheck();
        assertEquals(detectedCollisions, collidingObjects);
        assertFalse(detectedCollisions.contains(notCollidingObjects.get(0)));
        assertFalse(detectedCollisions.contains(notCollidingObjects.get(1)));
    }

    @Test
    public void testMovePlayerWithinBounds(){
        Player player = new Player("avatar1");
        player.setBounds(new Rectangle(0,0,100,100));

        // Player within bounds
        player.setX(10);
        player.setY(10);
        player.movePlayerWithinBounds();
        assertEquals(10,player.getX(),0.0001);
        assertEquals(10,player.getY(),0.0001);

        //Player out of bounds by going under X and Y limits
        player.setX(-1000);
        player.setY(-1000);
        player.movePlayerWithinBounds();
        assertEquals(-4*player.scale,player.getX(),0.0001);
        assertEquals(0,player.getY(),0.0001);

        // Player out of bounds by going over X and Y limits
        player.setX(1000);
        player.setY(1000);
        player.movePlayerWithinBounds();
        assertEquals(100-player.feet.getWidth()-4*player.scale,player.getX(),0.0001);
        assertEquals(100-player.feet.getHeight(),player.getY(),0.0001);
    }

    @Test
    public void testFindClosestInteractableObject(){
        Player player = new Player("avatar1");
        GameObject closestObject = new GameObject(player.getX()-1,player.getY()-1,1,1);
        GameObject midObject = new GameObject(player.getX()-2,player.getY()-2,1,1);
        GameObject furthestObject = new GameObject(player.getX()-3,player.getY()-3,1,1);
        GameObject outOfRangeObject = new GameObject(player.getX()-1000, player.getY()-1000,1,1);
        GameObject unInteractableObject = new GameObject(player.getX()-1,player.getY()-1,1,1);

        closestObject.put("text","hello");
        midObject.put("text","hello");
        furthestObject.put("text","hello");
        outOfRangeObject.put("text","hello");

        assertNull(player.findClosestInteractableObject());
        player.addCollidable(unInteractableObject);
        assertNull(player.findClosestInteractableObject());
        player.addCollidable(outOfRangeObject);
        assertNull(player.findClosestInteractableObject());

        player.addCollidable(midObject);
        assertEquals(midObject,player.findClosestInteractableObject());
        player.addCollidable(furthestObject);
        assertEquals(midObject,player.findClosestInteractableObject());
        player.addCollidable(closestObject);
        assertEquals(closestObject,player.findClosestInteractableObject());
    }

    @Test
    public void testNearObject(){
        Player player = new Player("avatar1");
        GameObject closeObject = new GameObject(player.getX()-1,player.getY()-1,1,1);
        GameObject outOfRangeObject = new GameObject(player.getX()-1000, player.getY()-1000,1,1);
        GameObject unInteractableObject = new GameObject(player.getX()-1,player.getY()-1,1,1);

        closeObject.put("text","hello");
        outOfRangeObject.put("text","hello");

        // We run findClosestInteractableObject as by the Javadoc
        // for the nearObject method, it needs to be run before
        // nearObject is run.
        player.findClosestInteractableObject();
        assertFalse(player.nearObject());

        player.addCollidable(unInteractableObject);
        player.findClosestInteractableObject();
        assertFalse(player.nearObject());

        player.addCollidable(outOfRangeObject);
        player.findClosestInteractableObject();
        assertFalse(player.nearObject());

        player.addCollidable(closeObject);
        player.findClosestInteractableObject();
        assertTrue(player.nearObject());
    }
    @Test
    public void testGetClosestObject(){
        Player player = new Player("avatar1");
        GameObject closestObject = new GameObject(player.getX()-1,player.getY()-1,1,1);
        GameObject midObject = new GameObject(player.getX()-2,player.getY()-2,1,1);
        GameObject furthestObject = new GameObject(player.getX()-3,player.getY()-3,1,1);
        GameObject outOfRangeObject = new GameObject(player.getX()-1000, player.getY()-1000,1,1);
        GameObject unInteractableObject = new GameObject(player.getX()-1,player.getY()-1,1,1);

        closestObject.put("text","hello");
        midObject.put("text","hello");
        furthestObject.put("text","hello");
        outOfRangeObject.put("text","hello");

        player.findClosestInteractableObject();
        assertNull(player.getClosestObject());
        player.addCollidable(unInteractableObject);
        player.findClosestInteractableObject();
        assertNull(player.getClosestObject());
        player.addCollidable(outOfRangeObject);
        player.findClosestInteractableObject();
        assertNull(player.getClosestObject());

        player.addCollidable(midObject);
        player.findClosestInteractableObject();
        assertEquals(midObject,player.getClosestObject());
        player.addCollidable(furthestObject);
        player.findClosestInteractableObject();
        assertEquals(midObject,player.getClosestObject());
        player.addCollidable(closestObject);
        player.findClosestInteractableObject();
        assertEquals(closestObject,player.getClosestObject());
    }

    @Test
    public void testIsMoving(){
        Player player = new Player("avatar1");
        player.moving = false;
        assertFalse(player.isMoving());
        player.moving = true;
        assertTrue(player.isMoving());
    }

    @Test
    public void testSetMoving(){
        Player player = new Player("avatar1");
        player.setMoving(false);
        assertFalse(player.isMoving());
        player.setMoving(true);
        assertTrue(player.isMoving());
    }

    @Test
    public void getPosAsVec3(){
        Player player = new Player("avatar1");
        player.setX(10);
        player.setY(10);
        assertEquals(new Vector3(10,10,0),player.getPosAsVec3());
    }

    @Test
    public void testSetX(){
        Player player = new Player("avatar1");
        // Set up values
        player.scale = 4;
        player.eventHitbox.setWidth(20);
        player.sprite.setWidth(10);

        player.setX(100);

        // We check if after setting the X value, we get the values we expect
        // for each different metric
        assertEquals(100,player.sprite.getX(),0.0001);
        assertEquals(116,player.feet.getX(),0.0001);
        assertEquals(95,player.eventHitbox.getX(),0.0001);
        assertEquals(105,player.centreX,0.0001);
    }

    @Test
    public void testSetY(){
        Player player = new Player("avatar1");
        player.eventHitbox.setHeight(20);
        player.sprite.setHeight(10);

        player.setY(100);
        assertEquals(100,player.sprite.getY(),0.0001);
        assertEquals(100,player.feet.getY(),0.0001);
        assertEquals(95,player.eventHitbox.getY(),0.0001);
        assertEquals(105,player.centreY,0.0001);
    }

    // Note: We do not test methods like distanceFrom or
    // recalcCentre as they are private. They are tested by testing
    // the public methods that use them.
}