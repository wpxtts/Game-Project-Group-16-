package com.RichTeam.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.io.FileNotFoundException;

/**
 * A class that is initially created by DesktopLauncher, loads consistent files at the start of the game and initialises lots of important classes.
 * Loads the map, ui skin, text files and makes sound manager and more
 */
public class HustleGame extends Game {
	public SpriteBatch batch;
	public int WIDTH;
	public int HEIGHT;
	public Skin skin;
	public static TiledMap map;
	public static TiledMap town_map;
	public static TiledMap current_map;
	private static boolean eastActive = true;
	public String credits, tutorialText;
	public static GameScreen gameScreen;
	public MenuScreen menuScreen;
	public ShapeRenderer shapeRenderer;
	public SoundManager soundManager;
	public Stage blueBackground;
	public int[] backgroundLayers, foregroundLayers, objectLayers;
	public static int mapSquareSize;
	public static float mapScale;
	public MapProperties mapProperties;

	public static final String skinPath = "Interface/BlockyInterface.json";
	public static final String mapPath = "East Campus/east_campus.tmx";
	public static final String townMapPath ="East Campus/town_map.tmx";
	public static final String whiteSquarePath = "Sprites/white_square.png";
	public static final String creditsPath = "Text/credits.txt";
	public static final String tutorialTextPath = "Text/tutorial_text.txt";
	/**
	 * A class to initialise a lot of the assets required for the game, including the map, sound and UI skin.
	 * A instance of this object should be shared to most screens to allow resources to be shared and disposed of
	 * correctly.
	 * Should be created in DesktopLauncher,
	 *
	 * @param width Width of the window
	 * @param height Height of the window
	 */
	public HustleGame (int width, int height) {
		WIDTH = width;
		HEIGHT = height;
	}

	/**
	 * Loads resources used throughout the game.
	 * Creates a new spritebatch
	 * Loads the UI skin to use
	 * Loads the map and configures which layers are background, foreground and object layers
	 * Loads a shape renderer for debug options
	 * Loads a sound manager to play sounds
	 * Loads credit and tutorial texts
	 * Creates a stage with a blue background for screens to use
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal(skinPath));
		// Map
		map = new TmxMapLoader().load(mapPath);
		town_map = new TmxMapLoader().load(townMapPath);
		if (getMap() == null){
			current_map = map;
		} else {
			current_map = getMap();
		}
		mapProperties = current_map.getProperties();

		// Define background, foreground and object layers
		// IMPORTANT: CHANGE THESE WHEN UPDATING THE LAYERS IN YOUR EXPORTED MAP FROM TILED
		// Bottom most layer on 'layers' tab is 0
		backgroundLayers = new int[]{0, 1, 2, 3, 4, 5, 6}; // Rendered behind player
		foregroundLayers = new int[]{7}; // Rendered in front of player
		objectLayers = new int[]{8}; // Rectangles for the player to collide with

		mapSquareSize = mapProperties.get("tilewidth", Integer.class);
		mapScale = 70f;

		shapeRenderer = new ShapeRenderer();
		soundManager = new SoundManager();

		// Make a stage with a blue background that any screen can draw
		Image blueImage = new Image(new Texture(Gdx.files.internal(whiteSquarePath)));
		blueImage.setColor(0.53f, 0.81f, 0.92f, 1);
		blueImage.setName("blue image");
		blueBackground = new Stage();
		blueBackground.addActor(blueImage);

        try {
            credits = readTextFile(creditsPath);
			tutorialText = readTextFile(tutorialTextPath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        this.setScreen(new MenuScreen(this,true));

		if(!Gdx.files.local("leaderboard.csv").exists()) {
			FileHandle file = Gdx.files.local("leaderboard.csv");// Use Gdx.files.local for writing
			file.writeString("Player Name,Score\n", true);
		}
	}

	/**
	 * Changes rendered map when player interacts with a bus stop
	 */
	public static void setMap() {
		if (eastActive) {
			current_map = town_map;
			eastActive = false;
		} else {
			current_map = map;
			eastActive = true;
		}
		float unitScale = HustleGame.mapScale / HustleGame.mapSquareSize;
		gameScreen.clearPlayerObjects();
		gameScreen.loadPlayerObjects(unitScale);
		gameScreen.mapRenderer.setMap(current_map);
	}

	/**
	 * Returns map currently being rendered
	 * @return current_map the map being rendered
	 */
	public static TiledMap getMap() {
		return current_map;
	}

	/**
	 * Very important, renders the game, remove super.render() to get a black screen
	 */
	@Override
	public void render () {
		super.render();
	}

	/**
	 * Disposes of elements that are loaded at the start of the game
	 */
	@Override
	public void dispose () {
		batch.dispose();
		blueBackground.dispose();
		skin.dispose();
		current_map.dispose();
		shapeRenderer.dispose();
		soundManager.dispose();
	}

	/**
	 * Reads and returns text read from the provided text file path
	 * @param filepath The path to the text file
	 * @return The contents of the file as a String
	 */
	public String readTextFile(String filepath) throws FileNotFoundException {
		FileHandle file = Gdx.files.internal(filepath);

		if (!file.exists()) {
			throw new FileNotFoundException("File not found");
		} else {
			return file.readString();
		}

	}
}
