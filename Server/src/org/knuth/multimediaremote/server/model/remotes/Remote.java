package org.knuth.multimediaremote.server.model.remotes;

/**
 * Represents a Remote-Interface which uses the Native System-
 *  library's to pass the action to the Multimedia Player.
 */
public interface Remote {

    /**
     * Plays (when stopped or paused) or pauses (when
     *  currently playing) the current song.
     */
    public void pausePlay();

    /**
     * Stops the current song and jumps to the start.
     *  It can be played again by using {@code pausePlay()}.
     */
    public void stop();

    /**
     * Jumps to the next track and starts playing (if a
     *  song is currently playing) or waits at the tracks
     *  start (if no song is currently playing).
     */
    public void nextTrack();

    /**
     * Jumps to the previous track and starts playing (if a
     *  song is currently playing) or waits at the tracks
     *  start (if no song is currently playing).
     */
    public void previousTrack();

    /**
     * Turns the computers master-volume one step higher.
     */
    public void volUp();

    /**
     * Turns the computers master-volume one step lower.
     */
    public void volDown();

    /**
     * Mutes the computers master-volume.
     */
    public void mute();
}
