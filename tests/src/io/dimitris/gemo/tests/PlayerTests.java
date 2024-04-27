package io.dimitris.gemo.tests;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.skloch.game.*;
import org.junit.Test;
import org.junit.runner.RunWith;

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
}