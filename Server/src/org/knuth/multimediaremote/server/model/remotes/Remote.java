package org.knuth.multimediaremote.server.model.remotes;

/**
 * Represents a Remote-Interface which uses the Native System-
 *  library's to pass the action to the Multimedia Player.
 */
public interface Remote {

    public void play();

    public void pause();
}
