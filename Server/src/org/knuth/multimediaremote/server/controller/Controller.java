package org.knuth.multimediaremote.server.controller;

import org.knuth.multimediaremote.server.model.remotes.DetermineOS;
import org.knuth.multimediaremote.server.model.remotes.Remote;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author Lukas Knuth
 * @version 1.0
 * Gets queried by the servers and performs the given
 *  action.
 */
public enum Controller {

    /** The instance to work with */
    INSTANCE;

    /**
     * Possible actions that the Controller does
     *  understand.
     */
    public enum Actions{
        PAUSE_PLAY, START, STOP,
        NEXT, PREVIOUS,
        VOL_UP, VOL_DOWN, MUTE
    }

    /** BlockingQueue to manage the incoming actions */
    private BlockingQueue<Actions> queue;

    /** The remote to execute the actions on */
    private Remote remote;

    /**
     * Executes the first action from the queue
     */
    private Runnable execute_action = new Runnable() {
        /**
         * Wait on the queue to proceed and execute the
         *  given action.
         */
        public void run() {
            System.out.println("Starting to listen for Queue...");
            while (true){
                try {
                    Actions action = queue.take();
                    // Execute action on Remote.
                    System.out.println("Execute: "+action);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    /**
     * Initialize everything necessary and enforce the Singleton-
     *  pattern.
     */
    private Controller(){
        queue = new ArrayBlockingQueue<Actions>(3);
        // Determine OS and get the correct remote.
        remote = new DetermineOS().getNativeRemote();
        // Start the queue:
        new Thread(execute_action).start();
    }

    /**
     * Add a new action to the queue.
     * <br>
     * It will be executed as soon as possible.
     * @param action The action to add.
     */
    public void addAction(Actions action){
        queue.add(action);
    }

}