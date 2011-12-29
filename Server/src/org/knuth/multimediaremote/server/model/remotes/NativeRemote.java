package org.knuth.multimediaremote.server.model.remotes;

/**
 * Maps the {@code Remote}-methods to a native
 *  interface for JNI usage.
 */
class NativeRemote implements Remote {

    /**
     * Native mapping of the {@code pausePlay()}- method.
     * @see org.knuth.multimediaremote.server.model.remotes.Remote#pausePlay()
     */
    private native void nPausePlay();
    public void pausePlay() {
        nPausePlay();
    }

    /**
     * Native mapping of the {@code stop()}- method.
     * @see org.knuth.multimediaremote.server.model.remotes.Remote#stop()
     */
    private native void nStop();
    public void stop() {
        nStop();
    }

    /**
     * Native mapping of the {@code nextTrack()}- method.
     * @see org.knuth.multimediaremote.server.model.remotes.Remote#nextTrack()
     */
    private native void nNextTrack();
    public void nextTrack() {
        nNextTrack();
    }

    /**
     * Native mapping of the {@code previousTrack()}- method.
     * @see org.knuth.multimediaremote.server.model.remotes.Remote#previousTrack()
     */
    private native void nPreviousTrack();
    public void previousTrack() {
        nPreviousTrack();
    }

    /**
     * Native mapping of the {@code volUp()}- method.
     * @see org.knuth.multimediaremote.server.model.remotes.Remote#volUp()
     */
    private native void nVolUp();
    public void volUp() {
        nVolUp();
    }

    /**
     * Native mapping of the {@code volDown()}- method.
     * @see org.knuth.multimediaremote.server.model.remotes.Remote#volDown()
     */
    private native void nVolDown();
    public void volDown() {
        nVolDown();
    }

    /**
     * Native mapping of the {@code mute()}- method.
     * @see org.knuth.multimediaremote.server.model.remotes.Remote#mute()
     */
    private native void nMute();
    public void mute() {
        nMute();
    }
}
