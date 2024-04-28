package io.dimitris.gemo.tests;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.skloch.game.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(GdxTestRunner.class)
public class SoundManagerTests {
    @Test
    public void testMusicAssetsExists() {
        assertTrue("The overworld music doesn't exist",
                Gdx.files.internal(SoundManager.overworldMusicPath).exists());
        assertTrue("The menu music doesn't exist",
                Gdx.files.internal(SoundManager.menuMusicPath).exists());
    }

    @Test
    public void testSoundEffectsExist(){
        assertTrue("The footstep 1 sound doesn't exist",
                Gdx.files.internal(SoundManager.footstep1Path).exists());
        assertTrue("The footstep 2 sound doesn't exist",
                Gdx.files.internal(SoundManager.footstep2Path).exists());
        assertTrue("The pause sound doesn't exist",
                Gdx.files.internal(SoundManager.pauseSoundPath).exists());
        assertTrue("The dialogue open sound doesn't exist",
                Gdx.files.internal(SoundManager.dialogueOpenSoundPath).exists());
        assertTrue("The dialogue option sound doesn't exist",
                Gdx.files.internal(SoundManager.dialogueOptionSoundPath).exists());
        assertTrue("The button sound doesn't exist",
                Gdx.files.internal(SoundManager.buttonSoundPath).exists());
    }
}