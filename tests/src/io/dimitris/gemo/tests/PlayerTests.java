package io.dimitris.gemo.tests;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.skloch.game.*;
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
}