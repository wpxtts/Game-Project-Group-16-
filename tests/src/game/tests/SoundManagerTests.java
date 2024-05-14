package game.tests;
import com.RichTeam.game.SoundManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.RichTeam.game.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void testSetMusicVolume(){
        SoundManager soundManager = new SoundManager();

        soundManager.overworldMusic = mock(Music.class);
        soundManager.menuMusic = mock(Music.class);

        // Test valid volume values
        soundManager.setMusicVolume(0.5f);
        assertEquals(0.5f, soundManager.getMusicVolume(),0.0001);
        // Verify checks that the correct .setVolume() call was made
        verify(soundManager.overworldMusic).setVolume(0.5f);
        verify(soundManager.menuMusic).setVolume(0.5f);

        // Test edge cases
        soundManager.setMusicVolume(0.0f);
        assertEquals(0.0f, soundManager.getMusicVolume(),0.0001);
        verify(soundManager.overworldMusic).setVolume(0.0f);
        verify(soundManager.menuMusic).setVolume(0.0f);

        soundManager.setMusicVolume(1f);
        assertEquals(1f, soundManager.getMusicVolume(),0.0001);
        verify(soundManager.overworldMusic).setVolume(1f);
        verify(soundManager.menuMusic).setVolume(1f);

        //Note: We can't test to make sure the overworldMusic and menuMusic
        //has updated volumes because overworldMusic and menuMusic are mockups
        //and so don't actually get updated.

        assertThrows(IllegalArgumentException.class,()->soundManager.setMusicVolume(-0.1f));
        assertThrows(IllegalArgumentException.class,()->soundManager.setMusicVolume(1.1f));
    }

    @Test
    public void testSetSFXVolume(){
        SoundManager soundManager = new SoundManager();
        // Test valid volume values
        soundManager.setSfxVolume(0.5f);
        assertEquals(0.5f, soundManager.getSfxVolume(),0.0001);
        // Test edge cases
        soundManager.setSfxVolume(0.0f);
        assertEquals(0.0f, soundManager.getSfxVolume(),0.0001);
        soundManager.setSfxVolume(1f);
        assertEquals(1f, soundManager.getSfxVolume(),0.0001);

        assertThrows(IllegalArgumentException.class,()->soundManager.setSfxVolume(-0.1f));
        assertThrows(IllegalArgumentException.class,()->soundManager.setSfxVolume(1.1f));
    }

    @Test
    public void testProcessTimers(){
        SoundManager soundManager = new SoundManager();

        soundManager.setFootstepTimer(1.0f);
        soundManager.processTimers(0.5f);
        assertEquals(0.5f,soundManager.getFootstepTimer(),0.0001);

        soundManager.setFootstepTimer(1.0f);
        soundManager.processTimers(1.f);
        assertEquals(0.0f,soundManager.getFootstepTimer(),0.0001);

        soundManager.setFootstepTimer(1.0f);
        soundManager.processTimers(1.5f);
        assertEquals(0.0f,soundManager.getFootstepTimer(),0.0001);

        assertThrows(IllegalArgumentException.class,()->soundManager.processTimers(-0.1f));
    }

    @Test
    public void testPlayFootstep(){
        SoundManager soundManager = new SoundManager();
        soundManager.setFootstepTimer(0.5f);
        soundManager.footstepBool = false;
        soundManager.footstep1 = mock(Sound.class);
        soundManager.footstep2 = mock(Sound.class);

        // Before anytime has passed neither footstep should be played
        verify(soundManager.footstep1,never()).play(soundManager.getSfxVolume());
        verify(soundManager.footstep2,never()).play(soundManager.getSfxVolume());

        //When 0.5f of time goes past then footstep1 should be played
        //but footstep 2 should not be played
        soundManager.processTimers(0.5f);
        soundManager.playFootstep();
        verify(soundManager.footstep1, Mockito.times(1)).play(soundManager.getSfxVolume());
        verify(soundManager.footstep2, never()).play(soundManager.getSfxVolume());

        //When another 0.5f of time goes past then footstep2 should be played
        //but footstep1 shouldn't be played
        soundManager.processTimers(0.5f);
        soundManager.playFootstep();
        verify(soundManager.footstep1, Mockito.times(1)).play(soundManager.getSfxVolume());
        verify(soundManager.footstep2, Mockito.times(1)).play(soundManager.getSfxVolume());
    }
}