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
public class GameScreenTests {
    private GameScreen game;
    private HustleGame hGame;
    private EventManager eventManager;
    @Before
    public void setUp(){
        eventManager = mock(EventManager.class);
        int avatarChoice = 1;
        boolean draw = true;
        game = new GameScreen(hGame, avatarChoice, draw);
    }
    @Test
    public void testPOIAssetsExists() {
        //Check POI sprites exist
    }
}