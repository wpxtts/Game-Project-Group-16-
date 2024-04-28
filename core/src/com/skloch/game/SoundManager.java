package com.skloch.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

/**
 * A class handling loading, playing and disposing of sounds.
 */
public class SoundManager implements Disposable {
    public Music overworldMusic, menuMusic;

    public static final String overworldMusicPath = "../assets/Music/OverworldMusic.mp3";
    public static final String menuMusicPath = "../assets/Music/Streetlights.ogg";
    public static final String footstep1Path = "../assets/Sounds/footstep1 grass.ogg";
    public static final String footstep2Path = "../assets/Sounds/footstep2 grass.ogg";
    public static final String pauseSoundPath = "../assets/Sounds/Pause01.wav";
    public static final String dialogueOpenSoundPath = "../assets/Sounds/DialogueOpen.wav";
    public static final String dialogueOptionSoundPath = "../assets/Sounds/DialogueOption.wav";
    public static final String buttonSoundPath = "../assets/Sounds/Button.wav";
    public Sound footstep1, footstep2;
    public boolean footstepBool;
    private float footstepTimer;
    private float sfxVolume = 0.8f, musicVolume = 0.8f;
    private Sound pauseSound, dialogueOpenSound, dialogueOptionSound, buttonSound;

    /**
     * A class to handle playing sounds in the game, handles loading and playing of music and sounds
     * so a GameScreen can just call "play overworld music" without needing to know the track title.
     * Also handles disposing sounds and music
     */
    public SoundManager () {
        // Load music
        overworldMusic = Gdx.audio.newMusic(Gdx.files.internal(overworldMusicPath));
        overworldMusic.setLooping(true);
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal(menuMusicPath));
        menuMusic.setLooping(true);

        // Load SFX
        footstep1 = Gdx.audio.newSound(Gdx.files.internal(footstep1Path));
        footstep2 = Gdx.audio.newSound(Gdx.files.internal(footstep2Path));

        pauseSound = Gdx.audio.newSound(Gdx.files.internal(pauseSoundPath));
        dialogueOpenSound = Gdx.audio.newSound(Gdx.files.internal(dialogueOpenSoundPath));
        dialogueOptionSound = Gdx.audio.newSound(Gdx.files.internal(dialogueOptionSoundPath));
        buttonSound = Gdx.audio.newSound(Gdx.files.internal(buttonSoundPath));
    }

    /**
     * Sets the volume of the music
     * @param volume
     */
    public void setMusicVolume (float volume) {
        if(volume<0.0f || volume>1.0f){
            throw new IllegalArgumentException("Volume must be between 0.0 and 1.0");
        }
        this.musicVolume = volume;
        overworldMusic.setVolume(musicVolume);
        menuMusic.setVolume(musicVolume);
    }

    /**
     * Sets the sound effect volume
     * @param volume
     */
    public void setSfxVolume (float volume) {
        if(volume<0.0f || volume>1.0f){
            throw new IllegalArgumentException("Volume must be between 0.0 and 1.0");
        }
        this.sfxVolume = volume;
    }

    /**
     * A sound for when the pause menu appears
     */
    public void playPauseSound() {pauseSound.play(sfxVolume);}

    /**
     * A sound for when the dialogue box appears
     */
    public void playDialogueOpen() {dialogueOpenSound.play(sfxVolume);}

    /**
     * A sound for when the arrow in the selectBox is moved
     */
    public void playDialogueOption() {dialogueOptionSound.play(sfxVolume);}

    /**
     * A sound for when a button is pressed
     */
    public void playButton() {buttonSound.play(sfxVolume);}

    /**
     * Plays the music for the overworld (main game)
     */
    public void playOverworldMusic() {overworldMusic.play();}

    /**
     * Stops the music for the overworld
     */
    public void stopOverworldMusic() {overworldMusic.stop();}

    /**
     * Plays the music for the menu
     */
    public void playMenuMusic() {menuMusic.play();}

    /**
     * Stops the music for the menu
     */
    public void stopMenuMusic() {menuMusic.stop();}

    /**
     * Pauses the overworld music, so it can be resumed from this point later
     */
    public void pauseOverworldMusic() {overworldMusic.pause();}

    /**
     * @return The current music volume
     */
    public float getMusicVolume() {return musicVolume;}

    /**
     * @return The current sound effect volume
     */
    public float getSfxVolume() {return sfxVolume;}

    /**
     * Updates the timers for sounds that repeat regularly, needs to be called every render cycle.
     * Specifically handles triggering footsteps when the player is moving
     * @param delta Time passed since the last render
     */

    public float getFootstepTimer(){
        return footstepTimer;
    }

    public void setFootstepTimer(float time){
        footstepTimer = time;
    }

    public void processTimers(float delta) {
        // Decrements timers for any recurring sounds, like footsteps
        // Events that make these sounds can then check that their specific timer is zero and play a noise
        if(delta<0.0f){
            throw new IllegalArgumentException("delta must be 0 or greater");
        }
        footstepTimer -= delta;
        if (footstepTimer < 0) {
            footstepTimer = 0;
        }
    }

    /**
     * Plays an alternating footstep sound when the player is moving and the footstepTimer variable has hit zero.
     * Uses two different SFX to sound more realistic and to allow the timing to be configured
     */
    public void playFootstep() {
        // If it is time to play a footstep, play one
        if (footstepTimer <= 0) {
            footstepTimer = 0.5f; // Delay between each footstep sound, increase to have slower steps
            if (footstepBool == false) {
                footstep1.play(sfxVolume);
                footstepBool = true;
            } else {
                footstep2.play(sfxVolume);
                footstepBool = false;
            }
        }
    }


    /**
     * Disposes of all music and sound effects
     */
    @Override
    public void dispose () {
        overworldMusic.dispose();
        menuMusic.dispose();
        footstep1.dispose();
        footstep2.dispose();
        pauseSound.dispose();
        dialogueOpenSound.dispose();
        dialogueOptionSound.dispose();
    }
}
