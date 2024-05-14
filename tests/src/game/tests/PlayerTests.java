package game.tests;
import com.RichTeam.game.GameObject;
import com.RichTeam.game.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.RichTeam.game.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class PlayerTests {
    @Test
    public void testPlayerAssetsExists() {
        assertTrue("The atlas asset for player doesn't exist",
                Gdx.files.internal(Player.atlasConfig).exists());
        assertTrue("The sprite asset for the player doesn't exist",
                Gdx.files.internal(Player.playerSprite).exists());
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